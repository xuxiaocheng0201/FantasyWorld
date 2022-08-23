package CraftWorld.World.Dimension;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Dimensions.NullDimension;
import CraftWorld.Utils.QuickTick;
import CraftWorld.Utils.WorldSystemUtils;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.BasicInformation.DimensionId;
import CraftWorld.World.World;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HRandomHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HNotNullMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Dimension implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -4936855319467494864L;
    public static final DSTId id = DSTId.getDstIdInstance("Dimension");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected final @NotNull World world;
    protected final @NotNull File dimensionSavedDirectory;
    protected boolean unloaded = true;
    protected @NotNull UUID uuid;
    protected @NotNull IDimensionBase instance = new NullDimension(); {this.instance.setExistingDimensionInstance(this);}
    protected final @NotNull QuickTick tickHasUpdated = new QuickTick();
    protected @Nullable String randomSeed;

    protected final @NotNull HNotNullMapRegisterer<ChunkPos, Chunk> loadedChunks = new HNotNullMapRegisterer<>(false);

    public Dimension(@NotNull World world) {
        super();
        this.world = world;
        this.uuid = HRandomHelper.getRandomUUID();
        this.randomSeed = WorldSystemUtils.getDimensionSeed(world.getRandomSeed(), this.uuid);
        this.dimensionSavedDirectory = new File(this.world.getDimensionDirectory(this.uuid));
    }

    public Dimension(@NotNull World world, @Nullable IDimensionBase instance) {
        super();
        this.world = world;
        this.uuid = HRandomHelper.getRandomUUID();
        if (instance != null)
            this.setInstance(instance);
        this.randomSeed = WorldSystemUtils.getDimensionSeed(world.getRandomSeed(), this.uuid);
        this.dimensionSavedDirectory = new File(this.world.getDimensionDirectory(this.uuid));
    }

    public Dimension(@NotNull World world, @Nullable UUID dimensionUUID) throws IOException {
        super();
        this.world = world;
        this.uuid = Objects.requireNonNullElse(dimensionUUID, HRandomHelper.getRandomUUID());
        this.randomSeed = WorldSystemUtils.getDimensionSeed(world.getRandomSeed(), this.uuid);
        this.dimensionSavedDirectory = new File(this.world.getDimensionDirectory(this.uuid));
        this.readInformation();
    }

    public void update() {
        this.tickHasUpdated.set(this.world.getTick().getFullTick());
        for (Chunk chunk: this.loadedChunks.values())
            chunk.update();
    }

    public @NotNull File getDimensionSavedDirectory() {
        return this.dimensionSavedDirectory;
    }

    public @NotNull String getInformationFile() {
        return this.dimensionSavedDirectory.getPath() + "\\information.dat";
    }

    public @NotNull String getChunksDirectory() {
        return this.dimensionSavedDirectory.getPath() + "\\chunks";
    }

    public @NotNull String getChunkDirectory(@NotNull ChunkPos pos) {
        return this.getChunksDirectory() + '\\' +
                pos.getBigX().toString(ConstantStorage.SAVE_NUMBER_RADIX) + '\\' +
                pos.getBigY().toString(ConstantStorage.SAVE_NUMBER_RADIX) + '\\' +
                pos.getBigZ().toString(ConstantStorage.SAVE_NUMBER_RADIX) + ".dat";
    }

    public boolean isUnloaded() {
        return this.unloaded;
    }

    public @NotNull World getWorld() {
        return this.world;
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    public @NotNull IDimensionBase getInstance() {
        return this.instance;
    }

    public void setInstance(@Nullable IDimensionBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, NullDimension::new);
        this.instance.setExistingDimensionInstance(this);
    }

    public @NotNull QuickTick getTickHasUpdated() {
        return this.tickHasUpdated;
    }

    public @Nullable String getRandomSeed() {
        return this.randomSeed;
    }

    public @NotNull Chunk loadChunk(@NotNull ChunkPos pos) throws IOException {
        if (this.unloaded)
            throw new IllegalStateException("Load chunk in an unloaded dimension.");
        try {
            return this.loadedChunks.getElement(pos);
        } catch (HElementNotRegisteredException ignore) {
        }
        Chunk chunk = new Chunk(this, pos);
        chunk.load();
        try {
            this.loadedChunks.register(pos, chunk);
        } catch (HElementRegisteredException ignore) {
        }
        return chunk;
    }

    public void unloadChunk(@Nullable ChunkPos pos) throws IOException {
        Chunk chunk;
        try {
            chunk = this.loadedChunks.getElement(pos);
        } catch (HElementNotRegisteredException ignore) {
            return;
        }
        this.loadedChunks.deregisterKey(pos);
        chunk.unload();
    }

    public void unloadAllChunks() throws IOException {
        Iterable<ChunkPos> chunkPos = new ArrayList<>(this.loadedChunks.keys());
        for (ChunkPos pos: chunkPos)
            this.unloadChunk(pos);
    }

    public void loadPrepareChunks() throws IOException {
        for (ChunkPos pos: this.instance.getPrepareChunkPos())
            this.loadChunk(pos);
    }

    public void readInformation() throws IOException {
        String informationFile = this.getInformationFile();
        try (DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(informationFile))))) {
            if (!prefix.equals(dataInputStream.readUTF()))
                throw new DSTFormatException();
            this.read(dataInputStream);
        } catch (DSTFormatException | FileNotFoundException ignore) {
            this.writeInformation();
        }
    }

    public void writeInformation() throws IOException {
        String informationFile = this.getInformationFile();
        HFileHelper.createNewFile(informationFile);
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(informationFile))));
        this.write(dataOutputStream);
        dataOutputStream.close();
    }

    public void load() throws IOException {
        this.unloaded = false;
        this.readInformation();
        this.loadPrepareChunks();
    }

    public void unload() throws IOException {
        this.unloadAllChunks();
        this.writeInformation();
        this.unloaded = true;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.unloaded = input.readBoolean();
        this.uuid = new UUID(input.readLong(), input.readLong());
        DimensionId dimensionId = new DimensionId();
        if (!DimensionId.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        dimensionId.read(input);
        try {
            this.setInstance(DimensionUtils.getInstance().getElementInstance(dimensionId, false));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.setInstance(null);
        }
        this.instance.read(input);
        if (!QuickTick.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.tickHasUpdated.read(input);
        if (input.readBoolean())
            this.randomSeed = input.readUTF();
        else
            this.randomSeed = null;
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeBoolean(this.unloaded);
        output.writeLong(this.uuid.getMostSignificantBits());
        output.writeLong(this.uuid.getLeastSignificantBits());
        this.instance.write(output);
        this.tickHasUpdated.write(output);
        if (this.randomSeed != null) {
            output.writeBoolean(true);
            output.writeUTF(this.randomSeed);
        } else
            output.writeBoolean(false);
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        return "Dimension{" +
                "uuid=" + this.uuid +
                ", instance=" + this.instance +
                ", tickHasUpdated=" + this.tickHasUpdated +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof Dimension dimension)) return false;
        return this.uuid.equals(dimension.uuid) && this.instance.equals(dimension.instance) && this.tickHasUpdated.equals(dimension.tickHasUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid);
    }
}
