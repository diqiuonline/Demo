package com.dhcc.tmp;

import com.seeta.sdk.*;
import com.seeta.sdk.util.LoadNativeCore;
import com.seeta.sdk.util.SeetafaceUtil;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * 文件改名字
 */
public class FileNameReplacer {

    public static void main(String[] args) {
        String directoryPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\小蓝蓝\\bili"; // 替换为实际的目录路径
        String oldname = "小蓝蓝plus";
        String newname = "小蓝蓝";
        File directory = new File(directoryPath);

        // 检查目录是否存在
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        // 检查文件名中是否包含字符 'a'
                        if (fileName.contains(oldname)) {
                            // 替换文件名中的 'a' 为 'b'
                            String newFileName = fileName.replace(oldname, newname);
                            Path oldPath = Paths.get(directoryPath, fileName);
                            Path newPath = Paths.get(directoryPath, newFileName);
                            try {
                                Files.move(oldPath, newPath);
                                System.out.println("Renamed: " + fileName + " -> " + newFileName);
                            } catch (IOException e) {
                                System.err.println("Error renaming file: " + e.getMessage());
                            }
                        }
                    }
                }
            } else {
                System.err.println("Directory is empty or not readable.");
            }
        } else {
            System.err.println("Invalid directory path.");
        }
    }
}

/**
 * 加指定前缀
 */
class AddPrefixToFiles {
    public static void main(String[] args) {
        String directoryPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\一小央泽\\weibo"; // 替换为实际的文件夹路径
        String prefix = "一小央泽_2014-01-01"; // 指定要添加到文件名前面的前缀

        addPrefixToFiles(directoryPath, prefix);
    }

    private static void addPrefixToFiles(String directoryPath, String prefix) {
        File directory = new File(directoryPath);

        // 确保目录存在且是文件夹
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("指定的路径不是一个有效的文件夹！");
            return;
        }

        // 获取文件夹中的所有文件
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                // 构建新的文件名
                String newFileName = prefix + file.getName();
                File newFile = new File(file.getParent(), newFileName);

                // 重命名文件
                if (file.renameTo(newFile)) {
                    System.out.println("文件已重命名为: " + newFileName);
                } else {
                    System.out.println("无法重命名文件: " + file.getName());
                }
            }
        } else {
            System.out.println("文件夹为空或者无法读取！");
        }
    }
}

/**
 * 2017.07.19 --》 2017-07-19
 */
class FileNameDateFormatter {

    public static void main(String[] args) {
        String directoryPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\古川kagura\\写真"; // 替换为实际的目录路径
        renameFilesWithDateInDirectory(directoryPath);
    }

    private static void renameFilesWithDateInDirectory(String directoryPath) {
        Path directory = Paths.get(directoryPath);
        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        Pattern pattern = Pattern.compile("\\d{4}\\.\\d{2}\\.\\d{2}");
                        Matcher matcher = pattern.matcher(fileName);

                        if (matcher.find()) {
                            LocalDate date = LocalDate.parse(matcher.group(), originalFormat);
                            String newFileName = matcher.replaceAll(date.format(newFormat));
                            Path newPath = path.resolveSibling(newFileName);
                            try {
                                Files.move(path, newPath);
                                System.out.println("Renamed " + fileName + " to " + newFileName);
                            } catch (IOException e) {
                                System.err.println("Failed to rename file: " + e.getMessage());
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 文件夹改名字
 */
class RenameDirectory {
    public static void main(String[] args) throws IOException {
        String directoryPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\是依酱呀\\059"; // 替换为你的目录路径
        String fromChar = "是依酱呀 ";
        String toChar = "";
        String excludeDirName = "exclude-me"; // 要排除的目录名

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath), 1)) { // 限制深度为1
            paths.filter(p -> !p.toFile().getName().equals(excludeDirName)) // 排除特定目录
                    .filter(Files::isDirectory)
                    .filter(p -> p.getParent() != null && p.getParent().equals(Paths.get(directoryPath))) // 只选择一级子目录
                    .forEach(path -> {
                        try {
                            renameIfContainsCharacter(path, fromChar, toChar);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error walking the directory: " + e.getMessage());
        }
    }

    private static void renameIfContainsCharacter(Path path, String fromChar, String toChar) throws IOException {
        String folderName = path.getFileName().toString();
        if (folderName.indexOf(fromChar) >= 0) {
            String newName = folderName.replace(fromChar, toChar);
            Path newPath = path.resolveSibling(newName);
            Files.move(path, newPath);
            System.out.println("Renamed " + path + " to " + newPath);
        }
    }
}

/**
 * 获取文件夹名字并重名文件
 */
class FileRenamer {

    public static void main(String[] args) {
        String directoryPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\舞小喵\\写真"; // 指定目录路径

        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path.");
            return;
        }

        File[] subDirectories = directory.listFiles(File::isDirectory);

        if (subDirectories != null) {
            for (File subDirectory : subDirectories) {
                renameFilesInSubDirectory(subDirectory);
            }
        } else {
            System.out.println("No subdirectories found.");
        }
    }

    private static void renameFilesInSubDirectory(File subDirectory) {
        String folderName = subDirectory.getName();

        File[] files = subDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String newName = "枣糕_"+folderName + "_" + fileName;
                    File newFile = new File(subDirectory, newName);

                    if (file.renameTo(newFile)) {
                        System.out.println("File " + fileName + " renamed to " + newName);
                    } else {
                        System.out.println("Failed to rename " + fileName);
                    }
                }
            }
        } else {
            System.out.println("No files found in " + subDirectory.getAbsolutePath());
        }
    }
}

