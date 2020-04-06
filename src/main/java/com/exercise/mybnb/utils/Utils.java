package com.exercise.mybnb.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    // no slash "/" is given
    private static String mainPath = "/home/teomandi/bnb_app_photos/";

    public static void storeImage(String filename, byte[] content) throws IOException {
        Path path = Paths.get(mainPath + filename);
        Files.write(path, content);
        System.out.println("Image stored");
    }

    public static boolean makeDir(int placeID){
        return new File(mainPath + "places/" + placeID).mkdir();
    }

    public static boolean deletePlaceImage(String filename){
        File file = new File(mainPath + "places/" +filename);
        return file.delete();
    }


}
