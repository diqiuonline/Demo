package com.dhcc.service;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 图片查看器应用程序
 * 支持图片浏览、音频播放和图片删除功能
 */
public class ImageViewerApp extends JFrame {
    // 图片显示组件
    private JLabel imageLabel;
    // 状态显示组件
    private JLabel statusLabel;
    // 滚动面板用于显示大图片
    private JScrollPane scrollPane;

    // 图片文件列表
    private List<File> imageFiles;
    // 浏览历史记录
    private List<File> history;
    // 当前图片索引
    private int currentIndex;
    // 当前目录
    private File currentDirectory;
    // 原始图片缓存
    private BufferedImage originalImage;

    // 音频播放相关变量
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private JLabel audioStatusLabel;
    private File currentAudioFile;

    /**
     * 构造函数，初始化图片查看器界面和组件
     */
    public ImageViewerApp() {
        // 初始化JavaFX环境
        new JFXPanel();

        // 设置窗口属性
        setTitle("图片查看器 - 保持原始比例");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 初始化菜单栏
        initializeMenuBar();

        // 初始化主界面
        initializeMainPanel();

        // 添加键盘监听器
        setupKeyListener();

        // 添加鼠标滚轮监听器
        setupMouseWheelListener();

        // 初始化数据结构
        imageFiles = new ArrayList<>();
        history = new ArrayList<>();
        currentIndex = -1;
    }

    /**
     * 初始化菜单栏
     */
    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        JMenuItem openMenuItem = new JMenuItem("打开图片");
        JMenuItem exitMenuItem = new JMenuItem("退出");

        // 音频菜单
        JMenu audioMenu = new JMenu("音频");
        JMenuItem openAudioMenuItem = new JMenuItem("打开MP3");
        JMenuItem playPauseMenuItem = new JMenuItem("播放/暂停");
        JMenuItem stopMenuItem = new JMenuItem("停止");

        // 添加事件监听器
        openMenuItem.addActionListener(e -> openImage());
        exitMenuItem.addActionListener(e -> System.exit(0));
        openAudioMenuItem.addActionListener(e -> openAudio());
        playPauseMenuItem.addActionListener(e -> togglePlayPause());
        stopMenuItem.addActionListener(e -> stopAudio());

        // 组装文件菜单
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        // 组装音频菜单
        audioMenu.add(openAudioMenuItem);
        audioMenu.add(playPauseMenuItem);
        audioMenu.add(stopMenuItem);
        menuBar.add(audioMenu);

