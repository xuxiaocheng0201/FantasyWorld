package CraftWorld.Instance.DST;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DSTTagBooleanList implements IDSTBase {
    public static final String id = "BooleanList";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagBooleanList.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
    }

    private String name = id;
    private final List<Boolean> data = new ArrayList<>();

    public DSTTagBooleanList() {
        super();
    }

    public DSTTagBooleanList(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        int length = input.readInt();
        for (int i = 0; i < length; ++i)
            this.data.add(input.readBoolean());
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeInt(this.data.size());
        for (boolean datum: this.data)
            output.writeBoolean(datum);
    }

    public String getDSTName() {
        return name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public List<Boolean> getData() {
        return data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagBooleanList{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagBooleanList))
            return false;
        return Objects.equals(this.name, ((DSTTagBooleanList) a).name) &&
                this.data == ((DSTTagBooleanList) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
