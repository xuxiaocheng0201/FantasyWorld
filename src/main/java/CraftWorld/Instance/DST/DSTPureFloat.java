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

public class DSTPureFloat implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -6654553278710532324L;
    public static final String id = "DSTPureFloat";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTPureFloat.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private float data;

    public DSTPureFloat() {
        super();
    }

    public DSTPureFloat(float data) {
        super();
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.data = input.readFloat();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeFloat(this.data);
        output.writeUTF(suffix);
    }

    public float getData() {
        return this.data;
    }

    public void setData(float data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DSTPureFloat{" + this.data + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTPureFloat that)) return false;
        return Float.compare(that.data, this.data) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
