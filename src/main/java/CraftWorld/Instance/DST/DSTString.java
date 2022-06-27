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

public class DSTString extends PureDSTBase<String> {
    @Serial
    private static final long serialVersionUID = -2996774276718275691L;
    public static final String id = "DSTString";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTString() {
        super();
    }

    public DSTString(String data) {
        super();
        this.setData(data);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readUTF();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.data);
        output.writeUTF(suffix);
    }

    @Override
    public void setData(String data) {
        this.data = Objects.requireNonNullElse(data, "");
    }

    @Override
    public @NotNull String toString() {
        return "DSTString{'" + this.data + "'}";
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTString that)) return false;
        return Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
