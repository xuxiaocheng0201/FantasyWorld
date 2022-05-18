package CraftWorld.World;

import Core.EventBus.EventSubscribe;
import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Events.ChunkGenerateEvent;
import CraftWorld.Instance.DST.DSTMetaCompound;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.Dimension;
import CraftWorld.World.Dimension.DimensionUtils;
import CraftWorld.World.Dimension.IDimensionBase;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import HeadLibs.Registerer.HSetRegisterer;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class World implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -383984335814983830L;
    public static final String id = "World";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, World.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private @NotNull File worldSavedDirectory = (new File(ConstantStorage.WORLD_PATH)).getAbsoluteFile();
    private boolean unloaded = true;

    private String worldName = "New world";
    private final DSTMetaCompound dst = new DSTMetaCompound();
    private final @NotNull QuickTick tick = new QuickTick();

    // Load all dimensions which id is in keys of {@code prepareDimensionsID}.
    // At least load value dimensions include {@code prepareDimensionsUUID}.
    private final HMapRegisterer<String, Integer> prepareDimensionsID = new HMapRegisterer<>(true);
    private final HSetRegisterer<UUID> prepareDimensionsUUID = new HSetRegisterer<>();
    private final HMapRegisterer<UUID, Dimension> loadedDimensions = new HMapRegisterer<>(false);
    private final HMapRegisterer<String, Set<UUID>> generatedDimensions = new HMapRegisterer<>(true);

    public World() throws IOException {
        super();
        HFileHelper.createNewDirectory(ConstantStorage.WORLD_PATH);
    }

    public World(String worldDirectoryPath) throws IOException {
        super();
        this.setWorldSavedDirectory(worldDirectoryPath);
    }

    public void update() {
        this.tick.aT();
        for (Dimension dimension: this.loadedDimensions.getMap().values())
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
        return this.getDimensionsDirectory() + "\\" + dimensionUUID.toString();
    }

    public String getDimensionDirectory(Dimension dimension) {
        return this.getDimensionsDirectory() + "\\" + dimension.getUUID().toString();
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

    public DSTMetaCompound getDst() {
        return this.dst;
    }

    public @NotNull QuickTick getTick() {
        return this.tick;
    }

    public void addPrepareDimension(String dimensionId) {
        if (this.prepareDimensionsID.isRegisteredKey(dimensionId)) {
            try {
                Integer count = this.prepareDimensionsID.getElement(dimensionId);
                if (count == null) {
                    this.prepareDimensionsID.reset(dimensionId, 1);
                    return;
                }
                this.prepareDimensionsID.reset(dimensionId, count + 1);
            } catch (HElementNotRegisteredException ignore) {
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
        } catch (HElementNotRegisteredException ignore) {
        }
    }

    public void setPrepareDimension(String dimensionId, int count) {
        if (count <= 0)
            this.prepareDimensionsID.deregisterKey(dimensionId);
        else
            this.prepareDimensionsID.reset(dimensionId, count);
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

    public @NotNull Set<UUID> getDimensionsUUID(String dimensionId) {
        try {
            Set<UUID> uuids = this.generatedDimensions.getElement(dimensionId);
            if (uuids == null)
                return new HashSet<>();
            return uuids;
        } catch (HElementNotRegisteredException exception) {
            return new HashSet<>();
        }
    }

    public @Nullable Dimension getAndLoadDimension(UUID dimensionUUID) throws IOException {
        try {
            return this.loadedDimensions.getElement(dimensionUUID);
        } catch (HElementNotRegisteredException ignore) {
        }
        Dimension dimension = Dimension.getFromUUID(this, dimensionUUID);
        if (dimension == null)
            return null;
        dimension.load();
        try {
            this.loadedDimensions.register(dimensionUUID, dimension);
        } catch (HElementRegisteredException ignore) {
        }
        return dimension;
    }

    public @NotNull Set<Dimension> getDimensions(String dimensionId) throws IOException {
        Set<UUID> uuids = this.getDimensionsUUID(dimensionId);
        Set<Dimension> dimensions = new HashSet<>(uuids.size());
        for (UUID uuid: uuids) {
            Dimension dimension = this.getAndLoadDimension(uuid);
            if (dimension != null)
                dimensions.add(dimension);
        }
        return dimensions;
    }

    public @NotNull Dimension getAndLoadNewDimension(String dimensionId) throws HElementNotRegisteredException, NoSuchMethodException, IOException {
        Dimension dimension = new Dimension(this, DimensionUtils.getInstance().getElementInstance(dimensionId, false));
        HFileHelper.createNewDirectory(this.getDimensionDirectory(dimension));
        try {
            this.loadedDimensions.register(dimension.getUUID(), dimension);
        } catch (HElementRegisteredException exception) {
            throw new IllegalStateException("Reiterated UUID!", exception);
        }
        dimension.load();
        try {
            Set<UUID> uuids = this.generatedDimensions.getElement(dimensionId);
            if (uuids == null)
                throw new HElementNotRegisteredException();
            uuids.add(dimension.getUUID());
        } catch (HElementNotRegisteredException exception) {
            Set<UUID> uuids = new HashSet<>();
            uuids.add(dimension.getUUID());
            try {
                this.generatedDimensions.register(dimensionId, uuids);
            } catch (HElementRegisteredException ignore) {
            }
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
        for (UUID dimensionUUID: this.prepareDimensionsUUID.getSet())
            if (!this.loadedDimensions.isRegisteredKey(dimensionUUID))
                this.getAndLoadDimension(dimensionUUID);
        for (Map.Entry<String, Integer> entry: this.prepareDimensionsID.getMap().entrySet()) {
            Set<UUID> uuids = this.getDimensionsUUID(entry.getKey());
            int count = entry.getValue() - uuids.size();
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
        Iterable<UUID> dimensions = new ArrayList<>(this.loadedDimensions.getMap().keySet());
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
        for (Dimension dimension: this.loadedDimensions.getMap().values())
            dimension.writeAll();
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.unloaded = input.readBoolean();
        this.worldName = input.readUTF();
        if (!DSTMetaCompound.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.dst.read(input);
        this.tick.set(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
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
            int size0 = input.readInt();
            Set<UUID> uuids = new HashSet<>(size0);
            for (int j = 0; j < size0; ++j)
                uuids.add(new UUID(input.readLong(), input.readLong()));
            try {
                this.generatedDimensions.register(dimensionId, uuids);
            } catch (HElementRegisteredException ignore) {
            }
        }
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeBoolean(this.unloaded);
        output.writeUTF(this.worldName);
        this.dst.write(output);
        output.writeUTF(this.tick.getFullTick().toString(ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeInt(this.prepareDimensionsUUID.getRegisteredCount());
        for (UUID uuid: this.prepareDimensionsUUID.getSet()) {
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
        }
        output.writeInt(this.prepareDimensionsID.getRegisteredCount());
        for (Map.Entry<String, Integer> entry: this.prepareDimensionsID.getMap().entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeInt(entry.getValue());
        }
        output.writeInt(this.generatedDimensions.getRegisteredCount());
        for (Map.Entry<String, Set<UUID>> entries: this.generatedDimensions.getMap().entrySet()) {
            output.writeUTF(entries.getKey());
            output.writeInt(entries.getValue().size());
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
                ", dst=" + this.dst +
                ", tick=" + this.tick +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof World world)) return false;
        return this.unloaded == world.unloaded && this.worldName.equals(world.worldName) && this.dst.equals(world.dst) && this.tick.equals(world.tick) && this.prepareDimensionsID.equals(world.prepareDimensionsID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.unloaded, this.worldName, this.dst, this.prepareDimensionsID);
    }

    @EventSubscribe
    public static class NullDimension implements IDimensionBase {
        @Serial
        private static final long serialVersionUID = -5202694397165174510L;

        public static String id = "NullDimension";
        public static String prefix = DSTUtils.prefix(id);
        public static String suffix = DSTUtils.suffix(id);
        static {
            try {
                DimensionUtils.getInstance().register(id, NullDimension.class);
                DSTUtils.getInstance().register(id, NullDimension.class);
            } catch (HElementRegisteredException exception) {
                HLog.logger(HLogLevel.ERROR, exception);
            }
        }
        @Override
        public String getDimensionId() {
            return id;
        }

        @Override
        public String getDimensionName() {
            return "Null";
        }

        @Override
        public void setDimensionName(String name) {
        }

        @Override
        public Set<ChunkPos> getPrepareChunkPos() {
            return new HashSet<>();
        }

        @Override
        public void read(DataInput input) throws IOException {
        }

        @Override
        public void write(DataOutput output) throws IOException {
            output.writeUTF(prefix);
        }

        private final DSTMetaCompound dst = new DSTMetaCompound();

        @Override
        public DSTMetaCompound getDimensionDST() {
            return this.dst;
        }

        @Override
        public String toString() {
            return "NullDimension{" +
                    "dst=" + this.dst +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NullDimension that)) return false;
            return this.dst.equals(that.dst);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.dst);
        }

        @SuppressWarnings("MethodMayBeStatic")
        @Subscribe
        public void generate(ChunkGenerateEvent event) {
            if (event.chunk().getDimension().getInstance().getDimensionId().equals(id))
                event.chunk().clearBlocks();
        }
    }
}
