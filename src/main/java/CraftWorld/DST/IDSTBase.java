package CraftWorld.DST;

import Core.Addition.Implement.ElementImplement;
import Core.Addition.Implement.NewElementImplementCore;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

@NewElementImplementCore(elementName = "DST")
public interface IDSTBase extends ElementImplement, Serializable {
    String getDSTName();
    void setDSTName(String name);
    void read(DataInput input) throws IOException;
    void write(DataOutput output) throws IOException;
}
