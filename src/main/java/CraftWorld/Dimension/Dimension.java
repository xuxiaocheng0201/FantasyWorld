package CraftWorld.Dimension;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Dimensions.EarthSurfaceDimension;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.*;
import java.util.Objects;

public class Dimension implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -4936855319467494864L;
    public static final String id = "Dimension";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, Dimension.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private File saveFilePath;

    public File getSaveFilePath() {
        return this.saveFilePath;
    }

    public void setSaveFilePath(File saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    private IDimensionBase instance = new EarthSurfaceDimension();

    public Dimension() {
        super();
    }

    public Dimension(IDimensionBase instance) {
        super();
        this.instance = Objects.requireNonNullElseGet(instance, EarthSurfaceDimension::new);
    }

    public IDimensionBase getInstance() {
        return this.instance;
    }

    public void setInstance(IDimensionBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, EarthSurfaceDimension::new);
    }

    @Override
    public void read(DataInput input) throws IOException {
        try {
            this.instance = DimensionUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.instance = new EarthSurfaceDimension();
        }
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.instance.getDimensionName());
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "instance=" + this.instance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Dimension dimension = (Dimension) o;
        return this.instance.equals(dimension.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.instance);
    }
}
