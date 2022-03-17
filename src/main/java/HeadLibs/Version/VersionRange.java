package HeadLibs.Version;

import HeadLibs.Helper.HStringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* Version form: [,]&(,]&{,,} */
public class VersionRange {
    private final List<SingleVersionRange> versionRanges = new ArrayList<>();
    private final List<String> versionSingles = new ArrayList<>();

    public VersionRange() {
        super();
    }

    public VersionRange(String version) throws IllegalArgumentException {
        addVersions(version);
    }

    public void addVersions(String versions) throws IllegalArgumentException {
        String[] versions_ = versions.split("&");
        for (String version: versions_)
            addVersion(version);
    }

    public void addVersion(String version) throws IllegalArgumentException {
        if (version.charAt(0) == '{' && version.charAt(version.length() - 1) == '}') {
            String[] versionS = HStringHelper.delBlankHeadAndTail(version.substring(1, version.length() - 1).split(","));
            versionSingles.addAll(Arrays.asList(versionS));
            return;
        }
        this.versionRanges.add(new SingleVersionRange(version));
    }

    public List<SingleVersionRange> getVersionRanges() {
        return versionRanges;
    }

    public List<String> getVersionSingles() {
        return versionSingles;
    }

    public void clear() {
        this.versionRanges.clear();
        this.versionSingles.clear();
    }

    public boolean versionInRange(String version) {
        for (SingleVersionRange range: this.versionRanges)
            if (range.versionInRange(version))
                return true;
        for (String s: this.versionSingles)
            if (HVersionComparator.compareVersion(version, s) == 0)
                return true;
        return false;
    }

    @Override
    public String toString() {
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
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < versionRanges.size(); ++i) {
            builder.append((versionRanges.get(i).toString()));
            if (i != versionSingles.size() - 1)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VersionRange that = (VersionRange) o;

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
        private String versionLeft = null;
        private String versionRight = null;

        public SingleVersionRange() {
            super();
        }

        public SingleVersionRange(String version) throws IllegalArgumentException {
            setVersionRange(version);
        }

        public void setVersionRange(String version) throws IllegalArgumentException {
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
            String versions = HStringHelper.delBlankHeadAndTail(version.substring(1, version.length() - 1));
            int locationComma = versions.indexOf(",");
            int locationComma1 = versions.lastIndexOf(",");
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
                this.leftEquable = false;
                this.rightEquable = false;
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

        public String getVersionLeft() {
            return versionLeft;
        }

        public void setVersionLeft(String versionLeft) {
            this.versionLeft = versionLeft;
        }

        public String getVersionRight() {
            return versionRight;
        }

        public void setVersionRight(String versionRight) {
            this.versionRight = versionRight;
        }

        @Override
        public String toString() {
            return HStringHelper.merge((isLeftEquable() ? '[' : '('), getVersionLeft(), ',', getVersionRight(), (isRightEquable() ? ']' : ')'));
        }

        @Override
        public boolean equals(Object o) {
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
