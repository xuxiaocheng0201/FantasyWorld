package CraftWorld.World;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Dimension.Dimension;
import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Instance.DST.DSTMetaCompound;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.Range;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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

    private boolean unloaded;
    private File worldSavedDirectory;
    private final Collection<String> prepareDimensions = new HashSet<>();
    private String worldName = "New world";
    private final DSTMetaCompound dst = new DSTMetaCompound();
    private @Range(from = 0, to = Long.MAX_VALUE) long tick;
    private final HMapRegisterer<String, Dimension> dimensions = new HMapRegisterer<>(false);

    public World() throws IOException {
        this(ConstantStorage.WORLD_PATH);
    }

    public World(String worldDirectoryPath) throws IOException {
        super();
        this.setWorldSavedDirectory(worldDirectoryPath);
    }

    public boolean isUnloaded() {
        return this.unloaded;
    }

    public File getWorldSavedDirectory() {
        return this.worldSavedDirectory;
    }

    public void setWorldSavedDirectory(String worldSavedDirectoryPath) throws IOException {
        HFileHelper.createNewDirectory(worldSavedDirectoryPath);
        this.worldSavedDirectory = (new File(worldSavedDirectoryPath)).getAbsoluteFile();
    }

    public void addPrepareDimension(String dimensionId) {
        this.prepareDimensions.add(dimensionId);
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

    public long getTick() {
        return this.tick;
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

    public Dimension loadDimension(String dimensionId) throws IOException, HElementNotRegisteredException, NoSuchMethodException {
        try {
            return this.dimensions.getElement(dimensionId);
        } catch (HElementNotRegisteredException ignore) {
        }
        if (this.unloaded)
            return null;
        Dimension dimension = new Dimension(this, DimensionUtils.getInstance().getElementInstance(dimensionId, false));
        if (HFileHelper.checkDirectoryAvailable(this.getDimensionDirectory(dimensionId)))
            dimension.readAll();
        else
            dimension.writeAll();
        dimension.loadPrepareChunks();
        try {
            this.dimensions.register(dimensionId, dimension);
        } catch (HElementRegisteredException ignore) {
        }
        return dimension;
    }

    public void unloadDimension(String dimensionId) throws IOException {
        Dimension dimension;
        try {
            dimension = this.dimensions.getElement(dimensionId);
        } catch (HElementNotRegisteredException exception) {
            return;
        }
        this.dimensions.deregisterKey(dimensionId);
        if (dimension == null)
            return;
        dimension.unloadAllChunks();
    }

    public void loadPrepareDimensions() throws IOException, HElementNotRegisteredException, NoSuchMethodException {
        if (this.unloaded)
            return;
        for (String dimensionId: this.prepareDimensions)
            this.loadDimension(dimensionId);
    }

    public void unloadAllDimensions() throws IOException {
        this.unloaded = true;
        Iterable<String> dimensions = new ArrayList<>(this.dimensions.getMap().keySet());
        for (String dimension: dimensions)
            this.unloadDimension(dimension);
        this.writeAll();
    }

    public void readAll() throws IOException, HElementNotRegisteredException, NoSuchMethodException {
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
        this.loadPrepareDimensions();
    }

    public void writeAll() throws IOException {
        HFileHelper.createNewDirectory(this.worldSavedDirectory.getPath());
        String informationFile = this.getInformationFile();
        HFileHelper.createNewFile(informationFile);
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(informationFile)));
        this.write(dataOutputStream);
        dataOutputStream.close();
        String dimensionsDirectory = this.getDimensionsDirectory();
        HFileHelper.createNewDirectory(dimensionsDirectory);
        for (Dimension dimension: this.dimensions.getMap().values())
            dimension.writeAll();
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.worldName = input.readUTF();
        if (!DSTMetaCompound.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.dst.read(input);
        this.tick = input.readLong();
        this.prepareDimensions.clear();
        String dimensionId = input.readUTF();
        while (!suffix.equals(dimensionId)) {
            this.prepareDimensions.add(dimensionId);
            dimensionId = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.worldName);
        this.dst.write(output);
        output.writeLong(this.tick);
        for (String dimensionId: this.prepareDimensions)
            output.writeUTF(dimensionId);
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
        if (o == null || this.getClass() != o.getClass()) return false;
        World world = (World) o;
        return this.tick == world.tick && this.worldName.equals(world.worldName) && this.dimensions.equals(world.dimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.worldName, this.tick);
    }
}
