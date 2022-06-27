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
import java.util.Collection;
import java.util.Objects;

public class DSTCollection extends PureDSTBase<Collection<IDSTBase>> {
    @Serial
    private static final long serialVersionUID = -6813356256765513708L;
    public static final String id = "DSTCollection";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTCollection(@NotNull Collection<IDSTBase> collection) {
        super();
        this.data = collection;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        int size = input.readInt();
        for (int i = 0; i < size; ++i) {
            IDSTBase dst;
            try {
                dst = DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()), false);
            } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                throw new DSTFormatException(exception);
            }
            dst.read(input);
            this.data.add(dst);
        }
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeInt(this.data.size());
        for (IDSTBase datum: this.data)
            datum.write(output);
        output.writeUTF(suffix);
    }

    public @NotNull Collection<IDSTBase> getData() {
        return this.data;
    }

    @Override
    public @NotNull String toString() {
        return "DSTCollection:" + this.data;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTCollection that)) return false;
        return this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
