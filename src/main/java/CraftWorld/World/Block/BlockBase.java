package CraftWorld.World.Block;

import CraftWorld.Instance.DST.DSTComplexMeta;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public abstract class BlockBase implements IBlockBase {
    @Serial
    private static final long serialVersionUID = -5018818557258671495L;

    protected @NotNull String name = "";
    protected final @NotNull DSTComplexMeta dst = new DSTComplexMeta();

    protected BlockBase() {
        super();
    }

    protected BlockBase(@NotNull String name) {
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
}
