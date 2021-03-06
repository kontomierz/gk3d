import java.io.*;

import javax.media.opengl.*;

/**
 * Image loading class that converts BufferedImages into a data structure that
 * can be easily passed to OpenGL.
 * 
 * @author Pepijn Van Eeckhoudt Downloaded from:
 *         http://www.felixgers.de/teaching/jogl/
 */

// Uses the class GLModel from JautOGL to load and display obj files.
public class ModelLoaderOBJ {	
	public static GLModel LoadModel(String objPath, String mtlPath, GL2 gl)
	{
		GLModel model = null;
		
		try {
			FileInputStream r_path1 = new FileInputStream(objPath);
			BufferedReader b_read1 = new BufferedReader(new InputStreamReader(r_path1));
			model = new GLModel(b_read1, true, mtlPath, gl);
			r_path1.close();
			b_read1.close();
		} catch (Exception e) {
			System.out.println("LOADING ERROR" + e);
		}
		
		return model;
	}
}
