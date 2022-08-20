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
import java.util.Collection;
import java.util.Objects;

public class DSTCollection implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -6813356256765513708L;
    public static final DSTId id = DSTId.getDstIdInstance("DSTCollection");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull Collection<IDSTBase> data;

    public DSTCollection(@NotNull Collection<IDSTBase> collection) {
        super();
        this.data = collection;
    }

    public @NotNull Collection<IDSTBase> getData() {
        return this.data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        String type = input.readUTF();
        try {
            //noinspection unchecked
            Collection<IDSTBase> temp = (Collection<IDSTBase>) HClassHelper.getInstance(Class.forName(type), false);
            if (temp == null)
                throw new RuntimeException("Failed to create a new instance. [type='" + type + "']");
            this.data = temp;
        } catch (ClassNotFoundException | ClassCastException exception) {
            throw new DSTFormatException(exception);
        }
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
        output.writeUTF(this.data.getClass().getName());
        output.writeInt(this.data.size());
        for (IDSTBase datum : this.data)
            datum.write(output);
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        return "DSTCollection:" + this.data;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTCollection that)) return false;
        return Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
