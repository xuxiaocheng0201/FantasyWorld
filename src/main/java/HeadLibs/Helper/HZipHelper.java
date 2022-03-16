package HeadLibs.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class HZipHelper {
    public static void zip(File directory, File zipFile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(directory);
        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        while (!queue.isEmpty()) {
            directory = queue.pop();
            for (File kid: Objects.requireNonNull(directory.listFiles())) {
                String name = base.relativize(kid.toURI()).getPath();
                if (kid.isDirectory()) {
                    queue.push(kid);
                    name = name.endsWith("/") ? name : name + "/";
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
}
