package CraftWorld.Instance.DST;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.DataStructures.Pair;
import HeadLibs.Registerer.HElementNotRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;

public class DSTPair implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1883796214316083288L;
    public static final DSTId id = DSTId.getDstIdInstance("DSTPair");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected final @NotNull Pair<IDSTBase, IDSTBase> data = new Pair<>();

    public DSTPair() {
        super();
    }

    public DSTPair(@Nullable IDSTBase left, @Nullable IDSTBase right) {
        super();
        this.setLeft(left);
        this.setRight(right);
    }

    public @Nullable IDSTBase getLeft() {
        return this.data.getKey();
    }

    public void setLeft(@Nullable IDSTBase left) {
        this.data.setKey(left);
    }

    public @Nullable IDSTBase getRight() {
        return this.data.getValue();
    }

    public void setRight(@Nullable IDSTBase right) {
        this.data.setValue(right);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        try {
            String name = input.readUTF();
            if ("null".equals(DSTUtils.dePrefix(name).getId()))
                this.data.setKey(null);
            else {
                this.data.setKey(DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(name), false));
                this.data.getKey().read(input);
            }
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            throw new DSTFormatException(exception);
        }
        try {
            String name = input.readUTF();
            if ("null".equals(DSTUtils.dePrefix(name).getId()))
                this.data.setValue(null);
            else {
                this.data.setValue(DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(name), false));
                this.data.getValue().read(input);
            }
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            throw new DSTFormatException(exception);
        }
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        if (this.data.getKey() == null)
            output.writeUTF(DSTUtils.prefix(DSTId.getDstIdInstance("null")));
        else
            this.data.getKey().write(output);
        if (this.data.getValue() == null)
            output.writeUTF(DSTUtils.prefix(DSTId.getDstIdInstance("null")));
        else
            this.data.getValue().write(output);
        output.writeUTF(suffix);
    }
}
