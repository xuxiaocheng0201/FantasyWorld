package com.xuxiaocheng.FantasyWorldTest.Platform.Additions;

import com.xuxiaocheng.FantasyWorld.Platform.Additions.AdditionalLoader;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.TestUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class AdditionalLoaderTest {
    @Test
    public void load() throws IOException {
        TestUtil.assetsEquals(AdditionalLoader.loadJar(new JarFile("libs/HeadLibs.main.jar")), List.of());
        TestUtil.assetsEquals(AdditionalLoader.getUnmodifiableModifications(), Map.of());
        TestUtil.assetsEquals(AdditionalLoader.loadJar(new JarFile("ADDITIONS/TestAddition01-1.0-SNAPSHOT.jar")), List.of());
        TestUtil.assetsEquals(AdditionalLoader.getUnmodifiableModifications().size(), 1);
    }
}
