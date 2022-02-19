package CraftWorld.Block;

import CraftWorld.Exception.RegisteredException;
import HeadLibs.Helper.HStringHelper;

import java.util.HashMap;
import java.util.Map;

public class BlockUtils {
    private static final Map<String, Class<? extends IBlockBase>> blocks = new HashMap<>();

    public static void register(String name, Class<? extends IBlockBase> blockClass) {
        try {
            if (blocks.containsKey(name))
                throw new RegisteredException("Registered name");
            if (blocks.containsValue(blockClass))
                throw new RegisteredException("Registered class");
            blocks.put(name, blockClass);
        } catch (RegisteredException exception) {
            exception.printStackTrace();
        }
    }

    public static int getRegisteredCount() {
        return blocks.size();
    }

    public static IBlockBase get(String name) {
        try {
            if (!blocks.containsKey(name))
                throw new IllegalArgumentException(HStringHelper.merge("Type is not registered! [name='", name, "']"));
            return blocks.get(name).getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String prefix(String name) {
        return HStringHelper.merge("start", name);
    }

    public static String dePrefix(String prefix) {
        if (prefix == null)
            return "null";
        if (prefix.startsWith("start"))
            return prefix.substring(5);
        return prefix;
    }

    public static String suffix(String name) {
        return HStringHelper.merge("end", name);
    }

    public static String deSuffix(String suffix) {
        if (suffix == null)
            return "null";
        if (suffix.startsWith("end"))
            return suffix.substring(3);
        return suffix;
    }
}
