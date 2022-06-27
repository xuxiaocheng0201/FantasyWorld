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

public class DSTBoolean extends PureDSTBase<Boolean> {
    @Serial
    private static final long serialVersionUID = -2610545773823934027L;
    public static final String id = "DSTBoolean";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTBoolean() {
        super();
    }

    public DSTBoolean(Boolean data) {
        super();
        this.setData(data);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readBoolean();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeBoolean(this.data);
        output.writeUTF(suffix);
    }

    @Override
    public void setData(Boolean data) {
        this.data = Objects.requireNonNullElse(data, false);
    }

    @Override
    public @NotNull String toString() {
        return "DSTBoolean{" + this.data + '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTBoolean that)) return false;
        return Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
