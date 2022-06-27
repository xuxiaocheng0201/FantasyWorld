package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
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
    public static final String id = "DSTPair";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    private @Nullable IDSTBase left;
    private @Nullable IDSTBase right;

    public DSTPair() {
        super();
    }

    public DSTPair(@Nullable IDSTBase left, @Nullable IDSTBase right) {
        super();
        this.left = left;
        this.right = right;
    }

    public @Nullable IDSTBase getLeft() {
        return this.left;
    }

    public void setLeft(@Nullable IDSTBase left) {
        this.left = left;
    }

    public @Nullable IDSTBase getRight() {
        return this.right;
    }

    public void setRight(@Nullable IDSTBase right) {
        this.right = right;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        try {
            String name = input.readUTF();
            if ("null".equals(DSTUtils.dePrefix(name)))
                this.left = null;
            else {
                this.left = DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(name), false);
                this.left.read(input);
            }
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            throw new DSTFormatException(exception);
        }
        try {
            String name = input.readUTF();
            if ("null".equals(DSTUtils.dePrefix(name)))
                this.right = null;
            else {
                this.right = DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(name), false);
                this.right.read(input);
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
        if (this.left == null)
            output.writeUTF(DSTUtils.prefix("null"));
        else
            this.left.write(output);
        if (this.right == null)
            output.writeUTF(DSTUtils.prefix("null"));
        else
            this.right.write(output);
        output.writeUTF(suffix);
    }
}
