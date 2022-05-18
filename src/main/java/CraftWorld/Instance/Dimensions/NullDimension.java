package CraftWorld.Instance.Dimensions;

import Core.EventBus.EventSubscribe;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Events.ChunkGenerateEvent;
import CraftWorld.Instance.DST.DSTMetaCompound;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.DimensionUtils;
import CraftWorld.World.Dimension.IDimensionBase;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EventSubscribe
public class NullDimension implements IDimensionBase {
    @Serial
    private static final long serialVersionUID = -5202694397165174510L;

    public static String id = "NullDimension";
    public static String prefix = DSTUtils.prefix(id);
    public static String suffix = DSTUtils.suffix(id);
    static {
        try {
            DimensionUtils.getInstance().register(id, NullDimension.class);
            DSTUtils.getInstance().register(id, NullDimension.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    @Override
    public String getDimensionId() {
        return id;
    }

    @Override
    public String getDimensionName() {
        return "Null";
    }

    @Override
    public void setDimensionName(String name) {
    }

    @Override
    public Set<ChunkPos> getPrepareChunkPos() {
        return new HashSet<>();
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
    }

    private final DSTMetaCompound dst = new DSTMetaCompound();

    @Override
    public DSTMetaCompound getDimensionDST() {
        return this.dst;
    }

    @Override
    public String toString() {
        return "NullDimension{" +
                "dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NullDimension that)) return false;
        return this.dst.equals(that.dst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dst);
    }

    @SuppressWarnings("MethodMayBeStatic")
    @Subscribe
    public void generate(ChunkGenerateEvent event) {
        if (event.chunk().getDimension().getInstance().getDimensionId().equals(id))
            event.chunk().clearBlocks();
    }
}