package CraftWorld.Utils;

import HeadLibs.Helper.HFileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SevenZipUtils {
    public static void compressFiles(String outputFilePath, String ...inputFilePaths) {
        List<File> inputFiles = new ArrayList<>();
        for (String inputFilePath: inputFilePaths)
            if (HFileHelper.checkFileAvailable(inputFilePath))
                inputFiles.add(new File(inputFilePath));

    }
}
