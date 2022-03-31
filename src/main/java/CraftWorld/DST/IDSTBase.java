package CraftWorld.DST;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.NewElementImplement;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

@NewElementImplement(elementName = "DST")
public interface IDSTBase extends ElementImplement, Serializable {
    String getDSTName();
    void setDSTName(String name);
    void read(DataInput input) throws IOException;
    void write(DataOutput output) throws IOException;
}
