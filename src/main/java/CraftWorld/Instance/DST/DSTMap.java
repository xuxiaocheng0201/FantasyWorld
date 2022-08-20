package CraftWorld.Instance.DST;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HClassHelper;
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

public class DSTMap implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 3369986060792813939L;
    public static final DSTId id = DSTId.getDstIdInstance("DSTMap");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull Map<IDSTBase, IDSTBase> data;

    public DSTMap(@NotNull Map<IDSTBase, IDSTBase> map) {
        super();
        this.data = map;
    }

    public @NotNull Map<IDSTBase, IDSTBase> getData() {
        return this.data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        String type = input.readUTF();
        try {
            //noinspection unchecked
            Map<IDSTBase, IDSTBase> temp = (Map<IDSTBase, IDSTBase>) HClassHelper.getInstance(Class.forName(type), false);
            if (temp == null)
                throw new RuntimeException("Failed to create a new instance. [type='" + type + "']");
            this.data = temp;
        } catch (ClassNotFoundException | ClassCastException exception) {
            throw new DSTFormatException(exception);
        }
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
        output.writeUTF(this.data.getClass().getName());
        output.writeInt(this.data.size());
        for (Entry<IDSTBase, IDSTBase> datum: this.data.entrySet()) {
            datum.getKey().write(output);
            datum.getValue().write(output);
        }
        output.writeUTF(suffix);
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
