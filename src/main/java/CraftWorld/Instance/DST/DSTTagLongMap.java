package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DSTTagLongMap implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 9071346361327680970L;
    public static final String id = "LongMap";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagLongMap.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final Map<String, Long> data = new HashMap<>();

    public DSTTagLongMap() {
        super();
    }

    public DSTTagLongMap(String name) {
        super();
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.data.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            this.data.put(name, input.readLong());
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (Map.Entry<String, Long> entry : this.data.entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeLong(entry.getValue());
        }
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public Map<String, Long> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagLongMap{",
                "name='", this.name, '\'',
                ", data=", this.data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagLongMap))
            return false;
        return Objects.equals(this.name, ((DSTTagLongMap) a).name) &&
                Objects.equals(this.data, ((DSTTagLongMap) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
