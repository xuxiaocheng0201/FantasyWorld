package com.xuxiaocheng.FantasyWorldTest.Platform.Additions;

import com.xuxiaocheng.FantasyWorld.Core.FantasyWorld;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.Addition;
import com.xuxiaocheng.FantasyWorld.Platform.Additions.AdditionsLoader;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import com.xuxiaocheng.TestUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.jar.JarFile;

public class AdditionsLoaderTest {
    @Test
    public void load() throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        AdditionsLoader.addAddition(new JarFile("libs/HeadLibs.main.jar"));
        TestUtil.assetsEquals(AdditionsLoader.getUnmodifiableModifications(), Map.of());
        final Constructor<?> constructor = FantasyWorld.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        TestUtil.<Map<String, Addition>, AdditionsLoader>getField(AdditionsLoader.class, "Modifications", null)
                .put("FantasyWorld", (Addition) constructor.newInstance());
        AdditionsLoader.addAddition(new JarFile("ADDITIONS/TestAddition01-1.0-SNAPSHOT.jar"));
        TestUtil.assetsEquals(AdditionsLoader.getUnmodifiableModifications().size(), 2);
        AdditionsLoader.initializeAdditions();
    }

    @Test
    public void delete() {
        HLog.DefaultLogger.log(HLogLevel.INFO, AdditionsLoader.deleteAddition("FantasyWorld"));
    }
}
