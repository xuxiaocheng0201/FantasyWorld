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
import java.util.Objects;

public class DSTTagChar implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 3466972365440780620L;
    public static final String id = "Char";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagChar.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private char data;

    public DSTTagChar() {
        super();
    }

    public DSTTagChar(String name) {
        super();
        this.name = name;
    }

    public DSTTagChar(char data) {
        super();
        this.data = data;
    }

    public DSTTagChar(String name, char data) {
        super();
        this.name = name;
        this.data = data;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        this.data = input.readChar();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeChar(this.data);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public char getData() {
        return this.data;
    }

    public void setData(char data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return HStringHelper.concat("DSTTagChar{",
                "name='", this.name, '\'',
                ", data=", this.data,
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
        return Objects.hash(this.name, this.data);
    }
}
