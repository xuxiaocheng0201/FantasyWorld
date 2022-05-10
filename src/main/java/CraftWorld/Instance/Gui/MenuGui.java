package CraftWorld.Instance.Gui;

import Core.EventBus.EventBusManager;
import Core.Gui.Callback.KeyCallbackEvent;
import Core.Gui.IBasicGui;
import CraftWorld.CraftWorld;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("MethodMayBeStatic")
public class MenuGui implements IBasicGui {
    @Subscribe
    public void exit(KeyCallbackEvent event) {
        if (event.key() == GLFW.GLFW_KEY_ESCAPE && event.action() == GLFW.GLFW_PRESS)
            CraftWorld.getInstance().clientRunning = false;
    }

    @Override
    public void init() {
        EventBusManager.getGLEventBus().register(this);
    }

    @Override
    public void update(double interval) {
    }

    private long theta = 0;

    @Override
    public void render() {
        GL11.glPushMatrix();
        GL11.glRotatef(this.theta, 0.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
        //画立方体的6个面
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f( 0.0F, 0.0F, 1.0F);
        GL11.glVertex3f( 0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(-0.5f,-0.5f, 0.5f);
        GL11.glVertex3f( 0.5f,-0.5f, 0.5f);
        //1----------------------------
        GL11.glNormal3f( 0.0F, 0.0F,-1.0F);
        GL11.glVertex3f(-0.1f,-0.5f,-0.5f);
        GL11.glVertex3f(-0.5f, 0.5f,-0.5f);
        GL11.glVertex3f( 0.5f, 0.5f,-0.5f);
        GL11.glVertex3f( 0.5f,-0.5f,-0.5f);
        //2----------------------------
        GL11.glNormal3f( 0.0F, 1.0F, 0.0F);
        GL11.glVertex3f( 0.5f, 0.5f, 0.5f);
        GL11.glVertex3f( 0.5f, 0.5f,-0.5f);
        GL11.glVertex3f(-0.5f, 0.5f,-0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        //3----------------------------
        GL11.glNormal3f( 0.0F,-1.0F, 0.0F);
        GL11.glVertex3f(-0.5f,-0.5f,-0.5f);
        GL11.glVertex3f( 0.5f,-0.5f,-0.5f);
        GL11.glVertex3f( 0.5f,-0.5f, 0.5f);
        GL11.glVertex3f(-0.5f,-0.5f, 0.5f);
        //4----------------------------
        GL11.glNormal3f( 1.0F, 0.0F, 0.0F);
        GL11.glVertex3f( 0.5f, 0.5f, 0.5f);
        GL11.glVertex3f( 0.5f,-0.5f, 0.5f);
        GL11.glVertex3f( 0.5f,-0.5f,-0.5f);
        GL11.glVertex3f( 0.5f, 0.5f,-0.5f);
        //5----------------------------
        GL11.glNormal3f(-1.0F, 0.0F, 0.0F);
        GL11.glVertex3f(-0.5f,-0.5f,-0.5f);
        GL11.glVertex3f(-0.5f,-0.5f, 0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(-0.5f, 0.5f,-0.5f);
        //6----------------------------*/
        GL11.glEnd();
        GL11.glPopMatrix();
        ++this.theta;
    }

    @Override
    public boolean finished() {
        return IBasicGui.super.finished();
    }

    @Override
    public void destroy() {
    }
}
