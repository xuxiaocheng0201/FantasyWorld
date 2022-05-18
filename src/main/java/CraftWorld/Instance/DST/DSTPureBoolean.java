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

public class DSTPureBoolean implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -2610545773823934027L;
    public static final String id = "DSTPureBoolean";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTPureBoolean.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private boolean data;

    public DSTPureBoolean() {
        super();
    }

    public DSTPureBoolean(boolean data) {
        super();
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readBoolean();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeBoolean(this.data);
        output.writeUTF(suffix);
    }

    public boolean getData() {
        return this.data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DSTPureBoolean{" + this.data + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTPureBoolean that)) return false;
        return this.data == that.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
