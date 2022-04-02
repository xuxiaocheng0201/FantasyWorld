package CraftWorld.Instance.DST;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DSTTagFloatMap implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -8243981388503064375L;
    public static final String id = "FloatMap";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagFloatMap.class);
        } catch (ElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final Map<String, Float> data = new HashMap<>();

    public DSTTagFloatMap() {
        super();
    }

    public DSTTagFloatMap(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        data.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            data.put(name, input.readFloat());
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (Map.Entry<String, Float> entry : data.entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeFloat(entry.getValue());
        }
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public Map<String, Float> getData() {
        return data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagFloatMap{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagFloatMap))
            return false;
        return Objects.equals(this.name, ((DSTTagFloatMap) a).name) &&
                Objects.equals(this.data, ((DSTTagFloatMap) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
