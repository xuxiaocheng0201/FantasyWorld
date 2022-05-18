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

public class DSTTagByte implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -9182691971751276790L;
    public static final String id = "Byte";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagByte.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private byte data;

    public DSTTagByte() {
        super();
    }

    public DSTTagByte(String name) {
        super();
        this.name = name;
    }

    public DSTTagByte(byte data) {
        super();
        this.data = data;
    }

    public DSTTagByte(String name, byte data) {
        super();
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readByte();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeByte(this.data);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public byte getData() {
        return this.data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("DSTTagByte{",
                "name='", this.name, '\'',
                ", data=", this.data,
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
        return Objects.hash(this.name, this.data);
    }
}
