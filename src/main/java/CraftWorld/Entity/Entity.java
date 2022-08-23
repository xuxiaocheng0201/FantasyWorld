package CraftWorld.Entity;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Entity.BasicInformation.EntityId;
import CraftWorld.Instance.Entity.EntityItem;
import CraftWorld.Utils.Angle;
import CraftWorld.Utils.QuickTick;
import CraftWorld.World.Dimension.Dimension;
import HeadLibs.Helper.HRandomHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected final @NotNull Dimension dimension;
    protected final @NotNull EntityPos pos = new EntityPos();
    protected final @NotNull EntityPos lastTickPos = new EntityPos();
    protected final @NotNull QuickTick tickHasExist = new QuickTick();
    protected final @NotNull QuickTick tickHasUpdated = new QuickTick();
    protected final @NotNull Angle facingX = new Angle();
    protected final @NotNull Angle facingY = new Angle();
    protected final @NotNull Angle facingZ = new Angle();
    protected @NotNull IEntityBase instance = new EntityItem(); {this.instance.setExistingEntityInstance(this);}
    protected double speedX;
    protected double speedY;
    protected double speedZ;
    protected double accelerateX;
    protected double accelerateY;
    protected double accelerateZ;

    public Entity(@NotNull Dimension dimension) {
        super();
        this.dimension = dimension;
    }

    public Entity(@NotNull Dimension dimension, @Nullable IEntityBase instance) {
        super();
        this.dimension = dimension;
        if (instance != null)
            this.setInstance(instance);
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    public @NotNull Dimension getDimension() {
        return this.dimension;
    }

    public @NotNull EntityPos getPos() {
        return this.pos;
    }

    public @NotNull EntityPos getLastTickPos() {
        return this.lastTickPos;
    }

    public @NotNull QuickTick getTickHasExist() {
        return this.tickHasExist;
    }

    public @NotNull QuickTick getTickHasUpdated() {
        return this.tickHasUpdated;
    }

    public @NotNull IEntityBase getInstance() {
        return this.instance;
    }

    public void setInstance(@Nullable IEntityBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, EntityItem::new);
        this.instance.setExistingEntityInstance(this);
    }

    public double getSpeedX() {
        return this.speedX;
    }

    public double getSpeedY() {
        return this.speedY;
    }

    public double getSpeedZ() {
        return this.speedZ;
    }

    public double getAccelerateX() {
        return this.accelerateX;
    }

    public double getAccelerateY() {
        return this.accelerateY;
    }

    public double getAccelerateZ() {
        return this.accelerateZ;
    }

    public void setAccelerateX(double accelerateX) {
        this.accelerateX = accelerateX;
    }

    public void setAccelerateY(double accelerateY) {
        this.accelerateY = accelerateY;
    }

    public void setAccelerateZ(double accelerateZ) {
        this.accelerateZ = accelerateZ;
    }

    public @NotNull Angle getFacingX() {
        return this.facingX;
    }

    public @NotNull Angle getFacingY() {
        return this.facingY;
    }

    public @NotNull Angle getFacingZ() {
        return this.facingZ;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.uuid = new UUID(input.readLong(), input.readLong());
        if (!EntityPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.pos.read(input);
        EntityId entityId = new EntityId();
        if (!EntityId.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        entityId.read(input);
        try {
            this.setInstance(EntityUtils.getInstance().getElementInstance(entityId, false));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.setInstance(null);
        }
        this.instance.read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeLong(this.uuid.getMostSignificantBits());
        output.writeLong(this.uuid.getLeastSignificantBits());
        this.pos.write(output);
        this.instance.write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "uuid=" + this.uuid +
                ", dimension=" + this.dimension +
                ", pos=" + this.pos +
                ", lastTickPos=" + this.lastTickPos +
                ", tickHasExist=" + this.tickHasExist +
                ", tickHasUpdated=" + this.tickHasUpdated +
                ", instance=" + this.instance +
                ", speedX=" + this.speedX +
                ", speedY=" + this.speedY +
                ", speedZ=" + this.speedZ +
                ", accelerateX=" + this.accelerateX +
                ", accelerateY=" + this.accelerateY +
                ", accelerateZ=" + this.accelerateZ +
                ", facingX=" + this.facingX +
                ", facingY=" + this.facingY +
                ", facingZ=" + this.facingZ +
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
