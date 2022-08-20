package CraftWorld.Entity.BoundingBox.BasicInformation;

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
public class BoundingBoxId extends DSTId {
    @Serial
    private static final long serialVersionUID = -8044892357262959418L;
    public static final DSTId id = DSTId.getDstIdInstance("BoundingBoxId");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public BoundingBoxId() {
        super();
    }

    public BoundingBoxId(@Nullable String elementId) {
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
        if (!(o instanceof BoundingBoxId that)) return false;
        return this.elementId.equals(that.elementId);
    }

    public static @NotNull BoundingBoxId getBoundingBoxIdInstance(@Nullable String id) {
        return new BoundingBoxId(id);
    }
}
