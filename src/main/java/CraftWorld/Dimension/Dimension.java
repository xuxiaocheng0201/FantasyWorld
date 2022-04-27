package CraftWorld.Dimension;

import CraftWorld.Chunk.Chunk;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
import CraftWorld.World.World;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;

import java.io.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

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

    private boolean unloaded;
    private final World world;
    private File dimensionSavedDirectory;
    private IDimensionBase instance;
    private final HMapRegisterer<ChunkPos, Chunk> loadedChunks = new HMapRegisterer<>(false);

    public Dimension(World world) throws IOException {
        this(world, new DimensionEarthSurface());
    }

    public Dimension(World world, IDimensionBase instance) throws IOException {
        super();
        this.world = world;
        this.setInstance(instance);
    }

    public boolean isUnloaded() {
        return this.unloaded;
    }

    public World getWorld() {
        return this.world;
    }

    public File getDimensionSavedDirectory() {
        return this.dimensionSavedDirectory;
    }

    public IDimensionBase getInstance() {
        return this.instance;
    }

    public void setInstance(IDimensionBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, DimensionEarthSurface::new);
        this.dimensionSavedDirectory = new File(this.world.getDimensionDirectory(this.instance.getDimensionId()));
    }

    public String getInformationFile() {
        return this.dimensionSavedDirectory.getPath() + "\\information.dat";
    }

    public String getChunkSaveFile(ChunkPos pos) {
        return this.dimensionSavedDirectory.getPath() + "\\chunks\\" +
                pos.getBigX().toString(ChunkPos.SAVE_RADIX) + "\\" +
                pos.getBigY().toString(ChunkPos.SAVE_RADIX) + "\\" +
                pos.getBigZ().toString(ChunkPos.SAVE_RADIX) + ".dat";
    }

    public Chunk loadChunk(ChunkPos pos) throws IOException {
        try {
            return this.loadedChunks.getElement(pos);
        } catch (HElementNotRegisteredException ignore) {
        }
        if (this.unloaded)
            return null;
        String chunkSaveFilePath = this.getChunkSaveFile(pos);
        Chunk chunk = new Chunk();
        if (HFileHelper.checkFileAvailable(chunkSaveFilePath)) {
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(chunkSaveFilePath)));
            if (!Chunk.prefix.equals(dataInputStream.readUTF()))
                throw new DSTFormatException();
            chunk.read(dataInputStream);
            dataInputStream.close();
        }
        else {
            HFileHelper.createNewFile(chunkSaveFilePath);
            chunk.regenerate();
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(chunkSaveFilePath)));
            chunk.write(dataOutputStream);
            dataOutputStream.close();
        }
        try {
            this.loadedChunks.register(pos, chunk);
        } catch (HElementRegisteredException ignore) {
        }
        return chunk;
    }

    public void unloadChunk(ChunkPos pos) throws IOException {
        Chunk chunk;
        try {
            chunk = this.loadedChunks.getElement(pos);
        } catch (HElementNotRegisteredException exception) {
            return;
        }
        this.loadedChunks.deregisterKey(pos);
        if (chunk == null)
            return;
        this.saveChunk(chunk);
    }

    public void saveChunk(Chunk chunk) throws IOException {
        String saveFilePath = this.getChunkSaveFile(chunk.getPos());
        HFileHelper.createNewFile(saveFilePath);
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFilePath)));
        chunk.write(dataOutputStream);
        dataOutputStream.close();
    }

    public void loadPrepareChunks() throws IOException {
        if (this.unloaded)
            return;
        for (ChunkPos pos: this.instance.getPrepareChunkPos())
            this.loadChunk(pos);
    }

    public void unloadAllChunks() throws IOException {
        this.unloaded = true;
        Set<ChunkPos> chunkPos = this.loadedChunks.getMap().keySet();
        for (ChunkPos pos: chunkPos)
            this.unloadChunk(pos);
    }

    public void saveAllChunks() throws IOException {
        Collection<Chunk> chunks = this.loadedChunks.getMap().values();
        for (Chunk chunk: chunks)
            this.saveChunk(chunk);
    }

    public void readAll() throws IOException {
        String informationFile = this.getInformationFile();
        if (!HFileHelper.checkFileAvailable(informationFile))
            throw new IOException("Unavailable information file: " + informationFile);
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(informationFile)));
        if (!prefix.equals(dataInputStream.readUTF()))
            throw new DSTFormatException();
        this.read(dataInputStream);
        dataInputStream.close();
        this.loadPrepareChunks();
    }

    public void writeAll() throws IOException {
        String informationFile = this.getInformationFile();
        HFileHelper.createNewFile(informationFile);
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(informationFile)));
        this.write(dataOutputStream);
        dataOutputStream.close();
        this.saveAllChunks();
    }

    @Override
    public void read(DataInput input) throws IOException {
        try {
            this.setInstance(DimensionUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()), false));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.instance = new DimensionEarthSurface();
        }
        this.instance.read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.instance.write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Dimension:" + this.instance;
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