/**
 * 扩展名前加指定字符
 */
class RenameAddress {
    public static void main(String[] args) {
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\小蓝蓝\\小蓝蓝PLUS\\小蓝蓝PLUS微博付费图【129P+3V 85M】"; // 替换为实际文件夹路径
        String prefix = "_weibo"; // 替换为需要添加的前缀字符

        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();



                    int splitPos = name.length() - 4; // 倒数第4位的位置
                    String secondPart = name.substring(splitPos);




                    String newName = name.substring(0,splitPos)+prefix+secondPart;
                    //System.out.println(name+"-------->"+newName);
                    File newFile = new File(file.getParent(), newName);
                    if (file.renameTo(newFile)) {
                        System.out.println("成功修改文件名： " + file.getName() + " -> " + newFile);
                    } else {
                        System.out.println("修改文件名失败： " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("给定的路径不是一个文件夹");
        }
    }
}


/**
 * 依据照片修改时间改文件夹名
 */
class RenameSubFoldersWithEarliestPhotoDate {

    public static void main(String[] args) throws IOException {
        Path rootDir = Paths.get("D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\舞小喵\\写真"); // 替换为你的根目录路径

        Files.walk(rootDir)
                .filter(Files::isDirectory)
                .sorted(Comparator.reverseOrder())
                .forEach(dir -> {
                    try {
                        renameSubFolderWithEarliestPhotoDate(rootDir,dir);
                    } catch (IOException e) {
                        System.err.println("Error processing directory: " + dir);
                    }
                });
    }

    private static void renameSubFolderWithEarliestPhotoDate(Path rootDir, Path dir) throws IOException {
        if (dir.equals(rootDir)) return; // Skip the root directory itself

        LocalDateTime earliestCreationTime = LocalDateTime.MAX;
        LocalDateTime earliestModificationTime = LocalDateTime.MAX;

        List<LocalDateTime> creationTimeList = new ArrayList<>();
        List<LocalDateTime> modificationTimeList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {

            for (Path file : stream) {
                if (Files.isDirectory(file)) {
                    continue; // Ignore subdirectories
                }
                if (isImageFile(file)) {
                    LocalDateTime creationTime = getCreationTime(file);
                    LocalDateTime modificationTime = getModificationTime(file);

                    creationTimeList.add(creationTime);
                    modificationTimeList.add(modificationTime);

                }
            }
        }
        LocalDateTime minCreationTime = creationTimeList.stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime minModificationTime = modificationTimeList.stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);


        LocalDateTime earliestTime = minCreationTime != null && minCreationTime.isBefore(minModificationTime) ? minCreationTime : minModificationTime;

        if (earliestTime != null) { // Ensure there was at least one image

            String formattedDate = earliestTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Files.move(dir, dir.resolveSibling(formattedDate + "_" + dir.getFileName()));

        }
    }

    private static boolean isImageFile(Path file) {
        String fileName = file.getFileName().toString();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")|| fileName.endsWith(".JPG");
    }

    private static LocalDateTime getCreationTime(Path imagePath) {
        try {
            // 获取元数据
            ImageMetadata metadata = Imaging.getMetadata(new File(String.valueOf(imagePath)));

            // 检查是否为JPEG格式
            if (metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

                // 查找DateTimeOriginal字段
                TiffField dateTimeField = jpegMetadata.findEXIFValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                if (dateTimeField != null) {
                    String dateTimeOriginal = dateTimeField.getStringValue();
                    return LocalDateTime.parse(dateTimeOriginal,DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
                } else {
                    return LocalDateTime.MAX;
                }
            } else {
                return LocalDateTime.MAX;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("读取图像文件时发生错误: " + imagePath);
        }
        return LocalDateTime.MAX;
    }

    private static LocalDateTime getModificationTime(Path file) {
        try {
            FileTime fileTime = Files.getLastModifiedTime(file);
            return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            System.err.println("Failed to get modification time for " + file);
            return LocalDateTime.MAX;
        }
    }
}


/**
 * 去除文件夹名字中的NO.00*
 */
class RenameFolders {

    public static void main(String[] args) {
        String directoryPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\一小央泽\\写真\\081"; // 替换为实际的路径
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] subDirs = directory.listFiles(File::isDirectory);
            if (subDirs != null) {
                for (File subDir : subDirs) {
                    renameFolder(subDir);
                }
            } else {
                System.out.println("No subdirectories found.");
            }
        } else {
            System.out.println("The specified path is not a valid directory.");
        }
    }

