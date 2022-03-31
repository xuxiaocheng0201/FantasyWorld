package CraftWorld.Utils;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class IDResource implements IDSTBase {
    private String mod_id = "CraftWorld";
    private String assets = "null";

    public static final String id = "IDResource";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, IDResource.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
    }

    private String name = id;

    @Override
    public String getDSTName() {
        return name;
    }

    @Override
    public void setDSTName(String name) {
        this.name = name;
    }

    public IDResource() {

    }

    public IDResource(String assets) {
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
        mod_id = input.readUTF();
        assets = input.readUTF();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(mod_id);
        output.writeUTF(assets);
    }

    public String getMod_id() {
        return mod_id;
    }

    public void setMod_id(String mod_id) {
        this.mod_id = mod_id;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return HStringHelper.merge(this.mod_id, ":", this.assets);
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
        return assets.hashCode();
    }
}
