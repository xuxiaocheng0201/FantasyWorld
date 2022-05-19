package CraftWorld.World.Dimension;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Dimensions.NullDimension;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.World;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HRandomHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    private @NotNull File dimensionSavedDirectory;
    private boolean unloaded;

    private final @NotNull World world;
    private @NotNull UUID uuid;
    private @NotNull IDimensionBase instance;
    private final @NotNull QuickTick tickHasUpdated;
    private @NotNull String randomSeed;
    private final @NotNull Random random;

    private final @NotNull HMapRegisterer<ChunkPos, Chunk> loadedChunks = new HMapRegisterer<>(false);

    public Dimension(World world) {
        this(world, new NullDimension());
    }

    public Dimension(@NotNull World world, IDimensionBase instance) {
        super();
        this.world = world;
        this.instance = Objects.requireNonNullElseGet(instance, NullDimension::new);
        this.randomSeed = HRandomHelper.nextString(this.world.getRandom(), 1, HRandomHelper.nextInt(5, 10));
        this.random = new SecureRandom(this.randomSeed.getBytes());
        this.uuid = HRandomHelper.getRandomUUID(this.random);
        this.dimensionSavedDirectory = new File(this.world.getDimensionDirectory(this.uuid));
        this.tickHasUpdated = new QuickTick();
    }

    public void update() {
        this.tickHasUpdated.set(this.world.getTick().getFullTick());
        for (Chunk chunk: this.loadedChunks.getMap().values())
            chunk.update();
    }

    public @NotNull File getDimensionSavedDirectory() {
        return this.dimensionSavedDirectory;
    }

    public void setDimensionSavedDirectory(String dimensionSavedDirectory) throws IOException {
        HFileHelper.createNewDirectory(dimensionSavedDirectory);
        this.dimensionSavedDirectory = (new File(dimensionSavedDirectory)).getAbsoluteFile();
    }

    public String getInformationFile() {
        return this.dimensionSavedDirectory.getPath() + "\\information.dat";
    }

    public String getChunkSaveFile(ChunkPos pos) {
        return this.dimensionSavedDirectory.getPath() + "\\chunks\\" +
                pos.getBigX().toString(ConstantStorage.SAVE_NUMBER_RADIX) + "\\" +
                pos.getBigY().toString(ConstantStorage.SAVE_NUMBER_RADIX) + "\\" +
                pos.getBigZ().toString(ConstantStorage.SAVE_NUMBER_RADIX) + ".dat";
    }

    public boolean isUnloaded() {
        return this.unloaded;
    }

    public @NotNull World getWorld() {
        return this.world;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public @NotNull IDimensionBase getInstance() {
        return this.instance;
    }

    public void setInstance(IDimensionBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, NullDimension::new);
        this.dimensionSavedDirectory = new File(this.world.getDimensionDirectory(this));
    }

    public @NotNull QuickTick getTickHasUpdated() {
        return this.tickHasUpdated;
    }

    public @NotNull String getRandomSeed() {
        return this.randomSeed;
    }

    public @NotNull Random getRandom() {
        return this.random;
    }

    public Chunk loadChunk(ChunkPos pos) throws IOException {
        try {
            return this.loadedChunks.getElement(pos);
        } catch (HElementNotRegisteredException ignore) {
        }
        if (this.unloaded)
            return null;
        String chunkSaveFilePath = this.getChunkSaveFile(pos);
        Chunk chunk = new Chunk(this);
        if (HFileHelper.checkFileAvailable(chunkSaveFilePath)) {
            DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(chunkSaveFilePath))));
            if (!Chunk.prefix.equals(dataInputStream.readUTF()))
                throw new DSTFormatException();
            chunk.read(dataInputStream);
            dataInputStream.close();
        }
        else {
            HFileHelper.createNewFile(chunkSaveFilePath);
            chunk.setPos(pos);
            chunk.regenerate();
            DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(chunkSaveFilePath))));
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
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(saveFilePath))));
        chunk.write(dataOutputStream);
        dataOutputStream.close();
    }

    public void loadPrepareChunks() throws IOException {
        if (this.unloaded)
            return;
        for (ChunkPos pos: this.instance.getPrepareChunkPos().getSet())
            this.loadChunk(pos);
    }

    public void load() throws IOException {
        this.unloaded = false;
        this.readAll();
        this.loadPrepareChunks();
    }

    public void unload() throws IOException {
        this.unloaded = true;
        Iterable<ChunkPos> chunkPos = new ArrayList<>(this.loadedChunks.getMap().keySet());
        for (ChunkPos pos: chunkPos)
            this.unloadChunk(pos);
        this.writeAll();
    }

    public void saveAllChunks() throws IOException {
        Iterable<Chunk> chunks = new ArrayList<>(this.loadedChunks.getMap().values());
        for (Chunk chunk: chunks)
            this.saveChunk(chunk);
    }

    public void readAll() throws IOException {
        String informationFile = this.getInformationFile();
        if (!HFileHelper.checkFileAvailable(informationFile))
            this.writeAll();
        DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(informationFile))));
        if (!prefix.equals(dataInputStream.readUTF()))
            throw new DSTFormatException();
        this.read(dataInputStream);
        dataInputStream.close();
        this.loadPrepareChunks();
    }

    public void writeAll() throws IOException {
        String informationFile = this.getInformationFile();
        HFileHelper.createNewFile(informationFile);
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(informationFile))));
        this.write(dataOutputStream);
        dataOutputStream.close();
        this.saveAllChunks();
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.uuid = new UUID(input.readLong(), input.readLong());
        try {
            this.setInstance(DimensionUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()), false));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.instance = new NullDimension();
        }
        this.instance.read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeLong(this.uuid.getMostSignificantBits());
        output.writeLong(this.uuid.getLeastSignificantBits());
        this.instance.write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "uuid=" + this.uuid +
                ", instance=" + this.instance +
                ", tickHasUpdated=" + this.tickHasUpdated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dimension dimension)) return false;
        return this.uuid.equals(dimension.uuid) && this.instance.equals(dimension.instance) && this.tickHasUpdated.equals(dimension.tickHasUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid);
    }
}
