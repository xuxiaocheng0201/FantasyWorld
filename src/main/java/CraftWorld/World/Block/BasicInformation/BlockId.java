package CraftWorld.World.Block.BasicInformation;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;

@SuppressWarnings("EqualsAndHashcode")
public class BlockId extends DSTId {
    @Serial
    private static final long serialVersionUID = 1434498445645589134L;

    public static final DSTId id = DSTId.getDstIdInstance("BlockId");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public BlockId() {
        super();
    }

    public BlockId(@Nullable String elementId) {
        super(elementId);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.elementId = input.readUTF();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.elementId);
        output.writeUTF(suffix);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockId that)) return false;
        return this.elementId.equals(that.elementId);
    }

    public static @NotNull BlockId getBlockIdInstance(@Nullable String id) {
        return new BlockId(id);
    }
}