    private static void renameFolder(File folder) {
        String oldName = folder.getName();
        String newName = oldName.replaceAll("NO\\.\\d{1,3}\\s*", ""); // 使用正则表达式移除 "NO." 和其后最多三位的数字

        if (!oldName.equals(newName)) {
            File newFolder = new File(folder.getParent(), newName);
            boolean renamed = folder.renameTo(newFolder);
            if (renamed) {
                System.out.println(oldName + " renamed to " + newName);
            } else {
                System.out.println("Failed to rename " + oldName);
            }
        }
    }
}

/**
 * faceboot name 处理
 */
class  convertDatesInString {


    public static void main(String[] args) {

        String directoryPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\未归档\\菌子宝宝\\facebook"; // 替换为实际的目录路径

        Pattern pattern = Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2})");

        // 检查目录是否存在
        Path directory = Paths.get(directoryPath);
        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        Matcher matcher = pattern.matcher(fileName);
                        StringBuffer formattedStringBuffer = new StringBuffer();
                        while (matcher.find()) {
                            String dateString = matcher.group();
                            try {
                                LocalDate date = parseDate(dateString);
                                String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
                                matcher.appendReplacement(formattedStringBuffer, formattedDate);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid date format: " + dateString);
                                matcher.appendReplacement(formattedStringBuffer, dateString);
                            }
                        }
                        matcher.appendTail(formattedStringBuffer);
                        /*if (formattedStringBuffer.substring(19, 20).equals("-")) {
                            formattedStringBuffer = formattedStringBuffer.replace(19,20,"");
                        }*/

                        Path newPath = path.resolveSibling(String.valueOf(formattedStringBuffer));
                        try {
                            Files.move(path, newPath);
                            System.out.println("Renamed " + fileName + " to " + formattedStringBuffer);
                        } catch (IOException e) {
                            System.err.println("Failed to rename file: " + e.getMessage());
                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }






    }

    private static LocalDate parseDate(String dateString) {
        String[] parts = dateString.split("-");
        DateTimeFormatter formatter;

        if (parts.length == 3) {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (month > 12) {
                // Swap day and month
                formatter = DateTimeFormatter.ofPattern("yyyy-d-M");
            } else {
                formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
            }

            return LocalDate.parse(dateString, formatter);
        } else {
            throw new IllegalArgumentException("Invalid date string: " + dateString);
        }
    }
}


/**
 * 加扩展名
 */
class RenameLastAddress {
    public static void main(String[] args) {
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\枣糕-吃谷人黛博魂\\写真\\39枣糕"; // 替换为实际文件夹路径
        String prefix = ".zip"; // 替换为需要添加的前缀字符

        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    String newName = name+prefix;
                    //System.out.println(name+"-------->"+newName);
                    File newFile = new File(file.getParent(), newName);
                    if (file.renameTo(newFile)) {
                        System.out.println("成功修改文件名： " + file.getName() + " -> " + newFile);
                    } else {
                        System.out.println("修改文件名失败： " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("给定的路径不是一个文件夹");
        }
    }
}


/**
 * 挪移文件夹下子文件夹
 */

class MoveSubfolders {

