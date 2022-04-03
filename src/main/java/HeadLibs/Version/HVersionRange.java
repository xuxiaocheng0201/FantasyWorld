package HeadLibs.Version;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Available version range.
 * Like intervals in mathematics.
 * @author xuxiaocheng
 */
/* Version form: [,]&(,]&{,,} */
@SuppressWarnings("unused")
public class HVersionRange implements Serializable {
    @Serial
    private static final long serialVersionUID = -7755725541591955245L;
    /**
     * Single interval without any operation.
     */
    private final List<SingleVersionRange> versionRanges = new ArrayList<>();
    /**
     * A single version number.
     */
    private final List<String> versionSingles = new ArrayList<>();

    /**
     * Construct a (min, max) interval.
     */
    public HVersionRange() {
        super();
    }

    /**
     *
     * @param version Version range.
     * @throws IllegalArgumentException version isn't legal.
     */
    public HVersionRange(@NotNull String version) throws IllegalArgumentException {
        super();
        this.addVersions(version);
    }

    public void addVersions(@NotNull String versions) throws IllegalArgumentException {
        String[] versions_ = versions.split("&");
        for (String version: versions_)
            this.addVersion(version);
    }

    public void addVersion(@NotNull String version) throws IllegalArgumentException {
        if (version.charAt(0) == '{' && version.charAt(version.length() - 1) == '}') {
            String[] versionS = HStringHelper.delBlankHeadAndTail(version.substring(1, version.length() - 1).split(","));
            this.versionSingles.addAll(Arrays.asList(versionS));
            return;
        }
        if ((version.charAt(0) == '(' || version.charAt(0) == '[') && (version.charAt(version.length() - 1) == ')' || version.charAt(version.length() - 1) == ']'))
            this.versionRanges.add(new SingleVersionRange(version));
        else
            this.versionSingles.add(version);
    }

    public @NotNull List<SingleVersionRange> getVersionRanges() {
        return this.versionRanges;
    }

    public @NotNull List<String> getVersionSingles() {
        return this.versionSingles;
    }

    public void clear() {
        this.versionRanges.clear();
        this.versionSingles.clear();
    }

    public boolean versionInRange(@NotNull String version) {
        for (SingleVersionRange range: this.versionRanges)
            if (range.versionInRange(version))
                return false;
        for (String s: this.versionSingles)
            if (HVersionComparator.compareVersion(version, s) == 0)
                return false;
        return true;
    }

