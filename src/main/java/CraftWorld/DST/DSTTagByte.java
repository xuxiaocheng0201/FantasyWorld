package CraftWorld.DST;

import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DSTTagByte implements IDSTBase {
    public static final String id = "Byte";
    public static final String prefix = id;
    static {
        DSTUtils.register(id, DSTTagByte.class);
    }

    private String name = "";
    private byte data = 0;

    public DSTTagByte() {
        super();
    }

    public DSTTagByte(String name) {
        this.name = name;
    }

    public DSTTagByte(byte data) {
        this.data = data;
    }

    public DSTTagByte(String name, byte data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readByte();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeByte(this.data);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagByte{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagByte))
            return false;
        return Objects.equals(this.name, ((DSTTagByte) a).name) &&
                this.data == ((DSTTagByte) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
