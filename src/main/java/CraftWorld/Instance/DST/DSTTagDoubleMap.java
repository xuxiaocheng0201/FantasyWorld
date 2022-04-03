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

public class DSTTagDoubleMap implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 8202012369131060971L;
    public static final String id = "DoubleMap";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagDoubleMap.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final Map<String, Double> data = new HashMap<>();

    public DSTTagDoubleMap() {
        super();
    }

    public DSTTagDoubleMap(String name) {
        super();
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.data.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            this.data.put(name, input.readDouble());
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (Map.Entry<String, Double> entry : this.data.entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeDouble(entry.getValue());
        }
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public Map<String, Double> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagDoubleMap{",
                "name='", this.name, '\'',
                ", data=", this.data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagDoubleMap))
            return false;
        return Objects.equals(this.name, ((DSTTagDoubleMap) a).name) &&
                Objects.equals(this.data, ((DSTTagDoubleMap) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
