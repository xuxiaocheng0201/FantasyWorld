package CraftWorld.Utils;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class IDResource implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -2821429578891939306L;
    public static final DSTId id = DSTId.getDstIdInstance("IDResource");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull String mod_id = "CraftWorld";
    protected @NotNull String assets = "null";

    protected @NotNull String name = id.getId();

    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public IDResource() {
        super();
    }

    public IDResource(@Nullable String assets) {
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

    public IDResource(@Nullable String mod_id, @Nullable String assets) {
        this(assets);
        if (mod_id == null)
            return;
        this.mod_id = mod_id;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.mod_id = input.readUTF();
        this.assets = input.readUTF();
        if (!suffix.equals(input.readUTF()))
                throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.mod_id);
        output.writeUTF(this.assets);
        output.writeUTF(suffix);
    }

    public @NotNull String getMod_id() {
        return this.mod_id;
    }

    public void setMod_id(@NotNull String mod_id) {
        this.mod_id = mod_id;
    }

    public @NotNull String getAssets() {
        return this.assets;
    }

    public void setAssets(@NotNull String assets) {
        this.assets = assets;
    }

    @Override
    public @NotNull String toString() {
        return this.mod_id + ':' + this.assets;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof IDResource that)) return false;
        return this.mod_id.equals(that.mod_id) && this.assets.equals(that.assets) && this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mod_id, this.assets, this.name);
    }
}
