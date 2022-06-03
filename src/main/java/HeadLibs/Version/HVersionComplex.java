package HeadLibs.Version;

import HeadLibs.Helper.HStringHelper;
import HeadLibs.Version.HVersionRange.ImmutableVersionRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Available version range. Default: Empty.
 * Like intervals in mathematics.
 * String form: [,)&(,]&{,,}
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HVersionComplex implements Serializable {
    @Serial
    private static final long serialVersionUID = -7755725541591955245L;

    /**
     * Single interval without any operation.
     */
    protected final @NotNull List<HVersionRange> versionRanges = new ArrayList<>();
    /**
     * A single version number.
     */
    protected final @NotNull List<HStringVersion> versionSingles = new ArrayList<>();

    private boolean autoFixFlag = true;

    /**
     * Construct an empty interval.
     */
    public HVersionComplex() {
        super();
    }

    /**
     * Construct a version from version string.
     * @param version the version string
     * @throws HVersionFormatException Wrong formation of version string.
     */
    public HVersionComplex(@Nullable String version) throws HVersionFormatException {
        super();
        this.addVersions(version);
    }

    public HVersionComplex(@Nullable HStringVersion version) {
        super();
        this.addVersionSingle(version);
    }

    public HVersionComplex(@NotNull HStringVersion[] versions) {
        super();
        for (HStringVersion version: versions)
            this.addVersionSingle(version);
    }

    public HVersionComplex(@Nullable HVersionRange versionRange) {
        super();
        this.addVersionRange(versionRange);
    }

    public HVersionComplex(@NotNull HVersionRange[] versionRanges) {
        super();
        for (HVersionRange versionRange: versionRanges)
            this.addVersionRange(versionRange);
    }

    public void setUniversal() {
        this.versionRanges.clear();
        this.versionSingles.clear();
        this.versionRanges.add(new HVersionRange());
        this.autoFixFlag = true;
    }

    public void setEmpty() {
        this.versionRanges.clear();
        this.versionSingles.clear();
        this.autoFixFlag = true;
    }

    public void setVersionSingle(@Nullable String version) throws HVersionFormatException {
        this.setVersionSingle(new HStringVersion(version));
    }

    public void setVersionSingle(@Nullable HStringVersion version) {
        this.setEmpty();
        this.addVersionSingle(version);
    }

    public void setVersionRange(@Nullable String version) throws HVersionFormatException {
        this.setVersionRange(new HVersionRange(version));
    }

    public void setVersionRange(@Nullable HVersionRange version) {
        this.setEmpty();
        this.addVersionRange(version);
    }

    public void setVersions(@Nullable String version) throws HVersionFormatException {
        this.setEmpty();
        this.addVersions(version);
    }

    public void addVersionSingle(@Nullable String version) throws HVersionFormatException {
        this.addVersionSingle(new HStringVersion(version));
    }

    public void addVersionSingle(@Nullable HStringVersion version) {
        if (HStringVersion.isNull(version))
            return;
        this.versionSingles.add(version);
        this.autoFixFlag = false;
    }

    public void addVersionRange(@Nullable String version) throws HVersionFormatException {
        this.addVersionSingle(new HStringVersion(version));
    }

    public void addVersionRange(@Nullable HVersionRange versionRange) {
        this.versionRanges.add(versionRange);
        this.autoFixFlag = false;
    }

    public void addVersions(@Nullable String versions) throws HVersionFormatException {
        if (versions == null)
            return;
        String[] versions_ = HStringHelper.strip(versions.split("&"));
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
        this.autoFixFlag = false;
    }

    public void addVersions(@Nullable HVersionComplex versionComplex) {
        if (versionComplex == null)
            return;
        this.versionRanges.addAll(versionComplex.versionRanges);
        this.versionSingles.addAll(versionComplex.versionSingles);
        this.autoFixFlag = false;
    }

    public void autoFix() {
        if (this.autoFixFlag)
            return;
        // Remove null and duplicate values.
        this.versionRanges.removeIf(HVersionRange::isEmpty);
        Collection<HVersionRange> noDuplicateRanges = this.versionRanges.stream().distinct().toList();
        this.versionRanges.clear();
        this.versionRanges.addAll(noDuplicateRanges);
        this.versionSingles.removeIf(HStringVersion::isNull);
        Collection<HStringVersion> noDuplicateSingles = this.versionSingles.stream().distinct().toList();
        this.versionSingles.clear();
        this.versionSingles.addAll(noDuplicateSingles);
        // Find universal range and single element range.
        Collection<HVersionRange> deleteRanges = new HashSet<>();
        for (HVersionRange versionRange: this.versionRanges) {
            versionRange.autoFix();
            if (HStringVersion.compareVersion(versionRange.getLeftVersion(), versionRange.getRightVersion()) == 0) {
                if (!versionRange.isEmpty()) {
                    this.setUniversal();
                    return;
                }
                deleteRanges.add(versionRange);
                this.versionSingles.add(versionRange.getLeftVersion());
            }
        }
        this.versionRanges.removeAll(deleteRanges);
        // Merge single version to range version.
        Collection<HStringVersion> deleteSingles = new HashSet<>();
        for (HStringVersion version: this.versionSingles)
            for (HVersionRange versionRange: this.versionRanges) {
                int left = HStringVersion.compareVersionWithoutPosition(versionRange.getLeftVersion(), version, false);
                int right = HStringVersion.compareVersionWithoutPosition(version, versionRange.getRightVersion(), true);
                if (left == 0) {
                    versionRange.setLeftEquable(true);
                    deleteSingles.add(version);
                }
                if (right == 0) {
                    versionRange.setRightEquable(true);
                    deleteSingles.add(version);
                }
                if (left > 0 && right > 0)
                    deleteSingles.add(version);
            }
        this.versionSingles.removeAll(deleteSingles);
        // Sort versions.
        this.versionRanges.sort((o1, o2) -> {
            int result = HStringVersion.compareVersionWithoutPosition(o1.getLeftVersion(), o2.getLeftVersion(), false);
            if (result != 0) return result;
            return HStringVersion.compareVersionWithoutPosition(o1.getRightVersion(), o2.getRightVersion(), true);
        });
        this.versionSingles.sort((o1, o2) -> HStringVersion.compareVersionWithoutPosition(o1, o2, true));
        // Merge version ranges.
        List<HVersionRange> ranges = new ArrayList<>();
        for (HVersionRange now: this.versionRanges) {
            if (ranges.isEmpty()){
                ranges.add(now);
                continue;
            }
            HVersionRange last = ranges.get(ranges.size() - 1);
            int result = HStringVersion.compareVersion(now.getLeftVersion(), last.getRightVersion());
            if (result > 0 || (result == 0 && !last.isRightEquable() && !now.isLeftEquable())) {
                ranges.add(now);
                continue;
            }
            result = HStringVersion.compareVersionWithoutPosition(last.getRightVersion(), now.getRightVersion(), true);
            if (result > 0)
                continue;
            ranges.remove(last);
            HVersionRange h = new HVersionRange();
            h.setVersionRange(last.isLeftEquable(), last.getLeftVersion(), now.getRightVersion(), now.isRightEquable());
            if (result == 0)
                h.setRightEquable(last.isRightEquable() || now.isRightEquable());
            h.autoFix();
            ranges.add(h);
        }
        this.versionRanges.clear();
        this.versionRanges.addAll(ranges);
        this.autoFixFlag = true;
    }

    public boolean versionInRange(@Nullable String version) throws HVersionFormatException {
        return this.versionInRange(new HStringVersion(version));
    }

    public boolean versionInRange(@Nullable HStringVersion version) {
        if (version == null)
            return false;
        this.autoFix();
        for (HVersionRange range: this.versionRanges)
            if (range.versionInRange(version))
                return true;
        for (HStringVersion s: this.versionSingles)
            if (HStringVersion.compareVersionWithoutPosition(version, s, true) == 0)
                return true;
        return false;
    }

    public @NotNull ImmutableVersionComplex toImmutable() {
        return new ImmutableVersionComplex(this);
    }

    @Override
    public @NotNull String toString() {
        this.autoFix();
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

    public @NotNull List<HVersionRange> getVersionRanges() {
        return this.versionRanges;
    }

    public @NotNull List<HStringVersion> getVersionSingles() {
        return this.versionSingles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HVersionComplex that)) return false;
        return this.versionRanges.equals(that.versionRanges) && this.versionSingles.equals(that.versionSingles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.versionRanges, this.versionSingles);
    }

    public static class ImmutableVersionComplex extends HVersionComplex {
        @Serial
        private static final long serialVersionUID = HVersionComplex.serialVersionUID;

        public ImmutableVersionComplex() {
            super();
        }

        public ImmutableVersionComplex(@Nullable String version) throws HVersionFormatException {
            super(version);
        }

        public ImmutableVersionComplex(@Nullable HStringVersion version) {
            super(version);
        }

        public ImmutableVersionComplex(@NotNull HStringVersion[] versions) {
            super(versions);
        }

        public ImmutableVersionComplex(@Nullable HVersionRange versionRange) {
            super(versionRange);
        }

        public ImmutableVersionComplex(@NotNull HVersionRange[] versionRanges) {
            super(versionRanges);
        }

        public ImmutableVersionComplex(@Nullable HVersionComplex versionComplex) {
            super();
            if (versionComplex != null)
                super.addVersions(versionComplex);
        }

        @Override
        public void setUniversal() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setEmpty() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionSingle(@Nullable String version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionSingle(@Nullable HStringVersion version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionRange(@Nullable String version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionRange(@Nullable HVersionRange version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersions(@Nullable String version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addVersionSingle(@Nullable String version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addVersionSingle(@Nullable HStringVersion version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addVersionRange(@Nullable String version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addVersionRange(@Nullable HVersionRange versionRange) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addVersions(@Nullable String versions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addVersions(@Nullable HVersionComplex versionComplex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void autoFix() {
        }

        public @NotNull List<HVersionRange> getVersionRanges() {
            List<ImmutableVersionRange> list = new ArrayList<>(this.versionRanges.size());
            for (HVersionRange range: this.versionRanges)
                list.add(range.toImmutable());
            return Collections.unmodifiableList(list);
        }

        public @NotNull List<HStringVersion> getVersionSingles() {
            List<HStringVersion> list = new ArrayList<>(this.versionSingles.size());
            for (HStringVersion version: this.versionSingles)
                list.add(version.toImmutable());
            return Collections.unmodifiableList(list);
        }

        @Override
        public @NotNull ImmutableVersionComplex toImmutable() {
            return this;
        }
    }
}
