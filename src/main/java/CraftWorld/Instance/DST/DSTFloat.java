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

public class DSTFloat extends DSTBase<Float> {
    @Serial
    private static final long serialVersionUID = -6654553278710532324L;
    public static final DSTId id = DSTId.getDstIdInstance("DSTFloat");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTFloat() {
        super(0.0F);
    }

    public DSTFloat(@NotNull Float data) {
        super(data);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readFloat();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeFloat(this.data);
        output.writeUTF(suffix);
    }

    @Override
    public void setData(@NotNull Float data) {
        this.data = Objects.requireNonNullElse(data, 0.0F);
    }

    @Override
    public @NotNull String toString() {
        return "DSTFloat{" + this.data + '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTFloat that)) return false;
        return Float.compare(that.data, this.data) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
