package CraftWorld.Instance.DST;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DSTTagShort implements IDSTBase {
    public static final String id = "Short";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagShort.class);
        } catch (ElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private short data = 0;

    public DSTTagShort() {
        super();
    }

    public DSTTagShort(String name) {
        this.name = name;
    }

    public DSTTagShort(short data) {
        this.data = data;
    }

    public DSTTagShort(String name, short data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readShort();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeShort(this.data);
    }

    public String getDSTName() {
        return name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public short getData() {
        return data;
    }

    public void setData(short data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagShort{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagShort))
            return false;
        return Objects.equals(this.name, ((DSTTagShort) a).name) &&
                this.data == ((DSTTagShort) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
