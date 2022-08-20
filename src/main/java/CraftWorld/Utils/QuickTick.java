package CraftWorld.Utils;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.Objects;

public class QuickTick implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -7856577519248919328L;
    public static final DSTId id = DSTId.getDstIdInstance("QuickTick");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull BigInteger tick = BigInteger.ZERO;
    protected long cacheTick;
    protected long quickTick;

    public QuickTick() {
        super();
    }

    public QuickTick(long tick) {
        super();
        this.set(tick);
    }

    public QuickTick(@Nullable BigInteger tick) {
        super();
        this.set(tick);
    }

    public QuickTick(@NotNull String tick) {
        super();
        this.set(tick);
    }

    public QuickTick(@NotNull String tick, int radix) {
        super();
        this.set(tick, radix);
    }

    public QuickTick(@Nullable QuickTick tick) {
        super();
        this.set(tick);
    }

    public void clear() {
        this.tick = BigInteger.ZERO;
        this.cacheTick = 0L;
        this.quickTick = 0L;
    }

    public void set(long tick) {
        this.tick = BigInteger.valueOf(tick);
        this.cacheTick = tick;
        this.quickTick = 0;
    }

    public void set(@Nullable BigInteger tick) {
        this.tick = Objects.requireNonNullElse(tick, BigInteger.ZERO);
        this.cacheTick = this.tick.longValue();
        this.quickTick = 0L;
    }

    public void set(@NotNull String tick) {
        this.tick = new BigInteger(tick);
        this.cacheTick = this.tick.longValue();
        this.quickTick = 0L;
    }

    public void set(@NotNull String tick, int radix) {
        this.tick = new BigInteger(tick, radix);
        this.cacheTick = this.tick.longValue();
        this.quickTick = 0L;
    }

    public void set(@Nullable QuickTick tick) {
        if (tick == null) {
            this.tick = BigInteger.ZERO;
            this.cacheTick = 0L;
        } else {
            tick.standardized();
            this.tick = tick.tick;
            this.cacheTick = this.tick.longValue();
        }
        this.quickTick = 0L;
    }

    public void standardized() {
        if (this.quickTick == 0L)
            return;
        this.tick = this.tick.add(BigInteger.valueOf(this.quickTick));
        this.cacheTick = this.tick.longValue();
        this.quickTick = 0L;
    }

    public long getTick() {
        return this.cacheTick + this.quickTick;
    }

    public @NotNull BigInteger getFullTick() {
        this.standardized();
        return this.tick;
    }

    public long getDeltaTick() {
        return this.quickTick;
    }

    public long aaT() {  //++tick
        return this.cacheTick + (++this.quickTick);
    }

    public long Taa() {  //tick++
        return this.cacheTick + (this.quickTick++);
    }

    public long ssT() {  //--tick
        return this.cacheTick + (--this.quickTick);
    }

    public long Tss() {  //tick--
        return this.cacheTick + (this.quickTick--);
    }

    public void aT() {
        ++this.quickTick;
    }

    public void sT() {
        --this.quickTick;
    }

    public long add(long n) {  //tick+=n
        return this.cacheTick + (this.quickTick += n);
    }

    public long sub(long n) {  //tick-=n
        return this.cacheTick + (this.quickTick -= n);
    }

    public long aN(long n) {  //tick+n
        return this.cacheTick + this.quickTick + n;
    }

    public long sN(long n) {  //tick-n
        return this.cacheTick + this.quickTick - n;
    }

    public @NotNull ImmutableQuickTick toImmutable() {
        return new ImmutableQuickTick(this);
    }

    public @NotNull UpdatableQuickTick toUpdatable() {
        return new UpdatableQuickTick(this);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.set(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        this.standardized();
        output.writeUTF(prefix);
        output.writeUTF(this.tick.toString(ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        this.standardized();
        return this.tick.toString();
    }

    public @NotNull String toString(int radix) {
        this.standardized();
        return this.tick.toString(radix);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof QuickTick that)) return false;
        this.standardized();
        that.standardized();
        return this.tick.equals(that.tick);
    }

    @Override
    public int hashCode() {
        this.standardized();
        return Objects.hash(this.tick);
    }

    public static class ImmutableQuickTick extends QuickTick implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(QuickTick.serialVersionUID);

        public ImmutableQuickTick() {
            super();
        }

        public ImmutableQuickTick(long tick) {
            super();
            super.set(tick);
        }

        public ImmutableQuickTick(@Nullable BigInteger tick) {
            super();
            super.set(tick);
        }

        public ImmutableQuickTick(@NotNull String tick) {
            super();
            super.set(tick);
        }

        public ImmutableQuickTick(@NotNull String tick, int radix) {
            super();
            super.set(tick, radix);
        }

        public ImmutableQuickTick(@Nullable QuickTick tick) {
            super();
            super.set(tick);
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(long tick) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable BigInteger tick) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@NotNull String tick) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@NotNull String tick, int radix) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable QuickTick tick) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void standardized() {
        }

        @Override
        public long getTick() {
            return this.cacheTick;
        }

        @Override
        public @NotNull BigInteger getFullTick() {
            return this.tick;
        }

        @Override
        public long aaT() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long Taa() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long ssT() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long Tss() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void aT() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sT() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long add(long n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long sub(long n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ImmutableQuickTick toImmutable() {
            return this;
        }
    }

    public static class UpdatableQuickTick extends QuickTick implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(QuickTick.serialVersionUID);

        protected boolean updated;

        public UpdatableQuickTick() {
            super();
        }

        public UpdatableQuickTick(long tick) {
            super();
            super.set(tick);
        }

        public UpdatableQuickTick(@Nullable BigInteger tick) {
            super();
            super.set(tick);
        }

        public UpdatableQuickTick(@NotNull String tick) {
            super();
            super.set(tick);
        }

        public UpdatableQuickTick(@NotNull String tick, int radix) {
            super();
            super.set(tick, radix);
        }

        public UpdatableQuickTick(@Nullable QuickTick tick) {
            super();
            super.set(tick);
        }

        @Override
        public void clear() {
            super.clear();
            this.updated = true;
        }

        @Override
        public void set(long tick) {
            super.set(tick);
            this.updated = true;
        }

        @Override
        public void set(@Nullable BigInteger tick) {
            super.set(tick);
            this.updated = true;
        }

        @Override
        public void set(@NotNull String tick) {
            super.set(tick);
            this.updated = true;
        }

        @Override
        public void set(@NotNull String tick, int radix) {
            super.set(tick, radix);
            this.updated = true;
        }

        @Override
        public void set(@Nullable QuickTick tick) {
            super.set(tick);
            this.updated = true;
        }

        @Override
        public long aaT() {
            this.updated = true;
            return super.aaT();
        }

        @Override
        public long Taa() {
            this.updated = true;
            return super.Taa();
        }

        @Override
        public long ssT() {
            this.updated = true;
            return super.ssT();
        }

        @Override
        public long Tss() {
            this.updated = true;
            return super.Tss();
        }

        @Override
        public void aT() {
            super.aT();
            this.updated = true;
        }

        @Override
        public void sT() {
            super.sT();
            this.updated = true;
        }

        @Override
        public long add(long n) {
            this.updated = true;
            return super.add(n);
        }

        @Override
        public long sub(long n) {
            this.updated = true;
            return super.sub(n);
        }

        @Override
        public @NotNull UpdatableQuickTick toUpdatable() {
            return this;
        }

        @Override
        public boolean getUpdated() {
            return this.updated;
        }

        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
        }
    }
}
