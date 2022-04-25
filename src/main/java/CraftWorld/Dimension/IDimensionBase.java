package CraftWorld.Dimension;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.Instance.DST.DSTMetaCompound;

import java.util.Set;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Dimension")
public interface IDimensionBase extends ElementImplement {
    String getDimensionId();
    String getDimensionName();
    void setDimensionName(String name);
    Set<ChunkPos> getPrepareChunkPos();
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
