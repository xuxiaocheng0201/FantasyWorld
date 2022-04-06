package HeadLibs.Version;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Available version range.
 * Like intervals in mathematics.
 * @author xuxiaocheng
 */
/* Version form: [,]&(,]&{,,} */
@SuppressWarnings("unused")
public class HVersionComplex implements Serializable {
    @Serial
    private static final long serialVersionUID = -7755725541591955245L;

    /**
     * Single interval without any operation.
     */
    private List<HVersionRange> versionRanges = new ArrayList<>();
    /**
     * A single version number.
     */
    private List<HStringVersion> versionSingles = new ArrayList<>();

    /**
     * Construct a (min, max) interval.
     */
    public HVersionComplex() {
        super();
    }

    /* *
     *
     * @param version Version range.
     * @throws IllegalArgumentException version isn't legal.
     *//*
    public HVersionComplex(@NotNull String version) throws HVersionFormatException {
        super();
        this.addVersions(version);
    }

    public HVersionComplex(@NotNull HStringVersion version) throws HVersionFormatException {
        super();
        this.addVersion(version);
    }*/

    public @NotNull List<HVersionRange> getVersionRanges() {
        return this.versionRanges;
    }

    public @NotNull List<HStringVersion> getVersionSingles() {
        return this.versionSingles;
    }

    public void clear() {
        this.versionRanges.clear();
        this.versionSingles.clear();
    }

    public void addVersionSingle(@Nullable String version) throws HVersionFormatException {
        this.addVersionSingle(new HStringVersion(version));
    }

    public void addVersionSingle(@Nullable HStringVersion version) {
        if (HStringVersion.isNull(version))
            return;
        this.versionSingles.add(version);
        this.autoFix();
    }

    public void addVersionRange(@Nullable String version) throws HVersionFormatException {
        this.addVersionSingle(new HStringVersion(version));
    }

    public void addVersionRange(@Nullable HVersionRange versionRange) {
        this.versionRanges.add(versionRange);
        this.autoFix();
    }

    public void addVersions(@NotNull String versions) throws HVersionFormatException {
        String[] versions_ = versions.split("&");
        for (String version: versions_) {
            if (version.isBlank())
                continue;
            if (version.charAt(0) == '{' && version.charAt(version.length() - 1) == '}') {
                for (String v: version.substring(1, version.length() - 1).split(","))
                    this.addVersionSingle(new HStringVersion(v));
                continue;
            }
            if ((version.charAt(0) == '(' || version.charAt(0) == '[') && (version.charAt(version.length() - 1) == ')' || version.charAt(version.length() - 1) == ']'))
                this.addVersionRange(new HVersionRange(version));
            else
                this.addVersionSingle(new HStringVersion(version));
        }
    }

    public boolean versionInRange(@NotNull HStringVersion version) {
        for (HVersionRange range: this.versionRanges)
            if (range.versionInRange(version))
                return true;
        for (HStringVersion s: this.versionSingles)
            if (HStringVersion.compareVersion(version, s) == 0)
                return true;
        return false;
    }

    public void autoFix() {
        for (HVersionRange versionRange: this.versionRanges) {
            versionRange.autoFix();
            if (HStringVersion.compareVersion(versionRange.getLeftVersion(), versionRange.getRightVersion()) == 0) {
                if (versionRange.getLeftVersion().equals(HStringVersion.EMPTY)) {
                    this.versionRanges.clear();
                    this.versionSingles.clear();
                    this.versionRanges.add(new HVersionRange());
                    return;
                }
                this.versionRanges.remove(versionRange);
                this.versionSingles.add(versionRange.getLeftVersion());
            }
        }
        this.versionRanges.removeIf(HVersionRange::isEmpty);
        this.versionRanges = this.versionRanges.stream().distinct().collect(Collectors.toList());
        this.versionSingles.removeIf(HStringVersion::isNull);
        this.versionSingles = this.versionSingles.stream().distinct().collect(Collectors.toList());
        this.versionRanges.sort((o1, o2) -> {
            int result = HStringVersion.compareVersion(o1.getLeftVersion(), o2.getLeftVersion());
            if (result != 0) return result;
            return HStringVersion.compareVersion(o1.getRightVersion(), o2.getRightVersion());
        });
        this.versionSingles.sort(HStringVersion::compareVersion);
        Collection<HStringVersion> removed = new ArrayList<>();
        for (HStringVersion version: this.versionSingles) {
            boolean remove = false;
            for (HVersionRange versionRange: this.versionRanges) {
                int left = HStringVersion.compareVersion(version, versionRange.getLeftVersion());
                int right = HStringVersion.compareVersion(versionRange.getRightVersion(), version);
                if (left == 0) {
                    versionRange.setLeftEquable(true);
                    remove = true;
                }
                if (right == 0) {
                    versionRange.setRightEquable(true);
                    remove = true;
                }
                if (left > 0 && right > 0)
                    remove = true;
            }
            if (remove)
                removed.add(version);
        }
        this.versionSingles.removeAll(removed);
        if (this.versionRanges.size() > 1) {
            List<HVersionRange> ranges = new ArrayList<>();
            ranges.add(this.versionRanges.get(0));
            for (int i = 1; i < this.versionRanges.size(); ++i) {
                HVersionRange last = ranges.get(ranges.size() - 1);
                HVersionRange now = this.versionRanges.get(i);
                int result = HStringVersion.compareVersion(last.getRightVersion(), now.getLeftVersion());
                if (result < 0 || (result == 0 && !last.isRightEquable() && !now.isLeftEquable())) {
                    ranges.add(now);
                    continue;
                }
                ranges.remove(last);
                HVersionRange h = new HVersionRange();
                h.setVersionRange(last.isLeftEquable(), last.getLeftVersion(), now.getRightVersion(), now.isRightEquable());
                ranges.add(h);
            }
            this.versionRanges = ranges;
        }
    }

    @Override
    public @NotNull String toString() {
        StringBuilder builder = new StringBuilder(10);
        for (HVersionRange versionRange : this.versionRanges)
            builder.append((versionRange.toString())).append("&");
        if (this.versionSingles.isEmpty()) {
            if (builder.isEmpty())
                return "";
            return builder.deleteCharAt(builder.length() - 1).toString();
        }
        builder.append("{");
        for (HStringVersion version: this.versionSingles)
            builder.append(version.toString()).append(",");
        builder.deleteCharAt(builder.length() - 1).append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HVersionComplex that = (HVersionComplex) o;
        return this.versionRanges.equals(that.versionRanges) && this.versionSingles.equals(that.versionSingles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.versionRanges, this.versionSingles);
    }
}
