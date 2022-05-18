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

public class DSTPureShort implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 2438260511173385249L;
    public static final String id = "DSTPureShort";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTPureShort.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private short data;

    public DSTPureShort() {
        super();
    }

    public DSTPureShort(short data) {
        super();
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readShort();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeShort(this.data);
        output.writeUTF(suffix);
    }

    public short getData() {
        return this.data;
    }

    public void setData(short data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DSTPureShort{" + this.data + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTPureShort that)) return false;
        return this.data == that.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
