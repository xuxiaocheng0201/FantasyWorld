package CraftWorld;

import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.ElementsCheckedEvent;
import Core.EventBus.Events.PreInitializationModsEvent;
import Core.FileTreeStorage;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BoundingBox.BoundingBoxUtils;
import CraftWorld.Entity.Entity;
import CraftWorld.Entity.EntityPos;
import CraftWorld.Entity.Living.EntityLiving;
import CraftWorld.Instance.Blocks.BlockAir;
import CraftWorld.Instance.Blocks.BlockStone;
import CraftWorld.Instance.DST.*;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
import CraftWorld.Instance.Dimensions.NullDimension;
import CraftWorld.Instance.Entity.BoundingBox.BoundingBoxCuboid;
import CraftWorld.Instance.Entity.BoundingBox.BoundingBoxSphere;
import CraftWorld.Utils.Angle;
import CraftWorld.Utils.IDResource;
import CraftWorld.World.Block.Block;
import CraftWorld.World.Block.BlockPos;
import CraftWorld.World.Block.BlockUtils;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.Dimension;
import CraftWorld.World.Dimension.DimensionUtils;
import CraftWorld.World.World;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.HElementRegisteredException;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@SuppressWarnings("MethodMayBeStatic")
@EventSubscribe
@NewMod(name = "CraftWorld", version = "0.0.1", requirements = "before:*")
public class CraftWorldMod implements ModImplement {
    private static final HLog logger = new HLog("CraftWorldMod");

    @Subscribe(priority = Integer.MIN_VALUE)
    public void elementsChecked(@NotNull ElementsCheckedEvent event) throws HElementRegisteredException {
        if (!event.firstPost())
            return;
        DSTUtils.prefix("null");
        DSTUtils.getInstance().register(DSTBoolean.id, DSTBoolean.class);
        DSTUtils.getInstance().register(DSTByte.id, DSTByte.class);
        DSTUtils.getInstance().register(DSTCharacter.id, DSTCharacter.class);
        DSTUtils.getInstance().register(DSTDouble.id, DSTDouble.class);
        DSTUtils.getInstance().register(DSTFloat.id, DSTFloat.class);
        DSTUtils.getInstance().register(DSTInteger.id, DSTInteger.class);
        DSTUtils.getInstance().register(DSTLong.id, DSTLong.class);
        DSTUtils.getInstance().register(DSTShort.id, DSTShort.class);
        DSTUtils.getInstance().register(DSTString.id, DSTString.class);
        DSTUtils.getInstance().register(DSTPair.id, DSTPair.class);
        DSTUtils.getInstance().register(DSTCollection.id, DSTCollection.class);
        DSTUtils.getInstance().register(DSTMap.id, DSTMap.class);
        DSTUtils.getInstance().register(DSTComplexMeta.id, DSTComplexMeta.class);
        DSTUtils.getInstance().register(Angle.id, Angle.class);
        DSTUtils.getInstance().register(IDResource.id, IDResource.class);
        DSTUtils.getInstance().register(Block.id, Block.class);
        DSTUtils.getInstance().register(BlockPos.id, BlockPos.class);
        DSTUtils.getInstance().register(Chunk.id, Chunk.class);
        DSTUtils.getInstance().register(ChunkPos.id, ChunkPos.class);
        DSTUtils.getInstance().register(Dimension.id, Dimension.class);
        DSTUtils.getInstance().register(World.id, World.class);
        DSTUtils.getInstance().register(Entity.id, Entity.class);
        DSTUtils.getInstance().register(EntityPos.id, EntityPos.class);
        DSTUtils.getInstance().register(EntityLiving.id, EntityLiving.class);
        BoundingBoxUtils.getInstance().register(BoundingBoxCuboid.id, BoundingBoxCuboid.class);
        BoundingBoxUtils.getInstance().register(BoundingBoxSphere.id, BoundingBoxSphere.class);
        BlockUtils.getInstance().register(BlockAir.id, BlockAir.class);
        BlockUtils.getInstance().register(BlockStone.id, BlockStone.class);
        DimensionUtils.getInstance().register(NullDimension.id, NullDimension.class);
        DimensionUtils.getInstance().register(DimensionEarthSurface.id, DimensionEarthSurface.class);
    }

    @Subscribe
    public void preInitialize(@NotNull PreInitializationModsEvent event) throws IOException {
        if (!event.firstPost())
            return;
        FileTreeStorage.extractFiles(CraftWorldMod.class, "assets\\CraftWorld", "assets\\CraftWorld");
    }

    @Override
    public void mainInitialize() {
        logger.setName("CraftWorldMod", Thread.currentThread().getName());
        CraftWorld.getInstance().getWorld().addPrepareDimension(DimensionEarthSurface.id);
    }
}
