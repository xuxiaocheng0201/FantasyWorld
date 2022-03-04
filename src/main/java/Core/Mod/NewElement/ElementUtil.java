package Core.Mod.NewElement;

import java.util.HashMap;
import java.util.Map;

public interface ElementUtil<T extends ElementImplement> {
    Map<String, Class<T>> map = new HashMap<>();
    void register(String name, Class<? extends T> aClass);
}
