package ru.ncedu.lapshov.arc;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * This class allows do operations such as:
 * unzipping an archive, zipping files into an archive, add file in existing archive, add all files in directory to archive
 * <p/>
 * Also, you can read and add comments in existing zip files and create zip file with comments (pass comments as a parameter of method)
 */
public class ZipUnzip implements Archivable {
    private URI baseURI = null;

    /**
     * This method extract files from existing archive
     *
     * @param archiveName The file - archive
     * @param directory   Directory for extracting (optional)
     * @return <code>true</code>  if operation was done successfully
     */
    @Override
    public boolean unzipArchive(File archiveName, File directory) {
        File realDirectory;
        if (directory.isDirectory() || directory.mkdirs()) {
            realDirectory = directory;
        } else {
            realDirectory = new File("");
        }
        boolean result = false;
        try {
            ZipFile zipFile = new ZipFile(archiveName);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();

                File file = new File(realDirectory.getAbsolutePath() + File.separator + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();//create all directories
                    file.createNewFile();

                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    copy(inputStream, fileOutputStream);
                    inputStream.close();
                    fileOutputStream.close();
                }
            }
            result = true;

        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * This method creates an archive with files
     *
     * @param archiveName The name of the archive
     * @param files       ArrayList of files to be added into archive
     * @param comments    Comments for adding
     * @return <code>true</code> if operation was done successfully
     */
    @Override
    public boolean zipFiles(String archiveName, ArrayList<File> files, String comments) {
        boolean result;
        archiveName = archiveName + ".zip";
        ZipOutputStream zipOutputStream = null;
        try {
            String path = new File("").getAbsolutePath() + File.separator + archiveName;
            FileOutputStream fos = new FileOutputStream(path);
            zipOutputStream = new ZipOutputStream(fos);
            if (comments != null && !comments.equals("")) {
                zipOutputStream.setComment(comments);
            }
            for (File file : files) {
                addFile(file, zipOutputStream, false);//add files without creating folders
            }
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * This method creates an archive with all files (include subdirectories) in specified directory
     *
     * @param directory       Directory
     * @param zipOutputStream Stream to write data
     * @return <code>true</code> if operation was done successfully
     */

    private boolean zipDir(File directory, ZipOutputStream zipOutputStream) {
        boolean result;
        try {
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isDirectory()) {
                            zipDir(f, zipOutputStream);
                        } else {
                            addFile(f, zipOutputStream, true);//add file relatively
                        }
                    }
                }
            }
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } catch (StackOverflowError e1) {
            e1.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * This method runs zipping directory recursively
     *
     * @param directory Directory
     * @param filename  The name of output file
     * @param comments  Comments for adding
     * @return <code>true</code> if operation was done successfully
     */
    @Override
    public boolean zipDirectory(File directory, String filename, String comments) {
        boolean result = false;
        ZipOutputStream zos = null;
        if (directory.isDirectory()) {
            try {
                baseURI = directory.toURI();
                filename = filename + ".zip";
                zos = new ZipOutputStream(new FileOutputStream(filename));
                if (comments != null && !comments.equals("")) {
                    zos.setComment(comments);
                }
                result = zipDir(directory, zos);
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            } finally {
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    /**
     * This method adds file in existing archive
     *
     * @param archiveName Existing archive
     * @param fileToAdd   File to be added
     * @return <code>true</code> if operation was done successfully
     */
    @Override
    public boolean addFileInArchive(File archiveName, File fileToAdd) {
        boolean result = false;
        FileSystem zipfs = null;
      /* Define ZIP File System Properies in HashMap */
        Map<String, String> zip_properties = new HashMap<String, String>();
        /* We want to read an existing ZIP File, so we set this to False */
        zip_properties.put("create", "false");
        zip_properties.put("encoding", "UTF-8");

        /* Specify the path to the ZIP File that you want to read as a File System */

        try {
            URI jar = new URI("jar", archiveName.toURI().toString(), null);
            System.out.println(jar.toString());
            zipfs = FileSystems.newFileSystem(jar, zip_properties);
            /* Path inside ZIP File */
            Path pathInZipfile = zipfs.getPath(fileToAdd.getName());
            /* Add file to archive */
            Files.copy(fileToAdd.toPath(), pathInZipfile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (zipfs != null) {
                try {
                    zipfs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;

    }

    /**
     * This method reads comments from archive
     *
     * @param archive Existing archive
     * @return <code>String result</code> that contains comments
     */
    @Override
    public String readComments(File archive) {
        String result = null;
        try {
            ZipFile zipFile = new ZipFile(archive);
            result = zipFile.getComment();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method adds comments to an archive
     *
     * @param archive  Existing archive
     * @param comments Comments for adding
     * @return <code>true</code> if operation was done successfully
     */
    @Override
    public boolean addComments(File archive, String comments) {
        boolean result = false;
        ZipOutputStream zipOutputStream = null;
        if (comments != null && !comments.equals("")) {
            try {
                zipOutputStream = new ZipOutputStream(new FileOutputStream(archive, true));
                zipOutputStream.setComment(comments);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (zipOutputStream != null) {
                    try {
                        zipOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
        return result;
    }

    /**
     * This methods write a file in zipOutputStream
     *
     * @param file            File to write
     * @param zipOutputStream Stream for writing
     * @param relative        if <code>true</code> file will be added relative to base folder, otherwise name of file will be used
     * @throws java.io.IOException
     */
    private void addFile(File file, ZipOutputStream zipOutputStream, boolean relative) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(file);
        String path;
        if (relative) {
            path = baseURI.relativize(file.toURI()).getPath();
        } else {
            path = file.getName();
        }
        ZipEntry zipEntry = new ZipEntry(path);
        zipOutputStream.putNextEntry(zipEntry);

        copy(fileInputStream, zipOutputStream);

        zipOutputStream.closeEntry();
        fileInputStream.close();

    }

    /**
     * This method copy data from input stream and save them in output stream
     *
     * @param inputStream  Input Stream
     * @param outputStream Output Stream
     * @throws java.io.IOException
     */
    private void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {//read by the EOF
            outputStream.write(buf, 0, len);
        }
    }
}
