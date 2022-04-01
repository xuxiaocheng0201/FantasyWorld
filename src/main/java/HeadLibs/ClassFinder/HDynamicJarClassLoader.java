package HeadLibs.ClassFinder;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HDynamicJarClassLoader extends ClassLoader {
    private final JarFile jarFile;
    private final ClassLoader parent;

    public HDynamicJarClassLoader(JarFile jarFile) {
        super(Thread.currentThread().getContextClassLoader());
        this.parent = Thread.currentThread().getContextClassLoader();
        this.jarFile = jarFile;
    }

    public HDynamicJarClassLoader(JarFile jarFile, ClassLoader parent) {
        super(parent);
        this.parent = parent;
        this.jarFile = jarFile;
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (jarFile == null)
            return null;
        try {
            Class<?> c = null;
            String path = name.replace('.', '/').concat(".class");
            JarEntry entry = jarFile.getJarEntry(path);
            if (entry != null) {
                InputStream is = jarFile.getInputStream(entry);
                int availableLen = is.available();
                int len = 0;
                byte[] bt1 = new byte[availableLen];
                while (len < availableLen)
                    len += is.read(bt1, len, availableLen - len);
                is.close();
                c = defineClass(name, bt1, 0, bt1.length);
                if (resolve)
                    resolveClass(c);
            } else
                if (parent != null)
                    return parent.loadClass(name);
            return c;
        } catch (IOException exception) {
            throw new ClassNotFoundException(name);
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (jarFile == null)
            return null;
        InputStream is = null;
        try {
            JarEntry entry = jarFile.getJarEntry(name);
            if (entry != null)
                is = jarFile.getInputStream(entry);
            if (is == null)
                is = super.getResourceAsStream(name);
        } catch (IOException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
        return is;
    }
}
