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

    private String name = "Air";
    private DSTComplexMeta dst = new DSTComplexMeta();

    public BlockAir() {
        super();
    }

    public BlockAir(String name) {
        super();
        this.name = name;
    }

    public BlockAir(DSTComplexMeta dst) {
        super();
        this.dst = dst;
    }

    public BlockAir(String name, DSTComplexMeta dst) {
        super();
        this.name = name;
        this.dst = dst;
    }

    @Override
    public String getBlockName() {
        return this.name;
    }

    @Override
    public void setBlockName(String name) {
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
