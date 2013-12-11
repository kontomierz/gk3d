import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/// Sfera rozszerzona o pó³kule o sto¿ki na jej powierzchni
public class Satellite extends Sphere {
	public Satellite(GLU glu, float radius, float x, float y, float z)
	{
		super(glu, radius, x, y, x);
	}
	
	public void Draw(GL2 gl, GLU glu) {
		super.Draw(gl, glu);
		
		// bunkry
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -this.radius);
		glu.gluSphere(this.quadric, this.radius / 8, this.slices, this.stacks);
		
		gl.glTranslatef(0.0f, -this.radius, this.radius);
		glu.gluSphere(this.quadric, this.radius / 8, this.slices, this.stacks);
		
		gl.glTranslatef(-this.radius, this.radius, 0.0f);
		glu.gluSphere(this.quadric, this.radius / 8, this.slices, this.stacks);
		gl.glPopMatrix();
		// end bunkry
		
		// ostrza
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, this.radius, 0.0f);
		gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
		glu.gluCylinder(this.quadric, this.radius /8, 0, this.radius/2, this.slices, this.stacks);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(this.radius, 0.0f, 0.0f);
		gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
		glu.gluCylinder(this.quadric, this.radius /8, 0, this.radius/2, this.slices, this.stacks);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, this.radius);
		glu.gluCylinder(this.quadric, this.radius /8, 0, this.radius/2, this.slices, this.stacks);
		gl.glPopMatrix();		
		//end ostrza
	}
}
