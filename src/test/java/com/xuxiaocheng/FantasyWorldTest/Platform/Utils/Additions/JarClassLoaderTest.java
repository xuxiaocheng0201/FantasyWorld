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
import java.util.jar.JarFile;

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
            // assert: a class file.
            TestUtil.assetsEquals(bytes[0], (byte) 0xCA);
            TestUtil.assetsEquals(bytes[1], (byte) 0xFE);
            TestUtil.assetsEquals(bytes[2], (byte) 0xBA);
            TestUtil.assetsEquals(bytes[3], (byte) 0xBE);
        }
    }

    @Test
    public void findClass() throws IOException, ClassNotFoundException {
        final @NotNull JarClassLoader jarClassLoader = new JarClassLoader(new JarFile("libs/HeadLibs.main.jar"));
        TestUtil.assetsAt(jarClassLoader.loadClass("com.xuxiaocheng.HeadLibs.Logger.HLog"), HLog.class);
        TestUtil.assetsAt(jarClassLoader.loadClass("com.xuxiaocheng.HeadLibs.Logger.HLogLevel"), HLogLevel.class);
    }
}
