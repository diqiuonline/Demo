package com.dhcc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 用于跟踪和保存最后一次访问的目录路径
 */
public class LastDirectoryTracker {
    private static final String CONFIG_FILE = System.getProperty("user.home") + File.separator + ".imageviewer.properties";
    private static final String LAST_DIRECTORY_KEY = "last.directory";

    /**
     * 保存最后一次访问的目录路径
     *
     * @param directory 最后一次访问的目录
     */
    public static void saveLastDirectory(File directory) {
        Properties props = new Properties();
        
        // 如果配置文件已存在，先加载现有配置
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // 更新目录路径
        if (directory != null) {
            props.setProperty(LAST_DIRECTORY_KEY, directory.getAbsolutePath());
        } else {
            props.remove(LAST_DIRECTORY_KEY);
        }
        
        // 保存到配置文件
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            props.store(fos, "Image Viewer Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最后一次访问的目录路径
     *
     * @return 最后一次访问的目录，如果不存在则返回null
     */
    public static File getLastDirectory() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            return null;
        }
        
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);
            String lastDirPath = props.getProperty(LAST_DIRECTORY_KEY);
            if (lastDirPath != null && !lastDirPath.isEmpty()) {
                File lastDir = new File(lastDirPath);
                if (lastDir.exists() && lastDir.isDirectory()) {
                    return lastDir;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}