    @Override
    public @NotNull String toString() {
        if (this.versionRanges.isEmpty()) {
            if (this.versionSingles.isEmpty())
                return "";
            StringBuilder builder = new StringBuilder("{");
            for (int i = 0; i < this.versionSingles.size(); ++i) {
                builder.append(this.versionSingles.get(i));
                if (i != this.versionSingles.size() - 1)
                    builder.append(", ");
            }
            builder.append("}");
            return builder.toString();
        }
        StringBuilder builder = new StringBuilder(10);
        for (int i = 0; i < this.versionRanges.size(); ++i) {
            builder.append((this.versionRanges.get(i).toString()));
            if (i != this.versionRanges.size() - 1)
                builder.append(" & ");
        }
        if (this.versionSingles.isEmpty())
            return builder.toString();
        builder.append(" & {");
        for (int i = 0; i < this.versionSingles.size(); ++i) {
            builder.append(this.versionSingles.get(i));
            if (i != this.versionSingles.size() - 1)
                builder.append(", ");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        HVersionRange that = (HVersionRange) o;

        if (!this.versionRanges.equals(that.versionRanges))
            return false;
        return this.versionSingles.equals(that.versionSingles);
    }

    @Override
    public int hashCode() {
        int result = this.versionRanges.hashCode();
        result = 31 * result + this.versionSingles.hashCode();
        return result;
    }

    /* Version form: [,] or (,) or [,) or (,] */
    public static class SingleVersionRange {
        private boolean leftEquable;
        private boolean rightEquable;
        private @Nullable String versionLeft;
        private @Nullable String versionRight;

        public SingleVersionRange() {
            super();
        }

        public SingleVersionRange(@NotNull String version) throws IllegalArgumentException {
            super();
            this.setVersionRange(version);
        }

        public void setVersionRange(@NotNull String version) throws IllegalArgumentException {
            byte canEqualLeft = 0;
            if (version.charAt(0) == '[')
                canEqualLeft = 1;
            if (version.charAt(0) == '(')
                canEqualLeft = -1;
            byte canEqualRight = 0;
            if (version.charAt(version.length() - 1) == ']')
                canEqualRight = 1;
            if (version.charAt(version.length() - 1) == ')')
                canEqualRight = -1;
            if (canEqualLeft == 0 && canEqualRight == 0) {
                this.leftEquable = true;
                this.rightEquable = true;
                this.versionLeft = version;
                this.versionRight = version;
                return;
            }
            if (canEqualLeft == 0 || canEqualRight == 0)
                throw new IllegalArgumentException("Unknown symbols in brackets.");
            String versions = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(version.substring(1, version.length() - 1)));
            int locationComma = versions.indexOf(',');
            if (locationComma == -1)
                throw new IllegalArgumentException("No commas in versions.");
            int locationComma1 = versions.lastIndexOf(',');
            if (locationComma != locationComma1)
                throw new IllegalArgumentException("Too many commas in versions.");
            this.leftEquable = canEqualLeft == 1;
            this.rightEquable = canEqualRight == 1;
            this.versionLeft = HStringHelper.delBlankHeadAndTail(versions.substring(0, locationComma));
            this.versionRight = HStringHelper.delBlankHeadAndTail(versions.substring(locationComma + 1));
        }

        public boolean versionInRange(@NotNull String version) {
            this.autoFix();
            int leftResult = HVersionComparator.compareVersion(this.versionLeft, version);
            int rightResult = HVersionComparator.compareVersion(version, this.versionRight);
            if (leftResult == 0 && !this.leftEquable)
                return false;
            if (rightResult == 0 && !this.rightEquable)
                return false;
            return leftResult < 0 && rightResult < 0;
        }

        public boolean isLegal() {
            int result = HVersionComparator.compareVersion(this.versionLeft, this.versionRight);
            if (result == 0 && (this.leftEquable || this.rightEquable))
                return false;
            return result < 0;
        }

        public void autoFix() {
            int result = HVersionComparator.compareVersion(this.versionLeft, this.versionRight);
            if (result > 0 || (result == 0 && !this.leftEquable && !this.rightEquable))
                return;
            if (result == 0) {
                this.leftEquable = true;
                this.rightEquable = true;
            }
            if (result < 0) {
                String temp = this.versionLeft;
                this.versionLeft = this.versionRight;
                this.versionRight = temp;
            }
        }

        public void clear() {
            this.leftEquable = false;
            this.rightEquable = false;
            this.versionLeft = null;
            this.versionRight = null;
        }

        public boolean isLeftEquable() {
            if (this.versionLeft == null)
                return false;
            return this.leftEquable;
        }

        public void setLeftEquable(boolean leftEquable) {
            this.leftEquable = leftEquable;
        }

        public boolean isRightEquable() {
            if (this.versionRight == null)
                return false;
            return this.rightEquable;
        }

        public void setRightEquable(boolean rightEquable) {
            this.rightEquable = rightEquable;
        }

        public @Nullable String getVersionLeft() {
            return this.versionLeft;
        }

        public void setVersionLeft(@Nullable String versionLeft) {
            this.versionLeft = versionLeft;
        }

        public @Nullable String getVersionRight() {
            return this.versionRight;
        }

        public void setVersionRight(@Nullable String versionRight) {
            this.versionRight = versionRight;
        }

        @Override
        public @NotNull String toString() {
            return HStringHelper.merge((this.isLeftEquable() ? '[' : '('), this.versionLeft, ',', this.versionRight, (this.isRightEquable() ? ']' : ')'));
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;

            SingleVersionRange that = (SingleVersionRange) o;

            if (this.leftEquable != that.leftEquable) return false;
            if (this.rightEquable != that.rightEquable) return false;
            if (!Objects.equals(this.versionLeft, that.versionLeft)) return false;
            return Objects.equals(this.versionRight, that.versionRight);
        }

        @Override
        public int hashCode() {
            int result = (this.leftEquable ? 1 : 0);
            result = 31 * result + (this.rightEquable ? 1 : 0);
            result = 31 * result + (this.versionLeft != null ? this.versionLeft.hashCode() : 0);
            result = 31 * result + (this.versionRight != null ? this.versionRight.hashCode() : 0);
            return result;
        }
    }
}
