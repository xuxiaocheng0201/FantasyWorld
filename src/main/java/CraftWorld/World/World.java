package CraftWorld.World;

import CraftWorld.Chunk.Chunk;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Dimension.Dimension;
import CraftWorld.Dimension.DimensionUtils;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.Range;

import java.io.*;
import java.util.Map;
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

    private String worldName = "New world";
    private @Range(from = 0, to = Long.MAX_VALUE) long tick;
    private final HMapRegisterer<String, Dimension> dimensions = new HMapRegisterer<>(false);

    public World() {
        super();
    }

    public World(DataInput input) throws IOException {
        super();
        this.read(input);
    }

    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public long getTick() {
        return this.tick;
    }

    public void addDimension(Dimension dimension) {
        try {
            this.dimensions.register(dimension.getInstance().getDimensionId(), dimension);
        } catch (HElementRegisteredException ignore) {
        }
    }

    public Dimension getDimension(String id) throws IOException {
        try {
            return this.dimensions.getElement(id);
        } catch (HElementNotRegisteredException ignore) {
        }
        try {
            Dimension dimension = new Dimension(DimensionUtils.getInstance().getElementInstance(id));
            dimension.prepareChunks();
            this.addDimension(dimension);
            return dimension;
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            throw new IOException(exception);
        }
    }

    @Override
    @Deprecated
    public void read(DataInput input) throws IOException {
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    @Deprecated
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(suffix);
    }

    public void read(File worldDirectory) throws IOException {
        if (!worldDirectory.isDirectory())
            throw new IOException("Reading world need a directory file. [path='" + worldDirectory.getAbsolutePath() + "']");
        String root = worldDirectory.getAbsolutePath();
        File dimensionsDirectory = new File(root + "\\dimensions");
        if (!dimensionsDirectory.isDirectory())
            throw new IOException("Reading dimensions need a directory file. [path='" + dimensionsDirectory.getAbsolutePath() + "']");
        File[] dimensionsDirectoryFiles = dimensionsDirectory.listFiles();
        if (dimensionsDirectoryFiles == null)
            throw new IOException("Null list dimension files. [path='" + dimensionsDirectory.getAbsolutePath() + "']");
        IOException ioException = null;
        for (File dimensionDirectory: dimensionsDirectoryFiles)
            if (dimensionDirectory.isDirectory())
                try {
                    Dimension dimension = new Dimension(DimensionUtils.getInstance().getElementInstance(dimensionDirectory.getName()));
                    dimension.prepareChunks();
                    this.dimensions.register(dimensionDirectory.getName(), dimension);
                } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                    if (ioException == null)
                        ioException = new IOException("Fail to get dimension. [name='" + dimensionDirectory.getName() + "']");
                } catch (HElementRegisteredException ignore) {
                }
        if (ioException != null)
            throw ioException;
    }

    public void write(File worldDirectory) throws IOException {
        String root = worldDirectory.getAbsolutePath();
        String dimensionsDirectory = root + "\\dimensions";
        HFileHelper.createNewDirectory(dimensionsDirectory);
        for (Dimension dimension: this.dimensions.getMap().values()) {
            for (Map.Entry<ChunkPos, Chunk> entry: dimension.getLoadedChunks().entrySet())
                dimension.getNeedSaveChunks().add(entry.getValue());
            dimension.getLoadedChunks().clear();
            dimension.saveChunks();
        }
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