    public static void main(String[] args) {
        Path rootPath = Paths.get("F:\\nihaoa\\Downloads\\baidu\\288是依酱吖\\新建文件夹"); // 指定需要遍历的根目录
        Path targetDirectory = Paths.get("F:\\nihaoa\\Downloads\\baidu\\288是依酱吖\\新建文件夹 (2)"); // 指定移动到的目标目录

        try {
            Files.walk(rootPath)
                    .filter(path -> path.toFile().isDirectory() && !path.equals(rootPath))
                    .forEach(subDir -> moveFirstSubfolder(subDir, targetDirectory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void moveFirstSubfolder(Path subDir, Path targetDirectory) {
        try (Stream<Path> files = Files.list(subDir)) {
            files.filter(Files::isDirectory)
                    .findFirst()
                    .ifPresent(subFolder -> {
                        try {
                            Path newLocation = targetDirectory.resolve(subFolder.getFileName());
                            Files.move(subFolder, newLocation, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Moved " + subFolder + " to " + newLocation);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


/**
 * 人体检测
 */
class Person{

    public static final Logger logger = LoggerFactory.getLogger(Person.class);
    public static void main(String[] args) throws Exception {
            LoadNativeCore.LOAD_NATIVE(SeetaDevice.SEETA_DEVICE_AUTO);
            FaceDetector detector = new FaceDetector(new SeetaModelSetting(new String[]{"D:\\Program Files\\SD\\sf3.0_models\\face_detector.csta"}, SeetaDevice.SEETA_DEVICE_AUTO));

            String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\rioko凉凉子\\肉扣热热子\\img\\转发微博图片"; // 替换为实际文件夹路径
            String target = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\rioko凉凉子\\新建文件夹";
            File folder = new File(folderPath);

            if (folder.isDirectory()) {
                File[] files = folder.listFiles();
                for (File file : files) {
                   processImage(file, target,detector);
                }
            } else {
                System.out.println("给定的路径不是一个文件夹");
            }



    }

    private static void processImage(File file, String target,FaceDetector detector) {
        try {
            BufferedImage image = SeetafaceUtil.toBufferedImage(file);
            SeetaImageData imageData = SeetafaceUtil.toSeetaImageData(image);
            //检测到的人脸坐标
            SeetaRect[] detects = detector.Detect(imageData);
            if (detects.length != 1) {
                System.out.println(file+"有"+detects.length+"张人脸");
                Path source = Paths.get(file.getAbsolutePath());
                Path destination = Paths.get(target, file.getName());
                Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.err.println("处理图片时发生错误: " + file.getName() + " - " + e.getMessage()); // 在异常信息中包含文件名
        }
    }

}


/**
 * 文件夹名字前加指定字符
 */


class RenameSubFolders {

    public static void main(String[] args) throws IOException {
        Path rootDir = Paths.get("D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\枣糕-吃谷人黛博魂\\写真\\39枣糕"); // 替换为你的根目录路径

        Files.walk(rootDir)
                .filter(Files::isDirectory)
                .sorted(Comparator.reverseOrder())
                .forEach(dir -> {
                    try {
                        renameSubFolderWithEarliestPhotoDate(rootDir,dir);
                    } catch (IOException e) {
                        System.err.println("Error processing directory: " + dir);
                    }
                });
    }

    private static void renameSubFolderWithEarliestPhotoDate(Path rootDir, Path dir) throws IOException {
        if (dir.equals(rootDir)) return; // Skip the root directory itself


        Files.move(dir, dir.resolveSibling("2020-07-31" + "_" + dir.getFileName()));

    }


}



/**
 * 依据照片修改时间改文件名
 */
class RenameWithEarliestPhotoDate {

    public static void main(String[] args) throws IOException {
        String folderPath = "D:\\VMOS\\linux\\pc-192-168-2-115\\caches\\AAedss\\小蓝蓝\\小蓝蓝PLUS\\小蓝蓝PLUS微博付费图【129P+3V 85M】"; // 替换为实际文件夹路径
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    if (isImageFile(file.toPath())) {
                        LocalDateTime minCreationTime = getCreationTime(file.toPath());
                        LocalDateTime minModificationTime = getModificationTime(file.toPath());

                        LocalDateTime earliestTime = minCreationTime != null && minCreationTime.isBefore(minModificationTime) ? minCreationTime : minModificationTime;
                        String formattedDate = earliestTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        String newName = "小蓝蓝_"+formattedDate+"_"+file.getName();

                        File newFile = new File(file.getParent(), newName);
                        if (file.renameTo(newFile)) {
                            System.out.println("成功修改文件名： " + file.getName() + " -> " + newFile);
                        } else {
                            System.out.println("修改文件名失败： " + file.getName());
                        }
                    }
                }
            }
        }
    }



    private static boolean isImageFile(Path file) {
        String fileName = file.getFileName().toString();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")|| fileName.endsWith(".JPG");
    }

    private static LocalDateTime getCreationTime(Path imagePath) {
        try {
            // 获取元数据
            ImageMetadata metadata = Imaging.getMetadata(new File(String.valueOf(imagePath)));

            // 检查是否为JPEG格式
            if (metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

                // 查找DateTimeOriginal字段
                TiffField dateTimeField = jpegMetadata.findEXIFValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                if (dateTimeField != null) {
                    String dateTimeOriginal = dateTimeField.getStringValue();
                    return LocalDateTime.parse(dateTimeOriginal,DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
                } else {
                    return LocalDateTime.MAX;
                }
            } else {
                return LocalDateTime.MAX;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("读取图像文件时发生错误: " + imagePath);
        }
        return LocalDateTime.MAX;
    }

    private static LocalDateTime getModificationTime(Path file) {
        try {
            FileTime fileTime = Files.getLastModifiedTime(file);
            return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            System.err.println("Failed to get modification time for " + file);
            return LocalDateTime.MAX;
        }
    }
}
