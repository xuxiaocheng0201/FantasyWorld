package HeadLibs.Version;

import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Version.HStringVersion.UpdatableStringVersion;
import HeadLibs.Version.HVersionRange.ImmutableVersionRange;
import HeadLibs.Version.HVersionRange.UpdatableVersionRange;
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

    protected boolean autoFixed = true;

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

    public HVersionComplex(@Nullable HVersionComplex version) {
        super();
        this.setVersions(version);
    }

    public void setUniversal() {
        this.versionRanges.clear();
        this.versionSingles.clear();
        this.versionRanges.add(new HVersionRange());
        this.autoFixed = true;
    }

    public void setEmpty() {
        this.versionRanges.clear();
        this.versionSingles.clear();
        this.autoFixed = true;
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

    public void setVersions(@Nullable HVersionComplex version) {
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
        this.autoFixed = false;
    }

    public void addVersionRange(@Nullable String version) throws HVersionFormatException {
        this.addVersionSingle(new HStringVersion(version));
    }

    public void addVersionRange(@Nullable HVersionRange version) {
        this.versionRanges.add(version);
        this.autoFixed = false;
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
        this.autoFixed = false;
    }

    public void addVersions(@Nullable HVersionComplex versions) {
        if (versions == null)
            return;
        this.versionRanges.addAll(versions.versionRanges);
        this.versionSingles.addAll(versions.versionSingles);
        this.autoFixed = false;
    }

    public void autoFix() {
        if (this.autoFixed)
            return;
        this.autoFixed = true;
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

    public @NotNull UpdatableVersionComplex toUpdatable() {
        return new UpdatableVersionComplex(this);
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

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HVersionComplex that)) return false;
        return this.versionRanges.equals(that.versionRanges) && this.versionSingles.equals(that.versionSingles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.versionRanges, this.versionSingles);
    }

    public static class ImmutableVersionComplex extends HVersionComplex implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(HVersionComplex.serialVersionUID);

        protected void init() {
            Collection<ImmutableVersionRange> list1 = new ArrayList<>(this.versionRanges.size());
            for (HVersionRange range: this.versionRanges)
                list1.add(range.toImmutable());
            this.versionRanges.clear();
            this.versionRanges.addAll(list1);
            Collection<HStringVersion> list2 = new ArrayList<>(this.versionSingles.size());
            for (HStringVersion version: this.versionSingles)
                list2.add(version.toImmutable());
            this.versionSingles.clear();
            this.versionSingles.addAll(list2);
        }

        public ImmutableVersionComplex() {
            super();
            this.init();
        }

        public ImmutableVersionComplex(@Nullable String version) throws HVersionFormatException {
            super(version);
            this.init();
        }

        public ImmutableVersionComplex(@Nullable HStringVersion version) {
            super(version);
            this.init();
        }

        public ImmutableVersionComplex(@NotNull HStringVersion[] versions) {
            super(versions);
            this.init();
        }

        public ImmutableVersionComplex(@Nullable HVersionRange versionRange) {
            super(versionRange);
            this.init();
        }

        public ImmutableVersionComplex(@NotNull HVersionRange[] versionRanges) {
            super(versionRanges);
            this.init();
        }

        public ImmutableVersionComplex(@Nullable HVersionComplex version) {
            super(version);
            this.init();
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
        public void setVersions(@Nullable HVersionComplex version) {
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
        public void addVersionRange(@Nullable HVersionRange version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addVersions(@Nullable String versions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addVersions(@Nullable HVersionComplex versions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void autoFix() {
        }

        public @NotNull List<HVersionRange> getVersionRanges() {
            return Collections.unmodifiableList(this.versionRanges);
        }

        public @NotNull List<HStringVersion> getVersionSingles() {
            return Collections.unmodifiableList(this.versionSingles);
        }

        @Override
        public @NotNull ImmutableVersionComplex toImmutable() {
            return this;
        }
    }

    public static class UpdatableVersionComplex extends HVersionComplex implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(HVersionComplex.serialVersionUID);

        protected boolean updated;

        protected void init() {
            Collection<UpdatableVersionRange> list1 = new ArrayList<>(this.versionRanges.size());
            for (HVersionRange range: this.versionRanges)
                list1.add(range.toUpdatable());
            this.versionRanges.clear();
            this.versionRanges.addAll(list1);
            Collection<UpdatableStringVersion> list2 = new ArrayList<>(this.versionSingles.size());
            for (HStringVersion version: this.versionSingles)
                list2.add(version.toUpdatable());
            this.versionSingles.clear();
            this.versionSingles.addAll(list2);
        }

        public UpdatableVersionComplex() {
            super();
            this.init();
        }

        public UpdatableVersionComplex(@Nullable String version) throws HVersionFormatException {
            super(version);
            this.init();
        }

        public UpdatableVersionComplex(@Nullable HStringVersion version) {
            super(version);
            this.init();
        }

        public UpdatableVersionComplex(@NotNull HStringVersion[] versions) {
            super(versions);
            this.init();
        }

        public UpdatableVersionComplex(@Nullable HVersionRange versionRange) {
            super(versionRange);
            this.init();
        }

        public UpdatableVersionComplex(@NotNull HVersionRange[] versionRanges) {
            super(versionRanges);
            this.init();
        }

        public UpdatableVersionComplex(@Nullable HVersionComplex version) {
            super(version);
            this.init();
        }

        @Override
        public void setUniversal() {
            super.setUniversal();
            this.updated = false;
        }

        @Override
        public void setEmpty() {
            super.setEmpty();
            this.updated = false;
        }

        @Override
        public void setVersionSingle(@Nullable String version) throws HVersionFormatException {
            super.setVersionSingle(version);
            this.updated = false;
        }

        @Override
        public void setVersionSingle(@Nullable HStringVersion version) {
            super.setVersionSingle(version);
            this.updated = false;
        }

        @Override
        public void setVersionRange(@Nullable String version) throws HVersionFormatException {
            super.setVersionRange(version);
            this.updated = false;
        }

        @Override
        public void setVersionRange(@Nullable HVersionRange version) {
            super.setVersionRange(version);
            this.updated = false;
        }

        @Override
        public void setVersions(@Nullable String version) throws HVersionFormatException {
            super.setVersions(version);
            this.updated = false;
        }

        @Override
        public void setVersions(@Nullable HVersionComplex version) {
            super.setVersions(version);
            this.updated = false;
        }

        @Override
        public void addVersionSingle(@Nullable String version) throws HVersionFormatException {
            super.addVersionSingle(version);
            this.updated = false;
        }

        @Override
        public void addVersionSingle(@Nullable HStringVersion version) {
            super.addVersionSingle(version);
            this.updated = false;
        }

        @Override
        public void addVersionRange(@Nullable String version) throws HVersionFormatException {
            super.addVersionRange(version);
            this.updated = false;
        }

        @Override
        public void addVersionRange(@Nullable HVersionRange version) {
            super.addVersionRange(version);
            this.updated = false;
        }

        @Override
        public void addVersions(@Nullable String versions) throws HVersionFormatException {
            super.addVersions(versions);
            this.updated = false;
        }

        @Override
        public void addVersions(@Nullable HVersionComplex versions) {
            super.addVersions(versions);
            this.updated = false;
        }

        @Override
        public @NotNull UpdatableVersionComplex toUpdatable() {
            return this;
        }

        @Override
        public boolean getUpdated() {
            if (this.updated)
                return true;
            for (HVersionRange versionRange: this.versionRanges)
                if (((IUpdatable) versionRange).getUpdated())
                    return true;
            for (HStringVersion version: this.versionSingles)
                if (((IUpdatable) version).getUpdated())
                    return true;
            return false;
        }

        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
            for (HVersionRange versionRange: this.versionRanges)
                ((IUpdatable) versionRange).setUpdated(updated);
            for (HStringVersion version: this.versionSingles)
                ((IUpdatable) version).setUpdated(updated);
        }
    }
}
