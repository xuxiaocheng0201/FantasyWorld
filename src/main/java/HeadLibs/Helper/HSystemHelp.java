package HeadLibs.Helper;

public class HSystemHelp {
    public static boolean isJVM64Bit() {
        String temp = System.getProperty("sun.arch.data.model");
        if (temp != null && temp.contains("64"))
            return true;
        temp = System.getProperty("os.arch");
        if (temp != null && temp.contains("64"))
            return true;
        temp = System.getProperty("com.ibm.vm.bitmode");
        return (temp != null && temp.contains("64"));
    }

    public static String getRunningType() {
        String temp = System.getProperty("os.name").toLowerCase();
        if (temp.contains("win"))
            return "WINDOWS";
        if (temp.contains("mac"))
            return "OSX";
        if (temp.contains("solaris"))
            return "SOLARIS";
        if (temp.contains("sunos"))
            return "SOLARIS";
        if (temp.contains("linux"))
            return "LINUX";
        if (temp.contains("unix"))
            return "LINUX";
        return "UNKNOWN";
    }
}
