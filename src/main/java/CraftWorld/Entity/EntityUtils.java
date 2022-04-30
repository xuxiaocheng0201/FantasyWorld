package CraftWorld.Entity;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;

@NewElementUtilCore(modName = "CraftWorld", elementName = "Entity")
public class EntityUtils extends ElementUtil<IEntityBase> {
    private static final EntityUtils instance = new EntityUtils();
    public static EntityUtils getInstance() {
        return instance;
    }
}
