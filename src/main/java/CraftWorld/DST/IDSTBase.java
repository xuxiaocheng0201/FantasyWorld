package CraftWorld.DST;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.NewElementImplement;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

@NewElementImplement(name = "DST")
public interface IDSTBase extends ElementImplement, Serializable {
    void write(DataOutput output) throws IOException;
    void read(DataInput input) throws IOException;
}
