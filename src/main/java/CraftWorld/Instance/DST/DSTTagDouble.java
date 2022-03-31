package CraftWorld.Instance.DST;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DSTTagDouble implements IDSTBase {
    public static final String id = "Double";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagDouble.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
    }

    private String name = "";
    private double data = 0;

    public DSTTagDouble() {
        super();
    }

    public DSTTagDouble(String name) {
        this.name = name;
    }

    public DSTTagDouble(double data) {
        this.data = data;
    }

    public DSTTagDouble(String name, double data) {
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
        return name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagDouble{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagDouble))
            return false;
        return Objects.equals(this.name, ((DSTTagDouble) a).name) &&
                this.data == ((DSTTagDouble) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
