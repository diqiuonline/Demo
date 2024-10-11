package com.dhcc.service;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * b站图片下载
 */
public class TaoziquanDownloader {
    private static final ExecutorService executor = Executors.newFixedThreadPool(30);

    public static void main(String[] args) {

        String filePath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\赛高\\新建文件夹\\src.txt";

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath)).stream().map(String::trim).collect(Collectors.toList());
            Future<Map<Integer, String>> lineMapFuture = executor.submit(() -> createLineMap(lines));
            List<String> linesUrl = getLineUrl(lines,lineMapFuture.get());
            System.out.println("下载图片数量"+linesUrl.size());
            Future<List<String>> processedLinesFuture = executor.submit(() -> processLines(linesUrl));

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            executor.shutdownNow(); // 发生异常时取消所有任务并关闭线程池
        }


        executor.shutdown(); // 关闭线程池

    }



    private static Map<Integer, String> createLineMap(List<String> lines) {
        return IntStream.range(0, lines.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, lines::get));
    }




    private static List<String> processLines(List<String> linesUrl) throws IOException {
        String destinationDirectory = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\赛高\\新建文件夹\\"; // 目标目录
        String date = null;
        String newFileName = null;
        for (String line : linesUrl) {
            date = line.substring(line.length() - 17, line.length());


            newFileName = "赛高_" + date.substring(0,4)+"-"+date.substring(5,7)+"-"+date.substring(8,10) + "_" + line.substring(line.lastIndexOf('/') + 1, line.length()-22)+ "_桃子圈"+line.substring(line.lastIndexOf("."),line.lastIndexOf(".")+4);
            if (new File(destinationDirectory + newFileName).exists()) {
                continue;
            }
            //System.out.println(line+"------------>"+newFileName);
            boolean b = downloadImage(line.substring(0,line.length()-18), destinationDirectory, newFileName);

        }
        return null;
    }

    public static boolean getLengthBetweenChars(String line) {
        String dateTimePattern = "\\d{4}年\\d{2}月\\d{2}日 \\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(dateTimePattern);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }




    private static final OkHttpClient client = new OkHttpClient.Builder()
            .proxy(new Proxy(Proxy.Type.HTTP, java.net.InetSocketAddress.createUnresolved("127.0.0.1", 7890)))
            /*.connectTimeout(300, TimeUnit.SECONDS) // 设置连接超时时间为15秒
            .readTimeout(300, TimeUnit.SECONDS)    // 设置读取超时时间为20秒
            .writeTimeout(300, TimeUnit.SECONDS)   // 设置写入超时时间为20秒*/
            .connectionPool(new ConnectionPool(30, 5, TimeUnit.MINUTES)) // 创建一个新的连接池，最大空闲连接数为5，保持空闲连接5分钟
            .build();

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
            System.out.println("下载失败："+imageUrl);
            return false;
        }
    }
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors(); // 使用可用处理器数量作为线程池大小
    private static List<String> getLineUrl(List<String> lines, Map<Integer, String> lineMap) throws IOException, ExecutionException, InterruptedException {
        List<String> specifiedStrings = Arrays.asList("data-preview-url");
        lines = lines.stream()
                .filter(s -> specifiedStrings.stream().anyMatch(s::contains))
                .map(s -> s.substring(s.indexOf("data-preview-url")+18, s.lastIndexOf("data-index")-2)) // 对每个字符串进行截取
                .distinct()
                .collect(Collectors.toList());


        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Future<String>> futures = new ArrayList<>();
        for (String line : lines) {
            futures.add(executor.submit(() -> processLine(line, lineMap)));
        }



        List<String> urls = new ArrayList<>();
        for (Future<String> future : futures) {
            String result = future.get(); // 获取结果，此方法会阻塞直到有结果返回
            if (result != null && !result.isEmpty()) {
                urls.add(result);
            }
        }

        executor.shutdown(); // 关闭线程池
        urls = new ArrayList<>(new HashSet<>(urls)); // 使用HashSet去重后转回ArrayList
        return urls;



    }
    private static String processLine(String line, Map<Integer, String> lineMap) {
        String date = findDate(line, lineMap);
        return (line + "_" + date).trim();
    }
    private static String findDate(String line, Map<Integer, String> lineMap) {
        for (Map.Entry<Integer, String> entry : lineMap.entrySet()) {
            String value = entry.getValue();
            if (value.contains(line)) {
                for (int i = 0; i < 40; i++) {
                    if (entry.getKey() > i && getLengthBetweenChars(lineMap.get(entry.getKey() - i))) {
                        int index = lineMap.get(entry.getKey() - i).indexOf(">");
                        return lineMap.get(entry.getKey() - i).substring(index + 1, index + 18);
                    }
                }
                break;
            }
        }
        return ""; // 如果找不到日期，默认返回空字符串
    }


public static class Test{
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String filePath = "D:\\VMOS\\linux\\pc-192-168-2-115\\redss\\bak\\src.txt";

        List<String> lines = Files.readAllLines(Paths.get(filePath)).stream().map(String::trim).collect(Collectors.toList());
        Future<Map<Integer, String>> lineMapFuture = executor.submit(() -> createLineMap(lines));
        List<String> linesUrl = getLineUrl(lines,lineMapFuture.get());



        executor.shutdown(); // 关闭线程池

    }
}




}