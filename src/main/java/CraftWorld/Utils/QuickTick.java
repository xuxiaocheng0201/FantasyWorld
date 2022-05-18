package CraftWorld.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class QuickTick implements Serializable {
    @Serial
    private static final long serialVersionUID = -7856577519248919328L;

    private @NotNull BigInteger tick = BigInteger.ZERO;
    private transient long cacheTick;
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
        if (!(o instanceof QuickTick quickTick)) return false;
        this.standardized();
        quickTick.standardized();
        return this.tick.equals(quickTick.tick);
    }

    @Override
    public int hashCode() {
        this.standardized();
        return Objects.hash(this.tick);
    }
}
