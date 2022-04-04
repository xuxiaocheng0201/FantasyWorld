package CraftWorld.Dimension;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Dimensions.EarthSurfaceDimension;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Dimension implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -4936855319467494864L;

    private IDimensionBase instance;

    public static final String id = "Dimension";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, Dimension.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;

    @Override
    public String getDSTName() {
        return this.name;
    }

    @Override
    public void setDSTName(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        String name = input.readUTF();
        if ("null".equals(name)) {
            this.instance = null;
            return;
        }
        try {
            this.instance = DimensionUtils.getInstance().getElementInstance(DimensionUtils.dePrefix(name));
        } catch (NoSuchElementException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
        if (this.instance == null)
            this.instance = new EarthSurfaceDimension();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        if (this.instance == null) {
            output.writeUTF("null");
            return;
        }
        output.writeUTF(this.instance.getDimensionName());
    }

    public IDimensionBase getInstance() {
        return this.instance;
    }

    public void setInstance(IDimensionBase instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("Dimension{",
                "name=", (this.instance == null)? "null" : this.instance.getDimensionName(),
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof Dimension))
            return false;
        return Objects.equals(this.instance, ((Dimension) a).instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.instance);
    }
}
