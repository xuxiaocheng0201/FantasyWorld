package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.Version;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionFormatException;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionRange;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import com.xuxiaocheng.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

public class VersionRangeTest {
    @Test
    public void in() throws VersionFormatException {
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("[1.0,1.1]")), 0);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("[1.0,1.1)")),  1);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("[1.1,1.1]")), 0);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("(1.1,1.1]")), -2);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("(1.1,1.2]")),  -1);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("(,)")),  0);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("")),  -2);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("(1.0,)")),  0);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("[1.1,)")),  0);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("(,1.1]")),  0);
        TestUtil.assetsEquals(VersionRange.versionInRange(VersionSingle.create("1.1"), VersionRange.create("(,1.2)")),  0);
    }

    @Test
    public void compare() throws VersionFormatException {
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create(""), VersionRange.create("")), 0);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("(,)"), VersionRange.create("")), -2);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("[0,0]"), VersionRange.create("")), -2);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("[0,1]"), VersionRange.create("[0,1]")), 0);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("[0,1]"), VersionRange.create("[0,1)")),  1);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("[0,1)"), VersionRange.create("[0,1]")),  -1);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("[0,1]"), VersionRange.create("(0,1]")),  -1);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("(0,1]"), VersionRange.create("[0,1]")),  1);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("[0,1]"), VersionRange.create("[2,3]")),  -1);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("[2,3]"), VersionRange.create("[0,1]")),  1);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("(,2]"), VersionRange.create("[1,2]")),  -1);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("(,2]"), VersionRange.create("[1,)")),  -1);
        TestUtil.assetsEquals(VersionRange.compareVersionRange(VersionRange.create("(,)"), VersionRange.create("[1,)")),  -2);
    }

    @Test
    public void create() throws VersionFormatException {
        VersionRangeTest.checkVersionRangeInside("[a,b]", false, true, true, "a", "b");
        VersionRangeTest.checkVersionRangeInside("[a,b)", false, true, false, "a", "b");
        VersionRangeTest.checkVersionRangeInside("(a,b]", false, false, true, "a", "b");
        VersionRangeTest.checkVersionRangeInside("(a,b)", false, false, false, "a", "b");
        VersionRangeTest.checkVersionRangeInside("(0.1,0.2)", false, false, false, "0.1", "0.2");
        VersionRangeTest.checkVersionRangeInside("(,)", false, false, false, null, null);
        VersionRangeTest.checkVersionRangeInside("(,]", false, false, false, null, null, "(,)");
        VersionRangeTest.checkVersionRangeInside("[,]", false, false, false, null, null, "(,)");
        VersionRangeTest.checkVersionRangeInside("", true, false, false, null, null, "empty");
        VersionRangeTest.checkVersionRangeInside("[0,0)", true, false, false, null, null, "empty");
        VersionRangeTest.checkVersionRangeInside("(0,0]", true, false, false, null, null, "empty");
        VersionRangeTest.checkVersionRangeInside("(b,a]", true, false, false, null, null, "empty");
        VersionRangeTest.checkVersionRangeInside("(b,]", false, false, false, "b", null, "(b,)");
        TestUtil.assertThrow(() -> VersionRange.create("0"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionRange.create("[0"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionRange.create("[0]"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionRange.create("[0.., 1]"), VersionFormatException.class);
        TestUtil.assetsAt(VersionRange.create("(0,)").getRightVersion(), VersionSingle.EmptyVersion);
        TestUtil.assetsAt(VersionRange.create("(,)"), VersionRange.UniversionVersionRange);
        TestUtil.assetsAt(VersionRange.create(""), VersionRange.EmptyVersionRange);
        TestUtil.assetsAt(VersionRange.create("[0,0)"), VersionRange.EmptyVersionRange);
    }

    public static void checkVersionRangeInside(final @NotNull String version, final boolean empty, final boolean lE, final boolean rE, final @Nullable String l, final @Nullable String r) throws VersionFormatException {
        VersionRangeTest.checkVersionRangeInside(version, empty, lE, rE, l, r, version);
    }
    public static void checkVersionRangeInside(final @NotNull String version, final boolean empty, final boolean lE, final boolean rE, final @Nullable String l, final @Nullable String r, final @NotNull String string) throws VersionFormatException {
        final VersionRange versionRange = VersionRange.create(version);
        TestUtil.assetsEquals(TestUtil.getField(VersionRange.class, "empty", versionRange), empty);
        TestUtil.assetsEquals(TestUtil.getField(VersionRange.class, "leftEquable", versionRange), lE);
        TestUtil.assetsEquals(TestUtil.getField(VersionRange.class, "rightEquable", versionRange), rE);
        TestUtil.assetsEquals(TestUtil.getField(VersionRange.class, "leftVersion", versionRange), VersionSingle.create(l));
        TestUtil.assetsEquals(TestUtil.getField(VersionRange.class, "rightVersion", versionRange), VersionSingle.create(r));
        TestUtil.assetsEquals(TestUtil.getField(VersionRange.class, "version", versionRange), string);
    }
}
