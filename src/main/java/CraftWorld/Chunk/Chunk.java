package CraftWorld.Chunk;

import CraftWorld.Block.Block;
import CraftWorld.Block.BlockPos;
import HeadLibs.Helper.HStringHelper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

public class Chunk {
    public static final int SIZE = 16;

    private ChunkPos pos = new ChunkPos();
    private Block[][][] blocks = new Block[SIZE][SIZE][SIZE];

    public Chunk(int x, int y, int z) {
        for (int a = 0; a < SIZE; ++a) {
            blocks[a] = new Block[SIZE][SIZE];
            for (int b = 0; b < SIZE; ++b) {
                blocks[a][b] = new Block[SIZE];
                for (int c = 0; c < SIZE; ++c) {
                    blocks[a][b][c] = new Block();
                    blocks[a][b][c].setPos(new BlockPos(
                            pos.getBigX().multiply(BigInteger.valueOf(SIZE)).add(BigInteger.valueOf(a)),
                            pos.getBigY().multiply(BigInteger.valueOf(SIZE)).add(BigInteger.valueOf(b)),
                            pos.getBigZ().multiply(BigInteger.valueOf(SIZE)).add(BigInteger.valueOf(c))));
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
