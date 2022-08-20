package CraftWorld;

import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.ElementsCheckedEvent;
import Core.EventBus.Events.PreInitializationModsEvent;
import Core.EventBus.Events.ServerStopEvent;
import Core.FileTreeStorage;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BasicInformation.EntityId;
import CraftWorld.Entity.BoundingBox.BasicInformation.BoundingBoxId;
import CraftWorld.Entity.BoundingBox.BoundingBoxUtils;
import CraftWorld.Entity.Entity;
import CraftWorld.Entity.EntityPos;
import CraftWorld.Entity.EntityUtils;
import CraftWorld.Entity.Living.EntityLiving;
import CraftWorld.Instance.Blocks.BlockAir;
import CraftWorld.Instance.Blocks.BlockStone;
import CraftWorld.Instance.DST.*;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
import CraftWorld.Instance.Dimensions.NullDimension;
import CraftWorld.Instance.Entity.BoundingBox.BoundingBoxCuboid;
import CraftWorld.Instance.Entity.BoundingBox.BoundingBoxSphere;
import CraftWorld.Instance.Entity.EntityHuman;
import CraftWorld.Utils.Angle;
import CraftWorld.Utils.IDResource;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Block.BasicInformation.BlockId;
import CraftWorld.World.Block.Block;
import CraftWorld.World.Block.BlockPos;
import CraftWorld.World.Block.BlockUtils;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.BasicInformation.DimensionId;
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

    @Subscribe
    public void elementsChecked(@NotNull ElementsCheckedEvent event) throws HElementRegisteredException {
        if (!event.firstPost())
            return;
        DSTUtils.prefix(DSTId.getDstIdInstance("null"));
        DSTUtils.getInstance().register(DSTId.id, DSTId.class);
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
        DSTUtils.getInstance().register(QuickTick.id, QuickTick.class);
        DSTUtils.getInstance().register(Block.id, Block.class);
        DSTUtils.getInstance().register(BlockPos.id, BlockPos.class);
        DSTUtils.getInstance().register(Chunk.id, Chunk.class);
        DSTUtils.getInstance().register(ChunkPos.id, ChunkPos.class);
        DSTUtils.getInstance().register(Dimension.id, Dimension.class);
        DSTUtils.getInstance().register(World.id, World.class);
        DSTUtils.getInstance().register(Entity.id, Entity.class);
        DSTUtils.getInstance().register(EntityPos.id, EntityPos.class);
        DSTUtils.getInstance().register(EntityLiving.id, EntityLiving.class);
        DSTUtils.getInstance().register(BlockId.id, BlockId.class);
        BlockUtils.getInstance().register(BlockAir.id, BlockAir.class);
        BlockUtils.getInstance().register(BlockStone.id, BlockStone.class);
        DSTUtils.getInstance().register(BoundingBoxId.id, BoundingBoxId.class);
        BoundingBoxUtils.getInstance().register(BoundingBoxCuboid.id, BoundingBoxCuboid.class);
        BoundingBoxUtils.getInstance().register(BoundingBoxSphere.id, BoundingBoxSphere.class);
        DSTUtils.getInstance().register(DimensionId.id, DimensionId.class);
        DimensionUtils.getInstance().register(NullDimension.id, NullDimension.class);
        DimensionUtils.getInstance().register(DimensionEarthSurface.id, DimensionEarthSurface.class);
        DSTUtils.getInstance().register(EntityId.id, EntityId.class);
        EntityUtils.getInstance().register(EntityHuman.id, EntityHuman.class);
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
        CraftWorld.getInstance().getWorld().addPrepareDimensionCount(DimensionEarthSurface.id);
    }

    @SuppressWarnings("MagicNumber")
    @Subscribe
    public void test(ServerStopEvent event) {
//        BoundingBoxCuboid bbc = new BoundingBoxCuboid();
//        bbc.setWidth(1);
//        bbc.setLength(1);
//        bbc.setHeight(1);
//        bbc.getRotationZ().setAngleDegree(90);
//        logger.log(bbc.toStringUpdated());
//        bbc.getRotationX().setAngleDegree(45);
//        logger.log(bbc.toStringUpdated());
    }
}
