package HeadLibs.DataStructures;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Triad<A, B, C> implements Serializable {
    @Serial
    private static final long serialVersionUID = 742298861997187407L;

    protected @Nullable A a;
    protected @Nullable B b;
    protected @Nullable C c;

    public Triad() {
        super();
    }

    public Triad(@Nullable A a, @Nullable B b, @Nullable C c) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public @Nullable A getA() {
        return this.a;
    }

    public void setA(@Nullable A a) {
        this.a = a;
    }

    public @Nullable B getB() {
        return this.b;
    }

    public void setB(@Nullable B b) {
        this.b = b;
    }

    public @Nullable C getC() {
        return this.c;
    }

    public void setC(@Nullable C c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Triad{" +
                "a=" + this.a +
                ", b=" + this.b +
                ", c=" + this.c +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triad<?, ?, ?> that)) return false;
        return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b) && Objects.equals(this.c, that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b, this.c);
    }
}
