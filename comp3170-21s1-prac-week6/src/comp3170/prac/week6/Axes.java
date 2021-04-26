package comp3170.prac.week6;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Axes {
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int indexBufferX;
	private int indexBufferY;
	private int indexBufferZ;
	private Shader shader;
	private Matrix4f modelMatrix;

	public Axes(Shader shader) {
		this.shader = shader;
		
		// A set of i,j,k axes		
		
		this.vertices = new Vector4f[] {
			new Vector4f(0,0,0,1),
			new Vector4f(1,0,0,1),
			new Vector4f(0,1,0,1),
			new Vector4f(0,0,1,1),				
		};
		this.vertexBuffer = shader.createBuffer(vertices);

		this.indexBufferX = shader.createIndexBuffer(new int[] {0,1});		
		this.indexBufferY = shader.createIndexBuffer(new int[] {0,2});		
		this.indexBufferZ = shader.createIndexBuffer(new int[] {0,3});
		
		this.modelMatrix = new Matrix4f();
	}

	
	

	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// activate the shader
		shader.enable();		

		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setAttribute("a_position", vertexBuffer);

		// X axis in red

		shader.setUniform("u_colour", new float[] {1,0,0});
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBufferX);
		gl.glDrawElements(GL.GL_LINES, 2, GL.GL_UNSIGNED_INT, 0);		

		// Y axis in green

		shader.setUniform("u_colour", new float[] {0,1,0});
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBufferY);
		gl.glDrawElements(GL.GL_LINES, 2, GL.GL_UNSIGNED_INT, 0);		

		// Z axis in blue

		shader.setUniform("u_colour", new float[] {0,0,1});
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBufferZ);
		gl.glDrawElements(GL.GL_LINES, 2, GL.GL_UNSIGNED_INT, 0);		

	}

}
