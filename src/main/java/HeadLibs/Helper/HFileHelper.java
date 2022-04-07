package HeadLibs.Helper;

import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

/**
 * Some tools about {@link File}
 */
public class HFileHelper {
    /**
     * Delete directory recursively.
     * @param path directory path
     * @return true - success. false - failure.
     */
    public static boolean deleteDirectories(@NotNull String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory())
            return false;
        File[] files = file.listFiles();
        if (files == null)
            return file.delete();
        for (File i: files)
            if (i.isDirectory()) {
                if (!deleteDirectories(i.getPath()))
                    return false;
            }
            else
                if (!i.delete())
                    return false;
        return file.delete();
    }

    /**
     * Copy file to file. If you mean copy director, use {@link HFileHelper#copyFiles(String, String, boolean)}
     * @param sourcePath source file path
     * @param targetPath target file path
     * @param overwrite overwrite target file
     * @throws IOException Copy exception
     */
    public static void copyFile(@NotNull String sourcePath, @NotNull String targetPath, boolean overwrite) throws IOException {
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        if (!sourceFile.exists())
            throw new NoSuchFileException(HStringHelper.concat("Source file not exists. sourcePath='", sourcePath, "', targetPath='", targetPath, "'."));
        if (sourceFile.isDirectory() || (targetFile.exists() && targetFile.isDirectory())) {
            copyFiles(sourcePath, targetPath, overwrite);
            return;
        }
        if (targetFile.exists()) {
            if (!overwrite)
                return;
            if (!targetFile.delete())
                throw new IOException(HStringHelper.concat("Fail to delete existed target file. sourcePath='", sourcePath, "', targetPath='", targetPath, "'."));
        }
        Files.copy(sourceFile.toPath(), targetFile.toPath());
    }

    /**
     * Copy directory to directory. if you mean copy file, use {@link HFileHelper#copyFile(String, String, boolean)}
     * @param sourcePath source directory path
     * @param targetPath target directory path
     * @param overwrite overwrite target directory
     * @throws IOException copy exception
     */
    public static void copyFiles(@NotNull String sourcePath, @NotNull String targetPath, boolean overwrite) throws IOException {
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        if (!sourceFile.exists())
            throw new NoSuchFileException(HStringHelper.concat("Source file not exists. sourcePath='", sourcePath, "', targetPath='", targetPath, "'."));
        if (sourceFile.isFile()) {
            if (targetFile.exists()) {
                if (targetFile.isDirectory())
                    throw new IOException(HStringHelper.concat("Target file is directory. sourcePath='", sourcePath, "', targetPath='", targetPath, "'."));
                if (overwrite)
                    copyFile(sourcePath, targetPath, true);
                return;
            }
            copyFile(sourcePath, targetPath, overwrite);
            return;
        }
        if (targetFile.exists()) {
            if (targetFile.isFile() && overwrite && !targetFile.delete())
                throw new IOException(HStringHelper.concat("Fail to delete existed target file. sourcePath='", sourcePath, "', targetPath='", targetPath, "'."));
            }
        if (!targetFile.exists() && !targetFile.mkdirs())
            throw new IOException(HStringHelper.concat("Fail to make directories to create target file. sourcePath='", sourcePath, "', targetPath='", targetPath, "'."));
        File[] files = sourceFile.listFiles();
        if (files == null)
            return;
        for (File file: files) {
            File newFile = new File(HStringHelper.concat(targetFile.getPath(), "\\", file.getName()));
            copyFiles(file.getPath(), newFile.getPath(), overwrite);
        }
    }

    /**
     * Create a new file and check I/O access.
     * @param path file path
     * @throws IOException create failed
     */
    public static void createNewFile(@Nullable String path) throws IOException {
        if (path == null)
            throw new IOException("Argument file path is null.");
        File file = new File(path).getAbsoluteFile();
        if (!file.exists()) {
            if(!file.getParentFile().exists() && !file.getParentFile().mkdirs())
                throw new IOException("Creating directories failed.");
            if(!file.createNewFile())
                throw new IOException("Creating file failed.");
        }
        if (!file.isFile())
            throw new IOException(HStringHelper.concat("Argument path is invalid. [path='", path, "']"));
        if (!file.canRead()) {
            if (!file.setReadable(true))
                throw new IOException(HStringHelper.concat("File in path can't be read. [path='", path, "']"));
            HLog.logger(HLogLevel.CONFIGURATION, HStringHelper.concat("File in path has been set to readable. [path='", path, "']"));
        }
        if (!file.canWrite()) {
            if (!file.setWritable(true))
                throw new IOException(HStringHelper.concat("File in path can't be written. [path='", path, "']"));
            HLog.logger(HLogLevel.CONFIGURATION, HStringHelper.concat("File in path has been set to writable. [path='", path, "']"));
        }
    }
}
