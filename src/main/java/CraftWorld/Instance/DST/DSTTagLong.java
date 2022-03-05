package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DSTTagLong implements IDSTBase {
    public static final String id = "Long";
    public static final String prefix = id;
    static {
        DSTUtils.getInstance().register(id, DSTTagLong.class);
    }

    private String name = "";
    private long data = 0;

    public DSTTagLong() {
        super();
    }

    public DSTTagLong(String name) {
        this.name = name;
    }

    public DSTTagLong(long data) {
        this.data = data;
    }

    public DSTTagLong(String name, long data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readLong();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeLong(this.data);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagLong{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagLong))
            return false;
        return Objects.equals(this.name, ((DSTTagLong) a).name) &&
                this.data == ((DSTTagLong) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
