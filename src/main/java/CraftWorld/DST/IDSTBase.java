package CraftWorld.DST;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

@NewElementImplementCore(elementName = "DST")
public interface IDSTBase extends ElementImplement, Serializable {
    default String getDSTName(){return null;};
    default void setDSTName(String name){};
    void read(DataInput input) throws IOException;
    void write(DataOutput output) throws IOException;


    static @NotNull String prefix(@Nullable String name) {
        if (name == null)
            return "";
        return "start" + name;
    }

    static @NotNull String dePrefix(@Nullable String prefix) {
        if (prefix == null)
            return "";
        if (prefix.startsWith("start"))
            return prefix.substring(5);
        return prefix;
    }

    static @NotNull String suffix(@Nullable String name) {
        if (name == null)
            return "";
        return "end" + name;
    }

    static @NotNull String deSuffix(@Nullable String suffix) {
        if (suffix == null)
            return "";
        if (suffix.startsWith("end"))
            return suffix.substring(3);
        return suffix;
    }
}
