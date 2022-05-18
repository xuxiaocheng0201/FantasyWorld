package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DSTMetaCompound implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 5912925151440251840L;
    public static final String id = "DSTMetaCompound";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTMetaCompound.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final Map<String, IDSTBase> dstMap = new HashMap<>();

    public DSTMetaCompound() {
        super();
    }

    public DSTMetaCompound(String name) {
        super();
        this.name = name;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.dstMap.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            IDSTBase dst;
            try {
                dst = DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()), false);
            } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                throw new DSTFormatException(exception);
            }
            dst.read(input);
            this.dstMap.put(name, dst);
            name = input.readUTF();
        }
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        for (Map.Entry<String, IDSTBase> entry : this.dstMap.entrySet()) {
            output.writeUTF(entry.getKey());
            entry.getValue().write(output);
        }
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public Map<String, IDSTBase> getDstMap() {
        return this.dstMap;
    }

    @Override
    public String toString() {
        return "DSTMetaCompound{" +
                "name='" + this.name + '\'' +
                ", dstMap=" + this.dstMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        DSTMetaCompound that = (DSTMetaCompound) o;
        return Objects.equals(this.name, that.name) && this.dstMap.equals(that.dstMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.dstMap);
    }
}
