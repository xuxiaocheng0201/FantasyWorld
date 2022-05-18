package CraftWorld.World.Dimension;

import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.World.Chunk.ChunkPos;

import java.util.Set;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Dimension")
public interface IDimensionBase extends IDSTBase {
    String getDimensionId();
    Set<ChunkPos> getPrepareChunkPos();
    String getDimensionName();
    void setDimensionName(String name);
    DSTComplexMeta getDimensionDST();
}
