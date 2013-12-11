import javax.media.opengl.glu.GLU;

public class Camera {
	private GLU glu;
	
	/// Zawiera zmiany kierunku patrzenia i obrotu (strza³ki na klawiaturze s¹ wciœniête)
	private double[] angleDelta;
	
	/// Moja macierz widoku, zawiera aktualny kierunek patrzenia oraz kierunek do góry
	/// jest modyfikowana poprzez obracanie siê (strza³ki na klawiaturze)
	private Matrix view;
	
	/// Aktualna pozycja w przestrzeni
	private Matrix position;
	
	/// Przechowuje kierunek w którym siê poruszamy
	/// Jest modyfikowana za pomoca klawiszy ASWD oraz QE
	private Matrix direction;

	public Camera(double x, double y, double z) {
		this.glu = new GLU();
		this.angleDelta = new double[] {0, 0 ,0};
		this.position = new Matrix(new double[][] {{x},{y},{z},{1}});
		this.direction = new Matrix(new double[][] {{0},{0},{0},{0}});
		this.view = new Matrix(new double[][] {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});		
	}

	public Camera() {
		this(0, 0, 0);
	}

	public void Update(long miliseconds) {
		this.UpdateAngles();
		glu.gluLookAt(this.position.Get(0,0), this.position.Get(1,0), this.position.Get(2,0),
				this.position.Get(0,0) + this.view.Get(0, 2), this.position.Get(1,0) + this.view.Get(1, 2), this.position.Get(2,0) + this.view.Get(2, 2),
				this.view.Get(0, 1), this.view.Get(1, 1), this.view.Get(2, 1));
	}
	
	/// Aktualizacja wszystkich macierzy
	private void UpdateAngles() {
		/// aktualizacja zmian kierunku obrtu
		double sa = Math.sin(this.angleDelta[0]);
		double ca = Math.cos(this.angleDelta[0]);
		double sb = Math.sin(this.angleDelta[1]);
		double cb = Math.cos(this.angleDelta[1]);
		double sg = Math.sin(this.angleDelta[2]);
		double cg = Math.cos(this.angleDelta[2]);
		
		Matrix mx = new Matrix(new double[][] {
			{1  ,0  ,0  ,0},
			{0  ,ca ,sa ,0},
			{0  ,-sa,ca ,0},
			{0  ,0  ,0  ,1}
		});
		
		Matrix my = new Matrix(new double[][] {
			{cb ,0 ,-sb,0},
			{0  ,1  ,0  ,0},
			{sb ,0  ,cb ,0},
			{0  ,0  ,0  ,1}
		});
		
		Matrix mz = new Matrix(new double[][] {
			{cg ,sg ,0  ,0},
			{-sg,cg ,0  ,0},
			{0  ,0  ,1  ,0},
			{0  ,0  ,0  ,1}
		});
		
		/// wyznaczenie zmiany kierunku patrzenia
		Matrix angleMatrix = mx.times(my.times(mz));

		/// wyznaczenie nowego kierunku patrzenia
		this.view = this.view.times(angleMatrix);

		/// wyznaczenie nowej pozycji w przestrzeni
		this.position = this.position.plus(this.view.times(this.direction));
	}
	
	/// Ustawienie kierunku poruszania siê (WSADQE)
	public void ChangePosition(int dir, double distance) {
		distance *= 0.5;
		this.direction.Set(dir, 0, distance);
	}

	/// Ustawienie kierunku obrotów siê (strza³ki)
	public void ChangeAngle(int angleIndex, double degrees) {
		degrees = degrees * 2 * Math.PI / 360;
		this.angleDelta[angleIndex] = degrees;
	}
}
