package HeadLibs;

import java.io.File;
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
    private boolean recursive = true;
    private final List<Class<?>> classList = new ArrayList<>();

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

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
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

    private void checkAndAddClass(Class<?> aClass) {
        //TODO
        classList.add(aClass);
    }

    public void startFind() {
        try {
            Enumeration<URL> urls = this.classLoader.getResources(this.PACKAGE_PATH.replaceAll("\\.", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url == null)
                    continue;
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    addClass(url.getPath(), this.PACKAGE_PATH);
                }
                if ("jar".equals(protocol)) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    Enumeration<JarEntry> jarEntries = jarFile.entries();
                    while (jarEntries.hasMoreElements()) {
                        JarEntry jarEntry = jarEntries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class")) {
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                            if (this.recursive || className.substring(0, className.lastIndexOf(".")).equals(this.PACKAGE_PATH))
                                checkAndAddClass(Class.forName(className));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClass(String packagePath, String packageName) {
        try {
            File[] files = new File(packagePath).listFiles((File file) -> {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            });
            if (files != null) {
                for (File file: files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = getClassName(packageName, fileName);
                        checkAndAddClass(Class.forName(className));
                    } else {
                        if (this.recursive) {
                            String subPackagePath = getSubPackagePath(packagePath, fileName);
                            String subPackageName = getSubPackageName(packageName, fileName);
                            addClass(subPackagePath, subPackageName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getClassName(String packageName, String fileName) {
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        if (packageName != null && !packageName.isBlank()) {
            className = packageName + "." + className;
        }
        return className;
    }

    private static String getSubPackagePath(String packagePath, String filePath) {
        String subPackagePath = filePath;
        if (packagePath != null && !packagePath.isBlank())
            subPackagePath = packagePath + "/" + subPackagePath;
        return subPackagePath;
    }

    private static String getSubPackageName(String packageName, String filePath) {
        String subPackageName = filePath;
        if (packageName != null && !packageName.isBlank())
            subPackageName = packageName + "." + subPackageName;
        return subPackageName;
    }
}
