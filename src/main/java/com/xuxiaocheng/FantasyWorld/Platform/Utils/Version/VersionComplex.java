package com.xuxiaocheng.FantasyWorld.Platform.Utils.Version;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Available version range. Default: Empty.
 * Like intervals in mathematics.
 * String form: [,)&(,]&{,,}
 * @author xuxiaocheng
 */
public final class VersionComplex implements Serializable {
    @Serial
    private static final long serialVersionUID = -7755725541591955245L;

    public static final @NotNull VersionComplex EmptyVersionComplex = new VersionComplex(false);
    public static final @NotNull VersionComplex UniversionVersionComplex = new VersionComplex(true);

    private final @NotNull Set<VersionRange> versionRanges = new TreeSet<>();
    private final @NotNull Set<VersionSingle> versionSingles = new ConcurrentSkipListSet<>();

    private @NotNull String version = "";

    private VersionComplex() {
        super();
    }

    private VersionComplex(final boolean universion) {
        super();
        if (universion) {
            this.versionRanges.add(VersionRange.UniversionVersionRange);
            this.version = "(,)";
        } else
            this.version = "empty";
    }

    private @Nullable Set<VersionRange> immutableVersionRanges;
    public Set<VersionRange> getVersionRanges() {
        if (this.immutableVersionRanges == null)
            this.immutableVersionRanges = this.versionRanges.stream().collect(Collectors.toUnmodifiableSet());
        return this.immutableVersionRanges;
    }

    private @Nullable Set<VersionSingle> immutableVersionSingles;
    public Set<VersionSingle> getVersionSingles() {
        if (this.immutableVersionSingles == null)
            this.immutableVersionSingles = Collections.unmodifiableSet(this.versionSingles);
        return this.immutableVersionSingles;
    }

    public @NotNull String getVersion() {
        return this.version;
    }

