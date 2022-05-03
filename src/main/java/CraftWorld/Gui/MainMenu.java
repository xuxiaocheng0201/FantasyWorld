package CraftWorld.Gui;

import Core.EventBus.Events.KeyCallbackEvent;
import Core.GlobalConfigurations;
import CraftWorld.CraftWorld;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class MainMenu {
    @SuppressWarnings("MethodMayBeStatic")
    @Subscribe
    public void exit(KeyCallbackEvent event) {
        if (event.key() == GLFW.GLFW_KEY_ESCAPE && event.action() == GLFW.GLFW_PRESS)
            CraftWorld.getInstance().clientRunning = false;
    }

    public boolean update() {
        return false;
    }

    public void render(int width, int height) {
        float ratio = (float) width / height;
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
        GL11.glVertex3f(-0.6f, -0.4f, 0.0f);
        GL11.glColor3f(0.0f, 1.0f, 0.0f);
        GL11.glVertex3f(0.6f, -0.4f, 0.0f);
        GL11.glColor3f(0.0f, 0.0f, 1.0f);
        GL11.glVertex3f(0.0f, 0.6f, 0.0f);
        GL11.glEnd();
    }
}
