package com.dhcc.service;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * b站图片下载
 */
public class BilibiliImageDownloader {
    private static final ExecutorService executor = Executors.newFixedThreadPool(30);
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(30, 5, TimeUnit.MINUTES)) // 创建一个新的连接池，最大空闲连接数为5，保持空闲连接5分钟
            .build();
    public static void main(String[] args) {

        String filePath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\小蓝蓝\\bili\\src.txt";

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath)).stream().map(String::trim).collect(Collectors.toList());
            Future<Map<Integer, String>> lineMapFuture = executor.submit(() -> createLineMap(lines));
            List<String> linesUrl = getLineUrl(lines,lineMapFuture.get());
            System.out.println("下载图片数量"+linesUrl.size());
            Future<List<String>> processedLinesFuture = executor.submit(() -> processLines(linesUrl));


            executor.shutdown(); // 关闭线程池
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            executor.shutdownNow(); // 发生异常时取消所有任务并关闭线程池
        }



    }



    private static Map<Integer, String> createLineMap(List<String> lines) {
        return IntStream.range(0, lines.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, lines::get));
    }




    private static List<String> processLines(List<String> linesUrl) throws IOException {
        String destinationDirectory = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\小蓝蓝\\bili"; // 目标目录
        List<String> tmp = new ArrayList<>();
        String imageUrl = null;
        String date = null;
        String newFileName = null;
        int j = 0;
        for (String line : linesUrl) {
            if (line.contains("null")) {
                continue;
            }
            int i = line.lastIndexOf(".");
            imageUrl = line.substring(0, i+4);
            if (tmp.contains(imageUrl)) {
                j++;
                continue;
            }
            tmp.add(imageUrl);
            date = line.substring(line.length() - 10, line.length());

            if (line.contains("vip")) {
                newFileName = "小蓝蓝_" + date + "_" + getId(imageUrl).substring(0,getId(imageUrl).length()-4) + "_b站_vip"+getId(imageUrl).substring(getId(imageUrl).length()-4,getId(imageUrl).length());
            } else {

                newFileName = "小蓝蓝_" + date + "_" + getId(imageUrl).substring(0,getId(imageUrl).length()-4) + "_b站"+getId(imageUrl).substring(getId(imageUrl).length()-4,getId(imageUrl).length());
            }
            imageUrl = "https:" + imageUrl; // 图片链接

            boolean b = downloadImage(imageUrl, destinationDirectory, newFileName);

        }
        System.out.println("重复的图片："+j+"张");
        return null;
    }

    public static boolean getLengthBetweenChars(String line) {
        String fullDatePattern = "\\d{4}年\\d{1,2}月\\d{1,2}日";
        String monthDayPattern = "\\d{1,2}月\\d{1,2}日";

        Pattern year = Pattern.compile(fullDatePattern);
        Pattern month = Pattern.compile(monthDayPattern);

        return year.matcher(line).matches() || month.matcher(line).matches() ? true : false;


        /*if (line.length() < 11 || !line.substring(0, 11).contains("日")) {
            if ((line.length() == 6)) {
                int startIndex = line.indexOf("月");
                int endIndex = line.indexOf("日", startIndex + 1);
                return (endIndex - startIndex)==3;
            }
            return false;
        }
        int startIndex = line.indexOf("月");
        int endIndex = line.indexOf("日", startIndex + 1);
        return (endIndex - startIndex)==3;*/
    }



    public static String getId(String line) {
        int lastSlashIndex = line.lastIndexOf('/');
        return line.substring(lastSlashIndex + 1, line.length());
    }

    public static String dateString(String date) {

        date = date.replaceAll("年", "-").replaceAll("月", "-").replaceAll("日", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //System.out.println(date);
        LocalDate parsedDate = containsYear(date) ? LocalDate.parse(date, formatter) : LocalDate.parse("2024-" + date, formatter);
        return parsedDate.format(formatter);
    }
    private static boolean containsYear(String date) {
        return Pattern.compile("\\d{4}").matcher(date).find();
    }
    public static boolean downloadImage(String imageUrl, String destinationDirectory, String newFileName) {
        Request request = new Request.Builder()
                .url(imageUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            } else {
                // 获取图片数据
                InputStream inputStream = response.body().byteStream();
                // 保存到文件
                File file = new File(destinationDirectory, newFileName);
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                outputStream.close();
                System.out.println("Image downloaded to " + file.getAbsolutePath());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static List<String> getLineUrl(List<String> lines, Map<Integer, String> lineMap) throws IOException {
        List<String> specifiedStrings = Arrays.asList("jpg", "png");
        lines = lines.stream()
                .filter(s -> specifiedStrings.stream().anyMatch(s::contains))
                .filter(s -> !s.contains("bili-dyn-card-video__cover"))
                .filter(s -> !s.contains("face"))
                .filter(s -> !s.contains("IN4E1b8HNg"))
                .distinct()
                .collect(Collectors.toList());

        List<String> urls = new ArrayList<>();
        for (String line : lines) {
            boolean isVip = false;
            String date = null;

            Pattern pattern = Pattern.compile("<img[^>]*>");
            Matcher matcher = pattern.matcher(line);
            StringBuilder tagsOnly = new StringBuilder();
            while (matcher.find()) {
                tagsOnly.append(matcher.group());
            }

            pattern = Pattern.compile("(//.*?)@");
            matcher = pattern.matcher(tagsOnly.toString());

            //System.out.println(tagsOnly.toString());

            for (Map.Entry<Integer, String> entry : lineMap.entrySet()) {
                String value = entry.getValue();
                if (value.contains(line)) {
                    for (int i = 0; i < 20; i++) {
                        if (entry.getKey() > i && getLengthBetweenChars(lineMap.get(entry.getKey() - i))) {
                            int index = lineMap.get(entry.getKey() - i).indexOf("日");
                            date = dateString(lineMap.get(entry.getKey() - i).substring(0, index + 1));
                            value = lineMap.get(entry.getKey() - i + 1);
                            if (value.contains("IN4E1b8HNg")) {
                                isVip = true;
                            }
                            break;
                        }
                    }
                    break;
                }
            }

            while (matcher.find()) {
                if (!matcher.group(1).toString().contains("emote")) {
                    if (isVip) {
                        urls.add(matcher.group(1) + "vip_"+date);
                    } else {
                        urls.add((matcher.group(1))+"_"+date);
                    }
                }

            }


        }
        urls = urls.stream().filter(line -> !line.isEmpty()).distinct().collect(Collectors.toList());
        //System.out.println(urls);
        return urls;
    }



}