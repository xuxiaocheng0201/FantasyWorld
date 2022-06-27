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

public class DSTByte extends PureDSTBase<Byte> {
    @Serial
    private static final long serialVersionUID = -4917521423532940232L;
    public static final String id = "DSTByte";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTByte() {
        super();
    }

    public DSTByte(Byte data) {
        super();
        this.setData(data);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readByte();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeByte(this.data);
        output.writeUTF(suffix);
    }

    @Override
    public void setData(Byte data) {
        this.data = Objects.requireNonNullElse(data, (byte) 0);
    }

    @Override
    public @NotNull String toString() {
        return "DSTByte{" + this.data + '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTByte that)) return false;
        return Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
