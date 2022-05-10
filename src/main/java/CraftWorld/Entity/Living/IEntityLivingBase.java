package CraftWorld.Entity.Living;

import CraftWorld.Entity.Entity;
import CraftWorld.Entity.IEntityBase;

import java.util.List;

public interface IEntityLivingBase extends IEntityBase {
    boolean isDead();
    List<Entity> riddenOnEntities();
    List<Entity> riddenByEntities();
}
