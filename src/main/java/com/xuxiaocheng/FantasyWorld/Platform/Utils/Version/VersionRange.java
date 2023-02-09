package com.xuxiaocheng.FantasyWorld.Platform.Utils.Version;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Range for versions. Default: Empty.
 * String form: [,] or (,) or [,) or (,]
 * @author xuxiaocheng
 */
public final class VersionRange implements Serializable, Comparable<VersionRange> {
    @Serial
    private static final long serialVersionUID = 4882505281267885490L;

    public static final @NotNull VersionRange UniversionVersionRange = new VersionRange(false);
    public static final @NotNull VersionRange EmptyVersionRange = new VersionRange(true);

    /**
     * A flag to differentiate empty and universal set.
     */
    private final boolean empty;

    private boolean leftEquable;
    private boolean rightEquable;
    private @NotNull VersionSingle leftVersion;
    private @NotNull VersionSingle rightVersion;

    private @NotNull String version;

    private VersionRange(final boolean empty) {
        super();
        this.empty = empty;
        this.leftEquable = false;
        this.rightEquable = false;
        this.leftVersion = VersionSingle.EmptyVersion;
        this.rightVersion = VersionSingle.EmptyVersion;
        this.version = empty ? "empty" : "(,)";
    }

    public boolean isLeftEquable() {
        return this.leftEquable;
    }

    public boolean isRightEquable() {
        return this.rightEquable;
    }

    public @NotNull VersionSingle getLeftVersion() {
        return this.leftVersion;
    }

    public @NotNull VersionSingle getRightVersion() {
        return this.rightVersion;
    }

    public @NotNull String getVersion() {
        return this.version;
    }

