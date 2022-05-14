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

    private File worldSavedDirectory;
    private boolean unloaded;
    private String worldName = "New world";
    private final DSTMetaCompound dst = new DSTMetaCompound();
    private QuickTick tick;

    private final Collection<String> prepareDimensions = new ArrayList<>();
    private final Collection<UUID> prepareDimensionsUUID = new ArrayList<>();
    private final HMapRegisterer<UUID, Dimension> loadedDimensions = new HMapRegisterer<>(false);
    private final HMapRegisterer<String, Set<UUID>> generatedDimensions = new HMapRegisterer<>(true);

    public World() throws IOException {
        this(ConstantStorage.WORLD_PATH);
    }

    public World(String worldDirectoryPath) throws IOException {
        super();
        this.setWorldSavedDirectory(worldDirectoryPath);
    }

    public void update() {
        this.tick.a1();
        for (Dimension dimension: this.loadedDimensions.getMap().values())
            dimension.update();
    }

    public File getWorldSavedDirectory() {
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

    public QuickTick getTick() {
        return this.tick;
    }

    public void addPrepareDimension(String dimensionId) {
        this.prepareDimensions.add(dimensionId);
    }

    public void addPrepareDimension(UUID dimensionUUID) {
        this.prepareDimensionsUUID.add(dimensionUUID);
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

    public @Nullable Dimension getDimension(UUID dimensionUUID) {
        try {
            return this.loadedDimensions.getElement(dimensionUUID);
        } catch (HElementNotRegisteredException ignore) {
        }
        Dimension dimension = Dimension.getFromUUID(this, dimensionUUID);
        try {
            this.loadedDimensions.register(dimensionUUID, dimension);
        } catch (HElementRegisteredException ignore) {
        }
        return dimension;
    }

    public @NotNull Set<Dimension> getDimensions(String dimensionId) {
        Set<UUID> uuids = this.getDimensionsUUID(dimensionId);
        Set<Dimension> dimensions = new HashSet<>(uuids.size());
        for (UUID uuid: uuids) {
            Dimension dimension = this.getDimension(uuid);
            if (dimension != null)
                dimensions.add(dimension);
        }
        return dimensions;
    }

    public @NotNull Dimension getNewDimension(String dimensionId) throws HElementNotRegisteredException, NoSuchMethodException, IOException {
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

    public void loadPrepareDimensions() {
        for (UUID dimensionUUID: this.prepareDimensionsUUID) {
            Dimension dimension = this.getDimension(dimensionUUID);
            if (dimension != null)
                dimension.load();
        }
        for (String dimensionId: this.prepareDimensions) {
            for (UUID dimensionUUID: this.getDimensionsUUID(dimensionId)) {
                Dimension dimension = this.getDimension(dimensionUUID);
                if (dimension != null)
                    dimension.load();
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
        this.tick = new QuickTick(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        this.prepareDimensions.clear();
        int size = input.readInt();
        for (int i = 0; i < size; ++i)
            this.prepareDimensions.add(input.readUTF());
        size = input.readInt();
        for (int i = 0; i < size; ++i) {
            String dimensionId = input.readUTF();
            int s = input.readInt();
            Set<UUID> uuids = new HashSet<>(s);
            for (int j = 0; j < s; ++j) {
                UUID uuid = new UUID(input.readLong(), input.readLong());
                uuids.add(uuid);
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
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeBoolean(this.unloaded);
        output.writeUTF(this.worldName);
        this.dst.write(output);
        output.writeUTF(this.tick.getFullTick().toString(ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeInt(this.prepareDimensions.size());
        for (String dimensionId: this.prepareDimensions)
            output.writeUTF(dimensionId);
        output.writeInt(this.generatedDimensions.getMap().size());
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
        return this.unloaded == world.unloaded && this.worldName.equals(world.worldName) && this.dst.equals(world.dst) && this.tick.equals(world.tick) && this.prepareDimensions.equals(world.prepareDimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.unloaded, this.worldName, this.dst, this.prepareDimensions);
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
