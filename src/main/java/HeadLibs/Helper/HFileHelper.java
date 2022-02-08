package HeadLibs.Helper;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.File;
import java.io.IOException;

public class HFileHelper {
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
}
