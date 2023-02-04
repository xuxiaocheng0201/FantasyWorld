package com.xuxiaocheng.FantasyWorld.Platform.Utils.Version;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Package {@link String} to {@code Version}.
 * @author xuxiaocheng
 */
public final class VersionSingle implements Serializable, Comparable<VersionSingle> {
    @Serial
    private static final long serialVersionUID = -8779747563654866413L;

    public static final @NotNull VersionSingle EmptyVersion = new VersionSingle();

    private final @NotNull List<String> versionList = new ArrayList<>(5);

    private @NotNull String version = "";

    private VersionSingle() {
        super();
    }

    private @Nullable List<String> immutableVersionList;
    public List<String> getVersionLit() {
        if (this.immutableVersionList == null)
            this.immutableVersionList = Collections.unmodifiableList(this.versionList);
        return this.immutableVersionList;
    }

    public @NotNull String getVersion() {
        return this.version;
    }

    @Override
    public @NotNull String toString() {
        return "VersionSingle:" + this.version;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof VersionSingle that)) return false;
        return this.versionList.equals(that.versionList);
    }

    @Override
    public int hashCode() {
        return this.versionList.hashCode();
    }

    @Override
    public int compareTo(@Nullable final VersionSingle that) {
        return VersionSingle.compareVersion(this, that);
    }

    static final String VersionPattern = "(\\w+\\.)*\\w+";
    private static final Pattern VersionMatcher = Pattern.compile('^' + VersionSingle.VersionPattern + '$');
    private static final Pattern VersionExtractor = Pattern.compile("(?<code>\\w+)\\.?");
    public static @NotNull VersionSingle create(@Nullable final String versionIn) throws VersionFormatException {
        if (versionIn == null || versionIn.isBlank())
            return VersionSingle.EmptyVersion;
        final String version = versionIn.replace(" ", "");
        // match
        final Matcher matcher = VersionSingle.VersionMatcher.matcher(version);
        if (!matcher.matches())
            throw new VersionFormatException("Invalid version format.", version);
        // extract
        final VersionSingle versionSingle = new VersionSingle();
        final Matcher extractor = VersionSingle.VersionExtractor.matcher(version);
        while (extractor.find())
            versionSingle.versionList.add(extractor.group("code"));
        // to string
        versionSingle.version = Joiner.on('.').join(versionSingle.versionList);
        return versionSingle;
    }

    /*
     * 1: {@code a} is newer than {@code b} (a>b)
     * 0: {@code a} is equal to {@code b} (a==b)
     * -1: {@code a} is older than {@code b} (a<b)
     * -2: Incomparable (==empty)
     */
    public static int compareVersion(@Nullable final VersionSingle a, @Nullable final VersionSingle b) {
        if (Objects.requireNonNullElse(a, VersionSingle.EmptyVersion) == Objects.requireNonNullElse(b, VersionSingle.EmptyVersion))
            return 0;
        if (a == null || b == null || VersionSingle.EmptyVersion.equals(a) || VersionSingle.EmptyVersion.equals(b))
            return -2;
        final int minLength = Math.min(a.versionList.size(), b.versionList.size());
        for (int i = 0; i < minLength; ++i) {
            final String as = a.versionList.get(i);
            final String bs = b.versionList.get(i);
            final int res = Objects.compare(as, bs, String::compareTo);
            if (res < 0)
                return -1;
            if (res > 0)
                return 1;
        }
        return Ints.compare(a.versionList.size(), b.versionList.size());
    }
}
