import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

@SuppressWarnings("serial")
public class Scene extends GLJPanel implements GLEventListener {
	// domyœlny rozmiar okna
	public static final int width = 800;
	public static final int height = 600;
	
	public Camera camera;
	
	// opcje w³¹czania / wy³¹czania poszczególnych œwiate³
	public boolean enableAmbient = false;
	public boolean enableSunlight = true;
	public boolean enableReflectorSpot = true;
	public boolean enableCuriositySpot = true;
	
	// identyfikatory tekstur
	int earthTid;
	int marsTid;	
	
	// odpowiada za ustawienie fps dla animacji
	private FPSAnimator animator;

	// ostatnio zarejestrowana klatka, okres od ostatniej klatki w ms
	private long lastTime, delta;
	
	// œwiat³o dynamiczne, jego kolor oraz index aktualizowanego koloru
	private float[] rgbaDynamic = new float[] {1.0f, 0.0f, 0.0f, 1.0f};
	private int rgbaDynamicIndex = 0;
	// jak szybko ma byæ zmieniany kolor
	private float changeColorSpeed = 1.0f; // zmienia siê o jeden kolor na sekundê

	// obiekty na scenie
	private Satellite satellite;
	private Sphere planet;
	private Sphere earth;
	private Sphere mars;
	private CosmicStation cosmicStation;

	// modele
	private GLModel spotModel = null;
	private GLModel curiosityModel = null;
	
	private float asteroidPosXYZ = 20;
		
	public Scene() {
		this.setFocusable(true);
		this.addGLEventListener(this);
		this.animator = new FPSAnimator(this, 30, false);
		this.animator.start();
		this.camera = new Camera(0, 5, -60);
		this.lastTime = System.nanoTime();
	}
	
