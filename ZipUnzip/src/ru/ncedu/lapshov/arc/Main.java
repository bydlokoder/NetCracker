package ru.ncedu.lapshov.arc;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    static ArrayList<File> files = new ArrayList<File>();

    enum types {ZIP_DIRECTORY, UNZIP_FILES, ZIP_FILES}

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            new A();
        }
    }

    public static void start(String[] args) {
        ZipUnzip zipUnzip = new ZipUnzip();

        if (args.length != 0) {
            File f = new File(args[0]);
            if (args.length == 1) {
                if (f.isFile()) {//unzip file
                    printResult(zipUnzip.unzipArchive(f, new File("")), types.UNZIP_FILES);
                } else if (f.isDirectory()) {//zip directory
                    printResult(zipUnzip.zipDirectory(f, "ZippedDirectory", "Created by Lapshov Ivan"), types.ZIP_DIRECTORY);
                } else {
                    System.out.println("Incorrect input");
                }
            } else if (args.length >= 2) {
                File file2 = new File(args[1]);
                if (file2.isFile()) {//zip files
                    createFileList(args);
                    printResult(zipUnzip.zipFiles(f.toString(), files, "Created by Lapshov Ivan"), types.ZIP_DIRECTORY);

                } else if (f.isFile()) {//unzip files
                    printResult(zipUnzip.unzipArchive(f, file2), types.UNZIP_FILES);
                }

            } else {
                System.out.println("Arguments are required");
            }
        }
    }

    private static void createFileList(String[] args) {
        for (int i = 1; i < args.length; i++) {
            File file = new File(args[i]);
            if (file.exists()) {
                files.add(file);
            }
        }
    }

    private static void printResult(boolean result, types type) {

        switch (type) {
            case ZIP_DIRECTORY:
                if (result) {
                    System.out.println("Directory is zipped successfully");
                } else {
                    System.out.println("Error occurred during zipping the directory");
                }
                break;
            case UNZIP_FILES:
                if (result) {
                    System.out.println("Archive is unzipped successfully");
                } else {
                    System.out.println("Error occurred during unzipping the file");
                }
                break;
            case ZIP_FILES:
                if (result) {
                    System.out.println("Files are zipped successfully");
                } else {
                    System.out.println("Error occurred during zipping the files");
                }
                break;
        }
    }
}

