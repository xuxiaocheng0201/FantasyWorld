package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public class DSTTagBooleanCollection implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -5569154445343892835L;
    public static final String id = "DSTTagBooleanCollection";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    private String name = id;
    private final @NotNull Collection<Boolean> data;

    public DSTTagBooleanCollection(Supplier<? extends Collection<Boolean>> supplier) {
        super();
        this.data = supplier.get();
    }

    public DSTTagBooleanCollection(String name, Supplier<? extends Collection<Boolean>> supplier) {
        super();
        this.name = name;
        this.data = supplier.get();
    }

    public DSTTagBooleanCollection(@NotNull Collection<Boolean> collection) {
        super();
        this.data = collection;
    }

    public DSTTagBooleanCollection(String name, @NotNull Collection<Boolean> collection) {
        super();
        this.name = name;
        this.data = collection;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        int size = input.readInt();
        for (int i = 0; i < size; ++i)
            this.data.add(input.readBoolean());
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeInt(this.data.size());
        for (boolean datum: this.data)
            output.writeBoolean(datum);
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public @NotNull Collection<Boolean> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "DSTTagBooleanCollection{" +
                "name='" + this.name + '\'' +
                ", data=" + this.data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTTagBooleanCollection that)) return false;
        return Objects.equals(this.name, that.name) && this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
