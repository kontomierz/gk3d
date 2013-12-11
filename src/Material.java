import javax.media.opengl.GL2;

public abstract class Material {

	public float[] ambient;
	public float[] diffuse;
	public float[] specular;
	public float shininess;
	
	public Material() {
		this.ambient = new float[] {0.0f, 0.0f, 0.0f};
		this.diffuse = new float[] {0.0f, 0.0f, 0.0f};
		this.specular = new float[] {0.0f, 0.0f, 0.0f};
	}

	public void SetMaterial(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, this.ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, this.diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, this.specular, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, this.shininess);		
	}
	
}
