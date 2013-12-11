import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;


public class CosmicStation extends Material {
	
	public CosmicStation() {}
	
	public void Draw(GL2 gl, GLU glu) {
		GLUquadric quadric = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
        glu.gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);

        // pomieszczenia stacji kosmicznej
		gl.glPushMatrix();

		gl.glTranslatef(0.0f, 0.0f, 4.0f);
		glu.gluSphere(quadric, 1.0f, 8, 8);

		gl.glTranslatef(0.0f, 0.0f, -8.0f);
		glu.gluSphere(quadric, 1.0f, 8, 8);

		gl.glTranslatef(4.0f, 0.0f, 4.0f);
		glu.gluSphere(quadric, 1.0f, 8, 8);
				
		gl.glPopMatrix();	
		// end pomieszczenia

		// korytarze ³¹cz¹ce pomieszczenia stacji kosmicznej
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -4.0f);
		gl.glRotatef(45, 0.0f, 1.0f, 0.0f);
		glu.gluCylinder(quadric, 0.35f, 0.35f, 5.65f, 10, 8);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, 4.0f);
		gl.glRotatef(135, 0.0f, 1.0f, 0.0f);
		glu.gluCylinder(quadric, 0.35f, 0.35f, 5.65f, 10, 8);
		gl.glPopMatrix();
		// end korytarze		
	}
}
