package CraftWorld.DST;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DSTMetaCompound extends DSTBase {
    public static final String id = "Compound";
    static {
        DSTUtils.register(id, DSTMetaCompound.class);
    }

    private final Map<String, DSTBase> dstMap = new HashMap<>();

    @Override
    public void read(DataInput input) throws IOException {
        input.readUTF();
        //todo: read dst
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(id);
        for (String s: dstMap.keySet())
            dstMap.get(s).write(output);
    }
}
