package ru.ncedu.lapshov.arc;

import java.io.File;
import java.util.ArrayList;

/**
 * This interface was written for better planning of the solution
 */
public interface Archivable {

    boolean unzipArchive(File archiveName, File directory);

    boolean zipFiles(String archiveName, ArrayList<File> files, String comments);

    boolean zipDirectory(File directory, String filename, String comments);

    /**
     * The next 3 methods aren't used in my program, but implemented
     */
    boolean addFileInArchive(File archiveName, File fileToAdd);

    String readComments(File archiveName);

    boolean addComments(File archiveName, String comments);
}
