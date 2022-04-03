package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;

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
}
