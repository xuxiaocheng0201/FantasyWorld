package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DSTTagIntList implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -7075392562538781129L;
    public static final String id = "IntList";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagIntList.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final List<Integer> data = new ArrayList<>();

    public DSTTagIntList() {
        super();
    }

    public DSTTagIntList(String name) {
        super();
        this.name = name;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        int length = input.readInt();
        for (int i = 0; i < length; ++i)
            this.data.add(input.readInt());
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeInt(this.data.size());
        for (int datum: this.data)
            output.writeInt(datum);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("DSTTagIntList{",
                "name='", this.name, '\'',
                ", data=", this.data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagIntList))
            return false;
        return Objects.equals(this.name, ((DSTTagIntList) a).name) &&
                this.data == ((DSTTagIntList) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}
