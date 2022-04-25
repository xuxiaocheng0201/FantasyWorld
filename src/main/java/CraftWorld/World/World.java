package CraftWorld.World;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.Range;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;

public class World implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -383984335814983830L;
    public static final String id = "World";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, World.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String worldName = "New world";
    private @Range(from = 0, to = Long.MAX_VALUE) long tick;

    public World() {
        super();
    }

    public World(DataInput input) throws IOException {
        super();
        this.read(input);
    }

    @Override
    public String getDSTName() {
        return this.worldName;
    }

    @Override
    public void setDSTName(String name) {
        this.worldName = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.worldName = input.readUTF();
        this.tick = input.readLong();

        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.worldName);
        output.writeLong(this.tick);

        output.writeUTF(suffix);
    }
}
