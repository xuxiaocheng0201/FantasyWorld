package CraftWorld.Chunk;

import CraftWorld.Block.Block;
import CraftWorld.Block.BlockPos;
import CraftWorld.Instance.Blocks.BlockAir;
import HeadLibs.Helper.HStringHelper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

public class Chunk {
    public static final int SIZE = 16;
    public static final BigInteger SIZE_B = BigInteger.valueOf(SIZE);

    private ChunkPos pos;
    private Block[][][] blocks = new Block[SIZE][SIZE][SIZE];

    public Chunk() {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public Chunk(int x, int y, int z) {
        this(BigInteger.valueOf(x), BigInteger.valueOf(y), BigInteger.valueOf(z));
    }

    public Chunk(BigInteger x, BigInteger y, BigInteger z) {
        super();
        this.pos = new ChunkPos(x, y, z);
        x = x.multiply(SIZE_B);
        y = y.multiply(SIZE_B);
        z = z.multiply(SIZE_B);
        for (int a = 0; a < SIZE; ++a) {
            this.blocks[a] = new Block[SIZE][SIZE];
            for (int b = 0; b < SIZE; ++b) {
                this.blocks[a][b] = new Block[SIZE];
                for (int c = 0; c < SIZE; ++c) {
                    this.blocks[a][b][c] = new Block();
                    this.blocks[a][b][c].setInstance(new BlockAir());
                    this.blocks[a][b][c].getInstance().setPos(new BlockPos(
                            x.add(BigInteger.valueOf(a)),
                            y.add(BigInteger.valueOf(b)),
                            z.add(BigInteger.valueOf(c))));
                }
            }
        }
    }

    public ChunkPos getPos() {
        return this.pos;
    }

    public void setPos(ChunkPos pos) {
        this.pos = pos;
    }

    public Block[][][] getBlocks() {
        return this.blocks;
    }

    public void setBlocks(Block[][][] blocks) {
        this.blocks = blocks;
    }

    public Block getBlock(int x, int y, int z) {
        return this.blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Block block) {
        this.blocks[x][y][z] = block;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("Chunk{",
                "pos=", this.pos,
                ", blocks=", Arrays.toString(this.blocks),
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof Chunk))
            return false;
        return Objects.equals(this.pos, ((Chunk) a).pos) &&
                Arrays.deepEquals(this.blocks, ((Chunk) a).blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pos, Arrays.deepHashCode(this.blocks));
    }
}
