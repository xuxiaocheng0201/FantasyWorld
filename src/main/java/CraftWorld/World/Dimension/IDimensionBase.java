package CraftWorld.World.Dimension;

import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.BasicInformation.DimensionId;
import HeadLibs.Registerer.HLinkedSetRegisterer;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Dimension")
public interface IDimensionBase extends IDSTBase {
    @NotNull DimensionId getDimensionId();
    @NotNull HLinkedSetRegisterer<ChunkPos> getPrepareChunkPos();
    @NotNull String getDimensionName();
    void setDimensionName(@NotNull String name);
    @NotNull DSTComplexMeta getDimensionDST();

    @Override
    default void read(@NotNull DataInput input) throws IOException {
        this.setDimensionName(input.readUTF());
        if (!DSTComplexMeta.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.getDimensionDST().read(input);
    }

    @Override
    default void write(@NotNull DataOutput output) throws IOException {
        this.getDimensionId().write(output);
        output.writeUTF(this.getDimensionName());
        this.getDimensionDST().write(output);
    }
}
