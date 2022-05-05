package CraftWorld.Gui;

import Core.EventBus.EventBusManager;
import Core.GlobalConfigurations;
import Core.Gui.Callback.KeyCallbackEvent;
import Core.Gui.IBasicGui;
import Core.Gui.Window;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class LoadingGui implements IBasicGui {
    public LoadingGui() {
        super();
    }

    @Override
    public void init() {
        EventBusManager.getGLEventBus().register(this);
    }

    @Override
    public void update(double interval) {
    }

    @Override
    public void render() {
        Window window = Window.getInstance();
        float ratio = (float) window.getWidth() / window.getHeight();
        /* Set orthographic projection */
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-ratio, ratio, -1.0f, 1.0f, 1.0f, -1.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        /* Rotate matrix */
        GL11.glLoadIdentity();
        GL11.glRotated(GLFW.glfwGetTime() * GlobalConfigurations.MAX_FPS, 0.0d, 0.0d, 1.0d);
        /* Render triangle */
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glVertex3f(-0.5f, -0.5f, 0.0f);
        GL11.glColor3f(0.0f, 1.0f, 0.0f);
        GL11.glVertex3f(0.5f, -0.5f, 0.0f);
        GL11.glColor3f(0.0f, 0.0f, 1.0f);
        GL11.glVertex3f(0.0f, 0.5f, 0.0f);
        GL11.glEnd();
    }

    private boolean finished;

    public boolean finished() {
        return this.finished || Window.getInstance().windowShouldClose();
    }

    @Subscribe
    public void ENTER(KeyCallbackEvent keyEvent) {
        if (keyEvent.key() == GLFW.GLFW_KEY_ENTER && keyEvent.action() == GLFW.GLFW_PRESS)
            this.finished = true;
    }
}
