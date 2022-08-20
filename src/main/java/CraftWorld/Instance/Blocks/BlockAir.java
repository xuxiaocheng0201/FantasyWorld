package CraftWorld.Instance.Blocks;

import CraftWorld.World.Block.BasicInformation.BlockId;
import CraftWorld.World.Block.BlockBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class BlockAir extends BlockBase {
    @Serial
    private static final long serialVersionUID = -6409986271941288545L;
    public static final BlockId id = BlockId.getBlockIdInstance("BlockAir");

    @Override
    public @NotNull BlockId getBlockId() {
        return id;
    }

    public BlockAir() {
        super("Air");
    }

    public BlockAir(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull String toString() {
        return "BlockAir{" +
                "name='" + this.name + '\'' +
                ", dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        BlockAir blockAir = (BlockAir) o;
        return this.name.equals(blockAir.name) && this.dst.equals(blockAir.dst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.dst);
    }
}
