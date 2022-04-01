package CraftWorld.Instance.DST;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DSTTagBooleanMap implements IDSTBase {
    public static final String id = "BooleanMap";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagBooleanMap.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
    }

    private String name = id;
    private final Map<String, Boolean> data = new HashMap<>();

    public DSTTagBooleanMap() {
        super();
    }

    public DSTTagBooleanMap(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        data.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            data.put(name, input.readBoolean());
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (String name: data.keySet()) {
            output.writeUTF(name);
            output.writeBoolean(data.get(name));
        }
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public Map<String, Boolean> getData() {
        return data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagBooleanMap{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagBooleanMap))
            return false;
        return Objects.equals(this.name, ((DSTTagBooleanMap) a).name) &&
                Objects.equals(this.data, ((DSTTagBooleanMap) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
