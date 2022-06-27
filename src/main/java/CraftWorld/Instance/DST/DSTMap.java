package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.DST.PureDSTBase;
import HeadLibs.Registerer.HElementNotRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class DSTMap extends PureDSTBase<Map<IDSTBase, IDSTBase>> {
    @Serial
    private static final long serialVersionUID = 3369986060792813939L;
    public static final String id = "DSTMap";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTMap(@NotNull Map<IDSTBase, IDSTBase> map) {
        super();
        this.data = map;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        int size = input.readInt();
        for (int i = 0; i < size; ++i) {
            IDSTBase key;
            try {
                key = DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()), false);
            } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                throw new DSTFormatException(exception);
            }
            key.read(input);
            IDSTBase value;
            try {
                value = DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()), false);
            } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                throw new DSTFormatException(exception);
            }
            value.read(input);
            this.data.put(key, value);
        }
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeInt(this.data.size());
        for (Entry<IDSTBase, IDSTBase> datum: this.data.entrySet()) {
            datum.getKey().write(output);
            datum.getValue().write(output);
        }
        output.writeUTF(suffix);
    }

    public @NotNull Map<IDSTBase, IDSTBase> getData() {
        return this.data;
    }

    @Override
    public @NotNull String toString() {
        return "DSTMap:" + this.data;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTMap that)) return false;
        return this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
