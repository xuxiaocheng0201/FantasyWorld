package CraftWorld.Instance.Gui;

import Core.Addition.Mod.ModImplement;
import Core.EventBus.EventBusManager;
import Core.EventBus.Events.*;
import Core.GlobalConfigurations;
import Core.Gui.Callback.KeyCallbackEvent;
import Core.Gui.IBasicGui;
import Core.Gui.Window;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class LoadingGui implements IBasicGui {
    private final ModLoadingStages modLoadingStages = new ModLoadingStages();

    @Override
    public void init() {
        EventBusManager.getGLEventBus().register(this);
        EventBusManager.getDefaultEventBus().register(this.modLoadingStages);
    }

    @Override
    public void destroy() {
        EventBusManager.getGLEventBus().unregister(this);
        EventBusManager.getDefaultEventBus().unregister(this.modLoadingStages);
    }

    @Override
    public void render() {
        Window window = Window.getInstance();
        // TODO: Loading Gui Renderer.
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

    @Override
    public boolean finished() {
        return Window.getInstance().windowShouldClose() || !this.modLoadingStages.loadedSuccess || this.modLoadingStages.loadingStage == 0;
    }

    private long pressTime = -1;
    @Subscribe
    public void forceExit(KeyCallbackEvent keyEvent) {
        if (keyEvent.key() == GLFW.GLFW_KEY_ESCAPE && keyEvent.action() == GLFW.GLFW_PRESS && this.pressTime == -1)
            this.pressTime = System.currentTimeMillis();
        if (keyEvent.key() == GLFW.GLFW_KEY_ESCAPE && keyEvent.action() == GLFW.GLFW_RELEASE && this.pressTime != -1) {
            long intervalTime = System.currentTimeMillis() - this.pressTime;
            if (intervalTime > 5000) //TODO: changeable interval time.
                Window.getInstance().setWindowShouldClose(true);
            this.pressTime = -1;
        }
    }

    @Override
    public void update(double interval) {
        if (this.pressTime != -1 && Window.getInstance().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            long intervalTime = System.currentTimeMillis() - this.pressTime;
            if (intervalTime > 5000) //TODO: changeable interval time.
                Window.getInstance().setWindowShouldClose(true);
        }
    }

    public static class ModLoadingStages {
        /**
         * -4: Preparing.
         * -3: {@link Core.EventBus.Events.ElementsCheckingEvent}
         * -2: {@link Core.EventBus.Events.ElementsCheckedEvent}
         * -1: Pre initializing. {@link Core.EventBus.Events.PreInitializationModsEvent}
         * 0: Post initializing. {@link Core.EventBus.Events.PostInitializationModsEvent}
         * 1 ~ 2n: Mod initializing. ({@link Core.EventBus.Events.ModInitializingEvent} & {@link Core.EventBus.Events.ModInitializedEvent})
         */
        private int loadingStage = -4;
        private int loadedModCount;
        private Class<? extends ModImplement> loadingModClass;
        private boolean loadedSuccess = true;

        @Subscribe
        public void elementCheckingEvent(ElementsCheckingEvent event) {
            this.loadingStage = -3;
        }

        @Subscribe
        public void elementCheckedEvent(ElementsCheckedEvent event) {
            this.loadingStage = -2;
        }

        @Subscribe
        public void preInitializationEvent(PreInitializationModsEvent event) {
            if (event.firstPost())
                this.loadingStage = -1;
        }

        @Subscribe
        public void modInitializingEvent(ModInitializingEvent event) {
            this.loadingStage = 2 * this.loadedModCount + 1;
            this.loadingModClass = event.modClass();
        }

        @Subscribe
        public void modInitializedEvent(ModInitializedEvent event) {
            this.loadingStage = 2 * ++this.loadedModCount;
            this.loadedSuccess = event.success();
            if (!event.modClass().equals(this.loadingModClass))
                this.loadedSuccess = false;
            this.loadingModClass = null;
        }

        @Subscribe
        public void postInitializationEvent(PostInitializationModsEvent event) {
            if (event.firstPost())
                this.loadingStage = 0;
        }
    }
}
