package HeadLibs.Version;

import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Version.HStringVersion.ImmutableStringVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Range for versions. Default: Universal.
 * String form: [,] or (,) or [,) or (,]
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HVersionRange implements Serializable {
    @Serial
    private static final long serialVersionUID = 4882505281267885490L;

    /**
     * A flag to differentiate empty and universal set.
     */
    protected boolean empty;

    protected boolean leftEquable;
    protected boolean rightEquable;
    protected /*final*/ @NotNull HStringVersion leftVersion = new HStringVersion();
    protected /*final*/ @NotNull HStringVersion rightVersion = new HStringVersion();

    protected boolean autoFixed;

    public HVersionRange() {
        super();
    }

    public HVersionRange(@Nullable String versionRange) throws HVersionFormatException {
        super();
        this.setVersionRange(versionRange);
    }

    public HVersionRange(@Nullable HVersionRange versionRange) {
        super();
        this.setVersionRange(versionRange);
    }

    public boolean isEmpty() {
        this.autoFix();
        return this.empty && !this.leftEquable && !this.rightEquable
                && HStringVersion.isNull(this.leftVersion) && HStringVersion.isNull(this.rightVersion);
    }

    public boolean isLeftEquable() {
        this.autoFix();
        return this.leftEquable;
    }

    public void setLeftEquable(boolean leftEquable) {
        this.leftEquable = leftEquable;
        this.autoFixed = false;
    }

    public boolean isRightEquable() {
        this.autoFix();
        return this.rightEquable;
    }

    public void setRightEquable(boolean rightEquable) {
        this.rightEquable = rightEquable;
        this.autoFixed = false;
    }

    public @NotNull ImmutableStringVersion getLeftVersion() {
        this.autoFix();
        return this.leftVersion.toImmutable();
    }

    public void setLeftVersion(@Nullable String leftVersion) throws HVersionFormatException {
        this.leftVersion.setVersion(leftVersion);
        this.autoFixed = false;
    }

    public void setLeftVersion(@Nullable HStringVersion leftVersion) {
        this.leftVersion.setVersion(leftVersion);
        this.empty = false;
        this.autoFixed = false;
    }

    public @NotNull ImmutableStringVersion getRightVersion() {
        this.autoFix();
        return this.rightVersion.toImmutable();
    }

    public void setRightVersion(@Nullable String rightVersion) throws HVersionFormatException {
        this.rightVersion.setVersion(rightVersion);
        this.autoFixed = false;
    }

    public void setRightVersion(@Nullable HStringVersion rightVersion) {
        this.rightVersion.setVersion(rightVersion);
        this.empty = false;
        this.autoFixed = false;
    }

    public void setUniversal() {
        this.leftEquable = false;
        this.rightEquable = false;
        this.leftVersion.setNull();
        this.rightVersion.setNull();
        this.empty = false;
        this.autoFixed = true;
    }

    public void setEmpty() {
        this.leftEquable = false;
        this.rightEquable = false;
        this.leftVersion.setNull();
        this.rightVersion.setNull();
        this.empty = true;
        this.autoFixed = true;
    }

    public void setVersionSingle(@Nullable String version) throws HVersionFormatException {
        this.setVersionSingle(new HStringVersion(version));
    }

    public void setVersionSingle(@Nullable HStringVersion version) {
        if (version == null) {
            this.setEmpty();
            return;
        }
        this.leftEquable = true;
        this.rightEquable = true;
        this.leftVersion.setVersion(version);
        this.rightVersion.setVersion(version);
        this.empty = false;
        this.autoFixed = true;
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
        this.autoFixed = false;
    }

    public void setVersionRange(@Nullable String versionRangeIn) throws HVersionFormatException {
        String versionRange = HStringHelper.nullableStrip(versionRangeIn);
        if (versionRange == null || versionRange.isEmpty()) {
            this.setUniversal();
            return;
        }
        byte canEqualLeft = 0;
        if (versionRange.charAt(0) == '[')
            canEqualLeft = 1;
        if (versionRange.charAt(0) == '(')
            canEqualLeft = -1;
        byte canEqualRight = 0;
        if (versionRange.charAt(versionRange.length() - 1) == ']')
            canEqualRight = 1;
        if (versionRange.charAt(versionRange.length() - 1) == ')')
            canEqualRight = -1;
        if (canEqualLeft == 0 && canEqualRight == 0) {
            this.setVersionSingle(new HStringVersion(versionRange));
            return;
        }
        if (canEqualLeft == 0 || canEqualRight == 0)
            throw new HVersionFormatException("Unpaired brackets.", versionRange);
        String versions = HStringHelper.notNullStrip(versionRange.substring(1, versionRange.length() - 1));
        int locationComma = versions.indexOf(',');
        if (locationComma == -1) {
            this.setVersionSingle(new HStringVersion(versions));
            return;
        }
        int locationComma1 = versions.lastIndexOf(',');
        if (locationComma != locationComma1)
            throw new HVersionFormatException("Too many commas in version range.", versionRange);
        this.leftEquable = canEqualLeft == 1;
        this.rightEquable = canEqualRight == 1;
        this.leftVersion.setVersion(versions.substring(0, locationComma));
        this.rightVersion.setVersion(versions.substring(locationComma + 1));
        this.empty = false;
        this.autoFixed = false;
    }

    public void setVersionRange(@Nullable HVersionRange versionRange) {
        if (versionRange == null) {
            this.setUniversal();
            return;
        }
        this.empty = versionRange.empty;
        this.leftEquable = versionRange.leftEquable;
        this.rightEquable = versionRange.rightEquable;
        this.leftVersion.setVersion(versionRange.leftVersion);
        this.rightVersion.setVersion(versionRange.rightVersion);
        this.autoFixed = false;
    }

    /**
     * Deal with some obvious mistakes automatically.
     */
    public void autoFix() {
        if (this.autoFixed)
            return;
        this.autoFixed = true;
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

    public @NotNull ImmutableVersionRange toImmutable() {
        return new ImmutableVersionRange(this);
    }

    public @NotNull UpdatableVersionRange toUpdatable() {
        return new UpdatableVersionRange(this);
    }

    @Override
    public @NotNull String toString() {
        this.autoFix();
        if (this.empty)
            return "empty";
        return this.leftEquable ? "[" : "(" +
                this.leftVersion.getVersion() + ',' + this.rightVersion.getVersion() +
                (this.rightEquable ? ']' : ')');
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HVersionRange that)) return false;
        this.autoFix();
        that.autoFix();
        if (this.empty && that.empty)
            return true;
        return this.empty == that.empty && this.leftEquable == that.leftEquable && this.rightEquable == that.rightEquable && this.leftVersion.equals(that.leftVersion) && this.rightVersion.equals(that.rightVersion);
    }

    @Override
    public int hashCode() {
        this.autoFix();
        if (this.empty) return 0;
        return Objects.hash(this.leftEquable, this.rightEquable, this.leftVersion, this.rightVersion);
    }

    public static class ImmutableVersionRange extends HVersionRange implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(HVersionRange.serialVersionUID);

        public ImmutableVersionRange() {
            super();
            this.leftVersion = this.leftVersion.toImmutable();
            this.rightVersion = this.rightVersion.toImmutable();
        }

        public ImmutableVersionRange(@Nullable String versionRange) throws HVersionFormatException {
            super(versionRange);
            this.leftVersion = this.leftVersion.toImmutable();
            this.rightVersion = this.rightVersion.toImmutable();
        }

        public ImmutableVersionRange(@Nullable HVersionRange versionRange) {
            super(versionRange);
            this.leftVersion = this.leftVersion.toImmutable();
            this.rightVersion = this.rightVersion.toImmutable();
        }

        @Override
        public void setLeftEquable(boolean leftEquable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setRightEquable(boolean rightEquable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLeftVersion(@Nullable String leftVersion) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLeftVersion(@Nullable HStringVersion leftVersion) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setRightVersion(@Nullable String rightVersion) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setRightVersion(@Nullable HStringVersion rightVersion) {
            throw new UnsupportedOperationException();
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
        public void setVersionRange(@Nullable String minVersion, @Nullable String maxVersion) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionRange(@Nullable HStringVersion minVersion, @Nullable HStringVersion maxVersion) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionRange(boolean leftEquable, @Nullable String minVersion, @Nullable String maxVersion, boolean rightEquable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionRange(boolean leftEquable, @Nullable HStringVersion minVersion, @Nullable HStringVersion maxVersion, boolean rightEquable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionRange(@Nullable String versionRangeIn) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setVersionRange(@Nullable HVersionRange versionRange) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void autoFix() {
        }

        @Override
        public @NotNull ImmutableVersionRange toImmutable() {
            return this;
        }
    }

    public static class UpdatableVersionRange extends HVersionRange implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(HVersionRange.serialVersionUID);

        protected boolean updated;

        public UpdatableVersionRange() {
            super();
            this.leftVersion = this.leftVersion.toUpdatable();
            this.rightVersion = this.rightVersion.toUpdatable();
        }

        public UpdatableVersionRange(@Nullable String versionRange) throws HVersionFormatException {
            super(versionRange);
            this.leftVersion = this.leftVersion.toUpdatable();
            this.rightVersion = this.rightVersion.toUpdatable();
        }

        public UpdatableVersionRange(@Nullable HVersionRange versionRange) {
            super(versionRange);
            this.leftVersion = this.leftVersion.toUpdatable();
            this.rightVersion = this.rightVersion.toUpdatable();
        }

        @Override
        public void setLeftEquable(boolean leftEquable) {
            super.setLeftEquable(leftEquable);
            this.updated = true;
        }

        @Override
        public void setRightEquable(boolean rightEquable) {
            super.setRightEquable(rightEquable);
            this.updated = true;
        }

        @Override
        public void setLeftVersion(@Nullable String leftVersion) throws HVersionFormatException {
            super.setLeftVersion(leftVersion);
            this.updated = true;
        }

        @Override
        public void setLeftVersion(@Nullable HStringVersion leftVersion) {
            super.setLeftVersion(leftVersion);
            this.updated = true;
        }

        @Override
        public void setRightVersion(@Nullable String rightVersion) throws HVersionFormatException {
            super.setRightVersion(rightVersion);
            this.updated = true;
        }

        @Override
        public void setRightVersion(@Nullable HStringVersion rightVersion) {
            super.setRightVersion(rightVersion);
            this.updated = true;
        }

        @Override
        public void setUniversal() {
            super.setUniversal();
            this.updated = true;
        }

        @Override
        public void setEmpty() {
            super.setEmpty();
            this.updated = true;
        }

        @Override
        public void setVersionSingle(@Nullable String version) throws HVersionFormatException {
            super.setVersionSingle(version);
            this.updated = true;
        }

        @Override
        public void setVersionSingle(@Nullable HStringVersion version) {
            super.setVersionSingle(version);
            this.updated = true;
        }

        @Override
        public void setVersionRange(@Nullable String minVersion, @Nullable String maxVersion) throws HVersionFormatException {
            super.setVersionRange(minVersion, maxVersion);
            this.updated = true;
        }

        @Override
        public void setVersionRange(@Nullable HStringVersion minVersion, @Nullable HStringVersion maxVersion) {
            super.setVersionRange(minVersion, maxVersion);
            this.updated = true;
        }

        @Override
        public void setVersionRange(boolean leftEquable, @Nullable String minVersion, @Nullable String maxVersion, boolean rightEquable) throws HVersionFormatException {
            super.setVersionRange(leftEquable, minVersion, maxVersion, rightEquable);
            this.updated = true;
        }

        @Override
        public void setVersionRange(boolean leftEquable, @Nullable HStringVersion minVersion, @Nullable HStringVersion maxVersion, boolean rightEquable) {
            super.setVersionRange(leftEquable, minVersion, maxVersion, rightEquable);
            this.updated = true;
        }

        @Override
        public void setVersionRange(@Nullable String versionRangeIn) throws HVersionFormatException {
            super.setVersionRange(versionRangeIn);
            this.updated = true;
        }

        @Override
        public void setVersionRange(@Nullable HVersionRange versionRange) {
            super.setVersionRange(versionRange);
            this.updated = true;
        }

        @Override
        public @NotNull UpdatableVersionRange toUpdatable() {
            return this;
        }

        @Override
        public boolean getUpdated() {
            if (!((IUpdatable) this.leftVersion).getUpdated())
                return false;
            if (!((IUpdatable) this.rightVersion).getUpdated())
                return false;
            return this.updated;
        }

        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
            ((IUpdatable) this.leftVersion).setUpdated(updated);
            ((IUpdatable) this.rightVersion).setUpdated(updated);
        }
    }
}
