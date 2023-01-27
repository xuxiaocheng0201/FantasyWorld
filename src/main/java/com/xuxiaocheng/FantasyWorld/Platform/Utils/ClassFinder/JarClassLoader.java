package com.xuxiaocheng.FantasyWorld.Platform.Utils.ClassFinder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends ClassLoader {
    protected @NotNull JarFile jarFile;
    protected @NotNull ClassLoader parent;

    public JarClassLoader(@NotNull final JarFile jarFile) {
        super(Thread.currentThread().getContextClassLoader());
        this.jarFile = jarFile;
        this.parent = Thread.currentThread().getContextClassLoader();
    }

    public JarClassLoader(@NotNull final JarFile jarFile, @Nullable final ClassLoader parent) {
        super(Objects.requireNonNullElse(parent, Thread.currentThread().getContextClassLoader()));
        this.jarFile = jarFile;
        this.parent = Objects.requireNonNullElse(parent, Thread.currentThread().getContextClassLoader());
    }

    @Override
    protected @NotNull Class<?> findClass(@Nullable final String name) throws ClassNotFoundException {
        if (name == null)
            throw new ClassNotFoundException("Null class name.");
        final String path = name.replace('.', '/') + ".class";
        final JarEntry entry = this.jarFile.getJarEntry(path);
        if (entry == null)
            return this.parent.loadClass(name);
        try (final InputStream inputStream = this.jarFile.getInputStream(entry)) {
            final int available = inputStream.available();
            final byte[] bytes = new byte[available];
            int len = 0;
            while (len < available)
                len += inputStream.read(bytes, len, available - len);
            return this.defineClass(name, bytes, 0, bytes.length);
        } catch (final IOException exception) {
            throw new ClassNotFoundException("Class " + name + '(' + this + ") not found.", exception);
        }
    }

    @Override
    public @Nullable InputStream getResourceAsStream(@Nullable final String name) {
        if (name == null)
            return null;
        final JarEntry entry = this.jarFile.getJarEntry(name);
        if (entry == null)
            return super.getResourceAsStream(name);
        try {
            return this.jarFile.getInputStream(entry);
        } catch (final IOException ignore) {
        }
        return null;
    }

    @Override
    public @NotNull String toString() {
        return "JarClassLoader{" +
                "jarFile=" + this.jarFile +
                '}';
    }
}
