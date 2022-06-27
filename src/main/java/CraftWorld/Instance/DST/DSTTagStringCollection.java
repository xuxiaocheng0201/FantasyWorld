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

public class DSTTagStringCollection implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -4543462114922579563L;
    public static final String id = "DSTTagStringCollection";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    private String name = id;
    private final @NotNull Collection<String> data;

    public DSTTagStringCollection(Supplier<? extends Collection<String>> supplier) {
        super();
        this.data = supplier.get();
    }

    public DSTTagStringCollection(String name, Supplier<? extends Collection<String>> supplier) {
        super();
        this.name = name;
        this.data = supplier.get();
    }

    public DSTTagStringCollection(@NotNull Collection<String> collection) {
        super();
        this.data = collection;
    }

    public DSTTagStringCollection(String name, @NotNull Collection<String> collection) {
        super();
        this.name = name;
        this.data = collection;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        int length = input.readInt();
        for (int i = 0; i < length; ++i)
            this.data.add(input.readUTF());
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeInt(this.data.size());
        for (String datum: this.data)
            output.writeUTF(datum);
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public @NotNull Collection<String> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "DSTTagStringCollection{" +
                "name='" + this.name + '\'' +
                ", data=" + this.data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTTagStringCollection that)) return false;
        return Objects.equals(this.name, that.name) && this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
