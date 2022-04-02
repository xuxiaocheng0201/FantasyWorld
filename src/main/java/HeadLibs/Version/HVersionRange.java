package HeadLibs.Version;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* Version form: [,]&(,]&{,,} */
public class HVersionRange {
    private final List<SingleVersionRange> versionRanges = new ArrayList<>();
    private final List<String> versionSingles = new ArrayList<>();

    public HVersionRange() {
        super();
    }

    public HVersionRange(@NotNull String version) throws IllegalArgumentException {
        addVersions(version);
    }

    public void addVersions(@NotNull String versions) throws IllegalArgumentException {
        String[] versions_ = versions.split("&");
        for (String version: versions_)
            addVersion(version);
    }

    public void addVersion(@NotNull String version) throws IllegalArgumentException {
        if (version.charAt(0) == '{' && version.charAt(version.length() - 1) == '}') {
            String[] versionS = HStringHelper.delBlankHeadAndTail(version.substring(1, version.length() - 1).split(","));
            versionSingles.addAll(Arrays.asList(versionS));
            return;
        }
        if ((version.charAt(0) == '(' || version.charAt(0) == '[') && (version.charAt(version.length() - 1) == ')' || version.charAt(version.length() - 1) == ']'))
            this.versionRanges.add(new SingleVersionRange(version));
        else
            this.versionSingles.add(version);
    }

    public @NotNull List<SingleVersionRange> getVersionRanges() {
        return versionRanges;
    }

    public @NotNull List<String> getVersionSingles() {
        return versionSingles;
    }

    public void clear() {
        this.versionRanges.clear();
        this.versionSingles.clear();
    }

    public boolean versionInRange(String version) {
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
        if (versionRanges.isEmpty()) {
            if (versionSingles.isEmpty())
                return "";
            StringBuilder builder = new StringBuilder("{");
            for (int i = 0; i < versionSingles.size(); ++i) {
                builder.append(versionSingles.get(i));
                if (i != versionSingles.size() - 1)
                    builder.append(", ");
            }
            builder.append("}");
            return builder.toString();
        }
        StringBuilder builder = new StringBuilder(10);
        for (int i = 0; i < versionRanges.size(); ++i) {
            builder.append((versionRanges.get(i).toString()));
            if (i != versionRanges.size() - 1)
                builder.append(" & ");
        }
        if (versionSingles.isEmpty())
            return builder.toString();
        builder.append(" & {");
        for (int i = 0; i < versionSingles.size(); ++i) {
            builder.append(versionSingles.get(i));
            if (i != versionSingles.size() - 1)
                builder.append(", ");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HVersionRange that = (HVersionRange) o;

        if (!versionRanges.equals(that.versionRanges))
            return false;
        return versionSingles.equals(that.versionSingles);
    }

    @Override
    public int hashCode() {
        int result = versionRanges.hashCode();
        result = 31 * result + versionSingles.hashCode();
        return result;
    }

    /* Version form: [,] or (,) or [,) or (,] */
    public static class SingleVersionRange {
        private boolean leftEquable = false;
        private boolean rightEquable = false;
        private @Nullable String versionLeft = null;
        private @Nullable String versionRight = null;

        public SingleVersionRange() {
            super();
        }

        public SingleVersionRange(@NotNull String version) throws IllegalArgumentException {
            setVersionRange(version);
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

        public boolean versionInRange(String version) {
            autoFix();
            int leftResult = HVersionComparator.compareVersion(versionLeft, version);
            int rightResult = HVersionComparator.compareVersion(version, versionRight);
            if (leftResult == 0 && !leftEquable)
                return false;
            if (rightResult == 0 && !rightEquable)
                return false;
            return leftResult < 0 && rightResult < 0;
        }

        public boolean isLegal() {
            int result = HVersionComparator.compareVersion(versionLeft, versionRight);
            if (result == 0 && (leftEquable || rightEquable))
                return false;
            return result < 0;
        }

        public void autoFix() {
            int result = HVersionComparator.compareVersion(versionLeft, versionRight);
            if (result > 0 || (result == 0 && !leftEquable && !rightEquable))
                return;
            if (result == 0) {
                this.leftEquable = true;
                this.rightEquable = true;
            }
            if (result < 0) {
                String temp = versionLeft;
                versionLeft = versionRight;
                versionRight = temp;
            }
        }

        public void clear() {
            this.leftEquable = false;
            this.rightEquable = false;
            this.versionLeft = null;
            this.versionRight = null;
        }

        public boolean isLeftEquable() {
            if (versionLeft == null)
                return false;
            return leftEquable;
        }

        public void setLeftEquable(boolean leftEquable) {
            this.leftEquable = leftEquable;
        }

        public boolean isRightEquable() {
            if (versionRight == null)
                return false;
            return rightEquable;
        }

        public void setRightEquable(boolean rightEquable) {
            this.rightEquable = rightEquable;
        }

        public @Nullable String getVersionLeft() {
            return versionLeft;
        }

        public void setVersionLeft(@Nullable String versionLeft) {
            this.versionLeft = versionLeft;
        }

        public @Nullable String getVersionRight() {
            return versionRight;
        }

        public void setVersionRight(@Nullable String versionRight) {
            this.versionRight = versionRight;
        }

        @Override
        public @NotNull String toString() {
            return HStringHelper.merge((isLeftEquable() ? '[' : '('), versionLeft, ',', versionRight, (isRightEquable() ? ']' : ')'));
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SingleVersionRange that = (SingleVersionRange) o;

            if (leftEquable != that.leftEquable) return false;
            if (rightEquable != that.rightEquable) return false;
            if (!Objects.equals(versionLeft, that.versionLeft)) return false;
            return Objects.equals(versionRight, that.versionRight);
        }

        @Override
        public int hashCode() {
            int result = (leftEquable ? 1 : 0);
            result = 31 * result + (rightEquable ? 1 : 0);
            result = 31 * result + (versionLeft != null ? versionLeft.hashCode() : 0);
            result = 31 * result + (versionRight != null ? versionRight.hashCode() : 0);
            return result;
        }
    }
}
