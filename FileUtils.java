package ceit.aut.ac.ir.utils;

import ceit.aut.ac.ir.model.Note;

import javax.swing.*;
import java.io.*;

public class FileUtils {

    private static final String NOTES_PATH = "./notes/";

    //It's a static initializer. It's executed when the class is loaded.
    //It's similar to constructor
    static {
        boolean isSuccessful = new File(NOTES_PATH).mkdirs();
        System.out.println("Creating " + NOTES_PATH + " directory is successful: " + isSuccessful);
    }

    public static File[] getFilesInDirectory() {
        return new File(NOTES_PATH).listFiles();
    }


    public static String fileReader(File file) {
        //TODO: Phase1: read content from file
        String result = "";
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int i =0;
            while ((i=bufferedReader.read())!=-1)
            {
                char in = (char)i;
                result = result + Character.toString(in);
            }
            bufferedReader.close();
            fileReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void fileWriter(String content) {
        //TODO: write content on file
        String fileName = getProperFileName(content);
        try {
            FileWriter writer = new FileWriter(NOTES_PATH+fileName+".txt");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileExistWriter(String content,String tabName) {
        //TODO: write content on file
        String fileName = tabName;
        try {
            FileWriter writer = new FileWriter(NOTES_PATH+fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //TODO: Phase1: define method here for reading file with InputStream
    public static String readFileStream(File file)
    {
        String result = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int i = 0;
            while ((i=fileInputStream.read())!=-1)
            {
                char in = (char)i;
                result = result + Character.toString(in);
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //TODO: Phase1: define method here for writing file with OutputStream
    public static void writeFileStream(String content) {
        //TODO: write content on file
        String fileName = getProperFileName(content);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(NOTES_PATH+fileName);
            byte[] strToByteArray = content.getBytes();
            fileOutputStream.write(strToByteArray);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: Phase2: proper methods for handling serialization
    public static void serializeData(Note note)
    {
        String fileName = getProperFileName(note.getContent());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(NOTES_PATH+fileName+".note");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(note);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Note deserializeData(File file)
    {
        Note result = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            result = (Note)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }


    private static String getProperFileName(String content) {
        int loc = content.indexOf("\n");
        if (loc != -1) {
            return content.substring(0, loc);
        }
        if (!content.isEmpty()) {
            return content;
        }
        return System.currentTimeMillis() + "_new file.txt";
    }
}

