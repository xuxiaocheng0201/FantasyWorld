package CraftWorld.World;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Dimension.Dimension;
import CraftWorld.Dimension.DimensionUtils;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HSetRegisterer;
import org.jetbrains.annotations.Range;

import java.io.*;

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
    private final HSetRegisterer<Dimension> dimensions = new HSetRegisterer<>();

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

    @Override
    @Deprecated
    public void read(DataInput input) throws IOException {
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
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
                    dimension.setSaveFilePath(dimensionDirectory);
                    this.dimensions.register(dimension);
                } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                    if (ioException == null)
                        ioException = new IOException("Fail to get dimension. [name='" + dimensionDirectory.getName() + "']");
                } catch (HElementRegisteredException ignore) {
                }
        if (ioException != null)
            throw ioException;
    }

    @Override
    @Deprecated
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(suffix);
    }

    public void write(File worldDirectory) throws IOException {
        if (!worldDirectory.isDirectory())
            throw new IOException("Reading world need a directory file. [path=\"" + worldDirectory.getAbsolutePath() + "\"].");
        String root = worldDirectory.getAbsolutePath();
        File dimensionsDirectory = new File(root + "\\dimensions");
        for (Dimension dimension: this.dimensions.getSet())
            dimension.setSaveFilePath(new File(dimensionsDirectory + "\\" + dimension.getInstance().getDimensionId()));
        //TODO
    }
}
