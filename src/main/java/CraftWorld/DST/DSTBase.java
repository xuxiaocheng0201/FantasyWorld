package CraftWorld.DST;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public abstract class DSTBase<T> implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1582363601327423090L;

    protected @NotNull T data;


    protected DSTBase(@NotNull T data) {
        super();
        this.data = data;
    }

    public @NotNull T getData() {
        return this.data;
    }

    public void setData(@NotNull T data) {
        this.data = data;
    }
}
