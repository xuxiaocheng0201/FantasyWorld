package CraftWorld.Utils;

import CraftWorld.Entity.EntityPos;
import CraftWorld.World.Block.BlockPos;
import CraftWorld.World.Chunk.ChunkPos;
import HeadLibs.Helper.HMathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PositionTransformationUtils {
    public static @NotNull ChunkPos toChunkPos(@Nullable BlockPos blockPos) {
        if (blockPos == null)
            return new ChunkPos();
        return blockPos.getChunkPos();
    }

    public static @NotNull ChunkPos toChunkPos(@Nullable EntityPos entityPos) {
        if (entityPos == null)
            return new ChunkPos();
        return entityPos.getChunkPos();
    }

    public static @NotNull BlockPos toBlockPos(@Nullable ChunkPos chunkPos) {
        return new BlockPos(chunkPos);
    }
    
    public static @NotNull BlockPos toBlockPos(@Nullable EntityPos entityPos) {
        if (entityPos == null)
            return new BlockPos();
        return new BlockPos(entityPos.getChunkPos(),
                HMathHelper.fastFloor(entityPos.getX()),
                HMathHelper.fastFloor(entityPos.getY()),
                HMathHelper.fastFloor(entityPos.getZ()));
    }
    
    public static @NotNull EntityPos toEntityPos(@Nullable ChunkPos chunkPos) {
        return new EntityPos(chunkPos);
    }

    public static @NotNull EntityPos toEntityPos(@Nullable BlockPos blockPos) {
        if (blockPos == null)
            return new EntityPos();
        return new EntityPos(blockPos.getChunkPos(), blockPos.getOffset().getX(), blockPos.getOffset().getY(), blockPos.getOffset().getZ());
    }
}
