package CraftWorld.Chunk;

import CraftWorld.Block.Block;
import CraftWorld.Block.BlockPos;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Blocks.BlockAir;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    public static final BigInteger SIZE_B = BigInteger.valueOf(SIZE);

    private ChunkPos pos;
    private final List<List<List<Block>>> blocks = Collections.synchronizedList(new ArrayList<>(SIZE));

    public Chunk() {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public Chunk(int x, int y, int z) {
        this(BigInteger.valueOf(x), BigInteger.valueOf(y), BigInteger.valueOf(z));
    }

    public Chunk(BigInteger x, BigInteger y, BigInteger z) {
        super();
        this.pos = new ChunkPos(x, y, z);
        this.clearBlocks();
    }

    public Chunk(ChunkPos pos) {
        super();
        this.pos = pos;
        this.clearBlocks();
    }

    @Override
    public void read(DataInput input) throws IOException {
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
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.pos.write(output);
        for (List<List<Block>> block_1: this.blocks)
            for (List<Block> block_2: block_1)
                for (Block block_3: block_2)
                    block_3.write(output);
        output.writeUTF(suffix);
    }

    public void clearBlocks() {
        BigInteger x = this.pos.getBigX().multiply(SIZE_B);
        BigInteger y = this.pos.getBigY().multiply(SIZE_B);
        BigInteger z = this.pos.getBigZ().multiply(SIZE_B);
        this.blocks.clear();
        for (int a = 0; a < SIZE; ++a) {
            List<List<Block>> block_1 = Collections.synchronizedList(new ArrayList<>(SIZE));
            for (int b = 0; b < SIZE; ++b) {
                List<Block> block_2 = Collections.synchronizedList(new ArrayList<>(SIZE));
                for (int c = 0; c < SIZE; ++c)
                    block_2.add(new Block(new BlockPos(
                            x.add(BigInteger.valueOf(a)),
                            y.add(BigInteger.valueOf(b)),
                            z.add(BigInteger.valueOf(c))),
                            new BlockAir()));
                block_1.add(block_2);
            }
            this.blocks.add(block_1);
        }
    }

    public ChunkPos getPos() {
        return this.pos;
    }

    public void setPos(ChunkPos pos) {
        this.pos = pos;
    }

    public Block getBlock(int x, int y, int z) {
        return this.blocks.get(x).get(y).get(z);
    }

    public void setBlock(int x, int y, int z, Block block) {
        this.blocks.get(x).get(y).set(z, block);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "pos=" + this.pos +
                ", blocks=" + this.blocks +
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
