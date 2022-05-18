package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class DSTPureByte implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -4917521423532940232L;
    public static final String id = "DSTPureByte";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTPureByte.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private byte data;

    public DSTPureByte() {
        super();
    }

    public DSTPureByte(byte data) {
        super();
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readByte();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeByte(this.data);
        output.writeUTF(suffix);
    }

    public byte getData() {
        return this.data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DSTPureByte{" + this.data + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTPureByte that)) return false;
        return this.data == that.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
