package HeadLibs.Helper;

import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Objects;
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
     * @throws IOException i/o exception
     */
    public static void zip(@NotNull File directory, @NotNull File zipFile) throws IOException {
        File directory1 = directory;
        URI base = directory1.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(directory1);
        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        while (!queue.isEmpty()) {
            directory1 = queue.pop();
            for (File kid: Objects.requireNonNull(directory1.listFiles())) {
                String name = base.relativize(kid.toURI()).getPath();
                if (kid.isDirectory()) {
                    queue.push(kid);
                    name = !name.isEmpty() && name.charAt(name.length() - 1) == '/' ? name : name + "/";
                    outputStream.putNextEntry(new ZipEntry(name));
                } else {
                    outputStream.putNextEntry(new ZipEntry(name));
                    Files.copy(kid.toPath(), outputStream);
                    outputStream.closeEntry();
                }
            }
        }
        outputStream.close();
    }

    /**
     * Extract files from jar file
     * @param jar Source jar file
     * @param sourceFileInJar file path in jar file
     * @param targetDir target directory
     */
    public static void extractFilesFromJar(@NotNull JarFile jar, @NotNull String sourceFileInJar, String targetDir) {
        try {
            Enumeration<JarEntry> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String name = jarEntry.getName();
                if (!name.startsWith(sourceFileInJar))
                    continue;
                name = name.replace(sourceFileInJar, "");
                File file = new File(HStringHelper.concat(targetDir, File.separator, name));
                if (jarEntry.isDirectory()) {
                    if (!file.mkdirs())
                        HLog.logger(HLogLevel.ERROR, "Making directories failed!");
                    continue;
                }
                InputStream input = jar.getInputStream(jarEntry);
                FileOutputStream output = new FileOutputStream(file);
                while (input.available() > 0)
                    output.write(input.read());
                input.close();
                output.close();
            }
        } catch (IOException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
}
