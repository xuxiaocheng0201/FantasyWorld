package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public class DSTTagLongCollection implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 3232039423244747601L;
    public static final String id = "DSTTagLongCollection";
    public static String prefix = DSTUtils.prefix(id);
    public static String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagLongCollection.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final @NotNull Collection<Long> data;

    public DSTTagLongCollection(Supplier<? extends Collection<Long>> supplier) {
        super();
        this.data = supplier.get();
    }

    public DSTTagLongCollection(String name, Supplier<? extends Collection<Long>> supplier) {
        super();
        this.name = name;
        this.data = supplier.get();
    }

    public DSTTagLongCollection(@NotNull Collection<Long> collection) {
        super();
        this.data = collection;
    }

    public DSTTagLongCollection(String name, @NotNull Collection<Long> collection) {
        super();
        this.name = name;
        this.data = collection;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        int size = input.readInt();
        for (int i = 0; i < size; ++i)
            this.data.add(input.readLong());
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeInt(this.data.size());
        for (long datum: this.data)
            output.writeLong(datum);
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public @NotNull Collection<Long> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "DSTTagLongCollection{" +
                "name='" + this.name + '\'' +
                ", data=" + this.data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTTagLongCollection that)) return false;
        return Objects.equals(this.name, that.name) && this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
