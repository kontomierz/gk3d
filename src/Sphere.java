import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/// Obiekt sfery o okreœlonym promieniu i pozycji
public class Sphere extends Material {
	public float x;
	public float y;
	public float z;

	protected GLUquadric quadric;
	protected final float radius;
	protected final int slices;
	protected final int stacks;	
	
	public Sphere(GLU glu, float radius, float x, float y, float z) {
		super();
        this.quadric = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
        glu.gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);
        this.x = x;
        this.y = y;
        this.z = z;        
        this.radius = radius;
        this.slices = this.stacks = (int)(Math.min(100, Math.max(radius * 10, 8)));
	}
	
	public Sphere(GLU glu, float radius) {
		this(glu, radius, 0, 0, 0);
	}
	
	public void Draw(GL2 gl, GLU glu) {
        glu.gluSphere(this.quadric, radius, this.slices, this.stacks);
	}
	
	public void DrawExt(GL2 gl, GLU glu) {
		gl.glPushMatrix();
		gl.glTranslatef(this.x, this.y, this.z);
		this.SetMaterial(gl);
		this.Draw(gl, glu);
		gl.glPopMatrix();		
	}
	
	public void DrawWithTexture(GL2 gl, GLU glu, int textureId) {
		glu.gluQuadricTexture(this.quadric, true);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId); 
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
		// TODO czy to jest potrzebne
		gl.glColor3d(1.0,0.8,0.8); 
		gl.glEnable(GL2.GL_TEXTURE_2D); 
		this.DrawExt(gl, glu);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}

	public float getRadius() {
		return radius;
	}	
}
