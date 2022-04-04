package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class DSTTagString implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -4888822638770534140L;
    public static final String id = "String";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagString.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private String data = "";

    public DSTTagString() {
        super();
    }

    public DSTTagString(String data) {
        super();
        this.data = data;
    }

    public DSTTagString(String name, String data) {
        super();
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

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("DSTTagString{",
                "name='", this.name, '\'',
                ", data='", this.data, '\'',
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
        return Objects.hash(this.name, this.data);
    }
}
