package CraftWorld.DST;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.HDoubleMapRegisterer;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HashCollisionsErrorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NewElementUtilCore(modName = "CraftWorld", elementName = "DST")
public class DSTUtils extends ElementUtil<IDSTBase> {
    private static final DSTUtils instance = new DSTUtils();
    public static DSTUtils getInstance() {
        return instance;
    }

    @Override
    public void register(@NotNull String key, @NotNull Class<? extends IDSTBase> value) throws HElementRegisteredException {
        HLog.logger(value);
    }

    private static final HDoubleMapRegisterer<String, String> prefixMap = new HDoubleMapRegisterer<>();
    private static final HDoubleMapRegisterer<String, String> suffixMap = new HDoubleMapRegisterer<>();

    public static @NotNull String prefix(@Nullable String name) {
        if (name == null)
            return "";
        try {
            return prefixMap.getValue(name);
        } catch (HElementNotRegisteredException ignore) {
        }
        String hash = String.valueOf(("start" + name).hashCode());
        try {
            prefixMap.register(name, hash);
        } catch (HElementRegisteredException exception) {
            try {
                throw new HashCollisionsErrorException(null, name, prefixMap.getKey(hash), exception);
            } catch (HElementNotRegisteredException ignore) {
            }
        }
        return hash;
    }

    public static @NotNull String dePrefix(@Nullable String prefix) {
        if (prefix == null)
            return "";
        try {
            return prefixMap.getKey(prefix);
        } catch (HElementNotRegisteredException ignore) {
        }
        return prefix;
    }

    public static @NotNull String suffix(@Nullable String name) {
        if (name == null)
            return "";
        try {
            return suffixMap.getValue(name);
        } catch (HElementNotRegisteredException ignore) {
        }
        String hash = String.valueOf(("end" + name).hashCode());
        try {
            suffixMap.register(name, hash);
        } catch (HElementRegisteredException exception) {
            try {
                throw new HashCollisionsErrorException(null, name, suffixMap.getKey(hash), exception);
            } catch (HElementNotRegisteredException ignore) {
            }
        }
        return hash;
    }

    public static @NotNull String deSuffix(@Nullable String suffix) {
        if (suffix == null)
            return "";
        try {
            return suffixMap.getKey(suffix);
        } catch (HElementNotRegisteredException ignore) {
        }
        return suffix;
    }
}
