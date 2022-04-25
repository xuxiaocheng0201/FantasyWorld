package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DSTTagByteMap implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -2298584734926981952L;
    public static final String id = "ByteMap";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagByteMap.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final Map<String, Byte> data = new HashMap<>();

    public DSTTagByteMap() {
        super();
    }

    public DSTTagByteMap(String name) {
        super();
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.data.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            this.data.put(name, input.readByte());
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (Map.Entry<String, Byte> entry : this.data.entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeByte(entry.getValue());
        }
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public Map<String, Byte> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("DSTTagByteMap{",
                "name='", this.name, '\'',
                ", data=", this.data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagByteMap))
            return false;
        return Objects.equals(this.name, ((DSTTagByteMap) a).name) &&
                Objects.equals(this.data, ((DSTTagByteMap) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
