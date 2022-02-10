package HeadLibs.Helper;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HFileHelper {
    public static boolean deleteDirectories(String path) {
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

    public static boolean createNewFile(String path) {
        try {
            File file = new File(path).getAbsoluteFile();
            if (!file.exists()) {
                if(!file.getParentFile().exists() && !file.getParentFile().mkdirs())
                    throw new IOException("Creating directories failed.");
                if(!file.createNewFile())
                    throw new IOException("Creating file failed.");
            }
            if (!file.isFile())
                throw new IOException(HStringHelper.merge("Argument path is invalid. [path='", path, "']"));
            if (!file.canRead()) {
                if (!file.setReadable(true))
                    throw new IOException(HStringHelper.merge("File in path can't be read. [path='", path, "']"));
                HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("File in path has been set to readable. [path='", path, "']"));
            }
            if (!file.canWrite()) {
                if (!file.setWritable(true))
                    throw new IOException(HStringHelper.merge("File in path can't be written. [path='", path, "']"));
                HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("File in path has been set to writable. [path='", path, "']"));
            }
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static void extractFilesFromJar(String sourceFileInJar, String targetDir) {
        String jarPath = System.getProperty("java.class.path");
        if (HStringHelper.isBlank(jarPath) || !jarPath.endsWith(".jar"))
            throw new NullPointerException("Get jar path failed!");
        try {
            JarFile jar = new JarFile(jarPath);
            Enumeration<JarEntry> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String name = jarEntry.getName();
                if (!name.startsWith(sourceFileInJar))
                    continue;
                name = name.replace(sourceFileInJar, "");
                File file = new File(HStringHelper.merge(targetDir, File.separator, name));
                if (jarEntry.isDirectory()) {
                    if (!file.mkdirs())
                        HLog.logger(HELogLevel.ERROR, "Making directories failed!");
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
            exception.printStackTrace();
        }
    }
}
