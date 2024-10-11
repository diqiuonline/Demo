package com.dhcc.mybaitsplus;

import com.dhcc.mybatisplus.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class test {

    public static final Logger logger = LoggerFactory.getLogger(test.class);
    @Autowired
    Usercontroller usercontroller;


    @Autowired
    IUserService userService;

    @Autowired
    UserMapper userMapper;




    @Test
    public void insertPojo() {


        for (int i = 1; i < 2; i++) {
            Path filePath = Paths.get("D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\枣糕-吃谷人黛博魂\\fc\\"+i+".txt"); // 替换为实际的文件路径
            Map<Integer, String> lineMap = new HashMap<>();
            List<User> users = new ArrayList<>();

            try (Stream<String> lines = Files.lines(Paths.get(String.valueOf(filePath)))) {
                AtomicInteger lineNumber = new AtomicInteger(1); // 使用AtomicInteger保证线程安全
                lines.forEach(line -> {
                    User user = new User();
                    user.setId((long) lineNumber.getAndIncrement());
                    user.setHtml(line);
                    users.add(user);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            int batchSize = 1000; // 批次大小可以根据实际情况调整
            for (int j = 0; j < users.size(); j += batchSize) {
                int end = Math.min(j + batchSize, users.size());
                List<User> batch = users.subList(j, end);
                int finalI = i;
                batch.forEach(user -> {
                    int tableNum = finalI; // 根据文件名确定插入哪个表
                    int id = Math.toIntExact(user.getId());
                    String html = user.getHtml();
                    userMapper.insertUser(tableNum, id, html);
                });
            }
            logger.info("表"+i+"插入数据："+users.size());

        }
    }


    @Autowired
    private PicMapper picMapper;

    @Test
    public void pojoToPic() {

        List<User> users = userMapper.selectList(null);
        List<User> toRemove = new ArrayList<>();
        Map<Long, String> linemap = new HashMap<>();
        for (User user : users) {
            if (user.getHtml().contains("hidden")) {
                toRemove.add(user);
            }
        }
        users.removeAll(toRemove);
        for (User user : users) {
            linemap.put(user.getId(), user.getHtml());
        }

        linemap.forEach((key,value) ->{
            Pic pic = new Pic();
            pic.setId(key);
            pic.setHtml(value);
            pic.setStatus("0");
            picMapper.insert(pic);
        });
    }
    @Test
    public void downloadFaceImage() throws Exception {

        List<Pic> pics = picMapper.selectList(null);
        AtomicBoolean downloadFailed = new AtomicBoolean(false);
        for (Pic pic : pics) {
            if (downloadFailed.get()) {
                break; // 如果下载已经失败，则提前结束循环
            }
            if (pic.getStatus().equals("0")) {
                Random random = new Random();
                int randomNumber = random.nextInt(5) + 1;
                logger.info("休息："+(randomNumber*5)+"秒");
                Thread.sleep(1000 * 5 * randomNumber);
                String html = pic.getHtml();
                String ContentUrl = null;
                try {
                    int startContent = html.indexOf("https://www.");
                    int stopContent = html.indexOf("\" role=\"link\"");
                    // 提交下载任务到线程池
                    ContentUrl = html.substring(startContent, stopContent);


                    String content = getContent(ContentUrl);

                    int picurlstart = html.indexOf("https://scontent-lax");
                    int picurljpgstop = html.indexOf(".jpg");
                    int picurlpngstop = html.indexOf(".png");

                    String picurlContent = html.substring(picurlstart, (picurljpgstop >= 0 && (picurlpngstop < 0 || picurljpgstop < picurlpngstop)) ? picurljpgstop : picurlpngstop + 4);

                    int picstart = content.lastIndexOf(picurlContent);
                    String bak = content.substring(picstart, picstart + 500);
                    int picstop = bak.indexOf("&amp;oe") + 16;
                    String imageUrl = bak.substring(0, picstop).replaceAll("amp;", "");

                    int timestarta = content.indexOf("<a aria-label=\"20");
                    int timestartb = content.indexOf("\" tabindex=\"0\">20");

                    String formattedDate = null;
                    if (timestarta == -1 && timestartb == -1) {
                        formattedDate = "2024-08-17";
                    } else {
                        int timestart = (timestarta >= 0 && (timestartb < 0 || timestarta < timestartb)) ? timestarta : timestartb;
                        String timebak = content.substring(timestart + 15, timestart + 200);
                        String time = timebak.substring(0, timebak.indexOf("日"));
                        formattedDate = time.replaceAll("年", "-").replaceAll("月", "-").replaceAll("日", "-");
                    }


                    String[] parts = formattedDate.split("-");
                    String month = parts[1];
                    String day = parts[2];

                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    formattedDate = parts[0] + "-" + month + "-" + day;
                    String directoryPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\枣糕-吃谷人黛博魂\\fc"; // 保存图片的目录路径
                    String filename = "枣糕_" + formattedDate + "_" + picurlContent.substring(picurlContent.lastIndexOf("/") + 1, picurlContent.length() - 4) + "_facebook.jpg"; // 下载文件的名称
                    if (!downloadImage(imageUrl, directoryPath, filename)) {
                        // 如果下载失败，则设置标志
                        downloadFailed.set(true);
                    } else {
                        pic.setStatus("1");
                        picMapper.updateById(pic);
                        logger.info("downloadImage--->" + filename);
                    }

                } catch (Exception e) {
                    logger.error("Failed to process HTML: " + ContentUrl, e);
                    downloadFailed.set(true);
                }
            }
        }


    }


    private String getContent(String contentUrl) throws Exception {
        String proxyHost = "127.0.0.1";
        int proxyPort = 7890;

        // 创建代理服务器实例
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new java.net.InetSocketAddress(proxyHost, proxyPort));
        URL url = new URL(contentUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

        String cookie1 = "sb=1EsFZi6mQ0wcVSuctDcF-BJr; datr=1EsFZquKDCwMm5lKzm3Kbx0Y; ps_n=1; ps_l=1; dpr=1.2000000476837158; c_user=100008766811745; fr=1I8F8j3Hd4j6Ktpo4.AWU9EMIYx-aoowbhPLPgfJaayO8.Bm3r7E..AAA.0.0.Bm3r7E.AWXS1W3JrKM; xs=45%3AuHO9oiZ5SMC8Ig%3A2%3A1725501865%3A-1%3A-1%3A%3AAcVCsLFIZh1UcgK_GirSiTMq3byqUMQGMRZz3zqAjQ; ar_debug=1; presence=C%7B%22t3%22%3A%5B%5D%2C%22utc3%22%3A1725874958836%2C%22v%22%3A1%7D; wd=3200x874";
        String cookie2 = "ps_l=1; ps_n=1; sb=FkbNZqnvjmJV49CzsX7Iyal4; datr=FkbNZnzkgs9r5uNXuMqinYwy; c_user=100015690136201; locale=zh_CN; dpr=1.2000000476837158; presence=C%7B%22t3%22%3A%5B%5D%2C%22utc3%22%3A1724754904756%2C%22v%22%3A1%7D; fr=1KxQjQtA7zH39Gmmq.AWXpvixGgyM9yg36ziEVwuzzyB8.BmzavW..AAA.0.0.BmzavW.AWWf6xLSDLE; xs=19%3AIZzzToiPxMMPqQ%3A2%3A1724729658%3A-1%3A8045%3A%3AAcUJ0ibQgK1B-HW5MhfTZ4DsOmGPsmFGBz54lvLsgA; wd=3200x874";

        int randomNumber = new Random().nextInt(1) + 1;

        // 设置请求属性
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        if (randomNumber == 1) {
            logger.info("使用q872125601@126.com cookie");
            connection.setRequestProperty("Cookie", cookie1);
        } else if (randomNumber == 2) {
            logger.info("使用qwennqadreet@hotmail.com cookie");
            connection.setRequestProperty("Cookie", cookie2);
        }

        // 建立连接
        connection.connect();


        // 读取网页内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        reader.close();
        return content.toString();

    }



    private boolean  downloadImage(String imageUrl, String directoryPath, String filename) throws IOException {
        // 确保文件名不为空或null
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        // 构建完整的文件路径
        String filePath = directoryPath + "/" + filename;

        String proxyHost = "127.0.0.1";
        int proxyPort = 7890;

        // 创建代理服务器实例
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new java.net.InetSocketAddress(proxyHost, proxyPort));
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

        // 设置请求属性（如果需要）
        connection.setRequestMethod("GET");

        // 建立连接并检查响应码
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return false; // 下载失败
        }

        // 读取图片内容并写入文件
        try (InputStream input = connection.getInputStream();
             FileOutputStream output = new FileOutputStream(filePath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // 捕获并处理异常
            e.printStackTrace();
            return false; // 下载失败
        } finally {
            // 关闭连接
            connection.disconnect();
        }

        return true; // 下载成功
    }
}
