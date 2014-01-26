import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;


public class SphereTextured extends Sphere {
	private int textureId;

	public SphereTextured(GLU glu, float radius, float x, float y, float z, int textureId) {
		super(glu, radius, x, y, z);
		this.textureId = textureId;
	}
	
	public void DrawWithTexture(GL2 gl, GLU glu) {
		
	}
}
