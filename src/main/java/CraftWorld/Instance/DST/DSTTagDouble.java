package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.ElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class DSTTagDouble implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -5332255853117667126L;
    public static final String id = "Double";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagDouble.class);
        } catch (ElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private double data;

    public DSTTagDouble() {
        super();
    }

    public DSTTagDouble(String name) {
        super();
        this.name = name;
    }

    public DSTTagDouble(double data) {
        super();
        this.data = data;
    }

    public DSTTagDouble(String name, double data) {
        super();
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readDouble();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeDouble(this.data);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public double getData() {
        return this.data;
    }

    public void setData(double data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagDouble{",
                "name='", this.name, '\'',
                ", data=", this.data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagDouble))
            return false;
        return Objects.equals(this.name, ((DSTTagDouble) a).name) &&
                Objects.equals(this.data, ((DSTTagDouble) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
