/*************************************************************************
 * Compilation: javac Matrix.java Execution: java Matrix
 * 
 * A bare-bones immutable data type for M-by-N matrices.
 * http://introcs.cs.princeton.edu/java/95linear/Matrix.java.html
 * 
 *************************************************************************/

final public class Matrix {
	private final int M; // number of rows
	private final int N; // number of columns
	private final double[][] data; // M-by-N array

	// create M-by-N matrix of 0's
	public Matrix(int M, int N) {
		this.M = M;
		this.N = N;
		data = new double[M][N];
	}

	public double Get(int i, int j) {
		return data[i][j];
	}
	
	public void Set(int i, int j, double value) {
		data[i][j] = value;
	}

	// create matrix based on 2d array
	public Matrix(double[][] data) {
		M = data.length;
		N = data[0].length;
		this.data = new double[M][N];
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				this.data[i][j] = data[i][j];
	}

	// copy constructor
	public Matrix(Matrix A) {
		this(A.data);
	}

	// create and return the N-by-N identity matrix
	public static Matrix identity(int N) {
		Matrix I = new Matrix(N, N);
		for (int i = 0; i < N; i++)
			I.data[i][i] = 1;
		return I;
	}

	// swap rows i and j
	/*
	 * private void swap(int i, int j) { double[] temp = data[i]; data[i] =
	 * data[j]; data[j] = temp; }
	 */

	// return C = A + B
	public Matrix plus(Matrix B) {
		Matrix A = this;
		if (B.M != A.M || B.N != A.N)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.data[i][j] = A.data[i][j] + B.data[i][j];
		return C;
	}

	// return C = A - B
	public Matrix minus(Matrix B) {
		Matrix A = this;
		if (B.M != A.M || B.N != A.N)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.data[i][j] = A.data[i][j] - B.data[i][j];
		return C;
	}

	// does A = B exactly?
	public boolean eq(Matrix B) {
		Matrix A = this;
		if (B.M != A.M || B.N != A.N)
			throw new RuntimeException("Illegal matrix dimensions.");
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				if (A.data[i][j] != B.data[i][j])
					return false;
		return true;
	}

	// return C = A * B
	public Matrix times(Matrix B) {
		Matrix A = this;
		if (A.N != B.M)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(A.M, B.N);
		for (int i = 0; i < C.M; i++)
			for (int j = 0; j < C.N; j++)
				for (int k = 0; k < A.N; k++)
					C.data[i][j] += (A.data[i][k] * B.data[k][j]);
		return C;
	}

	public Matrix times(double d) {
		Matrix A = this;

		Matrix C = new Matrix(A.M, A.N);
		for (int i = 0; i < C.M; i++)
			for (int j = 0; j < C.N; j++)
				C.data[i][j] = (A.data[i][j] * d);
		return C;
	}

	// print matrix to standard output
	public void show() {
		System.out.printf("%d ", M);
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++)
				System.out.printf("%9.4f ", data[i][j]);
			System.out.println();
		}
	}
	
	public static Matrix Rotate(double fi, Matrix v, Matrix w)
	{
		double c = Math.cos(fi);
		double s = Math.sin(fi);

		double dm[][] = {
				{v.Get(0,0)*v.Get(0,0)*(1-c)+c,     v.Get(0,0)*v.Get(0,1)*(1-c)-v.Get(0,2)*s,  v.Get(0,0)*v.Get(0,2)*(1-c)+v.Get(0,1)*s, 0},
				{v.Get(0,1)*v.Get(0,0)*(1-c)+v.Get(0,2)*s, v.Get(0,1)*v.Get(0,1)*(1-c)+c,      v.Get(0,1)*v.Get(0,2)*(1-c)-v.Get(0,0)*s, 0},
				{v.Get(0,0)*v.Get(0,2)*(1-c)-v.Get(0,1)*s, v.Get(0,1)*v.Get(0,2)*(1-c)+v.Get(0,0)*s,  v.Get(0,2)*v.Get(0,2)*(1-c)+c,     0},
				{0,                   0,                    0,                   1}
		};
		return w.times(new Matrix(dm));
	}
}
