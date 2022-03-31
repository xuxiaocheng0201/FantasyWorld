package CraftWorld.Instance.DST;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DSTTagBoolean implements IDSTBase {
    public static final String id = "Boolean";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagBoolean.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
    }

    private String name = "";
    private boolean data = false;

    public DSTTagBoolean() {
        super();
    }

    public DSTTagBoolean(String name) {
        this.name = name;
    }

    public DSTTagBoolean(boolean data) {
        this.data = data;
    }

    public DSTTagBoolean(String name, boolean data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readBoolean();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeBoolean(this.data);
    }

    public String getDSTName() {
        return name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public boolean getData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagBoolean{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagBoolean))
            return false;
        return Objects.equals(this.name, ((DSTTagBoolean) a).name) &&
                this.data == ((DSTTagBoolean) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
