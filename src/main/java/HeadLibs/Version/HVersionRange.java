package HeadLibs.Version;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/* Version form: [,] or (,) or [,) or (,]*/
@SuppressWarnings("unused")
public class HVersionRange implements Serializable {
    @Serial
    private static final long serialVersionUID = 4882505281267885490L;

    private boolean empty;
    private boolean leftEquable;
    private boolean rightEquable;
    private @NotNull HStringVersion leftVersion = HStringVersion.EMPTY;
    private @NotNull HStringVersion rightVersion = HStringVersion.EMPTY;

    public HVersionRange() {
        super();
    }

    public HVersionRange(@Nullable String version) throws HVersionFormatException {
        super();
        this.setVersionRange(version);
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isLeftEquable() {
        return this.leftEquable;
    }

    public void setLeftEquable(boolean leftEquable) {
        this.leftEquable = leftEquable;
    }

    public boolean isRightEquable() {
        return this.rightEquable;
    }

    public void setRightEquable(boolean rightEquable) {
        this.rightEquable = rightEquable;
    }

    public @NotNull HStringVersion getLeftVersion() {
        return this.leftVersion;
    }

    public void setLeftVersion(@Nullable String leftVersion) throws HVersionFormatException {
        this.leftVersion = new HStringVersion(leftVersion);
    }

    public void setLeftVersion(@Nullable HStringVersion versionLeft) {
        if (versionLeft == null)
            this.leftVersion = HStringVersion.EMPTY;
        else
            this.leftVersion = versionLeft;
        this.empty = false;
    }

    public @NotNull HStringVersion getRightVersion() {
        return this.rightVersion;
    }

    public void setRightVersion(@Nullable String rightVersion) throws HVersionFormatException {
        this.rightVersion = new HStringVersion(rightVersion);
    }

    public void setRightVersion(@Nullable HStringVersion rightVersion) {
        if (rightVersion == null)
            this.rightVersion = HStringVersion.EMPTY;
        else
            this.rightVersion = rightVersion;
        this.empty = false;
    }

    public void setAll() {
        this.leftEquable = false;
        this.rightEquable = false;
        this.leftVersion = HStringVersion.EMPTY;
        this.rightVersion = HStringVersion.EMPTY;
        this.empty = false;
    }

    public void setEmpty() {
        this.leftEquable = false;
        this.rightEquable = false;
        this.leftVersion = HStringVersion.EMPTY;
        this.rightVersion = HStringVersion.EMPTY;
        this.empty = true;
    }

    public void setVersionSingle(String version) throws HVersionFormatException {
        this.setVersionSingle(new HStringVersion(version));
    }

    public void setVersionSingle(HStringVersion version) {
        this.leftEquable = true;
        this.rightEquable = true;
        this.leftVersion = version;
        this.rightVersion = version;
        this.empty = false;
    }

    public void setVersionRange(@Nullable String minVersion, @Nullable String maxVersion) throws HVersionFormatException {
        this.setVersionRange(true, new HStringVersion(minVersion), new HStringVersion(maxVersion), true);
    }

    public void setVersionRange(@Nullable HStringVersion minVersion, @Nullable HStringVersion maxVersion) {
        this.setVersionRange(true, minVersion, maxVersion, true);
    }

    public void setVersionRange(boolean leftEquable, @Nullable String minVersion, @Nullable String maxVersion, boolean rightEquable) throws HVersionFormatException {
        this.setVersionRange(leftEquable, new HStringVersion(minVersion), new HStringVersion(maxVersion), rightEquable);
    }

    public void setVersionRange(boolean leftEquable, @Nullable HStringVersion minVersion, @Nullable HStringVersion maxVersion, boolean rightEquable) {
        this.leftEquable = leftEquable;
        this.rightEquable = rightEquable;
        this.setLeftVersion(minVersion);
        this.setRightVersion(maxVersion);
        this.autoFix();
    }

    public void setVersionRange(@Nullable String versionIn) throws HVersionFormatException {
        String version = HStringHelper.nullableStrip(versionIn);
        if (version == null || version.isEmpty()) {
            this.setAll();
            return;
        }
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
            this.setVersionSingle(new HStringVersion(version));
            return;
        }
        if (canEqualLeft == 0 || canEqualRight == 0)
            throw new HVersionFormatException("Unpaired brackets.", version);
        String versions = HStringHelper.notNullStrip(version.substring(1, version.length() - 1));
        int locationComma = versions.indexOf(',');
        if (locationComma == -1) {
            this.setVersionSingle(new HStringVersion(versions));
            return;
        }
        int locationComma1 = versions.lastIndexOf(',');
        if (locationComma != locationComma1)
            throw new HVersionFormatException("Too many commas in version range.", version);
        this.leftEquable = canEqualLeft == 1;
        this.rightEquable = canEqualRight == 1;
        this.leftVersion = new HStringVersion(versions.substring(0, locationComma));
        this.rightVersion = new HStringVersion(versions.substring(locationComma + 1));
        this.empty = false;
        this.autoFix();
    }

    public void autoFix() {
        if (this.empty) {
            this.setEmpty();
            return;
        }
        if (HStringVersion.isNull(this.leftVersion) || HStringVersion.isNull(this.rightVersion)) {
            if (HStringVersion.isNull(this.leftVersion))
                this.leftEquable = false;
            if (HStringVersion.isNull(this.rightVersion))
                this.rightEquable = false;
            return;
        }
        int result = HStringVersion.compareVersion(this.leftVersion, this.rightVersion);
        if (result > 0)
            this.setEmpty();
        if (result == 0) {
            this.leftEquable = true;
            this.rightEquable = true;
        }
    }

    public boolean versionInRange(@Nullable String version) throws HVersionFormatException {
        return this.versionInRange(new HStringVersion(version));
    }

    public boolean versionInRange(@Nullable HStringVersion version) {
        if (version == null || this.empty)
            return false;
        this.autoFix();
        int leftResult = HStringVersion.compareVersion(this.leftVersion, version);
        int rightResult = HStringVersion.compareVersion(version, this.rightVersion);
        if (leftResult == 0 && !this.leftEquable)
            return false;
        if (rightResult == 0 && !this.rightEquable)
            return false;
        return leftResult < 0 && rightResult < 0;
    }

    @Override
    public @NotNull String toString() {
        return this.empty ? "empty" :
                HStringHelper.concat(this.leftEquable ? '[' : '(',
                this.leftVersion.getVersion(), ',', this.rightVersion.getVersion(),
                this.rightEquable ? ']' : ')');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HVersionRange that = (HVersionRange) o;
        if (this.empty == that.empty)
            return this.empty;
        return this.leftEquable == that.leftEquable && this.rightEquable == that.rightEquable && this.leftVersion.equals(that.leftVersion) && this.rightVersion.equals(that.rightVersion);
    }

    @Override
    public int hashCode() {
        if (this.empty) return 0;
        return Objects.hash(this.leftEquable, this.rightEquable, this.leftVersion, this.rightVersion);
    }
}
