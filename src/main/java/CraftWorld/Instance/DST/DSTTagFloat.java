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

public class DSTTagFloat implements IDSTBase {
    public static final String id = "Float";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagFloat.class);
        } catch (ElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private float data = 0;

    public DSTTagFloat() {
        super();
    }

    public DSTTagFloat(String name) {
        this.name = name;
    }

    public DSTTagFloat(float data) {
        this.data = data;
    }

    public DSTTagFloat(String name, float data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readFloat();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeFloat(this.data);
    }

    public String getDSTName() {
        return name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagFloat{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagFloat))
            return false;
        return Objects.equals(this.name, ((DSTTagFloat) a).name) &&
                this.data == ((DSTTagFloat) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
