package CraftWorld.Instance.DST;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTBase;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class DSTLong extends DSTBase<Long> {
    @Serial
    private static final long serialVersionUID = -8391379240891769836L;
    public static final DSTId id = DSTId.getDstIdInstance("DSTLong");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTLong() {
        super(0L);
    }

    public DSTLong(@NotNull Long data) {
        super(data);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readLong();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeLong(this.data);
        output.writeUTF(suffix);
    }

    @Override
    public void setData(@NotNull Long data) {
        this.data = Objects.requireNonNullElse(data, 0L);
    }

    @Override
    public @NotNull String toString() {
        return "DSTLong{" + this.data + '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTLong that)) return false;
        return Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
