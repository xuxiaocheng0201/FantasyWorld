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

public class DSTPureLong implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -8391379240891769836L;
    public static final String id = "DSTPureLong";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTPureLong.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private long data;

    public DSTPureLong() {
        super();
    }

    public DSTPureLong(long data) {
        super();
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readLong();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeLong(this.data);
        output.writeUTF(suffix);
    }

    public long getData() {
        return this.data;
    }

    public void setData(long data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DSTPureLong{" + this.data + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTPureLong that)) return false;
        return this.data == that.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