    @Override
    public @NotNull String toString() {
        return "VersionRange:" + this.version;
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof VersionRange that)) return false;
        return this.empty == that.empty && this.leftEquable == that.leftEquable && this.rightEquable == that.rightEquable && this.leftVersion.equals(that.leftVersion) && this.rightVersion.equals(that.rightVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.empty, this.leftEquable, this.rightEquable, this.leftVersion, this.rightVersion);
    }

    @Override
    public int compareTo(final @Nullable VersionRange that) {
        return VersionRange.compareVersionRange(this, that);
    }

    private static final Pattern VersionMatcher = Pattern.compile("^[\\[|(](?<left>(" + VersionSingle.VersionPattern + ")|),(?<right>(" + VersionSingle.VersionPattern + ")|)[]|)]$");

    private static final @NotNull LoadingCache<String, VersionRange> VersionRangeCache = CacheBuilder.newBuilder()
            .maximumSize(100).weakValues().build(new CacheLoader<String, VersionRange>() {
                @Override
                public @NotNull VersionRange load(final @NotNull String version) throws VersionFormatException {
                    // match
                    final Matcher matcher = VersionRange.VersionMatcher.matcher(version);
                    if (!matcher.matches())
                        throw new VersionFormatException("Invalid version range format.", version);
                    // extract
                    return VersionRange.create(
                            version.charAt(0) == '[',
                            VersionSingle.create(matcher.group("left")),
                            VersionSingle.create(matcher.group("right")),
                            version.charAt(version.length() - 1) == ']'
                    );
                }
            });

    public static @NotNull VersionRange create(final @Nullable String versionIn) throws VersionFormatException {
        if (versionIn == null || versionIn.isBlank())
            return VersionRange.EmptyVersionRange;
        final String version = versionIn.replace(" ", "");
        if ("(,)".equals(version)) // Quick response
            return VersionRange.UniversionVersionRange;
        try {
            return VersionRange.VersionRangeCache.get(version);
        } catch (final ExecutionException exception) {
            throw new VersionFormatException(null, versionIn, exception);
        }
    }

    static VersionRange create(final boolean leftEquable, final @Nullable VersionSingle left, final @Nullable VersionSingle right, final boolean rightEquable) {
        // fix
        final boolean leftEmpty = left == null || VersionSingle.EmptyVersion.equals(left);
        final boolean rightEmpty = right == null || VersionSingle.EmptyVersion.equals(right);
        if (leftEmpty && rightEmpty)
            return VersionRange.UniversionVersionRange;
        final VersionRange versionRange = new VersionRange(false);
        if (leftEmpty) {
            versionRange.leftEquable = false;
            versionRange.leftVersion = VersionSingle.EmptyVersion;
        }
        else {
            versionRange.leftEquable = leftEquable;
            versionRange.leftVersion = left;
        }
        if (rightEmpty) {
            versionRange.rightEquable = false;
            versionRange.rightVersion = VersionSingle.EmptyVersion;
        }
        else {
            versionRange.rightEquable = rightEquable;
            versionRange.rightVersion = right;
        }
        final int cmp = VersionSingle.compareVersion(versionRange.leftVersion, versionRange.rightVersion);
        if (cmp == 0 && (!versionRange.leftEquable || !versionRange.rightEquable))
            return VersionRange.EmptyVersionRange;
        if (cmp == 1)
            return VersionRange.EmptyVersionRange;
        // to string
        versionRange.version =
                (versionRange.leftEquable ? "[" : "(") +
                        versionRange.leftVersion.getVersion() + ',' + versionRange.rightVersion.getVersion() +
                        (versionRange.rightEquable ? ']' : ')');
        return versionRange;
    }

    public static VersionRange create(final @Nullable VersionSingle versionSingle) {
        if (versionSingle == null || VersionSingle.EmptyVersion.equals(versionSingle))
            return VersionRange.EmptyVersionRange;
        final VersionRange versionRange = new VersionRange(false);
        versionRange.leftEquable = true;
        versionRange.rightEquable = true;
        versionRange.leftVersion = versionSingle;
        versionRange.rightVersion = versionSingle;
        versionRange.version = '[' + versionSingle.getVersion() + ',' + versionSingle.getVersion() + ']';
        return versionRange;
    }

    /*
     * 1: {@code a} is at the right of {@code b} (a>b)
     * 0: {@code a} is equal to {@code b} (a==b)
     * -1: {@code a} is at the left of {@code b} (a<b)
     * -2: Incomparable (==empty|universion)
     */
    public static int compareVersionRange(final @Nullable VersionRange a, final @Nullable VersionRange b) {
        if (Objects.requireNonNullElse(a, VersionRange.EmptyVersionRange) == Objects.requireNonNullElse(b, VersionRange.EmptyVersionRange))
            return 0;
        if (a == null || b == null
                || VersionRange.EmptyVersionRange.equals(a) || VersionRange.EmptyVersionRange.equals(b)
                || VersionRange.UniversionVersionRange.equals(a) || VersionRange.UniversionVersionRange.equals(b)
        )
            return -2;
        final boolean aLeftEmpty = VersionSingle.EmptyVersion.equals(a.leftVersion);
        final boolean bLeftEmpty = VersionSingle.EmptyVersion.equals(b.leftVersion);
        if (aLeftEmpty || bLeftEmpty) {
            if (!aLeftEmpty)
                return 1;
            if (!bLeftEmpty)
                return -1;
        } else {
            final int left = VersionSingle.compareVersion(a.leftVersion, b.leftVersion);
            if (left != 0)
                return left;
            if (a.leftEquable != b.leftEquable)
                return a.leftEquable ? -1 : 1;
        }
        final boolean aRightEmpty = VersionSingle.EmptyVersion.equals(a.rightVersion);
        final boolean bRightEmpty = VersionSingle.EmptyVersion.equals(b.rightVersion);
        if (aRightEmpty || bRightEmpty) {
            if (!aRightEmpty)
                return -1;
            if (!bRightEmpty)
                return 1;
        } else {
            final int right = VersionSingle.compareVersion(a.rightVersion, b.rightVersion);
            if (right != 0)
                return right;
            if (a.rightEquable != b.rightEquable)
                return a.rightEquable ? 1 : -1;
        }
        return 0;
    }

    /*
     * xxx (,) xxx
     *  ^   ^   ^
     *  |   |   |
     *  -1  0   1
     * -2: Incomparable
     */
    public static int versionInRange(final @NotNull VersionSingle version, final @NotNull VersionRange range) {
        if (VersionSingle.EmptyVersion.equals(version) || VersionRange.EmptyVersionRange.equals(range))
            return -2;
        if (VersionRange.UniversionVersionRange.equals(range))
            return 0;
        if (!VersionSingle.EmptyVersion.equals(range.leftVersion)) {
            final int left = VersionSingle.compareVersion(version, range.leftVersion);
            if (left < 0)
                return -1;
            if (left == 0 && !range.leftEquable)
                return -1;
        }
        if (!VersionSingle.EmptyVersion.equals(range.rightVersion)) {
            final int right = VersionSingle.compareVersion(version, range.rightVersion);
            if (right > 0)
                return 1;
            if (right == 0 && !range.rightEquable)
                return 1;
        }
        return 0;
    }
}