	@Override
	/// Called by the drawable immediately after the OpenGL context is initialized.
	/// Can be used to perform one-time OpenGL initialization such as setup of 
	/// lights and display lists. Note that this method may be called more than once 
	/// if the underlying OpenGL context for the GLAutoDrawable is destroyed and
	/// recreated, for example if a GLCanvas is removed from the widget hierarchy
	/// and later added again. 
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(1, (double) this.getWidth() / this.getHeight(), 1, 1000);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		this.InitializeObjects(gl, glu);
	}
	
	/// Inicjalizacja obiektów wystêpuj¹cych na scenie
	private void InitializeObjects(GL2 gl, GLU glu) {
		this.satellite = new Satellite(glu, 10, this.asteroidPosXYZ, this.asteroidPosXYZ, this.asteroidPosXYZ);
		this.satellite.ambient = new float[] {0.5f, 0.0f, 0.0f};
		this.satellite.diffuse = new float[] {0.3f, 0.6f, 0.1f};
		this.satellite.specular = new float[] {0.5f, 0.6f, 0.1f};
		this.satellite.shininess = 5.0f;
		
		this.planet = new Sphere(glu, 50, 0, -50, 0);
		this.planet.ambient = new float[] {0.0f, 0.0f, 0.5f};
		this.planet.diffuse = new float[] {0.0f, 0.0f, 0.5f};
		this.planet.specular = new float[] {0.0f, 0.0f, 0.5f};
		this.planet.shininess = 5.0f;
		
		this.cosmicStation = new CosmicStation();
		this.cosmicStation.ambient = new float[] {0.0f, 0.5f, 0.0f};
		this.cosmicStation.diffuse = new float[] {0.0f, 0.7f, 0.0f};
		this.cosmicStation.specular = new float[] {0.0f, 0.7f, 0.0f};
		this.cosmicStation.shininess = 10.0f;

		this.spotModel = ModelLoaderOBJ.LoadModel("./models/spot.obj", "./models/spot.mtl", gl);
		this.curiosityModel = ModelLoaderOBJ.LoadModel("./models/curiosity.obj", "./models/curiosity.mtl", gl);

		gl.glEnable(GL2.GL_TEXTURE_2D);
		earthTid = TextureLoader.setupTextures("./gfx/earth.png", gl);
		marsTid = TextureLoader.setupTextures("./gfx/mars.png", gl);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

		this.earth = new Sphere(glu, 10, -40, 10, 10);
		this.mars = new Sphere(glu, 8, 40, 10, 10);
	}
	
	/// Aktualizacja czasu, który up³yn¹³ od ostatniej klatki
	private void CalcTime() {
		long now = System.nanoTime();
		this.delta = (now - this.lastTime)/(1000* 1000);
		this.lastTime = now;
	}

	@Override
	/// Called by the drawable to initiate OpenGL rendering by the client. After all
	/// GLEventListeners have been notified of a display event, the drawable will
	/// swap its buffers if setAutoSwapBufferMode is enabled.
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		
		gl.glLoadIdentity();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);		

		this.CalcTime();
		this.camera.Update(delta);
		this.setGlobalLight(gl);
		
		// planeta
		this.planet.DrawExt(gl, glu);
		
		// stacja kosmiczna #1
		gl.glPushMatrix();
		gl.glScalef(2f, 2f, 2f);
		this.cosmicStation.SetMaterial(gl);
		this.cosmicStation.Draw(gl, glu);
		gl.glPopMatrix();
		
		// stacja kosmiczna #2 - ta po ciemnej stronie
		gl.glPushMatrix();
		gl.glTranslatef(this.planet.getRadius(), -this.planet.getRadius(), 0);
		gl.glRotatef(-90, 0.0f, 0.0f, 1.0f);
		this.cosmicStation.SetMaterial(gl);
		gl.glScalef(2f, 2f, 2f);
		this.cosmicStation.Draw(gl, glu);
		gl.glPopMatrix();
		
		// asteroida #1
		gl.glPushMatrix();
		gl.glTranslatef(this.satellite.x, this.satellite.y, this.satellite.z);
		this.satellite.SetMaterial(gl);
		this.satellite.Draw(gl, glu);
		gl.glPopMatrix();
		
		// asteroida #2
		gl.glPushMatrix();
		gl.glTranslatef(-this.satellite.x, this.satellite.y, this.satellite.z);
		gl.glRotatef(-120, 0.0f, 1.0f, 0.0f);
		this.satellite.SetMaterial(gl);
		this.satellite.Draw(gl, glu);
		gl.glPopMatrix();

		// model lampy
		gl.glPushMatrix();
		gl.glTranslatef(0, 1.0f, 0);
		gl.glRotatef(45, 0.0f, 1.0f, 0.0f);
		gl.glScalef(40,	40, 40);
		this.spotModel.opengldraw(gl);
		gl.glPopMatrix();
		
		// model ³azika
		gl.glPushMatrix();
		gl.glTranslatef(-10.0f, 5.45f, 5.0f);
		gl.glRotatef(160, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(-5, 0.0f, 0.0f, 1.0f);
		gl.glScalef(0.01f, 0.01f, 0.01f);
		this.curiosityModel.opengldraw(gl);
		gl.glPopMatrix();	
		
		// oteksturowane planety
		this.earth.DrawWithTexture(gl, glu, this.earthTid);
		this.mars.DrawWithTexture(gl, glu, this.marsTid);
		
		gl.glFlush();
	}

	@Override
	/// Called by the drawable during the first repaint after the component has been
	/// resized. The client can update the viewport and view volume of the window 
	/// appropriately, for example by a call to GL.glViewport(int, int, int, int); 
	/// note that for convenience the component has already called glViewport(x, y,
	/// width, height) when this method is called, so the client may not have to do 
	/// anything in this method. 
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(60, (double) getWidth() / getHeight(), 0.1, 1000);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// do nothing
	}

	/// Ustawienia œwiate³
	private void setGlobalLight(GL2 gl) {
		gl.glEnable(GL2.GL_LIGHTING);

		// œwiat³o ambient do pracy
		if (this.enableAmbient) {
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {-1, -1, -1, 0}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] {0.3f, 0.3f, 0.3f, 1.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glEnable(GL2.GL_LIGHT0);
		} else {
			gl.glDisable(GL2.GL_LIGHT0);
		}	
		
		// œwiat³o s³oneczne z oddali
		if (this.enableSunlight) {
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[] {-1, -1, -1, 0}, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[] {0.4f, 0.4f, 0.4f, 1.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, new float[] {0.6f, 0.6f, 0.6f, 1.0f}, 0);
			gl.glEnable(GL2.GL_LIGHT1);
		} else {
			gl.glDisable(GL2.GL_LIGHT1);
		}	

		// œwiat³o z latarni w kierunku satelity #1
		if (this.enableReflectorSpot) {
			gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, new float[] {0.1f, 2, 0.1f, 1}, 0);
			gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, new float[] {this.satellite.x, this.satellite.x, this.satellite.x}, 0);
			gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, new float[] {10}, 0);
			gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, new float[] {0.4f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, new float[] {0.6f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glEnable(GL2.GL_LIGHT2);
		} else {
			gl.glDisable(GL2.GL_LIGHT2);
		}
		
		// œwiat³o z ³azinka w kierunku satelity #2
		if (this.enableCuriositySpot) {
			this.CalcDynamicLightColor();
			gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_POSITION, new float[] {-5, 1, 0, 1}, 0);
			gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_SPOT_DIRECTION, new float[] {-this.satellite.x, this.satellite.x, this.satellite.x}, 0);
			gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_SPOT_CUTOFF, new float[] {30}, 0);
			gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_AMBIENT, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_DIFFUSE, this.rgbaDynamic, 0);
			gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_SPECULAR, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glEnable(GL2.GL_LIGHT3);
		} else {
			gl.glDisable(GL2.GL_LIGHT3);
		}
	}
	
	/// Wyliczenie nowego koloru dynamicznego œwiat³a
	private void CalcDynamicLightColor() {
		// p³ynne przejœcie przez 6 kolorów podstawowych w rgb czyli
		// 100, 110, 010, 011, 001, 101
		float one = 1.0f;
		float zero = 0.0f;
		if (this.rgbaDynamic[this.rgbaDynamicIndex] < one) {
			this.rgbaDynamic[this.rgbaDynamicIndex] = Math.min(
					this.rgbaDynamic[this.rgbaDynamicIndex] + (float)this.delta / 1000 / this.changeColorSpeed, 
					one);
		} else {
			int prevIndex = (this.rgbaDynamicIndex + 2) % 3;
			if (this.rgbaDynamic[prevIndex] > zero) {
				this.rgbaDynamic[prevIndex] = Math.max(
						this.rgbaDynamic[prevIndex] - (float)this.delta / 1000 / this.changeColorSpeed, 
						zero);
			} else {
				int nextIndex = (this.rgbaDynamicIndex + 1) % 3;
				if (this.rgbaDynamic[nextIndex] < one) {
					this.rgbaDynamic[nextIndex] = Math.min(
							this.rgbaDynamic[nextIndex] + (float)this.delta / 1000 / this.changeColorSpeed, 
							one);
				}
				if (this.rgbaDynamic[nextIndex] == one) {
					this.rgbaDynamicIndex = nextIndex;
				}
			}
		}
	}

	/// Punkt startowy aplikacji
	public static void main(String[] args) {
		JFrame window = new JFrame();
		final Scene scene = new Scene();
		window.getContentPane().add(scene);
		window.setSize(Scene.width, Scene.height);
		window.setVisible(true);
		scene.addKeyListener(new Keyboard(scene));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
