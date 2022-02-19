package CraftWorld.Chunk;

import CraftWorld.Block.Block;
import CraftWorld.Block.BlockPos;
import CraftWorld.Block.BlockUtils;
import HeadLibs.Helper.HStringHelper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

public class Chunk {
    public static final int SIZE = 16;

    private ChunkPos pos;
    private Block[][][] blocks = new Block[SIZE][SIZE][SIZE];

    public Chunk(int x, int y, int z) {
        this(BigInteger.valueOf(x), BigInteger.valueOf(y), BigInteger.valueOf(z));
    }

    public Chunk(BigInteger x, BigInteger y, BigInteger z) {
        this.pos = new ChunkPos(x, y, z);
        x = x.multiply(BigInteger.valueOf(SIZE));
        y = y.multiply(BigInteger.valueOf(SIZE));
        z = z.multiply(BigInteger.valueOf(SIZE));
        for (int a = 0; a < SIZE; ++a) {
            blocks[a] = new Block[SIZE][SIZE];
            for (int b = 0; b < SIZE; ++b) {
                blocks[a][b] = new Block[SIZE];
                for (int c = 0; c < SIZE; ++c) {
                    blocks[a][b][c] = new Block();
                    blocks[a][b][c].setInstance(BlockUtils.get("BlockAir"));
                    blocks[a][b][c].getInstance().setPos(new BlockPos(
                            x.add(BigInteger.valueOf(a)),
                            y.add(BigInteger.valueOf(b)),
                            z.add(BigInteger.valueOf(c))));
                }
            }
        }
    }

    public ChunkPos getPos() {
        return pos;
    }

    public void setPos(ChunkPos pos) {
        this.pos = pos;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][][] blocks) {
        this.blocks = blocks;
    }

    public Block getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Block block) {
        blocks[x][y][z] = block;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("Chunk{",
                "pos=", pos,
                ", blocks=", Arrays.toString(blocks),
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof Chunk))
            return false;
        return Objects.equals(pos, ((Chunk) a).pos) &&
                Arrays.deepEquals(blocks, ((Chunk) a).blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, Arrays.deepHashCode(blocks));
    }
}
