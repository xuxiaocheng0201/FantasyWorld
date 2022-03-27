package CraftWorld.Instance.DST;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DSTTagString implements IDSTBase {
    public static final String id = "String";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagString.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
    }

    private String name = "";
    private String data = "";

    public DSTTagString() {
        super();
    }

    public DSTTagString(String data) {
        this.data = data;
    }

    public DSTTagString(String name, String data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readUTF();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeUTF(this.data);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagString{",
                "name='", name, '\'',
                ", data='", data, '\'',
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagString))
            return false;
        return Objects.equals(this.name, ((DSTTagString) a).name) &&
                Objects.equals(this.data, ((DSTTagString) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
