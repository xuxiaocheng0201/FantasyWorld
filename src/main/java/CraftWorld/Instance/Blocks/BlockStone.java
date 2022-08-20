package CraftWorld.Instance.Blocks;

import CraftWorld.World.Block.BasicInformation.BlockId;
import CraftWorld.World.Block.BlockBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class BlockStone extends BlockBase {
    @Serial
    private static final long serialVersionUID = -1361010945938452870L;
    public static final BlockId id = BlockId.getBlockIdInstance("BlockStone");

    @Override
    public @NotNull BlockId getBlockId() {
        return id;
    }

    public BlockStone() {
        super("Stone");
    }

    public BlockStone(String name) {
        super(name);
    }

    @Override
    public @NotNull String toString() {
        return "BlockStone{" +
                "name='" + this.name + '\'' +
                ", dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        BlockStone blockAir = (BlockStone) o;
        return this.name.equals(blockAir.name) && this.dst.equals(blockAir.dst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.dst);
    }
}
