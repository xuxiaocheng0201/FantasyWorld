package HeadLibs.ClassFinder;

import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Load classed in jar file.
 */
@SuppressWarnings({"CustomClassloader", "unused"})
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
    protected synchronized @Nullable Class<?> loadClass(@NotNull String name, boolean resolve) throws ClassNotFoundException {
        if (this.jarFile == null)
            return null;
        try {
            Class<?> c = null;
            String path = name.replace('.', '/') + ".class";
            JarEntry entry = this.jarFile.getJarEntry(path);
            if (entry != null) {
                InputStream is = this.jarFile.getInputStream(entry);
                int availableLen = is.available();
                int len = 0;
                byte[] bt1 = new byte[availableLen];
                while (len < availableLen)
                    len += is.read(bt1, len, availableLen - len);
                is.close();
                c = this.defineClass(name, bt1, 0, bt1.length);
                if (resolve)
                    this.resolveClass(c);
            } else
                if (this.parent != null)
                    return this.parent.loadClass(name);
            return c;
        } catch (IOException exception) {
            throw new ClassNotFoundException(name, exception);
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (this.jarFile == null)
            return null;
        InputStream is = null;
        try {
            JarEntry entry = this.jarFile.getJarEntry(name);
            if (entry != null)
                is = this.jarFile.getInputStream(entry);
            if (is == null)
                is = super.getResourceAsStream(name);
        } catch (IOException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
        return is;
    }
}
