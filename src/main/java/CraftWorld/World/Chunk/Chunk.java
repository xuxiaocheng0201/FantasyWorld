package CraftWorld.World.Chunk;

import CraftWorld.CraftWorld;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Events.ChunkGenerateEvent;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Block.Block;
import CraftWorld.World.Block.BlockPosOffset;
import CraftWorld.World.Block.IBlockBase;
import CraftWorld.World.Dimension.Dimension;
import HeadLibs.Annotations.IntRange;
import HeadLibs.Helper.HFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Chunk implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1248493755702372576L;
    public static final DSTId id = DSTId.getDstIdInstance("Chunk");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public static final int SIZE = 16;
    public static final BigInteger SIZE_BigInteger = BigInteger.valueOf(SIZE);
    public static final BigDecimal SIZE_BigDecimal = BigDecimal.valueOf(SIZE);

    protected final @NotNull Dimension dimension;
    protected final @NotNull File chunkSavedDirectory;
    protected boolean unloaded = true;
    protected final @NotNull ChunkPos pos = new ChunkPos();
    protected final @NotNull DSTComplexMeta dst = new DSTComplexMeta();
    protected final @NotNull QuickTick tickHasUpdated = new QuickTick();
    protected @Nullable String randomSeed;

    protected final @NotNull List<List<List<Block>>> blocks = Collections.synchronizedList(new ArrayList<>(SIZE));

    public Chunk(@NotNull Dimension dimension, @Nullable ChunkPos pos) {
        super();
        this.dimension = dimension;
        this.pos.set(pos);
        this.chunkSavedDirectory = new File(this.dimension.getChunkDirectory(this.pos));
        this.clearBlocks();
    }

    public void update() {
        this.tickHasUpdated.set(this.dimension.getWorld().getTick().getFullTick());
        //TODO: update chunk.
    }

    public @NotNull File getChunkSavedDirectory() {
        return this.chunkSavedDirectory;
    }

    public boolean isUnloaded() {
        return this.unloaded;
    }

    public @NotNull Dimension getDimension() {
        return this.dimension;
    }

    public @NotNull ChunkPos getPos() {
        return this.pos;
    }

    public @NotNull DSTComplexMeta getDst() {
        return this.dst;
    }

    public @NotNull QuickTick getTickHasUpdated() {
        return this.tickHasUpdated;
    }

    public @Nullable String getRandomSeed() {
        return this.randomSeed;
    }

    public @NotNull Block getBlock(@IntRange(minimum = 0, maximum = SIZE, maximum_equally = false) int x,
                                   @IntRange(minimum = 0, maximum = SIZE, maximum_equally = false) int y,
                                   @IntRange(minimum = 0, maximum = SIZE, maximum_equally = false) int z) {
        return this.blocks.get(x).get(y).get(z);
    }

    public void setBlock(@IntRange(minimum = 0, maximum = SIZE, maximum_equally = false) int x,
                         @IntRange(minimum = 0, maximum = SIZE, maximum_equally = false) int y,
                         @IntRange(minimum = 0, maximum = SIZE, maximum_equally = false) int z, @Nullable IBlockBase block) {
        this.blocks.get(x).get(y).set(z, new Block(this, new BlockPosOffset(x, y, z), block));
    }

    public void clearBlocks() {
        this.blocks.clear();
        for (int a = 0; a < SIZE; ++a) {
            List<List<Block>> block_1 = Collections.synchronizedList(new ArrayList<>(SIZE));
            for (int b = 0; b < SIZE; ++b) {
                List<Block> block_2 = Collections.synchronizedList(new ArrayList<>(SIZE));
                for (int c = 0; c < SIZE; ++c)
                    block_2.add(new Block(this, new BlockPosOffset(a, b, c)));
                block_1.add(block_2);
            }
            this.blocks.add(block_1);
        }
    }

    public void regenerate() {
        CraftWorld.getCraftWorldEventBus().post(new ChunkGenerateEvent(this));
    }

    public void readInformation() throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(this.chunkSavedDirectory.getPath()))))) {
            if (!prefix.equals(dataInputStream.readUTF()))
                throw new DSTFormatException();
            this.read(dataInputStream);
        } catch (DSTFormatException | FileNotFoundException ignore) {
            this.writeInformation();
        }
    }

    public void writeInformation() throws IOException {
        HFileHelper.createNewFile(this.chunkSavedDirectory.getPath());
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(this.chunkSavedDirectory.getPath()))));
        this.write(dataOutputStream);
        dataOutputStream.close();
    }

    public void load() throws IOException {
        this.unloaded = false;
        this.readInformation();
    }

    public void unload() throws IOException {
        this.writeInformation();
        this.unloaded = true;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.unloaded = input.readBoolean();
        if (!ChunkPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.pos.read(input);
        if (!DSTComplexMeta.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.dst.read(input);
        if (!QuickTick.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.tickHasUpdated.read(input);
        if (input.readBoolean())
            this.randomSeed = input.readUTF();
        else
            this.randomSeed = null;
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
        output.writeBoolean(this.unloaded);
        this.pos.write(output);
        this.dst.write(output);
        this.tickHasUpdated.write(output);
        if (this.randomSeed != null) {
            output.writeBoolean(true);
            output.writeUTF(this.randomSeed);
        } else
            output.writeBoolean(false);
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
}
