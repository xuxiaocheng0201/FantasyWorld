package CraftWorld.Utils;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.Objects;

public class QuickTick implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -7856577519248919328L;
    public static final String id = "QuickTick";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    private @NotNull BigInteger tick = BigInteger.ZERO;
    private long cacheTick;
    private long quickTick;

    public QuickTick() {
        super();
    }

    public QuickTick(long tick) {
        super();
        this.tick = BigInteger.valueOf(tick);
        this.cacheTick = tick;
    }

    public QuickTick(BigInteger tick) {
        super();
        this.tick = Objects.requireNonNullElse(tick, BigInteger.ZERO);
        this.cacheTick = this.tick.longValue();
    }

    public QuickTick(String tick) {
        super();
        this.tick = new BigInteger(tick);
        this.cacheTick = this.tick.longValue();
    }

    public QuickTick(String tick, int radix) {
        super();
        this.tick = new BigInteger(tick, radix);
        this.cacheTick = this.tick.longValue();
    }

    public void set(long tick) {
        this.tick = BigInteger.valueOf(tick);
        this.cacheTick = tick;
        this.quickTick = 0;
    }

    public void set(BigInteger tick) {
        this.tick = Objects.requireNonNullElse(tick, BigInteger.ZERO);
        this.cacheTick = this.tick.longValue();
        this.quickTick = 0;
    }

    public void set(String tick) {
        this.tick = new BigInteger(tick);
        this.cacheTick = this.tick.longValue();
        this.quickTick = 0;
    }

    public void set(String tick, int radix) {
        this.tick = new BigInteger(tick, radix);
        this.cacheTick = this.tick.longValue();
        this.quickTick = 0;
    }

    public void standardized() {
        if (this.quickTick == 0)
            return;
        this.tick = this.tick.add(BigInteger.valueOf(this.quickTick));
        this.cacheTick = this.tick.longValue();
        this.quickTick = 0;
    }

    public long getTick() {
        return this.cacheTick + this.quickTick;
    }

    public BigInteger getFullTick() {
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
    public String toString() {
        this.standardized();
        return this.tick.toString();
    }

    public String toString(int radix) {
        this.standardized();
        return this.tick.toString(radix);
    }

    @Override
    public boolean equals(Object o) {
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
}