        setJMenuBar(menuBar);
    }
    
    /**
     * 检查文件是否为支持的图片格式
     * @param file 待检查的文件
     * @return 如果是图片文件返回true，否则返回false
     */
    private boolean isImageFile(File file) {
        if (!file.isFile()) return false;
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                name.endsWith(".png") || name.endsWith(".gif") ||
                name.endsWith(".bmp") || name.endsWith(".webp");
    }

    /**
     * 初始化主界面面板
     */
    private void initializeMainPanel() {
        // 创建主面板，使用BorderLayout布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建图片显示区域
        imageLabel = new JLabel("请打开一张图片", SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        scrollPane = new JScrollPane(imageLabel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 创建状态面板
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));

        // 创建状态标签，显示当前照片位置信息
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        statusPanel.add(statusLabel);

        // 创建音频状态标签
        audioStatusLabel = new JLabel("无音频播放", SwingConstants.CENTER);
        audioStatusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        statusPanel.add(audioStatusLabel);

        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    /**
     * 设置键盘监听器
     */
    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        // 设置焦点，以便接收键盘事件
        setFocusable(true);
        requestFocusInWindow();
    }


    /**
     * 设置鼠标滚轮监听器
     */
    private void setupMouseWheelListener() {
        this.addMouseWheelListener(e -> {
            if (!e.isControlDown()) {
                if (e.getPreciseWheelRotation() < 0) {
                    // 向上滚动 - 对应左键功能（上一张）
                    showPreviousImage();
                } else {
                    // 向下滚动 - 对应右键功能（随机一张）
                    showRandomImage();
                }
            }
        });
    }
    /**
     * 打开图片文件对话框
     */
    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // 设置上次访问的目录作为默认目录
        File lastDirectory = LastDirectoryTracker.getLastDirectory();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }

        // 设置图片过滤器
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || isImageFile(f);
            }

            @Override
            public String getDescription() {
                return "图片文件 (*.jpg, *.jpeg, *.png, *.gif, *.bmp, *.webp)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            currentDirectory = selectedFile.getParentFile();
            // 保存当前目录的父目录作为最后访问的目录
            LastDirectoryTracker.saveLastDirectory(currentDirectory.getParentFile());
            loadImagesFromDirectory();
            showImage(selectedFile);
        }
    }

    /**
     * 打开音频文件对话框
     */
    private void openAudio() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // 设置音频过滤器
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".mp3") || name.endsWith(".wav");
            }

            @Override
            public String getDescription() {
                return "音频文件 (*.mp3, *.wav)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadAudio(selectedFile);
        }
    }

    /**
     * 加载音频文件
     * @param audioFile 音频文件
     */
    private void loadAudio(File audioFile) {
        try {
            // 停止当前播放的音频
            stopAudio();

            // 加载新音频
            currentAudioFile = audioFile;
            String mediaPath = audioFile.toURI().toString();
            Media media = new Media(mediaPath);
            mediaPlayer = new MediaPlayer(media);

            // 设置播放结束监听器
            mediaPlayer.setOnEndOfMedia(() -> {
                isPlaying = false;
                audioStatusLabel.setText("播放完成");
            });

            audioStatusLabel.setText("已加载: " + audioFile.getName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "无法加载音频文件: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            audioStatusLabel.setText("加载音频失败");
        }
    }

    /**
     * 切换播放/暂停状态
     */
    private void togglePlayPause() {
        if (mediaPlayer == null) {
            JOptionPane.showMessageDialog(this, "请先打开一个音频文件",
                    "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            audioStatusLabel.setText("播放暂停");
        } else {
            mediaPlayer.play();
            isPlaying = true;
            audioStatusLabel.setText("正在播放...");
        }
    }

    /**
     * 停止音频播放
     */
    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPlaying = false;
            audioStatusLabel.setText("播放已停止");
        }
    }

    /**
     * 从当前目录加载所有图片文件
     */
    private void loadImagesFromDirectory() {
        imageFiles.clear();

        if (currentDirectory != null && currentDirectory.isDirectory()) {
            File[] files = currentDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (isImageFile(file)) {
                        imageFiles.add(file);
                    }
                }
            }
        }
    }

    /**
     * 显示指定的图片文件
     * @param imageFile 要显示的图片文件
     */
    private void showImage(File imageFile) {
        if (imageFile == null || !imageFile.exists()) return;

        try {
            // 读取原始图片
            originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                return;
            }

            // 更新当前索引
            currentIndex = imageFiles.indexOf(imageFile);
            if (currentIndex == -1) {
                // 如果文件不在列表中，重新加载列表
                loadImagesFromDirectory();
                currentIndex = imageFiles.indexOf(imageFile);
            }

            // 添加到历史记录（如果不是重复的最后一项）
            if (history.isEmpty() || !history.get(history.size() - 1).equals(imageFile)) {
                history.add(imageFile);
            }

            // 创建保持原始比例的图标
            ImageIcon icon = new ImageIcon(originalImage);
            imageLabel.setIcon(icon);
            imageLabel.setText("");

            // 设置标签的首选大小为图片的原始尺寸
            imageLabel.setPreferredSize(new Dimension(
                    originalImage.getWidth(),
                    originalImage.getHeight()
            ));

            // 更新滚动窗格
            scrollPane.revalidate();
            scrollPane.repaint();

            // 更新标题和状态标签
            setTitle("图片查看器 - " + imageFile.getName() + " (" +
                    originalImage.getWidth() + "x" + originalImage.getHeight() + ")");

            updateStatusLabel();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载图片: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 更新状态标签显示当前位置信息
     */
    private void updateStatusLabel() {
        if (currentIndex >= 0 && !imageFiles.isEmpty()) {
            statusLabel.setText("当前图片: " + (currentIndex + 1) + " / " + imageFiles.size());
        } else {
            statusLabel.setText(" ");
        }
    }

    /**
     * 处理键盘按键事件
     * @param e 键盘事件
     */
    private void handleKeyPress(KeyEvent e) {
        if (imageFiles.isEmpty()) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                showPreviousImage(); // 左键显示上一张浏览的照片
                break;
            case KeyEvent.VK_RIGHT:
                showRandomImage(); // 右键显示随机一张照片
                break;
            case KeyEvent.VK_DELETE:
                deleteCurrentImage();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            // 添加音频控制快捷键
            case KeyEvent.VK_P:
                togglePlayPause();
                break;
            case KeyEvent.VK_S:
                stopAudio();
                break;
        }
    }

    /**
     * 显示上一张浏览的照片
     */
    private void showPreviousImage() {
        if (history.size() <= 1) {
            // 如果历史记录只有一张或没有，不做任何操作
            return;
        }

        // 移除当前照片（历史记录的最后一项）
        history.remove(history.size() - 1);
        // 显示新的最后一项（即上一张）
        File previousFile = history.get(history.size() - 1);
        showImage(previousFile);
    }

    /**
     * 显示随机图片
     */
    private void showRandomImage() {
        if (imageFiles.size() <= 1) return;

        Random random = new Random();
        int newIndex;
        do {
            newIndex = random.nextInt(imageFiles.size());
        } while (newIndex == currentIndex && imageFiles.size() > 1);

        showImage(imageFiles.get(newIndex));
    }

    /**
     * 删除当前图片文件
     */
    private void deleteCurrentImage() {
        if (currentIndex < 0 || currentIndex >= imageFiles.size()) return;

        File fileToDelete = imageFiles.get(currentIndex);

        try {
            // 检查是否为符号链接文件（不限于.symlink扩展名）
            Path filePath = fileToDelete.toPath();
            boolean isSymbolicLink = Files.isSymbolicLink(filePath);

            if (isSymbolicLink || fileToDelete.getName().toLowerCase().endsWith(".symlink")) {
                deleteSymbolicLinkAndTarget(fileToDelete, filePath, isSymbolicLink);
            } else {
                // 对于普通文件，使用直接删除（不放入回收站）
                deleteRegularFile(fileToDelete, filePath);
            }

            // 从图像列表中移除已删除的文件
            imageFiles.remove(currentIndex);

            // 更新显示
            updateDisplayAfterDeletion();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "删除文件时出错: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除符号链接及其目标文件
     * @param fileToDelete 要删除的文件
     * @param filePath 文件路径
     * @param isSymbolicLink 是否为符号链接
     * @throws IOException IO异常
     */
    private void deleteSymbolicLinkAndTarget(File fileToDelete, Path filePath, boolean isSymbolicLink) throws IOException {
        File targetFile = null;
        
        if (isSymbolicLink) {
            // 获取符号链接指向的目标文件
            Path targetPath = Files.readSymbolicLink(filePath);
            targetFile = targetPath.toFile();
        } else {
            // 如果是.symlink文件但不是系统符号链接，尝试读取其内容获取目标路径
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToDelete))) {
                String targetPath = reader.readLine();
                if (targetPath != null && !targetPath.trim().isEmpty()) {
                    targetFile = new File(targetPath.trim());
                }
            } catch (IOException e) {
                System.err.println("无法读取.symlink文件内容: " + e.getMessage());
            }
        }

        // 删除目标文件（如果存在且可访问）
        if (targetFile != null && targetFile.exists()) {
            deleteFileWithLogging(targetFile.toPath(), "目标文件");
        }

        // 删除符号链接文件本身
        deleteFileWithLogging(filePath, "符号链接");
    }

    /**
     * 删除普通文件
     * @param fileToDelete 要删除的文件
     * @param filePath 文件路径
     * @throws IOException IO异常
     */
    private void deleteRegularFile(File fileToDelete, Path filePath) throws IOException {
        deleteFileWithLogging(filePath, "普通文件");
    }

    /**
     * 删除文件并记录日志
     * @param filePath 要删除的文件路径
     * @param fileType 文件类型描述
     * @throws IOException IO异常
     */
    private void deleteFileWithLogging(Path filePath, String fileType) throws IOException {
        try {
            Files.delete(filePath);
            System.out.println("已删除" + fileType + ": " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("无法删除" + fileType + ": " + e.getMessage());
            // 对于符号链接，显示错误消息并中断操作
            if ("符号链接".equals(fileType)) {
                JOptionPane.showMessageDialog(this, "无法删除符号链接文件: " + e.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
            throw e;
        }
    }

    /**
     * 删除文件后更新显示
     */
    private void updateDisplayAfterDeletion() {
        if (imageFiles.isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setText("没有更多图片");
            setTitle("图片查看器");
            currentIndex = -1;
        } else {
            if (currentIndex >= imageFiles.size()) {
                currentIndex = imageFiles.size() - 1;
            }
            showImage(imageFiles.get(currentIndex));
        }
    }

    /**
     * 程序入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // 静默处理错误
        }

        SwingUtilities.invokeLater(() -> {
            ImageViewerApp viewer = new ImageViewerApp();
            viewer.setVisible(true);
        });
    }
}