    @Override
    public @NotNull String toString() {
        return "VersionComplex:" + this.version;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof VersionComplex that)) return false;
        return this.versionRanges.equals(that.versionRanges) && this.versionSingles.equals(that.versionSingles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.versionRanges, this.versionSingles);
    }

    private static final String VersionRangePattern = "[\\[|(](" + VersionSingle.VersionPattern + "|),(" + VersionSingle.VersionPattern + "|)[]|)]";
    private static final String VersionOrPattern = "(" + VersionComplex.VersionRangePattern + ")|(\\{(((" + VersionSingle.VersionPattern + ")|),)*(" + VersionSingle.VersionPattern + "|)})";
    private static final String VersionComplexPattern = "((" + VersionComplex.VersionOrPattern + ")&)*(" + VersionComplex.VersionOrPattern + ')';
    private static final Pattern VersionMatcher = Pattern.compile('^' + VersionComplex.VersionComplexPattern + '$');
    private static final Pattern VersionExtractor = Pattern.compile("((?<range>" + VersionComplex.VersionRangePattern + ")&?)|(\\{(?<singles>(" + VersionSingle.VersionPattern + ",?)*)|}&?)");
    private static final Pattern SingleExtractor = Pattern.compile("(?<version>" + VersionSingle.VersionPattern + "),?");
    public static @NotNull VersionComplex create(@Nullable final String versionIn) throws VersionFormatException {
        if (versionIn == null || versionIn.isBlank())
            return VersionComplex.EmptyVersionComplex;
        final String version = versionIn.replace(" ", "");
        if ("(,)".equals(version)) // Quick response
            return VersionComplex.UniversionVersionComplex;
        // match
        final Matcher matcher = VersionComplex.VersionMatcher.matcher(version);
        if (!matcher.matches())
            throw new VersionFormatException("Invalid version complex format.", versionIn);
        // extract and pretreat
        final Collection<VersionRange> versionComplexRanges = new TreeSet<>();
        boolean leftLimitless = false;
        boolean rightLimitless = false;
        final Matcher extractor = VersionComplex.VersionExtractor.matcher(version);
        while (extractor.find()) {
            final String range = extractor.group("range");
            if (range != null) {
                final VersionRange versionRange = VersionRange.create(range);
                if (VersionRange.UniversionVersionRange.equals(versionRange))
                    return VersionComplex.UniversionVersionComplex;
                if (!VersionRange.EmptyVersionRange.equals(versionRange)) {
                    versionComplexRanges.add(versionRange);
                    if (VersionSingle.EmptyVersion.equals(versionRange.getLeftVersion()))
                        leftLimitless = true;
                    if (VersionSingle.EmptyVersion.equals(versionRange.getRightVersion()))
                        rightLimitless = true;
                    if (leftLimitless && rightLimitless)
                        return VersionComplex.UniversionVersionComplex;
                }
            } else {
                final String singles = extractor.group("singles");
                if (singles != null) {
                    final Matcher singleMatcher = VersionComplex.SingleExtractor.matcher(singles);
                    while (singleMatcher.find()) {
                        final VersionSingle versionSingle = VersionSingle.create(singleMatcher.group("version"));
                        if (!VersionSingle.EmptyVersion.equals(versionSingle))
                            versionComplexRanges.add(VersionRange.create(versionSingle));
                    }
                }
            }
        }
        if (versionComplexRanges.isEmpty())
            return VersionComplex.EmptyVersionComplex;
        // fix
        final Collection<VersionRange> versionRanges = new HashSet<>();
        VersionSingle lastVersionRangeLeft = null;
        boolean lastVersionRangeLeftEquable = false;
        VersionSingle lastVersionRangeRight = null;
        boolean lastVersionRangeRightEquable = false;
        for (final VersionRange versionRange: versionComplexRanges) {
            if (lastVersionRangeLeft == null) {
                lastVersionRangeLeft = versionRange.getLeftVersion();
                lastVersionRangeLeftEquable = versionRange.isLeftEquable();
                lastVersionRangeRight = versionRange.getRightVersion();
                lastVersionRangeRightEquable = versionRange.isRightEquable();
                continue;
            }
            // if current.left <= last.right:
            //   last.right = max(current.right, last.right)
            // else
            //   ADD(last)
            //   last = current
            final int cmp = VersionSingle.compareVersion(versionRange.getLeftVersion(), lastVersionRangeRight);
            if (cmp < 0 || (cmp == 0 && (versionRange.isLeftEquable() || lastVersionRangeRightEquable))) {
                final int right = VersionSingle.compareVersion(versionRange.getRightVersion(), lastVersionRangeRight);
                if (right == 0)
                    lastVersionRangeRightEquable |= versionRange.isRightEquable();
                if (right > 0) {
                    lastVersionRangeRight = versionRange.getRightVersion();
                    lastVersionRangeRightEquable = versionRange.isRightEquable();
                }
            } else {
                versionRanges.add(VersionRange.create(lastVersionRangeLeftEquable, lastVersionRangeLeft, lastVersionRangeRight, lastVersionRangeRightEquable));
                lastVersionRangeLeft = versionRange.getLeftVersion();
                lastVersionRangeLeftEquable = versionRange.isLeftEquable();
                lastVersionRangeRight = versionRange.getRightVersion();
                lastVersionRangeRightEquable = versionRange.isRightEquable();
            }
        }
        versionRanges.add(VersionRange.create(lastVersionRangeLeftEquable, lastVersionRangeLeft, lastVersionRangeRight, lastVersionRangeRightEquable));
        final VersionComplex versionComplex = new VersionComplex();
        versionComplex.versionRanges.addAll(versionRanges.stream().filter((v) -> {
            if (v.isLeftEquable() && v.isRightEquable() && Objects.equals(v.getLeftVersion(), v.getRightVersion())) {
                versionComplex.versionSingles.add(v.getLeftVersion());
                return false;
            }
            return true;
        }).collect(Collectors.toSet()));
        // toString
        final StringBuilder builder = new StringBuilder();
        for (final VersionRange versionRange: versionComplex.versionRanges)
            builder.append((versionRange.getVersion())).append('&');
        if (versionComplex.versionSingles.isEmpty()) {
            if (builder.isEmpty())
                return VersionComplex.EmptyVersionComplex;
            versionComplex.version = builder.deleteCharAt(builder.length() - 1).toString();
        } else {
            builder.append('{');
            for (final VersionSingle versionSingle: versionComplex.versionSingles)
                builder.append(versionSingle.getVersion()).append(',');
            builder.deleteCharAt(builder.length() - 1).append('}');
            versionComplex.version = builder.toString();
        }
        return versionComplex;
    }

    public static boolean versionInComplex(@NotNull final VersionSingle version, @NotNull final VersionComplex complex) {
        if (VersionSingle.EmptyVersion.equals(version))
            return VersionComplex.EmptyVersionComplex.equals(complex);
        if (VersionComplex.UniversionVersionComplex.equals(complex))
            return true;
        for (final VersionRange range: complex.versionRanges)
            if (VersionRange.versionInRange(version, range) == 0)
                return true;
        for (final VersionSingle single: complex.versionSingles)
            if (single.equals(version))
                return true;
        return false;
    }
}
