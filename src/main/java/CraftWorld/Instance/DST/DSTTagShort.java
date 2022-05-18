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

public class DSTTagShort implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -3389927232137215741L;
    public static final String id = "Short";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagShort.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private short data;

    public DSTTagShort() {
        super();
    }

    public DSTTagShort(String name) {
        super();
        this.name = name;
    }

    public DSTTagShort(short data) {
        super();
        this.data = data;
    }

    public DSTTagShort(String name, short data) {
        super();
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readShort();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeShort(this.data);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public short getData() {
        return this.data;
    }

    public void setData(short data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("DSTTagShort{",
                "name='", this.name, '\'',
                ", data=", this.data,
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
        return Objects.hash(this.name, this.data);
    }
}
