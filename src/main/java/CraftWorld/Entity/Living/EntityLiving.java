package CraftWorld.Entity.Living;

import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.Entity;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.Serial;
public class EntityLiving extends Entity {
    @Serial
    private static final long serialVersionUID = -3213804097843059139L;
    public static final String id = "EntityLiving";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, EntityLiving.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private IEntityLivingBase instance;
}
