package CraftWorld.Dimension;

import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.DST.DSTMetaCompound;

import java.util.Set;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Dimension")
public interface IDimensionBase extends IDSTBase {
    String getDimensionId();
    Set<ChunkPos> getPrepareChunkPos();

    String getDimensionName();
    void setDimensionName(String name);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
