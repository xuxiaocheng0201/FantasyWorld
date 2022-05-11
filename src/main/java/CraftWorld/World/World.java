package CraftWorld.World;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.DST.DSTMetaCompound;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Dimension.Dimension;
import CraftWorld.World.Dimension.DimensionUtils;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;

import java.io.*;
import java.util.*;

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
    private final Collection<String> prepareDimensions = new HashSet<>();
    private final HMapRegisterer<UUID, String> generatedDimensions = new HMapRegisterer<>(true);
    private final HMapRegisterer<String, Dimension> loadedDimensions = new HMapRegisterer<>(false);

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

    public String getDimensionDirectory(String dimensionId) {
        return this.getDimensionsDirectory() + "\\" + dimensionId;
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

    public Dimension getDimension(String dimensionId) throws IOException, HElementNotRegisteredException, NoSuchMethodException {
        try {
            return this.loadedDimensions.getElement(dimensionId);
        } catch (HElementNotRegisteredException ignore) {
        }
        this.loadDimension(dimensionId);
        return this.loadedDimensions.getElement(dimensionId);
    }

    public void loadDimension(String dimensionId) throws IOException, HElementNotRegisteredException, NoSuchMethodException {
        if (this.unloaded)
            return;
        Dimension dimension = new Dimension(this, DimensionUtils.getInstance().getElementInstance(dimensionId, false));
        if (HFileHelper.checkDirectoryAvailable(this.getDimensionDirectory(dimensionId)))
            dimension.readAll();
        else
            dimension.writeAll();
        dimension.loadPrepareChunks();
        try {
            this.loadedDimensions.register(dimensionId, dimension);
            this.generatedDimensions.register(dimension.getUUID(), dimensionId);
        } catch (HElementRegisteredException ignore) {
        }
    }

    public void unloadDimension(String dimensionId) throws IOException {
        Dimension dimension;
        try {
            dimension = this.loadedDimensions.getElement(dimensionId);
        } catch (HElementNotRegisteredException exception) {
            return;
        }
        this.loadedDimensions.deregisterKey(dimensionId);
        if (dimension == null)
            return;
        dimension.unload();
    }

    public void loadPrepareDimensions() throws IOException, HElementNotRegisteredException, NoSuchMethodException {
        for (String dimensionId: this.prepareDimensions)
            this.loadDimension(dimensionId);
    }

    public void load() throws IOException, HElementNotRegisteredException, NoSuchMethodException {
        this.unloaded = false;
        this.readInformation();
        this.loadPrepareDimensions();
    }

    public void unloadAllDimensions() throws IOException {
        Iterable<String> dimensions = new ArrayList<>(this.loadedDimensions.getMap().keySet());
        for (String dimension: dimensions)
            this.unloadDimension(dimension);
    }

    public void unload() throws IOException {
        this.unloaded = true;
        this.unloadAllDimensions();
        this.writeInformation();
    }

    public void readInformation() throws IOException {
        //TODO: Automatically fix missing file.
        if (!HFileHelper.checkDirectoryAvailable(this.worldSavedDirectory.getPath()))
            throw new IOException("Unavailable world directory: " + this.worldSavedDirectory.getPath());
        String informationFile = this.getInformationFile();
        if (!HFileHelper.checkFileAvailable(informationFile))
            throw new IOException("Unavailable information file: " + informationFile);
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(informationFile)));
        if (!prefix.equals(dataInputStream.readUTF()))
            throw new DSTFormatException();
        this.read(dataInputStream);
        dataInputStream.close();
    }

    public void writeInformation() throws IOException {
        HFileHelper.createNewDirectory(this.worldSavedDirectory.getPath());
        String informationFile = this.getInformationFile();
        HFileHelper.createNewFile(informationFile);
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(informationFile)));
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
            UUID uuid = new UUID(input.readLong(), input.readLong());
            try {
                this.generatedDimensions.register(uuid, input.readUTF());
            } catch (HElementRegisteredException exception) {
                throw new DSTFormatException("Reiterated UUID!", exception);
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
        for (Map.Entry<UUID, String> entries: this.generatedDimensions.getMap().entrySet()) {
            output.writeLong(entries.getKey().getMostSignificantBits());
            output.writeLong(entries.getKey().getLeastSignificantBits());
            output.writeUTF(entries.getValue());
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
}
