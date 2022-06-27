package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.PureDSTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class DSTShort extends PureDSTBase<Short> {
    @Serial
    private static final long serialVersionUID = 2438260511173385249L;
    public static final String id = "DSTShort";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTShort() {
        super();
    }

    public DSTShort(Short data) {
        super();
        this.setData(data);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readShort();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeShort(this.data);
        output.writeUTF(suffix);
    }

    @Override
    public void setData(Short data) {
        this.data = Objects.requireNonNullElse(data, (short) 0);
    }

    @Override
    public @NotNull String toString() {
        return "DSTShort{" + this.data + '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTShort that)) return false;
        return Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
