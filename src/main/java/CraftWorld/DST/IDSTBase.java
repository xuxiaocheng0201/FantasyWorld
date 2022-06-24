package CraftWorld.DST;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

@NewElementImplementCore(modName = "CraftWorld", elementName = "DST")
public interface IDSTBase extends ElementImplement, Serializable {
    void read(@NotNull DataInput input) throws IOException;
    void write(@NotNull DataOutput output) throws IOException;

    default void readShortly(@NotNull DataInput input) throws IOException {
        this.read(input);
    }
    default void writeShortly(@NotNull DataOutput output) throws IOException {
        this.write(output);
    }
}
