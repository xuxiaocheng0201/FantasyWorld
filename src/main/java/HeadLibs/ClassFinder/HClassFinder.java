package HeadLibs.ClassFinder;

import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Find classes in files and jars.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HClassFinder {
    /**
     * Find classes with package.
     */
    private @NotNull String PACKAGE_PATH = "";
    /**
     * Which jar the class file is in when searching.
     */
    private @Nullable File doingJar = null;
    /**
     * Available jars for searching.
     */
    private final Set<File> jarFiles = new HashSet<>();
    /**
     * Found classes must extend or implement these.
     */
    private final Set<Class<?>> superClass = new HashSet<>();
    /**
     * Found classes must have these annotations.
     */
    private final Set<Class<? extends Annotation>> annotationClass = new HashSet<>();
    /**
     * Recursively search classes in package.
     */
    private boolean recursive = true;
    /**
     * Found classes list.
     */
    private final Set<Class<?>> classList = new HashSet<>();
    /**
     * Found classes list with jars.
     */
    private final Map<Class<?>, File> classListWithJarFile = new HashMap<>();

    /**
     * This code's jar.
     */
    public static final File thisCodePath = new File(HClassFinder.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    /**
     * Construct a new finder.
     * Searching all classes in this code's jar.
     */
    public HClassFinder() {
        PACKAGE_PATH = "";
        addJarFile(thisCodePath);
    }

    /**
     * Construct a new finder.
     * Searching all classes in this code's jar with package.
     * @param PACKAGE_PATH Find classes with package.
     */
    public HClassFinder(@Nullable String PACKAGE_PATH) {
        setPACKAGE_PATH(PACKAGE_PATH);
        addJarFile(thisCodePath);
    }

    /**
     * Get package.
     * @return The package to find classes.
     */
    public @NotNull String getPACKAGE_PATH() {
        return PACKAGE_PATH;
    }

    /**
     * Set package.
     * @param PACKAGE_PATH The package to find classes.
     */
    public void setPACKAGE_PATH(@Nullable String PACKAGE_PATH) {
        if (PACKAGE_PATH == null) {
            this.PACKAGE_PATH = "";
            return;
        }
        this.PACKAGE_PATH = PACKAGE_PATH;
    }

    /**
     * Get available jars {@link HClassFinder#jarFiles}
     * @return Available jars for searching.
     */
    public @NotNull Set<File> getJarFiles() {
        return jarFiles;
    }

    /**
     * Add an available jar for searching.
     * @param jarFile available jar for searching.
     */
    public void addJarFile(@Nullable File jarFile) {
        if (jarFile == null || !jarFile.exists() || jarFiles.contains(jarFile))
            return;
        jarFiles.add(jarFile);
    }

    /**
     * Add available jars in directory for searching.
     * @param directory available jars in.
     */
    public void addJarFilesInDirectory(@Nullable File directory) {
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

    /**
     * Clear all available jars for searching.
     */
    public void clearJarFile() {
        this.jarFiles.clear();
    }

    /**
     * Get super classes {@link HClassFinder#superClass}
     * @return Super classes.
     */
    public @NotNull Set<Class<?>> getSuperClass() {
        return superClass;
    }

    /**
     * Add a super class.
     * @param superClass super class.
     */
    @SuppressWarnings("unchecked")
    public void addSuperClass(@Nullable Class<?> superClass) {
        if (superClass == null || this.superClass.contains(superClass))
            return;
        if (superClass.isAnnotation()) {
            addAnnotationClass((Class<? extends Annotation>) superClass);
            return;
        }
        this.superClass.add(superClass);
    }

    /**
     * Clear all super classes.
     */
    public void clearSuperClass() {
        this.superClass.clear();
    }

    /**
     * Get annotation classes {@link HClassFinder#annotationClass}
     * @return Annotation classes.
     */
    public @NotNull Set<Class<? extends Annotation>> getAnnotationClass() {
        return annotationClass;
    }

    /**
     * Add an annotation class.
     * @param annotation annotation class.
     */
    public void addAnnotationClass(@Nullable Class<? extends Annotation> annotation) {
        if (annotation == null || this.annotationClass.contains(annotation))
            return;
        this.annotationClass.add(annotation);
    }

    /**
     * Clear all annotation classes.
     */
    public void clearAnnotationClass() {
        this.annotationClass.clear();
    }

    /**
     * Is recursively search. {@link HClassFinder#recursive}
     * @return Need recursively
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * Set recursively search.
     * @param recursive Need recursively
     */
    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    /**
     * Get found classes list {@link HClassFinder#classList}
     * @return Found classes list
     */
    public @NotNull Set<Class<?>> getClassList() {
        return classList;
    }

    /**
     * Get found classes list with jars {@link HClassFinder#classListWithJarFile}
     * @return Found classes list with jars
     */
    public @NotNull Map<Class<?>, File> getClassListWithJarFile() {
        return classListWithJarFile;
    }

    /**
     * Clear all found classes.
     */
    public void clearClassList() {
        classList.clear();
        classListWithJarFile.clear();
    }

    /**
     * Check class availability and then add to class list.
     * @param className Searched class name to check and add.
     */
    private void checkAndAddClass(@NotNull String className) {
        Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException classNotFoundException) {
            try {
                aClass = loadClassInJar(this.doingJar, className);
            } catch (Exception exception) {
                HLog.logger(HELogLevel.ERROR, exception);
                return;
            }
        } catch (NoClassDefFoundError error) {
            HLog.logger(HELogLevel.MISTAKE, "Class.forName(\"", className, "\") failed. Message: ", error.getMessage(), ".");
            return;
        }
        if (aClass != null && checkSuper(aClass) && checkAnnotation(aClass)) {
            classList.add(aClass);
            classListWithJarFile.put(aClass, this.doingJar);
        }
    }

    /**
     * Check class availability of super. {@link HClassFinder#superClass}
     * @param aClass Searched class to check.
     */
    public boolean checkSuper(@NotNull Class<?> aClass) {
        if (this.superClass.isEmpty())
            return true;
        for (Class<?> Super: this.superClass)
            if (!Super.isAssignableFrom(aClass))
                return false;
        return true;
    }

    /**
     * Check class availability of annotation. {@link HClassFinder#annotationClass}
     * @param aClass Searched class to check.
     */
    public boolean checkAnnotation(@NotNull Class<?> aClass) {
        if (this.annotationClass.isEmpty())
            return true;
        Annotation[] annotations = aClass.getDeclaredAnnotations();
        for (Annotation annotation: annotations)
            if (this.annotationClass.contains(annotation.annotationType()))
                return true;
        return false;
    }

    /**
     * Find classes available.
     */
    public void startFind() {
        this.classList.clear();
        this.classListWithJarFile.clear();
        for (File file : this.jarFiles) {
            doingJar = file;
            if (file.getPath().endsWith(".jar") || file.getPath().endsWith(".zip"))
                try {
                    Enumeration<JarEntry> entryEnumeration = (new JarFile(file)).entries();
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
                } catch (IOException exception) {
                    HLog.logger(HELogLevel.ERROR, exception);
                }
            else
                findInFile(file, "");
        }
        doingJar = null;
    }

    /**
     * Find classes available in directory files.
     * @param jarFile The directory files.
     * @param packageName Directory package.
     */
    private void findInFile(@NotNull File jarFile, @NotNull String packageName) {
        if (jarFile.isDirectory()) {
            File[] files = jarFile.listFiles();
            if (files == null)
                return;
            for (File file: files) {
                String subPackageName = packageName.isEmpty() ? "" : HStringHelper.merge(packageName, ".");
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

    public static @Nullable Class<?> loadClassInJar(@Nullable File jarFile, @NotNull String className) throws ClassNotFoundException, IOException {
        if (jarFile == null || !jarFile.exists() || !jarFile.isFile() ||
                !(jarFile.getAbsolutePath().endsWith(".zip") || jarFile.getAbsolutePath().endsWith(".jar")))
            return null;
        try {
            @SuppressWarnings("ClassLoaderInstantiation")
            HDynamicJarClassLoader classLoader = new HDynamicJarClassLoader(new JarFile(jarFile));
            return classLoader.loadClass(className);
        } catch (MalformedURLException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        } catch (NoClassDefFoundError error) {
            HLog.logger(HELogLevel.MISTAKE, "HClassFinder.loadClassInJar(\"", jarFile.getAbsolutePath(), "\", \"", className, "\") failed. Message: ", error.getMessage(), ".");
        }
        return null;
    }
}
