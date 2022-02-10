package CraftWorld.DST;

import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DSTTagDoubleArray implements IDSTBase {
    public static final String id = "DoubleArray";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        DSTUtils.register(id, DSTTagDoubleArray.class);
    }

    private String name = "";
    private final Map<String, Double> data = new HashMap<>();

    public DSTTagDoubleArray() {
        super();
    }

    public DSTTagDoubleArray(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        data.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            data.put(name, input.readDouble());
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (String name: data.keySet()) {
            output.writeUTF(name);
            output.writeDouble(data.get(name));
        }
        output.writeUTF(suffix);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getData() {
        return data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagDoubleArray{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagDoubleArray))
            return false;
        return Objects.equals(this.name, ((DSTTagDoubleArray) a).name) &&
                Objects.equals(this.data, ((DSTTagDoubleArray) a).data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
