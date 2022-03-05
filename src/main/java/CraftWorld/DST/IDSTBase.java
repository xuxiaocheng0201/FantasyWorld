package CraftWorld.DST;

import Core.Mod.NewElement.ElementImplement;
import Core.Mod.NewElement.NewElementImplement;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@NewElementImplement(name = "DST")
public interface IDSTBase extends ElementImplement {
    void write(DataOutput output) throws IOException;
    void read(DataInput input) throws IOException;
}
