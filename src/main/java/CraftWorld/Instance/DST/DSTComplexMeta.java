package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Map.Entry;
import java.util.Objects;

public final class DSTComplexMeta implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 5912925151440251840L;
    public static final String id = "DSTComplexMeta";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    private final @NotNull HMapRegisterer<String, IDSTBase> dstMap = new HMapRegisterer<>(true);

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.dstMap.deregisterAll();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            IDSTBase dst;
            try {
                dst = DSTUtils.getInstance().getElementInstance(DSTUtils.dePrefix(name), false);
            } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
                throw new DSTFormatException(exception);
            }
            dst.read(input);
            try {
                this.dstMap.register(name, dst);
            } catch (HElementRegisteredException ignore) {
            }
            name = input.readUTF();
        }
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        for (Entry<String, IDSTBase> entry : this.dstMap) {
            output.writeUTF(entry.getKey());
            entry.getValue().write(output);
        }
        output.writeUTF(suffix);
    }

    public @NotNull HMapRegisterer<String, IDSTBase> getDstMap() {
        return this.dstMap;
    }

    @Override
    public @NotNull String toString() {
        return "DSTComplexMeta:" + this.dstMap;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTComplexMeta that)) return false;
        return this.dstMap.equals(that.dstMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dstMap);
    }
}
