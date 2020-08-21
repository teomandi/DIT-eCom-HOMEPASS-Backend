package com.exercise.mybnb.utils;

import com.exercise.mybnb.model.Place;
import com.exercise.mybnb.model.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    // no slash "/" is given
    private static String mainPath;

    public static void setMainPath(String mainPath) {
        Utils.mainPath = mainPath;
    }

    public static String getMainPath(){
        return mainPath;
    }

    public static void storeImage(String filename, byte[] content) throws IOException {
        Path path = Paths.get(mainPath + filename);
        Files.write(path, content);
        System.out.println("Image stored: " + mainPath + filename );
    }

    public static void storeImageInGallery(String fullPath, byte[] content) throws IOException {
        System.out.println("Full path: " + fullPath);
        Path path = Paths.get(fullPath);
        Files.write(path, content);
        System.out.println("Image stored in gallery: " + fullPath);
    }

    public static byte[] getUserImageBytes(User u) throws IOException {
        String imagePath = null;
        String imageName = u.getImageName();
        String username = u.getUsername();
        if(u.getImageName().equals("default_user.jpg"))
            imagePath = mainPath + imageName;
        else
            imagePath = mainPath + "users/" + imageName;

        System.out.println("User: " + username + " image-path: " + imagePath);
        File fimg = new File(imagePath);
        return FileUtils.readFileToByteArray(fimg);
    }

    public static boolean makeDir(int placeID){
        return new File(mainPath + "places/" + placeID).mkdir();
    }

    public static byte[] getImageBytes(Place place, String filename) throws IOException {
        String imagePath = Utils.getMainPath() + "places/" + place.getId() + "/" + filename;
        File fimg = new File(imagePath);
        System.out.println("Deleted: " + filename);
        return FileUtils.readFileToByteArray(fimg);
    }

    public static boolean deleteImage(String filepath){
        return new File(filepath).delete();
    }


}
