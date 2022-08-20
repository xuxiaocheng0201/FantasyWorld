package CraftWorld.World.Block;

import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.World.Block.BasicInformation.BlockId;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Block")
public interface IBlockBase extends IDSTBase {
    @NotNull BlockId getBlockId();
    @NotNull String getBlockName();
    void setBlockName(@NotNull String name);
    @NotNull DSTComplexMeta getBlockDST();

    @Override
    default void read(@NotNull DataInput input) throws IOException {
        this.setBlockName(input.readUTF());
        if (!DSTComplexMeta.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.getBlockDST().read(input);
    }

    @Override
    default void write(@NotNull DataOutput output) throws IOException {
        this.getBlockId().write(output);
        output.writeUTF(this.getBlockName());
        this.getBlockDST().write(output);
    }
}
