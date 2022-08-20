package CraftWorld.Entity;

import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Entity.BasicInformation.EntityId;
import CraftWorld.Instance.DST.DSTComplexMeta;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Entity")
public interface IEntityBase extends IDSTBase {
    @NotNull EntityId getEntityId();
    @NotNull String getEntityName();
    void setEntityName(@NotNull String name);
    @NotNull DSTComplexMeta getEntityDST();
    boolean needDelete();

    @Override
    default void read(@NotNull DataInput input) throws IOException {
        this.setEntityName(input.readUTF());
        if (!DSTComplexMeta.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.getEntityDST().read(input);
    }

    @Override
    default void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(this.getEntityName());
        this.getEntityDST().write(output);
    }
}
