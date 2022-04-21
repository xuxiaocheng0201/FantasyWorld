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

public class DSTTagShortMap implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -8133206058269937530L;
    public static final String id = "ShortMap";
    public static final String prefix = IDSTBase.prefix(id);
    public static final String suffix = IDSTBase.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagShortMap.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final Map<String, Short> data = new HashMap<>();

    public DSTTagShortMap() {
        super();
    }

    public DSTTagShortMap(String name) {
        super();
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.data.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            this.data.put(name, input.readShort());
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (Map.Entry<String, Short> entry : this.data.entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeShort(entry.getValue());
        }
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public Map<String, Short> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("DSTTagShortMap{",
                "name='", this.name, '\'',
                ", data=", this.data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagShortMap))
            return false;
        return Objects.equals(this.name, ((DSTTagShortMap) a).name) &&
                Objects.equals(this.data, ((DSTTagShortMap) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
