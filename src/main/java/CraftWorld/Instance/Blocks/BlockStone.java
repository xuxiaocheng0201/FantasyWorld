package CraftWorld.Instance.Blocks;

import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.World.Block.IBlockBase;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Objects;

public class BlockStone implements IBlockBase {
    @Serial
    private static final long serialVersionUID = -1361010945938452870L;
    public static final String id = "BlockStone";

    @Override
    public @NotNull String getBlockId() {
        return id;
    }

    private String name = "Stone";
    private DSTComplexMeta dst = new DSTComplexMeta();

    public BlockStone() {
        super();
    }

    public BlockStone(String name) {
        super();
        this.name = name;
    }

    public BlockStone(DSTComplexMeta dst) {
        super();
        this.dst = dst;
    }

    public BlockStone(String name, DSTComplexMeta dst) {
        super();
        this.name = name;
        this.dst = dst;
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
        return "BlockStone{" +
                "name='" + this.name + '\'' +
                ", dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(Object o) {
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
