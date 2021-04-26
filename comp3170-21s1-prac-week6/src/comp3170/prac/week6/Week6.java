package comp3170.prac.week6;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.GLException;
import comp3170.Shader;

public class Week6 extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/prac/week6"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Animator animator;
	private long oldTime;
	private InputManager input;

	private Axes axes;

	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;

	private Cylinder cylinder;

	private float cameraRotationSpeed = TAU*2;

	private float currentRotation;

	private float currentRotationX;


	public Week6() {
		super("Week 6 3D camera demo");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// set up Animator		

		this.animator = new Animator(canvas);
		this.animator.start();
		this.oldTime = System.currentTimeMillis();		

		// input
		
		this.input = new InputManager(canvas);
		
		// set up the JFrame
		
		this.setSize(width,height);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_BACK);
		gl.glFrontFace(GL.GL_CCW);
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			this.shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Set up the scene
		this.axes = new Axes(shader);
		
		this.cylinder = new Cylinder(shader);
		
		// allocate matrices
		
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
	}

	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		if(input.isKeyDown(KeyEvent.VK_RIGHT)) {
			currentRotation += this.cameraRotationSpeed * deltaTime;
		}
		if(input.isKeyDown(KeyEvent.VK_LEFT)) {
			currentRotation -= this.cameraRotationSpeed * deltaTime;
		}
		if(input.isKeyDown(KeyEvent.VK_UP)) {
			currentRotationX += this.cameraRotationSpeed * deltaTime;
		}
		if(input.isKeyDown(KeyEvent.VK_DOWN)) {
			currentRotationX -= this.cameraRotationSpeed * deltaTime;
		}
		cylinder.setAngle(currentRotationX, currentRotation,0);
		Vector3f test = new Vector3f();
		cylinder.getAngle(test);
		
		
		input.clear();
	}

	private final static float CAMERA_DISTANCE = 5;
	private final static float CAMERA_YAW = TAU / 8;
	private final static float CAMERA_PITCH = TAU / 12;
	private final static float CAMERA_FOVY = TAU / 6;
	private final static float CAMERA_ASPECT = 1;
	private final static float CAMERA_NEAR = 1;
	private final static float CAMERA_FAR = 10;
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		update();
		
        // clear the colour buffer
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		
		// set up the camera
		
		viewMatrix.identity();
		viewMatrix.rotateY(CAMERA_YAW);
		viewMatrix.rotateX(CAMERA_PITCH);
		viewMatrix.translate(0, 0, -CAMERA_DISTANCE);
		viewMatrix.rotateY(TAU/2);
		viewMatrix.invert();

		projectionMatrix.setPerspective(
				CAMERA_FOVY, CAMERA_ASPECT, CAMERA_NEAR, CAMERA_FAR);
		
		shader.setUniform("u_viewMatrix", viewMatrix);
		shader.setUniform("u_projectionMatrix", projectionMatrix);
		
		// draw the scene
		this.axes.draw();
		this.cylinder.draw();
		
	}

	@Override
	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) { 
		new Week6();
	}


}
