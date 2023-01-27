package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.Version;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionFormatException;
import com.xuxiaocheng.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

public class VersionSingleTest {
    @Test
    public void compare() throws VersionFormatException {
        TestUtil.assetsEquals(VersionSingle.compareVersion(null, VersionSingle.create("0")), -2);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("0"), null), -2);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create(""), VersionSingle.create("")), 0);
        // 0-9 A-Z _ a-z
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("0"), VersionSingle.create("1")), -1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("0"), VersionSingle.create("0")), 0);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("1"), VersionSingle.create("0")), 1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create(" "), VersionSingle.create("0")), -2);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("0"), VersionSingle.create("a")), -1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("0"), VersionSingle.create("A")), -1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("a"), VersionSingle.create("A")), 1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("_"), VersionSingle.create("0")), 1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("_"), VersionSingle.create("A")), 1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("_"), VersionSingle.create("a")), -1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("123"), VersionSingle.create("12c")), -1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("1.23"), VersionSingle.create("1.2c")), -1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("2.23"), VersionSingle.create("1.2c")), 1);
        TestUtil.assetsEquals(VersionSingle.compareVersion(VersionSingle.create("0.0"), VersionSingle.create("  0 .0")), 0);
    }

    @Test
    public void create() throws VersionFormatException {
        VersionSingleTest.checkVersionSingleInside("_", List.of("_"));
        VersionSingleTest.checkVersionSingleInside("0", List.of("0"));
        VersionSingleTest.checkVersionSingleInside("0.0", List.of("0", "0"));
        VersionSingleTest.checkVersionSingleInside("0.0.1", List.of("0", "0", "1"));
        VersionSingleTest.checkVersionSingleInside("0a.0.1c", List.of("0a", "0", "1c"));
        VersionSingleTest.checkVersionSingleInside("0.0_1c", List.of("0", "0_1c"));
        VersionSingleTest.checkVersionSingleInside("0 .0  1 c  ", List.of("0", "01c"), "0.01c");
        VersionSingleTest.checkVersionSingleInside("346eg.t245235.124", List.of("346eg", "t245235", "124"));
        VersionSingleTest.checkVersionSingleInside("", List.of());
        VersionSingleTest.checkVersionSingleInside(" ", List.of(), "");
        TestUtil.assertThrow(() -> VersionSingle.create("[]"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionSingle.create("[0.456"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionSingle.create(".4"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionSingle.create("_1]"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionSingle.create("%"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionSingle.create("&"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionSingle.create(".."), VersionFormatException.class);
        TestUtil.assetsAt(VersionSingle.create(""), VersionSingle.EmptyVersion);
    }

    public static void checkVersionSingleInside(@NotNull final String version, @NotNull final List<String> list) throws VersionFormatException {
        VersionSingleTest.checkVersionSingleInside(version, list, version);
    }
    public static void checkVersionSingleInside(@NotNull final String version, @NotNull final List<String> list, @NotNull final String string) throws VersionFormatException {
        final VersionSingle versionSingle = VersionSingle.create(version);
        TestUtil.assetsEquals(TestUtil.getField(VersionSingle.class, "versionList", versionSingle), list);
        TestUtil.assetsEquals(TestUtil.getField(VersionSingle.class, "version", versionSingle), string);
    }
}
