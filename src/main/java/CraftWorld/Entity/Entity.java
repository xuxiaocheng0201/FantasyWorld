package CraftWorld.Entity;

import CraftWorld.CraftWorld;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Entity.BasicInformation.EntityId;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.Instance.Entity.EntityHuman;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Dimension.Dimension;
import HeadLibs.Helper.HRandomHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

public class Entity implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1559268221345770411L;
    public static final DSTId id = DSTId.getDstIdInstance("Entity");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull UUID uuid = HRandomHelper.getRandomUUID();
    protected @NotNull Dimension dimension = new Dimension(CraftWorld.getInstance().getWorld());
    protected @NotNull EntityPos pos = new EntityPos();
    protected @NotNull EntityPos lastTickPos = new EntityPos();
    protected @NotNull QuickTick tickHasExist = new QuickTick();
    protected @NotNull QuickTick tickHasUpdated = new QuickTick();
    protected @NotNull IEntityBase instance = new EntityHuman();
    protected double speedX;
    protected double speedY;
    protected double speedZ;
    protected double accelerateX;
    protected double accelerateY;
    protected double accelerateZ;


    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.uuid = new UUID(input.readLong(), input.readLong());
        EntityId entityId = new EntityId();
        if (!EntityId.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        entityId.read(input);
        try {
            this.instance = EntityUtils.getInstance().getElementInstance(entityId, false);
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            //this.instance;TODO
        }
        if (!EntityPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.pos.read(input);
        this.instance.setEntityName(input.readUTF());
        if (!DSTComplexMeta.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.instance.getEntityDST().read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeLong(this.uuid.getMostSignificantBits());
        output.writeLong(this.uuid.getLeastSignificantBits());
        this.instance.getEntityId().write(output);
        this.pos.write(output);
        output.writeUTF(this.instance.getEntityName());
        this.instance.getEntityDST().write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "uuid=" + this.uuid +
                ", dimension=" + this.dimension +
                ", pos=" + this.pos +
                ", tickHasExist=" + this.tickHasExist +
                ", tickHasUpdated=" + this.tickHasUpdated +
                ", instance=" + this.instance +
                ", speedX=" + this.speedX +
                ", speedY=" + this.speedY +
                ", speedZ=" + this.speedZ +
                ", accelerateX=" + this.accelerateX +
                ", accelerateY=" + this.accelerateY +
                ", accelerateZ=" + this.accelerateZ +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity entity)) return false;
        return this.uuid.equals(entity.uuid) && this.dimension.equals(entity.dimension) && this.pos.equals(entity.pos) && this.tickHasExist.equals(entity.tickHasExist) && this.instance.equals(entity.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid);
    }
}
