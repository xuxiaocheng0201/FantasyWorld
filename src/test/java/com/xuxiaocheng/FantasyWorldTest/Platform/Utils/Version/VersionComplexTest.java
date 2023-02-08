package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.Version;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionFormatException;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionRange;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import com.xuxiaocheng.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class VersionComplexTest {
    @Test
    public void in() throws VersionFormatException {
        TestUtil.assetsEquals(VersionComplex.versionInComplex(VersionSingle.create("a"), VersionComplex.create("[a,b]")), true);
        TestUtil.assetsEquals(VersionComplex.versionInComplex(VersionSingle.create("b"), VersionComplex.create("[a,b]")), true);
        TestUtil.assetsEquals(VersionComplex.versionInComplex(VersionSingle.create("b"), VersionComplex.create("[a,b)")), false);
        TestUtil.assetsEquals(VersionComplex.versionInComplex(VersionSingle.create("1"), VersionComplex.create("[a,b)&{1}")), true);
        TestUtil.assetsEquals(VersionComplex.versionInComplex(VersionSingle.create("2"), VersionComplex.create("[a,b)&{1}")), false);
    }

    @Test
    public void create() throws VersionFormatException {
        VersionComplexTest.checkVersionComplexInside("[a,b]", Set.of("[a,b]"), Set.of());
        VersionComplexTest.checkVersionComplexInside("[0,1]&{2,3}", Set.of("[0,1]"), Set.of("2", "3"));
        VersionComplexTest.checkVersionComplexInside("{2}&[0,1]&{3}", Set.of("[0,1]"), Set.of("2", "3"), "[0,1]&{2,3}");
        VersionComplexTest.checkVersionComplexInside("{2}&[0,1]&{3}&[5,9]", Set.of("[0,1]", "[5,9]"), Set.of("2", "3"), "[0,1]&[5,9]&{2,3}");
        VersionComplexTest.checkVersionComplexInside("{2}&[5,9]&[0,6]&{3}", Set.of("[0,9]"), Set.of(), "[0,9]");
        VersionComplexTest.checkVersionComplexInside("{2}&(3,9]&[0,2)&{3}&(2,3)", Set.of("[0,9]"), Set.of(), "[0,9]");
        VersionComplexTest.checkVersionComplexInside("{2}&(3,9)&(0,2)&(2,3)", Set.of("(0,3)", "(3,9)"), Set.of(), "(0,3)&(3,9)");
        VersionComplexTest.checkVersionComplexInside("(,)&[2,3]", Set.of("(,)"), Set.of(), "(,)");
        VersionComplexTest.checkVersionComplexInside("[2,3]&(,)", Set.of("(,)"), Set.of(), "(,)");
        VersionComplexTest.checkVersionComplexInside("(,9)&[2,3]", Set.of("(,9)"), Set.of(), "(,9)");
        VersionComplexTest.checkVersionComplexInside("[2,3]&(,9)", Set.of("(,9)"), Set.of(), "(,9)");
        VersionComplexTest.checkVersionComplexInside("(4,9)&[2,)", Set.of("[2,)"), Set.of(), "[2,)");
        VersionComplexTest.checkVersionComplexInside("[2,)&(4,9)", Set.of("[2,)"), Set.of(), "[2,)");
        VersionComplexTest.checkVersionComplexInside("(1,9]&[2,3]", Set.of("(1,9]"), Set.of(), "(1,9]");
        VersionComplexTest.checkVersionComplexInside("{3}&(,1]&{3}&[0,3)", Set.of("(,3]"), Set.of(), "(,3]");
        VersionComplexTest.checkVersionComplexInside("{1}&{}", Set.of(), Set.of("1"), "{1}");
        VersionComplexTest.checkVersionComplexInside("", Set.of(), Set.of(), "empty");
        VersionComplexTest.checkVersionComplexInside("{}&{}", Set.of(), Set.of(), "empty");
        TestUtil.assertThrow(() -> VersionComplex.create("(1,2]&["), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionComplex.create("(1,2,3)"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionComplex.create("(1,2,3)"), VersionFormatException.class);
        TestUtil.assertThrow(() -> VersionComplex.create("(1,2"), VersionFormatException.class);
    }

    public static void checkVersionComplexInside(final @NotNull String version, final @NotNull Collection<String> ranges, final @NotNull Collection<String> singles) throws VersionFormatException {
        VersionComplexTest.checkVersionComplexInside(version, ranges, singles, version);
    }
    public static void checkVersionComplexInside(final @NotNull String version, final @NotNull Collection<String> ranges, final @NotNull Collection<String> singles, final @NotNull String string) throws VersionFormatException {
        final VersionComplex versionComplex = VersionComplex.create(version);
        TestUtil.assetsEquals(
                TestUtil.getField(VersionComplex.class, "versionRanges", versionComplex),
                new TreeSet<>(ranges.stream()
                        .map((v) -> {
                            try {
                                return VersionRange.create(v);
                            } catch (final VersionFormatException exception) {
                                throw new AssertionError(exception);
                            }
                        })
                        .collect(Collectors.toSet())));
        TestUtil.assetsEquals(
                TestUtil.getField(VersionComplex.class, "versionSingles", versionComplex),
                new TreeSet<>(singles.stream()
                        .map((v) -> {
                            try {
                                return VersionSingle.create(v);
                            } catch (final VersionFormatException exception) {
                                throw new AssertionError(exception);
                            }
                        })
                        .collect(Collectors.toSet())));
        TestUtil.assetsEquals(TestUtil.getField(VersionComplex.class, "version", versionComplex), string);
    }
}
