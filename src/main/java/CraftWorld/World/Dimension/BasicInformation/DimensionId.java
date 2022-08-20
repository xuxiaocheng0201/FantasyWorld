package CraftWorld.World.Dimension.BasicInformation;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;

@SuppressWarnings("EqualsAndHashcode")
public class DimensionId extends DSTId {
    @Serial
    private static final long serialVersionUID = 8104342685648797599L;
    public static final DSTId id = DSTId.getDstIdInstance("DimensionId");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DimensionId() {
        super();
    }

    public DimensionId(@Nullable String elementId) {
        super(elementId);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.elementId = input.readUTF();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.elementId);
        output.writeUTF(suffix);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DimensionId that)) return false;
        return this.elementId.equals(that.elementId);
    }

    public static @NotNull DimensionId getDimensionIdInstance(@Nullable String id) {
        return new DimensionId(id);
    }
}
