package CraftWorld.DST.BasicInformation;

import Core.Addition.Element.ElementId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;

@SuppressWarnings("EqualsAndHashcode")
public class DSTId extends ElementId implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 4758900507430963731L;
    public static final DSTId id = getDstIdInstance("DSTId");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public DSTId() {
        super();
    }

    public DSTId(@Nullable String elementId) {
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
    public void readShortly(@NotNull DataInput input) throws IOException {
        this.elementId = input.readUTF();
    }

    @Override
    public void writeShortly(@NotNull DataOutput output) throws IOException {
        output.writeUTF(this.elementId);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTId that)) return false;
        return this.elementId.equals(that.elementId);
    }

    public static @NotNull DSTId getDstIdInstance(@Nullable String id) {
        return new DSTId(id);
    }
}
