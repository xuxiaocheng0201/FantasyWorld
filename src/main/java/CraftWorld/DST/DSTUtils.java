package CraftWorld.DST;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import CraftWorld.DST.BasicInformation.DSTId;
import HeadLibs.Registerer.HDoubleMapRegisterer;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HashCollisionsErrorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NewElementUtilCore(modName = "CraftWorld", elementName = "DST")
public class DSTUtils extends ElementUtil<DSTId, IDSTBase> {
    private static final DSTUtils instance = new DSTUtils();
    public static DSTUtils getInstance() {
        return instance;
    }

    protected static final HDoubleMapRegisterer<DSTId, String> prefixMap = new HDoubleMapRegisterer<>();
    protected static final HDoubleMapRegisterer<DSTId, String> suffixMap = new HDoubleMapRegisterer<>();

    public static @NotNull String prefix(@Nullable DSTId name) {
        if (name == null)
            return "";
        try {
            return prefixMap.getValue(name);
        } catch (HElementNotRegisteredException ignore) {
        }
        String hash = String.valueOf(("start" + name.getId()).hashCode());
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

    public static @NotNull DSTId dePrefix(@Nullable String prefix) {
        if (prefix == null)
            return DSTId.getDstIdInstance("");
        try {
            return prefixMap.getKey(prefix);
        } catch (HElementNotRegisteredException ignore) {
        }
        return DSTId.getDstIdInstance(prefix);
    }

    public static @NotNull String suffix(@Nullable DSTId name) {
        if (name == null)
            return "";
        try {
            return suffixMap.getValue(name);
        } catch (HElementNotRegisteredException ignore) {
        }
        String hash = String.valueOf(("end" + name.getId()).hashCode());
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

    public static @NotNull DSTId deSuffix(@Nullable String suffix) {
        if (suffix == null)
            return DSTId.getDstIdInstance("");
        try {
            return suffixMap.getKey(suffix);
        } catch (HElementNotRegisteredException ignore) {
        }
        return DSTId.getDstIdInstance(suffix);
    }
}
