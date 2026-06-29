package com.dhcc.service;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

public class ImageViewer extends JFrame {
    private JLabel imageLabel;
    private JLabel statusLabel; // 用于显示当前照片位置信息
    private JScrollPane scrollPane;
    private List<File> imageFiles;
    private List<File> history; // 存储浏览历史
    private int currentIndex;
    private File currentDirectory;
    private BufferedImage originalImage;

    // 音频播放相关变量
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private JLabel audioStatusLabel;
    private File currentAudioFile;

    public ImageViewer() {
        // 初始化JavaFX
        new JFXPanel();

        setTitle("图片查看器 - 保持原始比例");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem openMenuItem = new JMenuItem("打开图片");
        JMenuItem exitMenuItem = new JMenuItem("退出");

        // 添加音频菜单
        JMenu audioMenu = new JMenu("音频");
        JMenuItem openAudioMenuItem = new JMenuItem("打开MP3");
        JMenuItem playPauseMenuItem = new JMenuItem("播放/暂停");
        JMenuItem stopMenuItem = new JMenuItem("停止");

        openMenuItem.addActionListener(e -> openImage());
        exitMenuItem.addActionListener(e -> System.exit(0));

        openAudioMenuItem.addActionListener(e -> openAudio());
        playPauseMenuItem.addActionListener(e -> togglePlayPause());
        stopMenuItem.addActionListener(e -> stopAudio());

        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        audioMenu.add(openAudioMenuItem);
        audioMenu.add(playPauseMenuItem);
        audioMenu.add(stopMenuItem);
        menuBar.add(audioMenu);

        setJMenuBar(menuBar);

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

        // 创建状态面板，包含图片状态和音频状态
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

        // 添加键盘监听
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        // 设置焦点，以便接收键盘事件
        setFocusable(true);
        requestFocusInWindow();

        imageFiles = new ArrayList<>();
        history = new ArrayList<>();
        currentIndex = -1;
    }

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // 设置图片过滤器
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                        name.endsWith(".png") || name.endsWith(".gif") ||
                        name.endsWith(".bmp") || name.endsWith(".webp");
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
            loadImagesFromDirectory();
            showImage(selectedFile);
        }
    }

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

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPlaying = false;
            audioStatusLabel.setText("播放已停止");
        }
    }

    // 其余方法保持不变...
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

    private boolean isImageFile(File file) {
        if (!file.isFile()) return false;
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                name.endsWith(".png") || name.endsWith(".gif") ||
                name.endsWith(".bmp") || name.endsWith(".webp");
    }

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
            
            // 移除之前的监听器（如果有的话）
            for (MouseWheelListener listener : imageLabel.getMouseWheelListeners()) {
                imageLabel.removeMouseWheelListener(listener);
            }
            
            // 为imageLabel添加鼠标滚轮监听器
            imageLabel.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    handleMouseWheel(e);
                }
            });

            // 更新滚动窗格
            scrollPane.revalidate();
            scrollPane.repaint();

            // 更新标题和状态标签
            setTitle("图片查看器 - " + imageFile.getName() + " (" +
                    originalImage.getWidth() + "x" + originalImage.getHeight() + ")");

            // 更新状态标签显示当前是第几张
            updateStatusLabel();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "无法加载图片: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 更新状态标签显示当前位置信息
    private void updateStatusLabel() {
        if (currentIndex >= 0 && !imageFiles.isEmpty()) {
            statusLabel.setText("当前图片: " + (currentIndex + 1) + " / " + imageFiles.size());
        } else {
            statusLabel.setText(" ");
        }
    }

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

    // 处理鼠标滚轮事件
    private void handleMouseWheel(MouseWheelEvent e) {
        if (imageFiles.isEmpty()) return;

        // 获取滚轮旋转的方向
        int rotation = e.getWheelRotation();
        
        if (rotation > 0) {
            // 向下滚动，相当于右键功能（显示随机图片）
            showRandomImage();
        } else if (rotation < 0) {
            // 向上滚动，相当于左键功能（显示上一张图片）
            showPreviousImage();
        }
        
        // 消费事件，防止传递给父组件
        e.consume();
    }

    // 显示上一张浏览的照片
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

    private void showRandomImage() {
        if (imageFiles.size() <= 1) return;

        Random random = new Random();
        int newIndex;
        do {
            newIndex = random.nextInt(imageFiles.size());
        } while (newIndex == currentIndex && imageFiles.size() > 1);

        showImage(imageFiles.get(newIndex));
    }

    private void deleteCurrentImage() {
        if (currentIndex < 0 || currentIndex >= imageFiles.size()) return;

        File fileToDelete = imageFiles.get(currentIndex);

        try {
            // 检查是否为符号链接文件（不限于.symlink扩展名）
            Path filePath = fileToDelete.toPath();
            boolean isSymbolicLink = Files.isSymbolicLink(filePath);

            if (isSymbolicLink || fileToDelete.getName().toLowerCase().endsWith(".symlink")) {
                // 获取符号链接指向的目标文件
                File targetFile = null;
                if (isSymbolicLink) {
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
                    try {
                        Files.delete(targetFile.toPath());
                        System.out.println("已删除目标文件: " + targetFile.getAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("无法删除目标文件: " + e.getMessage());
                        // 继续尝试删除符号链接本身
                    }
                }

                // 删除符号链接文件本身
                try {
                    Files.delete(filePath);
                    System.out.println("已删除符号链接: " + fileToDelete.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("无法删除符号链接: " + e.getMessage());
                    JOptionPane.showMessageDialog(this, "无法删除符号链接文件: " + e.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                    return; // 如果符号链接删除失败，不继续执行
                }

            } else {
                // 对于普通文件，使用直接删除（不放入回收站）
                try {
                    Files.delete(filePath);
                    System.out.println("已删除普通文件: " + fileToDelete.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("无法删除普通文件: " + e.getMessage());
                    JOptionPane.showMessageDialog(this, "无法删除文件: " + e.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 从图像列表中移除已删除的文件
            imageFiles.remove(currentIndex);

            // 更新显示
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

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "删除文件时出错: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // 静默处理错误
        }

        SwingUtilities.invokeLater(() -> {
            ImageViewer viewer = new ImageViewer();
            viewer.setVisible(true);
        });
    }
}