package CraftWorld.Instance.Blocks;

import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.World.Block.IBlockBase;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Objects;

public class BlockAir implements IBlockBase {
    @Serial
    private static final long serialVersionUID = -6409986271941288545L;
    public static final String id = "BlockAir";

    @Override
    public @NotNull String getBlockId() {
        return id;
    }

    protected String name = "Air";
    protected DSTComplexMeta dst = new DSTComplexMeta();

    public BlockAir() {
        super();
    }

    public BlockAir(@NotNull String name) {
        super();
        this.name = name;
    }

    @Override
    public @NotNull String getBlockName() {
        return this.name;
    }

    @Override
    public void setBlockName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull DSTComplexMeta getBlockDST() {
        return this.dst;
    }

    @Override
    public String toString() {
        return "BlockAir{" +
                "name='" + this.name + '\'' +
                ", dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(Object o) {
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
