package HeadLibs;

import HeadLibs.Helper.HStringHelper;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HClassFinder {
    private String PACKAGE_PATH;
    private final List<File> jarFiles = new ArrayList<>();
    private final List<Class<?>> superClass = new ArrayList<>();
    private final List<Class<? extends Annotation>> annotationClass = new ArrayList<>();
    private boolean recursive = true;
    private final List<Class<?>> classList = new ArrayList<>();

    public static final File thisCodePath = new File(HClassFinder.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    public HClassFinder() {
        PACKAGE_PATH = "";
        addJarFile(thisCodePath);
    }

    public HClassFinder(String PACKAGE_PATH) {
        setPACKAGE_PATH(PACKAGE_PATH);
        addJarFile(thisCodePath);
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

    public List<File> getJarFiles() {
        return jarFiles;
    }

    public void addJarFile(File jarFile) {
        if (jarFile == null || !jarFile.exists() || jarFiles.contains(jarFile))
            return;
        jarFiles.add(jarFile);
    }

    public void addJarFilesInDirectory(File directory) {
        if (directory == null || !directory.exists())
            return;
        if (directory.isFile())
            addJarFile(directory);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files == null)
                return;
            for (File file: files)
                addJarFile(file);
        }
    }

    public List<Class<?>> getSuperClass() {
        return superClass;
    }

    @SuppressWarnings("unchecked")
    public void addSuperClass(Class<?> superClass) {
        if (superClass == null || this.superClass.contains(superClass))
            return;
        if (superClass.isAnnotation()) {
            addAnnotationClass((Class<? extends Annotation>) superClass);
            return;
        }
        this.superClass.add(superClass);
    }

    public List<Class<? extends Annotation>> getAnnotationClass() {
        return annotationClass;
    }

    public void addAnnotationClass(Class<? extends Annotation> annotation) {
        if (annotation == null || this.annotationClass.contains(annotation))
            return;
        this.annotationClass.add(annotation);
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public List<Class<?>> getClassList() {
        return classList;
    }

    public void clearClassList() {
        classList.clear();
    }

    private void checkAndAddClass(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            if (checkSuper(aClass) && checkAnnotation(aClass))
                classList.add(aClass);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private boolean checkSuper(Class<?> aClass) {
        if (this.superClass.isEmpty())
            return true;
        for (Class<?> Super: this.superClass)
            if (!Super.isAssignableFrom(aClass))
                return false;
        return true;
    }

    private boolean checkAnnotation(Class<?> aClass) {
        if (this.annotationClass.isEmpty())
            return true;
        Annotation[] annotations = aClass.getDeclaredAnnotations();
        for (Annotation annotation: annotations)
            if (this.annotationClass.contains(annotation.annotationType()))
                return true;
        return false;
    }

    public void startFind() {
        this.classList.clear();
        for (File file : this.jarFiles) {
            if (file.getPath().endsWith(".jar") || file.getPath().endsWith(".zip"))
                try {
                    loadJar(file);
                    findInJar(new JarFile(file));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            else
                findInFile(file, "");
        }
    }

    private void findInJar(JarFile jarFile) {
        Enumeration<JarEntry> entryEnumeration = jarFile.entries();
        while (entryEnumeration.hasMoreElements()) {
            JarEntry jarEntry = entryEnumeration.nextElement();
            String classFullName = jarEntry.getName();
            if (classFullName.contains("META-INF"))
                continue;
            if (!classFullName.endsWith(".class"))
                continue;
            String className = classFullName.substring(0, classFullName.length() - 6).replace('\\', '.').replace('/', '.');
            checkAndAddClass(className);
        }
    }

    private void findInFile(File jarFile, String packageName) {
        if (jarFile.isDirectory()) {
            File[] files = jarFile.listFiles();
            if (files == null)
                return;
            for (File file: files) {
                String subPackageName = "".equals(packageName) ? "" : HStringHelper.merge(packageName, ".");
                String filePath = file.getAbsolutePath().replace('\\', '.').replace('/', '.');
                String subClassName = filePath.substring(filePath.lastIndexOf('.') + 1);
                if (subClassName.equals("class")) {
                    String filePathWithoutSuffix = filePath.substring(0, filePath.lastIndexOf('.'));
                    subClassName = filePathWithoutSuffix.substring(filePathWithoutSuffix.lastIndexOf('.') + 1);
                }
                findInFile(file, HStringHelper.merge(subPackageName, subClassName));
            }
            return;
        }
        String subClassName = jarFile.getAbsolutePath().substring(jarFile.getParentFile().getAbsolutePath().length() + 1)
                .replace('\\', '.').replace('/', '.');
        if (subClassName.contains("META-INF"))
            return;
        if (!subClassName.endsWith(".class"))
            return;
        checkAndAddClass(packageName);
    }

    public static void loadJar(File jarFile) {
        if (jarFile == null || !jarFile.exists())
            return;
        Method method;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException exception) {
            exception.printStackTrace();
            return;
        }
        try {
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            URL url = jarFile.toURI().toURL();
            method.invoke(classLoader, url);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
