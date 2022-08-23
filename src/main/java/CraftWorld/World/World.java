package CraftWorld.World;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Dimension.BasicInformation.DimensionId;
import CraftWorld.World.Dimension.Dimension;
import CraftWorld.World.Dimension.DimensionUtils;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Registerer.*;
import HeadLibs.Registerer.HSetRegisterer.ImmutableSetRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class World implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -383984335814983830L;
    public static final DSTId id = DSTId.getDstIdInstance("World");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull File worldSavedDirectory = (new File(ConstantStorage.WORLD_PATH)).getAbsoluteFile();
    protected boolean unloaded = true;
    protected @NotNull String worldName = "New world";
    protected final DSTComplexMeta dst = new DSTComplexMeta();
    protected final @NotNull QuickTick tick = new QuickTick();
    protected @Nullable String randomSeed;

    // {@code prepareDimensionsID}:   [Prepare] Load dimensions whose id is {key} {value} times.
    // {@code prepareDimensionsUUID}: [Prepare] Loaded dimensions last time.
    // *if {@code prepareDimensionsUUID} is not empty, {@code prepareDimensionsID} will be reduced.
    protected final @NotNull HNotNullMapRegisterer<DimensionId, Integer> prepareDimensionsID = new HNotNullMapRegisterer<>(true);
    protected final @NotNull HSetRegisterer<UUID> prepareDimensionsUUID = new HSetRegisterer<>(false);

    protected final @NotNull HNotNullMapRegisterer<UUID, Dimension> loadedDimensions = new HNotNullMapRegisterer<>(false);
    protected final @NotNull HNotNullMapRegisterer<DimensionId, HSetRegisterer<UUID>> generatedDimensions = new HNotNullMapRegisterer<>(false);

    public World() throws IOException {
        super();
    }

    public World(@Nullable String randomSeed) throws IOException {
        super();
        this.randomSeed = randomSeed;
    }

    public World(@Nullable String worldDirectoryPath, @Nullable String randomSeed) throws IOException {
        super();
        this.setWorldSavedDirectory(worldDirectoryPath);
        this.randomSeed = randomSeed;
    }

    public void update() {
        this.tick.aT();
        for (Dimension dimension: this.loadedDimensions.values())
            dimension.update();
        //TODO: tick time optimization.
    }

    public @NotNull File getWorldSavedDirectory() {
        return this.worldSavedDirectory;
    }

    public void setWorldSavedDirectory(@Nullable String worldSavedDirectoryPath) throws IOException {
        if (worldSavedDirectoryPath == null) {
            this.worldSavedDirectory = (new File(ConstantStorage.WORLD_PATH)).getAbsoluteFile();
            HFileHelper.createNewDirectory(this.worldSavedDirectory.getPath());
            return;
        }
        HFileHelper.createNewDirectory(worldSavedDirectoryPath);
        this.worldSavedDirectory = (new File(worldSavedDirectoryPath)).getAbsoluteFile();
    }

    public String getInformationFile() {
        return this.worldSavedDirectory.getPath() + "\\information.dat";
    }

    public String getDimensionsDirectory() {
        return this.worldSavedDirectory.getPath() + "\\dimensions";
    }

    public String getDimensionDirectory(@NotNull UUID dimensionUUID) {
        return this.getDimensionsDirectory() + '\\' + dimensionUUID;
    }

    public boolean isUnloaded() {
        return this.unloaded;
    }

    public @NotNull String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(@Nullable String worldName) {
        this.worldName = Objects.requireNonNullElse(worldName, "New world");
    }

    public @NotNull DSTComplexMeta getDst() {
        return this.dst;
    }

    public @NotNull QuickTick getTick() {
        return this.tick;
    }

    public @Nullable String getRandomSeed() {
        return this.randomSeed;
    }

    public void atLeastPrepareDimensionCount(@Nullable DimensionId dimensionId, int count) {
        if (dimensionId == null)
            return;
        int nowCount = 0;
        try {
            nowCount = this.prepareDimensionsID.getElement(dimensionId);
        } catch (HElementNotRegisteredException ignore) {
        }
        if (nowCount < count)
            this.setPrepareDimensionCount(dimensionId, count);
    }

    public void addPrepareDimensionCount(@Nullable DimensionId dimensionId) {
        if (dimensionId == null)
            return;
        try {
            this.prepareDimensionsID.register(dimensionId, 1);
        } catch (HElementRegisteredException exception) {
            try {
                Integer count = this.prepareDimensionsID.getElement(dimensionId);
                this.prepareDimensionsID.reset(dimensionId, count + 1);
            } catch (HElementNotRegisteredException | HElementRegisteredException ignore) {
            }
        }
    }

    public void subPrepareDimensionCount(@Nullable DimensionId dimensionId) {
        if (dimensionId == null)
            return;
        try {
            int count = this.prepareDimensionsID.getElement(dimensionId);
            if (count <= 1) {
                this.prepareDimensionsID.deregisterKey(dimensionId);
                return;
            }
            this.prepareDimensionsID.reset(dimensionId, count - 1);
        } catch (HElementNotRegisteredException | HElementRegisteredException ignore) {
        }
    }

    public void setPrepareDimensionCount(@Nullable DimensionId dimensionId, int count) {
        if (dimensionId == null)
            return;
        if (count <= 0)
            this.prepareDimensionsID.deregisterKey(dimensionId);
        else {
            try {
                this.prepareDimensionsID.reset(dimensionId, count);
            } catch (HElementRegisteredException ignore) {
            }
        }
    }

    public void addPrepareDimensionUUID(@Nullable UUID dimensionUUID) {
        try {
            this.prepareDimensionsUUID.register(dimensionUUID);
        } catch (HElementRegisteredException ignore) {
        }
    }

    public void subPrepareDimensionUUID(@Nullable UUID dimensionUUID) {
        this.prepareDimensionsUUID.deregister(dimensionUUID);
    }

    public @NotNull ImmutableSetRegisterer<UUID> getGeneratedDimensionsUUID(@Nullable DimensionId dimensionId) {
        try {
            return this.generatedDimensions.getElement(dimensionId).toImmutable();
        } catch (HElementNotRegisteredException exception) {
            return new ImmutableSetRegisterer<>();
        }
    }

    public @NotNull Dimension loadGeneratedDimension(@NotNull UUID dimensionUUID) throws IOException {
        try {
            return this.loadedDimensions.getElement(dimensionUUID);
        } catch (HElementNotRegisteredException ignore) {
        }
        Dimension dimension = new Dimension(this, dimensionUUID);
        dimension.load();
        try {
            this.loadedDimensions.register(dimensionUUID, dimension);
        } catch (HElementRegisteredException ignore) {
        }
        return dimension;
    }

    public @NotNull HSetRegisterer<Dimension> loadGeneratedDimensions(@Nullable DimensionId dimensionId) throws IOException {
        HSetRegisterer<UUID> uuids = this.getGeneratedDimensionsUUID(dimensionId);
        HSetRegisterer<Dimension> dimensions = new HSetRegisterer<>();
        for (UUID uuid: uuids) {
            Dimension dimension = this.loadGeneratedDimension(uuid);
            try {
                dimensions.register(dimension);
            } catch (HElementRegisteredException ignore) {
            }
        }
        return dimensions;
    }

    public @NotNull Dimension generateNewDimension(@NotNull DimensionId dimensionId) throws IOException {
        Dimension dimension;
        try {
            dimension = new Dimension(this, DimensionUtils.getInstance().getElementInstance(dimensionId, false));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            throw new IOException("Failed to get a new dimension instance.", exception);
        }
        try {
            HSetRegisterer<UUID> uuids = this.generatedDimensions.getElement(dimensionId);
            try {
                uuids.register(dimension.getUUID());
            } catch (HElementRegisteredException exception) {
                throw new HashCollisionsErrorException("Reiterated UUID!", dimension.getUUID(), null, exception);
            }
        } catch (HElementNotRegisteredException exception) {
            HSetRegisterer<UUID> uuids = new HSetRegisterer<>();
            try {
                uuids.register(dimension.getUUID());
                this.generatedDimensions.register(dimensionId, uuids);
            } catch (HElementRegisteredException ignore) {
            }
        }
        dimension.load();
        try {
            this.loadedDimensions.register(dimension.getUUID(), dimension);
        } catch (HElementRegisteredException ignore) {
        }
        return dimension;
    }

    public void unloadDimension(@Nullable UUID dimensionUUID) throws IOException {
        Dimension dimension;
        try {
            dimension = this.loadedDimensions.getElement(dimensionUUID);
        } catch (HElementNotRegisteredException ignore) {
            return;
        }
        this.loadedDimensions.deregisterKey(dimensionUUID);
        dimension.unload();
    }

    public void unloadDimensions(@Nullable DimensionId dimensionId) throws IOException {
        for (UUID uuid: this.getGeneratedDimensionsUUID(dimensionId))
            this.unloadDimension(uuid);
    }

    public void unloadAllDimensions() throws IOException {
        Iterable<UUID> dimensions = new ArrayList<>(this.loadedDimensions.keys());
        for (UUID dimensionUUID: dimensions)
            this.unloadDimension(dimensionUUID);
    }

    public void loadPrepareDimensions() throws IOException {
        for (UUID dimensionUUID: this.prepareDimensionsUUID)
            if (!this.loadedDimensions.isRegisteredKey(dimensionUUID))
                this.loadGeneratedDimension(dimensionUUID);
        for (Entry<DimensionId, Integer> entry: this.prepareDimensionsID) {
            HSetRegisterer<UUID> uuids = this.getGeneratedDimensionsUUID(entry.getKey());
            for (UUID uuid: uuids)
                if (!this.loadedDimensions.isRegisteredKey(uuid))
                    this.loadGeneratedDimension(uuid);
            int count = entry.getValue() - uuids.getRegisteredCount();
            if (count <= 0)
                continue;
            for (int i = 0; i < count; ++i)
                this.generateNewDimension(entry.getKey());
        }
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
        HFileHelper.createNewDirectory(this.worldSavedDirectory.getPath());
        String informationFile = this.getInformationFile();
        HFileHelper.createNewFile(informationFile);
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(informationFile))));
        this.write(dataOutputStream);
        dataOutputStream.close();
    }

    public void load() throws IOException {
        this.unloaded = false;
        this.readInformation();
        this.loadPrepareDimensions();
    }

    public void unload() throws IOException {
        this.unloadAllDimensions();
        this.writeInformation();
        this.unloaded = true;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.unloaded = input.readBoolean();
        this.worldName = input.readUTF();
        if (!DSTComplexMeta.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.dst.read(input);
        if (!QuickTick.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.tick.read(input);
        if (input.readBoolean())
            this.randomSeed = input.readUTF();
        else
            this.randomSeed = null;
        this.prepareDimensionsUUID.deregisterAll();
        int size = input.readInt();
        for (int i = 0; i < size; ++i)
            try {
                this.prepareDimensionsUUID.register(new UUID(input.readLong(), input.readLong()));
            } catch (HElementRegisteredException exception) {
                throw new DSTFormatException(exception);
            }
        this.prepareDimensionsID.deregisterAll();
        size = input.readInt();
        for (int i = 0; i < size; ++i) {
            DimensionId dimensionId = new DimensionId();
            if (!DimensionId.prefix.equals(input.readUTF()))
                throw new DSTFormatException();
            dimensionId.read(input);
            try {
                this.prepareDimensionsID.register(dimensionId, input.readInt());
            } catch (HElementRegisteredException exception) {
                throw new DSTFormatException(exception);
            }
        }
        this.generatedDimensions.deregisterAll();
        size = input.readInt();
        for (int i = 0; i < size; ++i) {
            DimensionId dimensionId = new DimensionId();
            if (!DimensionId.prefix.equals(input.readUTF()))
                throw new DSTFormatException();
            dimensionId.read(input);
            HSetRegisterer<UUID> uuids = new HSetRegisterer<>();
            int size0 = input.readInt();
            for (int j = 0; j < size0; ++j)
                try {
                    uuids.register(new UUID(input.readLong(), input.readLong()));
                } catch (HElementRegisteredException exception) {
                    throw new DSTFormatException(exception);
                }
            try {
                this.generatedDimensions.register(dimensionId, uuids);
            } catch (HElementRegisteredException exception) {
                throw new DSTFormatException(exception);
            }
        }
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeBoolean(this.unloaded);
        output.writeUTF(this.worldName);
        this.dst.write(output);
        this.tick.write(output);
        if (this.randomSeed != null) {
            output.writeBoolean(true);
            output.writeUTF(this.randomSeed);
        } else
            output.writeBoolean(false);
        output.writeInt(this.prepareDimensionsUUID.getRegisteredCount());
        for (UUID uuid: this.prepareDimensionsUUID) {
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
        }
        output.writeInt(this.prepareDimensionsID.getRegisteredCount());
        for (Entry<DimensionId, Integer> entry: this.prepareDimensionsID) {
            entry.getKey().write(output);
            output.writeInt(entry.getValue());
        }
        output.writeInt(this.generatedDimensions.getRegisteredCount());
        for (Entry<DimensionId, HSetRegisterer<UUID>> entries: this.generatedDimensions) {
            entries.getKey().write(output);
            output.writeInt(entries.getValue().getRegisteredCount());
            for (UUID uuid: entries.getValue()) {
                output.writeLong(uuid.getMostSignificantBits());
                output.writeLong(uuid.getLeastSignificantBits());
            }
        }
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        return "World{" +
                "worldName='" + this.worldName + '\'' +
                ", unloaded=" + this.unloaded +
                ", dst=" + this.dst +
                ", tick=" + this.tick +
                '}';
    }

    public @NotNull String toStringMore() {
        return "World{" +
                "worldSavedDirectory=" + this.worldSavedDirectory +
                ", unloaded=" + this.unloaded +
                ", worldName='" + this.worldName + '\'' +
                ", dst=" + this.dst +
                ", tick=" + this.tick +
                ", prepareDimensionsID=" + this.prepareDimensionsID +
                ", prepareDimensionsUUID=" + this.prepareDimensionsUUID +
                ", generatedDimensions=" + this.generatedDimensions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof World world)) return false;
        return this.unloaded == world.unloaded && this.worldName.equals(world.worldName) && this.dst.equals(world.dst) && this.tick.equals(world.tick) && this.prepareDimensionsUUID.equals(world.prepareDimensionsUUID) && this.prepareDimensionsID.equals(world.prepareDimensionsID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.unloaded, this.worldName, this.dst, this.prepareDimensionsID);
    }
}
