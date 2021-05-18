package com.main;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;

public class Sorter {

    public void setMode(int i) {
        modType = modificationType.values()[i];

        System.out.println(modType);
    }

    private enum modificationType {
        COPY,
        MOVE;
    }

    private modificationType modType;
    private String outputLog;
    private FileFilter filter;
    private int panicCounter;
    private int dupeCounter;



    public Sorter() {

        makeDefaultFilter();
        modType = modificationType.COPY;

    }

    /** TODO- Add filtering functionality. Make array with filters and iterate thru
     *  Creates a file filter for the program.
     */
    public void makeDefaultFilter() {
        /** Generate new filter */
        this.filter = file -> {

            /** Value to be equated */
            if (file.getName().endsWith(".png") ||
                file.getName().endsWith(".csp") ||
                file.getName().endsWith(".gif") ||
                file.getName().endsWith(".jpg") ||
                file.isDirectory())    {

                return true;

            }

            return false;
        };
    }

    /**
     * Begins the sort process
     * @param src the source directory
     * @param dst the destination directory
     * @return a string containing a results log
     */
    public String sort(File src, File dst) {

        panicCounter = 0;
        dupeCounter = 0;
        this.outputLog = "";

        if (!isCompatible(src, dst)) {
            return outputLog;
        }

        /** Generate list of files in source directory to sort */
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(src.listFiles(filter)));

        /** Generate path of destination file */
        Path destinationPath = Paths.get(dst.getPath());

        /** Begin the sorting */
        sortHelper(files, destinationPath);

        outputLog += "Transferred " + panicCounter + " file(s) into " + dst.getPath() + " successfully!\n";

        if (dupeCounter > 0) {
            outputLog += "Found " + dupeCounter + " duplicate(s).";
        }

        return outputLog;
    }


    /**
     * TODO- Fill this with checks
     * Checks if the parameters are valid.
     * @return True if compatible, false otherwise
     */
    private boolean isCompatible(File src, File dst) {

        if (src.getAbsolutePath().equals(dst.getAbsolutePath())) {
            this.outputLog += "ERR\nSource and Destination cannot be identical!";
            return false;
        }



        return true;

    }


    /**
     * This will sort the files based on date created in a file system following
     * the guidelines of year/month
     * @param allFiles an array of all of the files to be sorted
     * @param targetDir the path of the master directory to send sorted files to
     * @return a boolean signifying success or failure
     */
    public boolean sortHelper(ArrayList<File> allFiles, Path targetDir) {
        try {
            for (File file: allFiles) {

                if (panicCounter >= 300) {
                    outputLog+= "ERR\nPanic overload triggered, over 300 files scanned!!!!";
                    return false;
                }

                /** Check that this is a file */
                if (file.isFile()) {

                    /** Get path of current file */
                    Path currPath = Paths.get(file.getPath());

                    /** Obtain metadata of current file */
                    BasicFileAttributes fileData = Files.readAttributes(currPath, BasicFileAttributes.class);
                    String timestamp = fileData.lastModifiedTime().toString();
                    String year = timestamp.substring(0, 4);
                    String month = timestamp.substring(5, 7);

                    /** Generate year based on metadata and append to the destination path */
                    String newDir = targetDir.toAbsolutePath() + "\\" + year;
                    File newDestination = new File(newDir);
                    Path newDestinationPath = Paths.get(newDir);

                    /** Make directory for year if required */
                    if (!Files.exists(newDestinationPath)) {
                        newDestination.mkdir();
                    }

                    /** Generate month based on metadata and append to the destination path */
                    newDir += "\\" + month;
                    newDestination = new File(newDir);
                    newDestinationPath = Paths.get(newDir);

                    /** Make directory for month if required */
                    if (!Files.exists(newDestinationPath)) {
                        newDestination.mkdir();
                    }

                    /** Append file's name to the new path */
                    newDir += "\\" + file.getName();
                    newDestinationPath = Paths.get(newDir);

                    File existenceCheck = new File(newDestinationPath.toString());
                    if (existenceCheck.exists()) {
                        dupeCounter++;
                        continue;
                    }

                    /** Move it over */
                    panicCounter++;
                    if (modType == modificationType.COPY) {
                        Files.copy(currPath, newDestinationPath);
                    } else if (modType == modificationType.MOVE) {
                        Files.move(currPath, newDestinationPath);
                    }


                } else if (file.isDirectory()) {


                    /**
                     * Recurse into new directory and report error if it exists.
                     */
                    ArrayList<File> files = new ArrayList<File>(Arrays.asList(file.listFiles(filter)));
                    if(!sortHelper(files, targetDir)) {
                        return false;
                    }


                }
            }

            return true;
        } catch (Exception e) {
            outputLog += "ERR\n" + e.getMessage();
            return false;
        }
    }


}
