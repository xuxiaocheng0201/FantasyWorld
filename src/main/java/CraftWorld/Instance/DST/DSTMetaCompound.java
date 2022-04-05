package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;

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
    public static final String id = "Compound";
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
    public void read(DataInput input) throws IOException {
        this.dstMap.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            IDSTBase dst = null;
            try {
                dst = DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()));
            } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                HLog.logger(HLogLevel.ERROR, exception);
            }
            if (dst != null)
                dst.read(input);
            this.dstMap.put(name, dst);
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
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
        return HStringHelper.concat("DSTMetaCompound{",
                "name='", this.name, '\'',
                ", dstMap=", this.dstMap,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTMetaCompound))
            return false;
        return Objects.equals(this.name, ((DSTMetaCompound) a).name) &&
                Objects.equals(this.dstMap, ((DSTMetaCompound) a).dstMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.dstMap);
    }
}
