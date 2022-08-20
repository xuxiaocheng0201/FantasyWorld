package CraftWorld.Entity.Living;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.Entity;

import java.io.Serial;
public class EntityLiving extends Entity {
    @Serial
    private static final long serialVersionUID = -3213804097843059139L;
    public static final DSTId id = DSTId.getDstIdInstance("EntityLiving");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

//    private IEntityLivingBase instance;
}
