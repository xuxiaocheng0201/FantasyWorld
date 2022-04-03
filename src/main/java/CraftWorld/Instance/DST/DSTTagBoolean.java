package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class DSTTagBoolean implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 7888565642586987396L;
    public static final String id = "Boolean";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagBoolean.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private boolean data;

    public DSTTagBoolean() {
        super();
    }

    public DSTTagBoolean(String name) {
        super();
        this.name = name;
    }

    public DSTTagBoolean(boolean data) {
        super();
        this.data = data;
    }

    public DSTTagBoolean(String name, boolean data) {
        super();
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
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public boolean getData() {
        return this.data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagBoolean{",
                "name='", this.name, '\'',
                ", data=", this.data,
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
        return Objects.hash(this.name, this.data);
    }
}
