package CraftWorld.DST;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.NewElementImplement;

import java.io.*;

@NewElementImplement(elementName = "DST")
public interface IDSTBase extends ElementImplement, Serializable {
    String getDSTName();
    void setDSTName(String name);
    void read(DataInput input) throws IOException;
    void write(DataOutput output) throws IOException;
    default void readObject(ObjectInputStream in) throws IOException {
        this.read(in);
    }
    default void writeObject(ObjectOutputStream out) throws IOException {
        this.write(out);
    }
}
