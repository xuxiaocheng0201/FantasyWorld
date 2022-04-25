package CraftWorld.Dimension;

import CraftWorld.Chunk.Chunk;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.*;
import java.util.*;

public class Dimension implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -4936855319467494864L;
    public static final String id = "Dimension";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, Dimension.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private IDimensionBase instance;

    private boolean unloaded;
    private File chunksSavedDirectory;
    private final Map<ChunkPos, Chunk> loadedChunks = new HashMap<>();
    private final Queue<Chunk> needSaveChunks = new ArrayDeque<>();

    public Dimension() {
        this(ConstantStorage.WORLD_PATH, null);
    }

    public Dimension(String dimensionDirectoryPath, IDimensionBase instance) {
        super();
        this.setInstance(dimensionDirectoryPath, instance);
    }

    public IDimensionBase getInstance() {
        return this.instance;
    }

    public void setInstance(String dimensionDirectoryPath, IDimensionBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, DimensionEarthSurface::new);
        this.chunksSavedDirectory = new File(dimensionDirectoryPath + "\\" + this.instance.getDimensionId());
    }

    public File getChunksSavedDirectory() {
        return this.chunksSavedDirectory;
    }

    public void setChunksSavedDirectory(String chunksSavedDirectory) throws IOException {
        HFileHelper.createNewDirectory(chunksSavedDirectory);
        this.chunksSavedDirectory = (new File(chunksSavedDirectory)).getAbsoluteFile();
    }

    public Map<ChunkPos, Chunk> getLoadedChunks() {
        return this.loadedChunks;
    }

    public void saveChunks() throws IOException {
        while (!this.needSaveChunks.isEmpty()) {
            Chunk chunk;
            synchronized (this.needSaveChunks) {
                chunk = this.needSaveChunks.poll();
            }
            if (chunk == null)
                break;
            ChunkPos pos = chunk.getPos();
            String saveFilePath = this.chunksSavedDirectory.getPath() + "\\chunk(" +
                    pos.getBigX().toString(ChunkPos.SAVE_RADIX) + "," +
                    pos.getBigY().toString(ChunkPos.SAVE_RADIX) + "," +
                    pos.getBigZ().toString(ChunkPos.SAVE_RADIX) + ").dat";
            HFileHelper.createNewFile(saveFilePath);
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFilePath)));
            chunk.write(dataOutputStream);
            dataOutputStream.close();
        }
    }

    private Chunk loadChunk(ChunkPos pos) throws IOException {
        if (this.loadedChunks.containsKey(pos))
            return this.loadedChunks.get(pos);
        String saveFilePath = this.chunksSavedDirectory.getPath() + "\\chunk(" +
                pos.getBigX().toString(ChunkPos.SAVE_RADIX) + "," +
                pos.getBigY().toString(ChunkPos.SAVE_RADIX) + "," +
                pos.getBigZ().toString(ChunkPos.SAVE_RADIX) + ").dat";
        if (HFileHelper.checkFileAvailable(saveFilePath)) {
            Chunk chunk = new Chunk();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(saveFilePath)));
            if (!Chunk.prefix.equals(dataInputStream.readUTF()))
                throw new DSTFormatException();
            chunk.read(dataInputStream);
            dataInputStream.close();
            this.loadedChunks.put(pos, chunk);
            return chunk;
        } else {
            Chunk chunk = this.generateChunk(pos);
            this.loadedChunks.put(pos, this.generateChunk(pos));
            this.needSaveChunks.add(chunk);
            return chunk;
        }
    }

    public void unloadAllChunks() {

    }

    public void prepareChunks() throws IOException {
        for (ChunkPos pos: this.instance.getPrepareChunkPos())
            this.loadChunk(pos);
    }




    public Chunk generateChunk(ChunkPos pos) {
        //TODO: Chunk generator
        return new Chunk(pos);
    }

    @Override
    public void read(DataInput input) throws IOException {
        try {
            this.instance = DimensionUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()), false);
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.instance = new DimensionEarthSurface();
        }
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.instance.getDimensionName());
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "instance=" + this.instance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Dimension dimension = (Dimension) o;
        return this.instance.equals(dimension.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.instance);
    }
}
