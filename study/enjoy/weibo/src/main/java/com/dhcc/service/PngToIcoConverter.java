package com.dhcc.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import net.sf.image4j.codec.ico.ICOEncoder;

public class PngToIcoConverter {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java PngToIcoConverter <input.png> <output.ico>");
            System.exit(1);
        }
        
        String inputPath = args[0];
        String outputPath = args[1];
        
        try {
            // Read the PNG file
            BufferedImage image = ImageIO.read(new File(inputPath));
            
            // Convert and save as ICO using image4j library
            ICOEncoder.write(image, new File(outputPath));
            
            System.out.println("Successfully converted " + inputPath + " to " + outputPath);
        } catch (IOException e) {
            System.err.println("Error converting image: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}