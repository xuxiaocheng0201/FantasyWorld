package CraftWorld.DST;

import java.io.Serial;

public abstract class PureDSTBase<T> implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1582363601327423090L;

    protected T data;

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
