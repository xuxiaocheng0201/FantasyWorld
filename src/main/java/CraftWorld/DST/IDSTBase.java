package CraftWorld.DST;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface IDSTBase {
    void write(DataOutput output) throws IOException;
    void read(DataInput input) throws IOException;
}
