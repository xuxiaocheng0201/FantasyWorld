package HeadLibs;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URI;
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
        URL url = classLoader.getResource(PACKAGE_PATH.replace('.', '/'));
        if (url == null)
            throw new RuntimeException("Find class failed.");
        String protocol = url.getProtocol();
        if ("file".equals(protocol))
            findClassLocal(PACKAGE_PATH);
        if ("jar".equals(protocol))
            findClassJar(PACKAGE_PATH);
    }

    private void findClassLocal(String packName){
        URI uri;
        try {
            URL url = classLoader.getResource(packName.replace('.', '/'));
            if (url == null)
                throw new RuntimeException("Find class failed.");
            uri = url.toURI();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
        File[] files = (new File(uri)).listFiles((chiFile) -> {
            if(chiFile.isDirectory())
                findClassLocal(packName + "." + chiFile.getName());
            if(chiFile.getName().endsWith(".class")) {
                try {
                    Class<?> aClass = classLoader.loadClass(packName + "." + chiFile.getName().replace(".class", ""));
                    if(superClass.isAssignableFrom(aClass)){
                        classList.add(aClass);
                    }
                    return true;
                } catch (ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            }
            return false;
        });
        HLog.logger(HELogLevel.DEBUG, "Find Classes length: ", files == null ? 0 : files.length);
    }

    private void findClassJar(String packName){
        if (packName == null)
            return;
        String pathName = packName.replace('.', '/');
        JarFile jarFile;
        try {
            URL url = classLoader.getResource(pathName);
            if (url == null)
                throw new RuntimeException("Find class failed.");
            JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();
            if(jarEntryName.contains(pathName) && !jarEntryName.equals(pathName+"/")) {
                if(jarEntry.isDirectory()) {
                    String clazzName = jarEntry.getName().replace('/', '.');
                    int endIndex = clazzName.lastIndexOf('.');
                    String prefix = null;
                    if (endIndex > 0)
                        prefix = clazzName.substring(0, endIndex);
                    findClassJar(prefix);
                }
                if(jarEntry.getName().endsWith(".class")) {
                    try {
                        Class<?> aClass = classLoader.loadClass(jarEntry.getName().replace('/', '.').replace(".class", ""));
                        if(superClass.isAssignableFrom(aClass))
                            classList.add(aClass);
                    } catch (ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }
}
