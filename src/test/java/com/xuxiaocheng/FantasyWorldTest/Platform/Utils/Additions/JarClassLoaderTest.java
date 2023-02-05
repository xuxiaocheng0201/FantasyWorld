package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.Additions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions.JarClassLoader;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import com.xuxiaocheng.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JarClassLoaderTest {
    @Test
    public void getResource() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final @NotNull JarClassLoader jarClassLoader = new JarClassLoader(new JarFile("libs/HeadLibs.main.jar"));
        try (final InputStream is = jarClassLoader.getResourceAsStream("com/xuxiaocheng/HeadLibs/Logger/HLog.class")) {
            assert is != null;
            final int available = is.available();
            final byte[] bytes = new byte[available];
            int len = 0;
            while (len < available)
                len += is.read(bytes, len, available - len);
            final Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            method.setAccessible(true);
            final Class<?> c = (Class<?>) method.invoke(jarClassLoader, "com.xuxiaocheng.HeadLibs.Logger.HLog", bytes, 0, bytes.length);
            // Because of different ClassLoader, they cannot equal directly.
            TestUtil.assetsEquals(Stream.of(c.getMethods()).map(Method::toString).collect(Collectors.toList()),
                    Stream.of(HLog.class.getMethods()).map(Method::toString).collect(Collectors.toList()));
        }
    }

    @Test
    public void findClass() throws IOException, ClassNotFoundException {
        final @NotNull JarClassLoader jarClassLoader = new JarClassLoader(new JarFile("libs/HeadLibs.main.jar"));
        TestUtil.assetsAt(jarClassLoader.loadClass("com.xuxiaocheng.HeadLibs.Logger.HLog"), HLog.class);
        TestUtil.assetsAt(jarClassLoader.loadClass("com.xuxiaocheng.HeadLibs.Logger.HLogLevel"), HLogLevel.class);
    }
}
