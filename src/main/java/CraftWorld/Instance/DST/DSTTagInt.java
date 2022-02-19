package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DSTTagInt implements IDSTBase {
    public static final String id = "Int";
    public static final String prefix = id;
    static {
        DSTUtils.register(id, DSTTagInt.class);
    }

    private String name = "";
    private int data = 0;

    public DSTTagInt() {
        super();
    }

    public DSTTagInt(String name) {
        this.name = name;
    }

    public DSTTagInt(int data) {
        this.data = data;
    }

    public DSTTagInt(String name, int data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readInt();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeInt(this.data);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagInt{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagInt))
            return false;
        return Objects.equals(this.name, ((DSTTagInt) a).name) &&
                this.data == ((DSTTagInt) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
