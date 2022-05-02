package CraftWorld.Entity;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.DST.DSTMetaCompound;
import CraftWorld.World.Dimension.Dimension;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class Entity implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1559268221345770411L;
    public static final String id = "Entity";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, Entity.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private Dimension dimension;
    private EntityPos pos;
    private IEntityBase instance;




    @Override
    public void read(DataInput input) throws IOException {
        try {
            this.instance = EntityUtils.getInstance().getElementInstance(input.readUTF(), false);
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            //this.instance;TODO
        }
        if (!EntityPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.pos.read(input);
        this.instance.setEntityName(input.readUTF());
        if (!DSTMetaCompound.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.instance.getEntityDST().read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.instance.getEntityId());
        this.pos.write(output);
        output.writeUTF(this.instance.getEntityName());
        this.instance.getEntityDST().write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "pos=" + this.pos +
                ", instance=" + this.instance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return this.pos.equals(entity.pos) && this.instance.equals(entity.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pos, this.instance);
    }
}
