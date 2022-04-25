package CraftWorld.World;

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
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unused")
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
    private String worldName = "New world";
    private final DSTMetaCompound dst = new DSTMetaCompound();
    private @Range(from = 0, to = Long.MAX_VALUE) long tick;
    private final HMapRegisterer<String, Dimension> dimensions = new HMapRegisterer<>(false);

    public World() {
        super();
    }

    public World(String worldDirectoryPath) throws IOException {
        super();
        this.setWorldSavedDirectory(worldDirectoryPath);
    }

    public File getWorldSavedDirectory() {
        return this.worldSavedDirectory;
    }

    public void setWorldSavedDirectory(String worldSavedDirectoryPath) throws IOException {
        HFileHelper.createNewDirectory(worldSavedDirectoryPath);
        this.worldSavedDirectory = new File(worldSavedDirectoryPath);
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

    public void addDimension(String dimensionId) throws HElementNotRegisteredException, NoSuchMethodException {
        try {
            this.dimensions.register(dimensionId, new Dimension(this.worldSavedDirectory + "\\dimensions\\" + dimensionId,
                    DimensionUtils.getInstance().getElementInstance(dimensionId, false)));
        } catch (HElementRegisteredException ignore) {
        }
    }

    public void addDimension(Dimension dimension) throws HElementRegisteredException {
        this.dimensions.register(dimension.getInstance().getDimensionId(), dimension);
    }

    public void addDimensionForce(Dimension dimension) {
        this.dimensions.reset(dimension.getInstance().getDimensionId(), dimension);
    }

    public Dimension loadDimension(String dimensionId) throws HElementNotRegisteredException, NoSuchMethodException {
        try {
            return this.dimensions.getElement(dimensionId);
        } catch (HElementNotRegisteredException ignore) {
        }
        Dimension dimension = new Dimension(this.worldSavedDirectory + "\\dimensions\\" + dimensionId,
                DimensionUtils.getInstance().getElementInstance(dimensionId, false));
        try {
            this.addDimension(dimension);
        } catch (HElementRegisteredException ignore) {
        }
        return dimension;
    }

    public void unloadDimension(String dimensionId) {
        Dimension dimension;
        try {
            dimension = this.dimensions.getElement(dimensionId);
        } catch (HElementNotRegisteredException exception) {
            return;
        }
        this.dimensions.deregisterKey(dimensionId);
        dimension.unloadAllChunks();
    }

    public void unloadAllDimensions() {
        this.unloaded = true;
        Set<String> dimensions = this.dimensions.getMap().keySet();
        for (String dimension: dimensions)
            this.unloadDimension(dimension);
    }

    public void read(String worldDirectoryPath) throws IOException {
        File worldDirectory = new File(worldDirectoryPath);
        if (!HFileHelper.checkDirectoryAvailable(worldDirectoryPath))
            throw new IOException("Reading world need a directory file. [worldDirectory='" + worldDirectory.getAbsolutePath() + "']");
        String root = worldDirectory.getAbsolutePath();
        String dimensionsDirectory = root + "\\dimensions";
        if (!HFileHelper.checkDirectoryAvailable(dimensionsDirectory))
            throw new IOException("Reading dimensions need a directory file. [dimensionsDirectory='" + dimensionsDirectory + "']");
        File[] dimensionsDirectoryFiles = (new File(dimensionsDirectory)).listFiles();
        if (dimensionsDirectoryFiles == null)
            throw new IOException("Null list dimension files. [dimensionsDirectory='" + dimensionsDirectory + "']");
        IOException ioException = null;
        for (File dimensionDirectory: dimensionsDirectoryFiles)
            if (dimensionDirectory.isDirectory())
                try {
                    Dimension dimension = new Dimension(dimensionDirectory.getAbsolutePath(), DimensionUtils.getInstance().getElementInstance(dimensionDirectory.getName(), false));
                    dimension.prepareChunks();
                    try {
                        this.dimensions.register(dimensionDirectory.getName(), dimension);
                    } catch (HElementRegisteredException ignore) {
                    }
                } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                    if (ioException == null)
                        ioException = new IOException("Fail to get dimension. [name='" + dimensionDirectory.getName() + "']");
                }
        if (ioException != null)
            throw ioException;
    }

    public void write(String worldDirectoryPath) throws IOException {
        File worldDirectory = new File(worldDirectoryPath);
        String root = worldDirectory.getAbsolutePath();
        HFileHelper.createNewDirectory(root);
        String dimensionsDirectory = root + "\\dimensions";
        HFileHelper.createNewDirectory(dimensionsDirectory);
        this.unloadAllDimensions();
    }

    @Override
    public void read(DataInput input) throws IOException {

    }

    @Override
    public void write(DataOutput output) throws IOException {

    }

    @Override
    public String toString() {
        return "World{" +
                "worldName='" + this.worldName + '\'' +
                ", tick=" + this.tick +
                ", dimensions=" + this.dimensions +
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
