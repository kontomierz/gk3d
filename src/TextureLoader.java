import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2;

import de.matthiasmann.twl.utils.PNGDecoder;


public class TextureLoader {
	public static int setupTextures(String filename, GL2 gl) {
		IntBuffer intBuffer = IntBuffer.allocate(1);
	    gl.glGenTextures(1, intBuffer);
	    intBuffer.rewind();
	    //GL11.glGenTextures(tmp);
	    //tmp.rewind();
	    try {
	        InputStream in = new FileInputStream(filename);
	        PNGDecoder decoder = new PNGDecoder(in);

	        System.out.println("width=" + decoder.getWidth());
	        System.out.println("height=" + decoder.getHeight());

	        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
	        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
	        buf.flip();

	        gl.glBindTexture(GL2.GL_TEXTURE_2D, intBuffer.get(0));
	        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
	        		GL2.GL_NEAREST);
	        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
	        		GL2.GL_NEAREST);
	        gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 4);
	        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buf);
	        int unsigned = (buf.get(0) & 0xff);
	        System.out.println(unsigned);
	        System.out.println(buf.get(1));
	        System.out.println(buf.get(2));
	        System.out.println(buf.get(3));

	    } catch (java.io.FileNotFoundException ex) {
	        System.out.println("Error " + filename + " not found");
	    } catch (java.io.IOException e) {
	        System.out.println("Error decoding " + filename);
	    }
	    intBuffer.rewind();
	    return intBuffer.get(0);
	}
}

//GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
