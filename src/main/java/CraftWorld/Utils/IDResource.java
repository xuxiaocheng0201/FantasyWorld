package CraftWorld.Utils;

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
import java.util.Objects;

public class IDResource implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -2821429578891939306L;

    private String mod_id = "CraftWorld";
    private String assets = "null";

    public static final String id = "IDResource";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, IDResource.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IDResource() {
        super();

    }

    public IDResource(String assets) {
        super();
        if (assets == null)
            return;
        if (!assets.contains(":")) {
            this.assets = assets;
            return;
        }
        String[] s = assets.split(":");
        this.mod_id = s[0];
        this.assets = assets.substring(s[0].length() + 1);
    }

    public IDResource(String mod_id, String assets) {
        this(assets);
        if (mod_id == null)
            return;
        this.mod_id = mod_id;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.mod_id = input.readUTF();
        this.assets = input.readUTF();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.mod_id);
        output.writeUTF(this.assets);
    }

    public String getMod_id() {
        return this.mod_id;
    }

    public void setMod_id(String mod_id) {
        this.mod_id = mod_id;
    }

    public String getAssets() {
        return this.assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return HStringHelper.concat(this.mod_id, ":", this.assets);
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof IDResource))
            return false;
        return Objects.equals(this.mod_id, ((IDResource) a).mod_id) &&
                Objects.equals(this.assets, ((IDResource) a).assets);
    }

    @Override
    public int hashCode() {
        return this.assets.hashCode();
    }
}
