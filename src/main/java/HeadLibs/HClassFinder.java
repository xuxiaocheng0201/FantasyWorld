package HeadLibs;

import HeadLibs.Logger.HLog;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HClassFinder {
    private String PACKAGE_PATH;
    private Class<?> superClass = Object.class;
    private ClassLoader classLoader = HClassFinder.class.getClassLoader();
    private List<Class<?>> classList = new ArrayList<>();

    public HClassFinder() {
        PACKAGE_PATH = "";
        startFind();
    }

    public HClassFinder(Class<?> superClass) {
        setSuperClass(superClass);
        startFind();
    }

    public HClassFinder(ClassLoader classLoader) {
        setClassLoader(classLoader);
        startFind();
    }

    public HClassFinder(Class<?> superClass, ClassLoader classLoader) {
        setSuperClass(superClass);
        setClassLoader(classLoader);
        startFind();
    }

    public HClassFinder(String PACKAGE_PATH) {
        setPACKAGE_PATH(PACKAGE_PATH);
        startFind();
    }

    public HClassFinder(String PACKAGE_PATH, Class<?> superClass) {
        setPACKAGE_PATH(PACKAGE_PATH);
        setSuperClass(superClass);
        startFind();
    }

    public HClassFinder(String PACKAGE_PATH, ClassLoader classLoader) {
        setPACKAGE_PATH(PACKAGE_PATH);
        setClassLoader(classLoader);
        startFind();
    }

    public HClassFinder(String PACKAGE_PATH, Class<?> superClass, ClassLoader classLoader) {
        setPACKAGE_PATH(PACKAGE_PATH);
        setSuperClass(superClass);
        setClassLoader(classLoader);
        startFind();
    }

    public Class<?> getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Class<?> superClass) {
        if (superClass == null) {
            this.superClass = Object.class;
            return;
        }
        this.superClass = superClass;
    }

    public List<Class<?>> getClassList() {
        return classList;
    }

    public void clearClassList() {
        classList.clear();
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            this.classLoader = HClassFinder.class.getClassLoader();
            return;
        }
        this.classLoader = classLoader;
    }

    public String getPACKAGE_PATH() {
        return PACKAGE_PATH;
    }

    public void setPACKAGE_PATH(String PACKAGE_PATH) {
        if (PACKAGE_PATH == null) {
            this.PACKAGE_PATH = "";
            return;
        }
        this.PACKAGE_PATH = PACKAGE_PATH;
    }

    public void startFind() {
        this.classList = getClassList(this.PACKAGE_PATH, true);
    }

    public static List<Class<?>> getClassList(String packageName, boolean isRecursive) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
    HLog.logger(url, "     ", url.getProtocol());
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath();
                        addClass(classList, packagePath, packageName, isRecursive);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            if (jarEntryName.endsWith(".class")) {
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                if (isRecursive || className.substring(0, className.lastIndexOf(".")).equals(packageName)) {
                                    classList.add(Class.forName(className));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    private static void addClass(List<Class<?>> classList, String packagePath, String packageName, boolean isRecursive) {
        try {
            File[] files = getClassFiles(packagePath);
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = getClassName(packageName, fileName);
                        classList.add(Class.forName(className));
                    } else {
                        if (isRecursive) {
                            String subPackagePath = getSubPackagePath(packagePath, fileName);
                            String subPackageName = getSubPackageName(packageName, fileName);
                            addClass(classList, subPackagePath, subPackageName, isRecursive);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }private static File[] getClassFiles(String packagePath) {
        return new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
    }
    private static String getClassName(String packageName, String fileName) {
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        if (StringUtil.isNotEmpty(packageName)) {
            className = packageName + "." + className;
        }
        return className;
    }
    private static String getSubPackagePath(String packagePath, String filePath) {
        String subPackagePath = filePath;
        if (StringUtil.isNotEmpty(packagePath)) {
            subPackagePath = packagePath + "/" + subPackagePath;
        }
        return subPackagePath;
    }
    private static String getSubPackageName(String packageName, String filePath) {
        String subPackageName = filePath;
        if (StringUtil.isNotEmpty(packageName)) {
            subPackageName = packageName + "." + subPackageName;
        }
        return subPackageName;
    }

    private static class StringUtil {
        public static boolean isNotEmpty(String a) {
            if (a == null)
                return false;
            return !a.isBlank();
        }
    }
}
