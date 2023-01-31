package com.xuxiaocheng.FantasyWorld.Platform.Additions;

import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.IllegalAdditionException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.MismatchedAdditionAnnouncementException;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.MismatchedAdditionConstructorException;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions.JarClassLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class AdditionalLoader {
    private AdditionalLoader() {
        super();
    }

    private static final @NotNull Map<String, Addition> Modifications = new ConcurrentHashMap<>();

    private static final @NotNull Map<String, Addition> unmodifiableModifications = Collections.unmodifiableMap(AdditionalLoader.Modifications);
    @Contract(pure = true)
    public static @NotNull Map<String, Addition> getUnmodifiableModifications() {
        return AdditionalLoader.unmodifiableModifications;
    }

    public static @NotNull List<@NotNull IllegalAdditionException> loadJar(@NotNull final JarFile jarFile) {
        final JarClassLoader jarClassLoader = new JarClassLoader(jarFile);
        final List<IllegalAdditionException> exceptions = new ArrayList<>();
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String name = entry.getName();
            if (!name.endsWith(".class"))
                continue;
            final Class<?> c;
            try {
                c = jarClassLoader.loadClass(name.substring(0, name.length() - 6).replace('/', '.'));
            } catch (final ClassNotFoundException ignore) {
                continue;
            }
            if (Modifier.isAbstract(c.getModifiers()))
                continue;
            final Addition.Additional additional = c.getAnnotation(Addition.Additional.class);
            if (additional == null ^ !Addition.class.isAssignableFrom(c)) {
                exceptions.add(new MismatchedAdditionAnnouncementException(null, c));
                continue;
            }
            if (additional == null)
                continue;
            final Addition addition;
            try {
                final Constructor<?> constructor = c.getDeclaredConstructor();
                constructor.setAccessible(true);
                addition = (Addition) constructor.newInstance();
            } catch (final NoSuchMethodException exception) {
                exceptions.add(new MismatchedAdditionConstructorException(null, c, exception));
                continue;
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                exceptions.add(new MismatchedAdditionConstructorException("Addition constructor throws exception.", c, exception));
                continue;
            }
            AdditionalLoader.Modifications.put(additional.id(), addition);
        }
        return exceptions;
    }
}
