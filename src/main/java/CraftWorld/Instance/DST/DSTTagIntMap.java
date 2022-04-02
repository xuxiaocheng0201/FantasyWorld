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

public class DSTTagIntMap implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1688961670765410436L;
    public static final String id = "IntMap";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagIntMap.class);
        } catch (ElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final Map<String, Integer> data = new HashMap<>();

    public DSTTagIntMap() {
        super();
    }

    public DSTTagIntMap(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        data.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            data.put(name, input.readInt());
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeInt(entry.getValue());
        }
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagIntMap{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagIntMap))
            return false;
        return Objects.equals(this.name, ((DSTTagIntMap) a).name) &&
                Objects.equals(this.data, ((DSTTagIntMap) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
