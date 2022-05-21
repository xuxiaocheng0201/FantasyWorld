package CraftWorld.Utils;

import HeadLibs.Helper.HFileHelper;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.*;

public class SevenZipUtils {
    public static final int BUFFER_SIZE = 1024;

    public static void compressFile(String inputFilePath, String outputFilePath) throws IOException {
        if (!HFileHelper.checkFileAvailable(inputFilePath) && !HFileHelper.checkDirectoryAvailable(inputFilePath))
            throw new IOException("Unavailable input file for compressing. inputFilePath: " + inputFilePath);
        HFileHelper.createNewFile(outputFilePath);
        SevenZOutputFile sevenZOutputFile = new SevenZOutputFile(new File(outputFilePath));
        compress(sevenZOutputFile, new File(inputFilePath), null);
        sevenZOutputFile.close();
    }

    private static void compress(SevenZOutputFile sevenZOutputFile, File inputFile, String nameIn) throws IOException {
        String name = nameIn;
        if (name == null)
            name = inputFile.getName();
        if (inputFile.isDirectory()) {
            File[] files = inputFile.listFiles();
            if (files == null || files.length == 0)
                sevenZOutputFile.putArchiveEntry(sevenZOutputFile.createArchiveEntry(inputFile, name + '\\'));
            else
                for (File file: files)
                    compress(sevenZOutputFile, file, name + '\\' + file.getName());
        } else {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
            sevenZOutputFile.putArchiveEntry(sevenZOutputFile.createArchiveEntry(inputFile, name));
            int len;
            byte[] buf = new byte[BUFFER_SIZE];
            while ((len = inputStream.read(buf)) != -1)
                sevenZOutputFile.write(buf, 0, len);
            inputStream.close();
            sevenZOutputFile.closeArchiveEntry();
        }
    }

    public static void uncompressFile(String inputFilePath, String outputFilePath, char[] password) throws IOException {
        if (!HFileHelper.checkFileAvailable(inputFilePath))
            throw new IOException("Unavailable input file for uncompressing. inputFilePath: " + inputFilePath);
        SevenZFile sevenZFile = new SevenZFile(new File(inputFilePath), password);
        SevenZArchiveEntry sevenZArchiveEntry;
        while ((sevenZArchiveEntry = sevenZFile.getNextEntry()) != null)
            if (!sevenZArchiveEntry.isDirectory()) {
                File file = new File(outputFilePath, sevenZArchiveEntry.getName());
                HFileHelper.createNewFile(file.getPath());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                int len;
                byte[] buf = new byte[BUFFER_SIZE];
                while ((len = sevenZFile.read(buf)) != -1)
                    outputStream.write(buf, 0, len);
                outputStream.close();
            }
    }
}
