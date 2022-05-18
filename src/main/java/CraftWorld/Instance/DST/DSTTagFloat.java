package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class DSTTagFloat implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -5636635617251158912L;
    public static final String id = "Float";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagFloat.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private float data;

    public DSTTagFloat() {
        super();
    }

    public DSTTagFloat(String name) {
        super();
        this.name = name;
    }

    public DSTTagFloat(float data) {
        super();
        this.data = data;
    }

    public DSTTagFloat(String name, float data) {
        super();
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readFloat();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeFloat(this.data);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public float getData() {
        return this.data;
    }

    public void setData(float data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("DSTTagFloat{",
                "name='", this.name, '\'',
                ", data=", this.data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagFloat))
            return false;
        return Objects.equals(this.name, ((DSTTagFloat) a).name) &&
                Objects.equals(this.data, ((DSTTagFloat) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
