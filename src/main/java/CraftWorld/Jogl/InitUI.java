package CraftWorld.Jogl;

import CraftWorld.CraftWorld;
import HeadLibs.Helper.HStringHelper;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InitUI extends JFrame implements GLEventListener, KeyListener, MouseListener {
    private GL2 gl;
    private final GLU glu = new GLU();
    private final GLCapabilities glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
    private final GLCanvas glCanvas = new GLCanvas(glCapabilities);

    public InitUI() {
        super(HStringHelper.merge("Craft World ", CraftWorld.CURRENT_VERSION));
        glCanvas.addGLEventListener(this);
        glCanvas.addKeyListener(this);
        glCanvas.addMouseListener(this);
        getContentPane().add(glCanvas);
    }

    public void run() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        glCanvas.requestFocusInWindow();
        glCanvas.swapBuffers();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);    //清除颜色缓冲
        gl.glLoadIdentity();    //重置矩阵
        gl.glTranslatef(0.0f, 0.0f, -6.0f);    //向内(Z轴负方向)移动6
        gl.glColor3f(0.0f, 1.0f, 0.0f);        //设置颜色(r,g,b)绿色
        gl.glBegin(GL2.GL_QUADS);               //开始绘制多边形
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);  //第1个顶点
        gl.glVertex3f(1.0f, -1.0f, 0.0f);   //第2个顶点
        gl.glVertex3f(1.0f, 1.0f, 0.0f);    //第3个顶点
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);   //第4个顶点
        gl.glEnd();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0, 0, 0, 0);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);   //视见区域
        gl.glMatrixMode(GL2.GL_PROJECTION);//哪一个矩阵堆栈
        gl.glLoadIdentity(); //重置矩阵
        glu.gluPerspective(45.0, (float) width / (float) height, 1.0, 100.0);//创建透视矩阵
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
