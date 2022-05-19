package CraftWorld.World.Chunk;

import CraftWorld.CraftWorld;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Events.ChunkGenerateEvent;
import CraftWorld.Instance.Blocks.BlockAir;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.World.Block.Block;
import CraftWorld.World.Block.BlockPos;
import CraftWorld.World.Block.IBlockBase;
import CraftWorld.World.Dimension.Dimension;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Chunk implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1248493755702372576L;
    public static final String id = "Chunk";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, Chunk.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    public static final int SIZE = 16;
    public static final BigInteger SIZE_BigInteger = BigInteger.valueOf(SIZE);
    public static final BigDecimal SIZE_BigDecimal = BigDecimal.valueOf(SIZE);

    private final @NotNull Dimension dimension;
    private @NotNull ChunkPos pos;
    private final @NotNull DSTComplexMeta dst;
    private final @NotNull List<List<List<Block>>> blocks = Collections.synchronizedList(new ArrayList<>(SIZE));

    public Chunk(@NotNull Dimension dimension) {
        this(dimension, new ChunkPos());
    }

    public Chunk(@NotNull Dimension dimension, @Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
        this(dimension, new ChunkPos(x, y, z));
    }

    public Chunk(@NotNull Dimension dimension, @Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z, @Nullable DSTComplexMeta dst) {
        this(dimension, new ChunkPos(x, y, z), dst);
    }

    public Chunk(@NotNull Dimension dimension, @Nullable ChunkPos pos) {
        super();
        this.dimension = dimension;
        this.pos = Objects.requireNonNullElseGet(pos, ChunkPos::new);
        this.dst = new DSTComplexMeta();
        this.clearBlocks();
    }

    public Chunk(@NotNull Dimension dimension, @Nullable ChunkPos pos, @Nullable DSTComplexMeta dst) {
        super();
        this.dimension = dimension;
        this.pos = Objects.requireNonNullElseGet(pos, ChunkPos::new);
        this.dst = Objects.requireNonNullElseGet(dst, DSTComplexMeta::new);
        this.clearBlocks();
    }

    public @NotNull Dimension getDimension() {
        return this.dimension;
    }

    public @NotNull ChunkPos getPos() {
        return this.pos;
    }

    public void setPos(@Nullable ChunkPos pos) {
        this.pos = Objects.requireNonNullElseGet(pos, ChunkPos::new);
    }

    public @NotNull DSTComplexMeta getDst() {
        return this.dst;
    }

    public @NotNull Block getBlock(@Range(from = 0, to = SIZE - 1) int x, @Range(from = 0, to = SIZE - 1) int y, @Range(from = 0, to = SIZE - 1) int z) {
        return this.blocks.get(x).get(y).get(z);
    }

    public void setBlock(@Range(from = 0, to = SIZE - 1) int x, @Range(from = 0, to = SIZE - 1) int y, @Range(from = 0, to = SIZE - 1) int z, @Nullable IBlockBase block) {
        this.blocks.get(x).get(y).set(z, new Block(this, new BlockPos(this.pos, x, y, z), block));
    }

    public void clearBlocks() {
        this.blocks.clear();
        for (int a = 0; a < SIZE; ++a) {
            List<List<Block>> block_1 = Collections.synchronizedList(new ArrayList<>(SIZE));
            for (int b = 0; b < SIZE; ++b) {
                List<Block> block_2 = Collections.synchronizedList(new ArrayList<>(SIZE));
                for (int c = 0; c < SIZE; ++c)
                    block_2.add(new Block(this, new BlockPos(this.pos, a, b, c), new BlockAir()));
                block_1.add(block_2);
            }
            this.blocks.add(block_1);
        }
    }

    public void regenerate() {
        CraftWorld.getCraftWorldEventBus().post(new ChunkGenerateEvent(this));
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        if (!ChunkPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.pos.read(input);
        for (List<List<Block>> block_1: this.blocks)
            for (List<Block> block_2: block_1)
                for (Block block_3: block_2) {
                    if (!Block.prefix.equals(input.readUTF()))
                        throw new DSTFormatException();
                    block_3.read(input);
                }
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.pos.write(output);
        for (List<List<Block>> block_1: this.blocks)
            for (List<Block> block_2: block_1)
                for (Block block_3: block_2)
                    block_3.write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "pos=" + this.pos +
//                ", blocks=" + this.blocks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Chunk chunk = (Chunk) o;
        return this.pos.equals(chunk.pos) && this.blocks.equals(chunk.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pos);
    }

    public void update() {
    }
}
