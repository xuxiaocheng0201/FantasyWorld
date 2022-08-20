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

public class DSTDouble extends DSTBase<Double> {
    @Serial
    private static final long serialVersionUID = 6259019204523330694L;
    public static final DSTId id = DSTId.getDstIdInstance("DSTDouble");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTDouble() {
        super(0.0D);
    }

    public DSTDouble(@NotNull Double data) {
        super(data);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readDouble();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeDouble(this.data);
        output.writeUTF(suffix);
    }

    @Override
    public void setData(@NotNull Double data) {
        this.data = Objects.requireNonNullElse(data, 0.0D);
    }

    @Override
    public @NotNull String toString() {
        return "DSTDouble{" + this.data + '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTDouble that)) return false;
        return Double.compare(that.data, this.data) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
