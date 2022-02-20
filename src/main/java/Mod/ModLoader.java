package Mod;

public class ModLoader {/*
    private static List<Class<?>> elementType;
    public static List<Class<?>> getElementType() {
        if (elementType == null) {
            elementType = new ArrayList<>();
            List<Class<?>> classes = getClasses("");
            for (Class<?> c: classes)
                if (c.getAnnotation(NewElementImplement.class) != null)
                    elementType.add(c);
        }
        return elementType;
    }

    public static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String packageDir = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDir);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                HLog.logger(url);
                if ("jar".equals(protocol)) {
                    JarFile jar;
                    jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith("/"))
                            name = name.substring(1);
                        if (name.startsWith(packageDir)) {
                            int idx = name.lastIndexOf('/');
                            if (idx != -1)
                                packageName = name.substring(0, idx).replace('/', '.');
                            if (name.endsWith(".class") && !entry.isDirectory()) {
                                String className = name.substring(packageName.length() + 1, name.length() - 6);
                                classes.add(Class.forName(packageName + '.' + className));
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return classes;
    }



    private List<Class<?>> classes;
    private String packagePath = null;
    public getClasses(String basePackage) throws ClassNotFoundException {
        packagePath= System.getProperty("user.dir") + "\\src\\";
        String filePath= packagePath + basePackage.replace('.', '\\');
        classes= new ArrayList<>();
        fileScanner(new File(filePath));
    }

    private void fileScanner(File file) throws ClassNotFoundException {
        if (file.isFile() && file.getName().lastIndexOf(".java") == file.getName().length() - 5) {
            String filePath = file.getAbsolutePath();
            String qualifiedName = filePath.substring(packagePath.length(), filePath.length() - 5).replace('\\', '.');
            System.out.println(qualifiedName);
            classes.add(Class.forName(qualifiedName));
            return;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                fileScanner(f);
        }
    }

    public List<Class<?>> getClasses() {
        return this.classes;
    }*/
}
