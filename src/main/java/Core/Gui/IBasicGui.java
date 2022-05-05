package Core.Gui;

public interface IBasicGui {
    void init() throws Exception;
    void update(double interval) throws Exception;
    void render() throws Exception;
    default boolean finished() {
        return Window.getInstance().windowShouldClose();
    }
}
