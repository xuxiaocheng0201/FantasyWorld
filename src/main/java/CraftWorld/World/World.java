package CraftWorld.World;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Dimension.Dimension;
import CraftWorld.World.Dimension.DimensionUtils;
import CraftWorld.World.Dimension.IDimensionBase;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HRandomHelper;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import HeadLibs.Registerer.HSetRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class World implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -383984335814983830L;
    public static final String id = "World";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull File worldSavedDirectory = (new File(ConstantStorage.WORLD_PATH)).getAbsoluteFile();
    protected boolean unloaded = true;

    protected String worldName = "New world";
    protected final DSTComplexMeta dst = new DSTComplexMeta();
    protected final @NotNull QuickTick tick = new QuickTick();
    protected @NotNull String randomSeed;
    protected @NotNull Random random;

    // Load all dimensions whose id is in keys of {@code prepareDimensionsID}.
    // At least load value dimensions include {@code prepareDimensionsUUID}.
    protected final HMapRegisterer<String, Integer> prepareDimensionsID = new HMapRegisterer<>(true);
    protected final HSetRegisterer<UUID> prepareDimensionsUUID = new HSetRegisterer<>();
    protected final HMapRegisterer<UUID, Dimension> loadedDimensions = new HMapRegisterer<>(false);
    protected final HMapRegisterer<String, HSetRegisterer<UUID>> generatedDimensions = new HMapRegisterer<>(true);

    public World(@NotNull String randomSeed) throws IOException {
        super();
        HFileHelper.createNewDirectory(ConstantStorage.WORLD_PATH);
        this.randomSeed = randomSeed;
        this.random = new Random(HRandomHelper.getSeed(this.randomSeed));
    }

    public World(String worldDirectoryPath, @NotNull String randomSeed) throws IOException {
        super();
        this.setWorldSavedDirectory(worldDirectoryPath);
        this.randomSeed = randomSeed;
        this.random = new Random(HRandomHelper.getSeed(this.randomSeed));
    }

    public void update() {
        this.tick.aT();
        for (Dimension dimension: this.loadedDimensions.values())
            dimension.update();
    }

    public @NotNull File getWorldSavedDirectory() {
        return this.worldSavedDirectory;
    }

    public void setWorldSavedDirectory(String worldSavedDirectoryPath) throws IOException {
        HFileHelper.createNewDirectory(worldSavedDirectoryPath);
        this.worldSavedDirectory = (new File(worldSavedDirectoryPath)).getAbsoluteFile();
    }

    public String getInformationFile() {
        return this.worldSavedDirectory.getPath() + "\\information.dat";
    }

    public String getDimensionsDirectory() {
        return this.worldSavedDirectory.getPath() + "\\dimensions";
    }

    public String getDimensionDirectory(UUID dimensionUUID) {
        return this.getDimensionsDirectory() + '\\' + dimensionUUID.toString();
    }

    public String getDimensionDirectory(Dimension dimension) {
        return this.getDimensionsDirectory() + '\\' + dimension.getUUID().toString();
    }

    public boolean isUnloaded() {
        return this.unloaded;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public DSTComplexMeta getDst() {
        return this.dst;
    }

    public @NotNull QuickTick getTick() {
        return this.tick;
    }

    public @NotNull String getRandomSeed() {
        return this.randomSeed;
    }

    public @NotNull Random getRandom() {
        return this.random;
    }

    public void addPrepareDimension(String dimensionId) {
        if (this.prepareDimensionsID.isRegisteredKey(dimensionId)) {
            try {
                Integer count = this.prepareDimensionsID.getElement(dimensionId);
                if (count == null)
                    count = 0;
                this.prepareDimensionsID.reset(dimensionId, count + 1);
            } catch (HElementNotRegisteredException | HElementRegisteredException ignore) {
            }
        }
        else
            try {
                this.prepareDimensionsID.register(dimensionId, 1);
            } catch (HElementRegisteredException ignore) {
            }
    }

    public void subPrepareDimension(String dimensionId) {
        try {
            Integer count = this.prepareDimensionsID.getElement(dimensionId);
            if (count == null || count <= 1) {
                this.prepareDimensionsID.deregisterKey(dimensionId);
                return;
            }
            this.prepareDimensionsID.reset(dimensionId, count + 1);
        } catch (HElementNotRegisteredException | HElementRegisteredException ignore) {
        }
    }

    public void setPrepareDimension(String dimensionId, int count) {
        if (count <= 0)
            this.prepareDimensionsID.deregisterKey(dimensionId);
        else {
            try {
                this.prepareDimensionsID.reset(dimensionId, count);
            } catch (HElementRegisteredException ignore) {
            }
        }
    }

    public void addPrepareDimension(UUID dimensionUUID) {
        try {
            this.prepareDimensionsUUID.register(dimensionUUID);
        } catch (HElementRegisteredException ignore) {
        }
    }

    public void subPrepareDimension(UUID dimensionUUID) {
        this.prepareDimensionsUUID.deregister(dimensionUUID);
    }

    public @NotNull HSetRegisterer<UUID> getGeneratedDimensionsUUID(String dimensionId) {
        try {
            HSetRegisterer<UUID> uuids = this.generatedDimensions.getElement(dimensionId);
            if (uuids == null)
                return new HSetRegisterer<>();
            return uuids;
        } catch (HElementNotRegisteredException exception) {
            return new HSetRegisterer<>();
        }
    }

    public @Nullable Dimension getAndLoadDimension(UUID dimensionUUID) throws IOException {
        try {
            return this.loadedDimensions.getElement(dimensionUUID);
        } catch (HElementNotRegisteredException ignore) {
        }
        Dimension dimension = new Dimension(this);
        String directory = this.getDimensionDirectory(dimensionUUID);
        if (!HFileHelper.checkDirectoryAvailable(directory))
            return null;
        dimension.setDimensionSavedDirectory(directory);
        dimension.load();
        try {
            this.loadedDimensions.register(dimensionUUID, dimension);
        } catch (HElementRegisteredException ignore) {
        }
        return dimension;
    }

    public @NotNull HSetRegisterer<Dimension> getDimensions(String dimensionId) throws IOException {
        HSetRegisterer<UUID> uuids = this.getGeneratedDimensionsUUID(dimensionId);
        HSetRegisterer<Dimension> dimensions = new HSetRegisterer<>();
        for (UUID uuid: uuids) {
            Dimension dimension = this.getAndLoadDimension(uuid);
            if (dimension != null) {
                try {
                    dimensions.register(dimension);
                } catch (HElementRegisteredException ignore) {
                }
            }
        }
        return dimensions;
    }

    public @NotNull Dimension getAndLoadNewDimension(String dimensionId) throws HElementNotRegisteredException, NoSuchMethodException, IOException {
        while (true)
            try {
                return this.getAndLoadNewDimension(DimensionUtils.getInstance().getElementInstance(dimensionId, false));
            } catch (IllegalStateException ignore) {
            }
    }

    public @NotNull Dimension getAndLoadNewDimension(IDimensionBase dimensionBase) throws IOException {
        Dimension dimension = new Dimension(this, dimensionBase);
        try {
            HSetRegisterer<UUID> uuids = this.generatedDimensions.getElement(dimensionBase.getDimensionId());
            if (uuids == null)
                throw new HElementNotRegisteredException();
            try {
                uuids.register(dimension.getUUID());
            } catch (HElementRegisteredException exception) {
                throw new IllegalStateException("Reiterated UUID!", exception);
            }
        } catch (HElementNotRegisteredException exception) {
            HSetRegisterer<UUID> uuids = new HSetRegisterer<>();
            try {
                uuids.register(dimension.getUUID());
                this.generatedDimensions.register(dimensionBase.getDimensionId(), uuids);
            } catch (HElementRegisteredException ignore) {
            }
        }
        HFileHelper.createNewDirectory(this.getDimensionDirectory(dimension));
        dimension.load();
        try {
            this.loadedDimensions.register(dimension.getUUID(), dimension);
        } catch (HElementRegisteredException ignore) {
        }
        return dimension;
    }

    public void unloadDimension(UUID dimensionUUID) throws IOException {
        Dimension dimension;
        try {
            dimension = this.loadedDimensions.getElement(dimensionUUID);
        } catch (HElementNotRegisteredException exception) {
            return;
        }
        this.loadedDimensions.deregisterKey(dimensionUUID);
        if (dimension == null)
            return;
        dimension.unload();
    }

    public void loadPrepareDimensions() throws IOException {
        for (UUID dimensionUUID: this.prepareDimensionsUUID)
            if (!this.loadedDimensions.isRegisteredKey(dimensionUUID))
                this.getAndLoadDimension(dimensionUUID);
        for (Entry<String, Integer> entry: this.prepareDimensionsID) {
            HSetRegisterer<UUID> uuids = this.getGeneratedDimensionsUUID(entry.getKey());
            int count = entry.getValue() - uuids.getRegisteredCount();
            if (count <= 0)
                continue;
            try {
                for (int i = 0; i < count; ++i)
                    this.getAndLoadNewDimension(entry.getKey());
            } catch (HElementNotRegisteredException | NoSuchMethodException ignore) {
            }
        }
    }

    public void load() throws IOException, HElementNotRegisteredException, NoSuchMethodException {
        this.unloaded = false;
        this.readInformation();
        this.loadPrepareDimensions();
    }

    public void unloadAllDimensions() throws IOException {
        Iterable<UUID> dimensions = new ArrayList<>(this.loadedDimensions.keys());
        for (UUID dimensionUUID: dimensions)
            this.unloadDimension(dimensionUUID);
    }

    public void unload() throws IOException {
        this.unloaded = true;
        this.unloadAllDimensions();
        this.writeInformation();
    }

    public void readInformation() throws IOException {
        if (!HFileHelper.checkDirectoryAvailable(this.worldSavedDirectory.getPath()))
            this.writeInformation();
        String informationFile = this.getInformationFile();
        if (!HFileHelper.checkFileAvailable(informationFile))
            this.writeInformation();
        DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(informationFile))));
        if (!prefix.equals(dataInputStream.readUTF()))
            throw new DSTFormatException();
        this.read(dataInputStream);
        dataInputStream.close();
    }

    public void writeInformation() throws IOException {
        HFileHelper.createNewDirectory(this.worldSavedDirectory.getPath());
        String informationFile = this.getInformationFile();
        HFileHelper.createNewFile(informationFile);
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(informationFile))));
        this.write(dataOutputStream);
        dataOutputStream.close();
        String dimensionsDirectory = this.getDimensionsDirectory();
        HFileHelper.createNewDirectory(dimensionsDirectory);
        for (Dimension dimension: this.loadedDimensions.values())
            dimension.writeInformation();
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.unloaded = input.readBoolean();
        this.worldName = input.readUTF();
        if (!DSTComplexMeta.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.dst.read(input);
        this.tick.set(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        this.randomSeed = input.readUTF();
        this.random = new Random(HRandomHelper.getSeed(this.randomSeed));
        this.prepareDimensionsUUID.deregisterAll();
        int size = input.readInt();
        for (int i = 0; i < size; ++i) {
            try {
                this.prepareDimensionsUUID.register(new UUID(input.readLong(), input.readLong()));
            } catch (HElementRegisteredException ignore) {
            }
        }
        this.prepareDimensionsID.deregisterAll();
        size = input.readInt();
        for (int i = 0; i < size; ++i) {
            try {
                this.prepareDimensionsID.register(input.readUTF(), input.readInt());
            } catch (HElementRegisteredException ignore) {
            }
        }
        this.generatedDimensions.deregisterAll();
        size = input.readInt();
        for (int i = 0; i < size; ++i) {
            String dimensionId = input.readUTF();
            HSetRegisterer<UUID> uuids = new HSetRegisterer<>();
            int size0 = input.readInt();
            for (int j = 0; j < size0; ++j) {
                try {
                    uuids.register(new UUID(input.readLong(), input.readLong()));
                } catch (HElementRegisteredException ignore) {
                }
            }
            try {
                this.generatedDimensions.register(dimensionId, uuids);
            } catch (HElementRegisteredException ignore) {
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
        output.writeUTF(this.tick.getFullTick().toString(ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeUTF(this.randomSeed);
        output.writeInt(this.prepareDimensionsUUID.getRegisteredCount());
        for (UUID uuid: this.prepareDimensionsUUID) {
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
        }
        output.writeInt(this.prepareDimensionsID.getRegisteredCount());
        for (Entry<String, Integer> entry: this.prepareDimensionsID) {
            output.writeUTF(entry.getKey());
            output.writeInt(entry.getValue());
        }
        output.writeInt(this.generatedDimensions.getRegisteredCount());
        for (Entry<String, HSetRegisterer<UUID>> entries: this.generatedDimensions) {
            output.writeUTF(entries.getKey());
            output.writeInt(entries.getValue().getRegisteredCount());
            for (UUID uuid: entries.getValue()) {
                output.writeLong(uuid.getMostSignificantBits());
                output.writeLong(uuid.getLeastSignificantBits());
            }
        }
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "World{" +
                "worldName='" + this.worldName + '\'' +
                ", unloaded=" + this.unloaded +
                ", dst=" + this.dst +
                ", tick=" + this.tick +
                '}';
    }

    public String toMoreString() {
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
