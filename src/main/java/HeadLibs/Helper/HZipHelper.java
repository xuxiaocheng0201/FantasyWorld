package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Some tools about zip/jar file.
 */
public class HZipHelper {
    /**
     * Zip a directory to zip file.
     * @param directory source directory
     * @param zipFile target zip file
     * @throws IOException I/O failed.
     */
    public static void zip(@NotNull File directory, @NotNull File zipFile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(directory);
        try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            while (!queue.isEmpty()) {
                File directory1 = queue.pop();
                File[] files = directory1.listFiles();
                if (files == null)
                    continue;
                for (File kid : files) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = !name.isEmpty() && name.charAt(name.length() - 1) == '/' ? name : name + '/';
                        outputStream.putNextEntry(new ZipEntry(name));
                    } else {
                        outputStream.putNextEntry(new ZipEntry(name));
                        Files.copy(kid.toPath(), outputStream);
                        outputStream.closeEntry();
                    }
                }
            }
        }
    }

    /**
     * Extract files from jar file.
     * @param jarFilePath source jar file
     * @param sourceFilePathInJar file path in jar file
     * @param targetDirectory target directory path
     * @throws IOException I/O failed.
     */
    public static void extractFilesFromJar(@Nullable String jarFilePath, @Nullable String sourceFilePathInJar, @Nullable String targetDirectory) throws IOException {
        if (jarFilePath == null)
            throw new IOException("Null jar file path.");
        if (targetDirectory == null)
            throw new IOException("Null target directory.");
        String sourceFileInJar = sourceFilePathInJar == null ? "" : sourceFilePathInJar.replace('\\', '/');
        HFileHelper.createNewDirectory(targetDirectory);
        try (JarFile jar = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String name = jarEntry.getName();
                if (!name.startsWith(sourceFileInJar))
                    continue;
                name = name.substring(sourceFileInJar.length());
                File file = new File(targetDirectory + File.separator + name);
                if (jarEntry.isDirectory()) {
                    HFileHelper.createNewDirectory(file.getPath());
                    continue;
                }
                try (InputStream input = jar.getInputStream(jarEntry)) {
                    try (FileOutputStream output = new FileOutputStream(file)) {
                        while (input.available() > 0)
                            output.write(input.read());
                    }
                }
            }
        }
    }
}
