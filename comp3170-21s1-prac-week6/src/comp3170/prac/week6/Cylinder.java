package comp3170.prac.week6;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Cylinder {

	protected Shader shader;
	protected Vector3f colour;
	protected Vector3f position;
	protected Vector3f angle;
	protected float scale;
	protected Matrix4f modelMatrix;
	
	public static final float TAU = (float)Math.PI *2;
	private Vector4f[] vertices;
	private Vector4f[] topVertices;
	private Vector4f[] botVertices;
	private int vertexBuffer;
	private int[] indices;
	private int[] topIndices;
	private int[] botIndices;
	private int[] sideIndices;
	private int indexBuffer;
	private int sideIndexBuffer;
	public static int NSIDES = 6;
	public boolean lines=false;
	
	private Vector3f sideColour = new Vector3f(1f,0f,0f);
	private float[] sideColourf;
	private int colourBuffer;
	
	
	public Cylinder(Shader shader) {
		this.shader = shader;
		
		this.position = new Vector3f();
		this.angle = new Vector3f();
		this.scale = 1;
		this.modelMatrix = new Matrix4f();
		
		this.colour = new Vector3f(1,1,1); // default to white;
		
		// TODO: Implement vertex and index buffers
		
		
		this.botVertices = new Vector4f[NSIDES +1];
		this.botVertices[0]= new Vector4f(0,0,0,1);
		
		for(int i=0; i<NSIDES;i++) {
			float angle = i * TAU /NSIDES;
			botVertices[i+1] = new Vector4f(1,0,0,1);
			botVertices[i+1].rotateY(angle);
		}
		
		this.topVertices = new Vector4f[NSIDES +1];
		this.topVertices[0]= new Vector4f(0,1,0,1);
		
		for(int i=0; i<NSIDES;i++) {
			float angle = i * TAU /NSIDES;
			topVertices[i+1] = new Vector4f(1,1,0,1);
			topVertices[i+1].rotateY(angle);
		}
		
		this.vertices = new Vector4f[botVertices.length + topVertices.length];
		for (int i = 0; i< vertices.length; i++)
		{
			if(i<botVertices.length) {
				vertices[i] = botVertices[i];
			}
			else {
				vertices[i] = topVertices[(i%topVertices.length)];
			}
		}
		
		this.vertexBuffer = shader.createBuffer(vertices);
		
		if(lines) {
			this.botIndices = new int[NSIDES *4];
			
			int k =0;
			for(int i=1; i <= NSIDES; i++) {
				botIndices[k++]=0;
				botIndices[k++]=i;
				
				botIndices[k++]=i;
				botIndices[k++]=(i%NSIDES)+1;
			}
			
			this.topIndices = new int[NSIDES *4];
			int 
			topCentre = NSIDES +1;
			k=0;
			for(int i =1; i<=NSIDES; i++) {
				topIndices[k++]= topCentre;
				topIndices[k++]= i+topCentre;
				
				topIndices[k++]= i+topCentre;
				topIndices[k++]= (i%NSIDES)+topCentre+1;
			}
			
			this.sideIndices = new int[NSIDES *2];
			k=0;
			for(int i = 1; i<=NSIDES;i++) {
				sideIndices[k++]=i;
				sideIndices[k++]=i+topCentre;
			
			}
			
			this.indices = new int[botIndices.length + topIndices.length + sideIndices.length];
			for(int i =0;i<indices.length;i++) {
				if(i <botIndices.length) {
					indices[i] = botIndices[i];
				} 
				else if (i < botIndices.length + topIndices.length){
					indices[i] = topIndices[i%topIndices.length];
				}
				else {
					indices[i] = sideIndices[i%sideIndices.length];
				}
			}
			this.indexBuffer = shader.createIndexBuffer(indices);
		}
		
		if(!lines) {
			this.botIndices = new int[NSIDES *3];
			int k =0;
			for(int i=1; i <= NSIDES; i++) {
				botIndices[k++]=0;
				botIndices[k++]=(i% NSIDES)+1;				
				botIndices[k++]=i;

			}
			
			this.topIndices = new int[NSIDES *3];
			int 
			topCentre = NSIDES +1;
			k=0;
			for(int i =1; i<NSIDES; i++) {
				topIndices[k++]= topCentre;
				topIndices[k++]= i+topCentre;
				topIndices[k++]= (i%NSIDES)+topCentre+1;
			}
			
			this.indices = new int[botIndices.length + topIndices.length];
			for(int i =0;i<indices.length;i++) {
				if(i <botIndices.length) {
					indices[i] = botIndices[i];
				}				
				else if (i < botIndices.length + topIndices.length){
					indices[i] = topIndices[i%topIndices.length];
				}
			}
			this.indexBuffer = shader.createIndexBuffer(indices);
			this.sideIndices = new int[NSIDES*6];
			k=0;
			
			for(int i=1;i<=NSIDES;i++) {
				sideIndices[k++] = i;
				sideIndices[k++] = (i%NSIDES) + topCentre + 1;
				sideIndices[k++] = i +topCentre;
				
				sideIndices[k++] = i;
				sideIndices[k++] = (i%NSIDES)+ 1;
				sideIndices[k++] = (i%NSIDES)+ topCentre +1;
			}
			this.sideIndexBuffer = shader.createIndexBuffer(sideIndices);
			
			
		}
		
		
	}

	public Vector3f getPosition(Vector3f dest) {
		return dest.get(position);
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	public Vector3f getAngle(Vector3f angle) {
		return angle.set(this.angle);
	}

	public void setAngle(float pitch, float heading, float roll) {
		this.angle.set(pitch, heading, roll);
	}

	public void setAngle(Vector3f angle) {
		this.angle.set(angle);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getColour(Vector3f dest) {
		return dest.set(colour);
	}

	public void setColour(Color color) {		
		colour.x = color.getRed() / 255f;
		colour.y = color.getGreen() / 255f;
		colour.z = color.getBlue() / 255f;
	}

	public void draw() {
		// TODO: Draw the cylinder
		
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		if(lines) {
			gl.glDrawElements(GL.GL_LINES,indices.length, GL.GL_UNSIGNED_INT,0);
		}
		else {
			gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT,0);
			gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, sideIndexBuffer);
			shader.setUniform("u_colour", sideColour);
			gl.glDrawElements(GL.GL_TRIANGLES, sideIndices.length, GL.GL_UNSIGNED_INT, 0);
		}
		
		
	}

	protected void calcModelMatrix() {
		modelMatrix.identity();
		// TODO: Calculate the model matrix
		modelMatrix.translate(this.position);
		modelMatrix.rotateY(this.angle.y);
		modelMatrix.rotateX(this.angle.x);
		modelMatrix.rotateZ(this.angle.z);
		modelMatrix.scale(this.scale);
		
	}

}