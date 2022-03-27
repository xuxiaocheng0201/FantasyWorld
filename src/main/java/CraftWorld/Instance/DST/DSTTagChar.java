package CraftWorld.Instance.DST;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DSTTagChar implements IDSTBase {
    public static final String id = "Char";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagChar.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
    }

    private String name = "";
    private char data = 0;

    public DSTTagChar() {
        super();
    }

    public DSTTagChar(String name) {
        this.name = name;
    }

    public DSTTagChar(char data) {
        this.data = data;
    }

    public DSTTagChar(String name, char data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readChar();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeChar(this.data);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTTagChar{",
                "name='", name, '\'',
                ", data=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTTagChar))
            return false;
        return Objects.equals(this.name, ((DSTTagChar) a).name) &&
                this.data == ((DSTTagChar) a).data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
