package HeadLibs.Version;

import HeadLibs.Helper.HCharHelper;
import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Package {@link String} to {@code Version}.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HStringVersion implements Serializable, Comparable<HStringVersion> {
    @Serial
    private static final long serialVersionUID = -8779747563654866413L;

    /**
     * Null version.
     */
    public static HStringVersion EMPTY = new HStringVersion();

    /**
     * Version.
     */
    private @NotNull String version = "";

    /**
     * Construct a null version.
     */
    public HStringVersion() {
        super();
    }

    /**
     * Construct a version from version string.
     * @param version the version string
     * @throws HVersionFormatException Wrong formation of version string.
     */
    public HStringVersion(@Nullable String version) throws HVersionFormatException {
        super();
        this.setVersion(version);
    }

    public @NotNull String getVersion() {
        return this.version;
    }

    public void setVersion(@Nullable String version) throws HVersionFormatException {
        if (version == null) {
            this.version = "";
            return;
        }
        if (version.contains("(") || version.contains(")")
                || version.contains("[") || version.contains("]")
                || version.contains("{") || version.contains("}"))
            throw new HVersionFormatException("Found brackets in string version.", version);
        if (version.contains(",") || version.contains("&"))
            throw new HVersionFormatException("Found separation character in string version.", version);
        this.version = version.strip();
    }

    public boolean inRange(@Nullable String versionRange) throws HVersionFormatException {
        if (versionRange == null)
            return false;
        return (new HVersionRange(versionRange)).versionInRange(this);
    }

    public boolean inRange(@Nullable HVersionRange versionRange) {
        if (versionRange == null)
            return false;
        return versionRange.versionInRange(this);
    }

    @Override
    public @NotNull String toString() {
        return this.version;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HStringVersion that = (HStringVersion) o;
        return this.version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return this.version.hashCode();
    }

    @Override
    public int compareTo(@Nullable HStringVersion o) {
        return compareVersion(this, o);
    }

    /**
     * Is a null version?
     * @param version the version
     * @return true - null. false - notNull.
     */
    public static boolean isNull(@Nullable HStringVersion version) {
        if (version == null)
            return true;
        return version.version.isEmpty();
    }

    public static int compareVersion(@Nullable String leftVersion, @Nullable String rightVersion) throws HVersionFormatException {
        return compareVersion(new HStringVersion(leftVersion), new HStringVersion(rightVersion));
    }

    public static int compareVersion(@Nullable HStringVersion leftVersion, @Nullable HStringVersion rightVersion) {
        if (isNull(leftVersion) || isNull(rightVersion))
            return -1;
        return compareVersionWithoutPosition(leftVersion, rightVersion, true);
    }

    public static int compareVersionWithoutPosition(@Nullable String a, @Nullable String b, boolean nullMeanMax) throws HVersionFormatException {
        return compareVersionWithoutPosition(new HStringVersion(a), new HStringVersion(b), nullMeanMax);
    }

    public static int compareVersionWithoutPosition(@Nullable HStringVersion a, @Nullable HStringVersion b, boolean nullMeanMax) {
        if (isNull(a)) {
            if (isNull(b))
                return 0;
            return nullMeanMax ? 1 : -1;
        }
        if (isNull(b))
            return nullMeanMax ? -1 : 1;
        String[] versionArray1 = HStringHelper.strip(a.version.split("\\."));
        String[] versionArray2 = HStringHelper.strip(b.version.split("\\."));
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int difference = 0;
        for (int i = 0; i < minLength; ++i) {
            List<String> version1 = new ArrayList<>();
            boolean isNumber = false;
            StringBuilder temp = new StringBuilder(2);
            for (int j = 0; j < versionArray1[i].length(); ++j) {
                char ch = versionArray1[i].charAt(j);
                if (HCharHelper.isNumber(ch))
                    if (isNumber)
                        temp.append(ch);
                    else {
                        isNumber = true;
                        if (!temp.isEmpty())
                            version1.add(temp.toString());
                        temp = new StringBuilder(String.valueOf(ch));
                    }
                else
                if (isNumber) {
                    isNumber = false;
                    if (!temp.isEmpty())
                        version1.add(temp.toString());
                    temp = new StringBuilder(String.valueOf(ch));
                } else
                    temp.append(ch);
            }
            version1.add(temp.toString());
            List<String> version2 = new ArrayList<>();
            isNumber = false;
            temp = new StringBuilder(2);
            for (int j = 0; j < versionArray2[i].length(); ++j) {
                char ch = versionArray2[i].charAt(j);
                if (HCharHelper.isNumber(ch))
                    if (isNumber)
                        temp.append(ch);
                    else {
                        isNumber = true;
                        if (!temp.isEmpty())
                            version2.add(temp.toString());
                        temp = new StringBuilder(String.valueOf(ch));
                    }
                else
                if (isNumber) {
                    isNumber = false;
                    if (!temp.isEmpty())
                        version2.add(temp.toString());
                    temp = new StringBuilder(String.valueOf(ch));
                } else
                    temp.append(ch);
            }
            version2.add(temp.toString());
            int minLen = Math.min(version1.size(), version2.size());
            for (int j = 0; j < minLen; ++j) {
                boolean number1 = HCharHelper.isNumber(version1.get(j).charAt(0));
                boolean number2 = HCharHelper.isNumber(version2.get(j).charAt(0));
                if (number1 && number2)
                    difference = Integer.parseInt(version1.get(j)) - Integer.parseInt(version2.get(j));
                if (number1 && !number2)
                    difference = -1;//字母优先
                if (!number1 && number2)
                    difference = 1;//字母优先
                if (!number1 && !number2)
                    difference = version1.get(j).compareTo(version2.get(j));
                if (difference != 0)
                    break;
            }
            if (difference != 0)
                break;
        }
        return (difference != 0) ? difference : versionArray1.length - versionArray2.length;
    }
}
