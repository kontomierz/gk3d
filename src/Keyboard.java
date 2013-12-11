import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Keyboard implements KeyListener {

	private Scene scene;

	public Keyboard(Scene scene) {
		this.scene = scene;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		// poruszanie
		case KeyEvent.VK_W:
			scene.camera.ChangePosition(2, 1);
			break;			
		case KeyEvent.VK_S:
			scene.camera.ChangePosition(2, -1);
			break;			
		case KeyEvent.VK_A:
			scene.camera.ChangePosition(0, 1);
			break;			
		case KeyEvent.VK_D:
			scene.camera.ChangePosition(0, -1);
			break;
		case KeyEvent.VK_Q:
			scene.camera.ChangePosition(1, 1);
			break;
		case KeyEvent.VK_E:
			scene.camera.ChangePosition(1, -1);
			break;

		// obroty
		case KeyEvent.VK_UP:
			scene.camera.ChangeAngle(0, 1);
			break;
		case KeyEvent.VK_DOWN:
			scene.camera.ChangeAngle(0, -1);
			break;
		case KeyEvent.VK_LEFT:
			scene.camera.ChangeAngle(1, -1);
			break;
		case KeyEvent.VK_RIGHT:
			scene.camera.ChangeAngle(1, 1);
			break;
		case KeyEvent.VK_COMMA:
			scene.camera.ChangeAngle(2, 1);
			break;
		case KeyEvent.VK_PERIOD:
			scene.camera.ChangeAngle(2, -1);
			break;
			
		// œwiat³o
		case KeyEvent.VK_0:
			scene.enableAmbient = !scene.enableAmbient;
			break;
		case KeyEvent.VK_1:
			scene.enableSunlight = !scene.enableSunlight;
			break;
		case KeyEvent.VK_2:
			scene.enableReflectorSpot = !scene.enableReflectorSpot;
			break;
		case KeyEvent.VK_3:
			scene.enableCuriositySpot = !scene.enableCuriositySpot;
			break;

		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		// poruszanie
		case KeyEvent.VK_W:
			scene.camera.ChangePosition(2, 0);
			break;			
		case KeyEvent.VK_S:
			scene.camera.ChangePosition(2, 0);
			break;			
		case KeyEvent.VK_A:
			scene.camera.ChangePosition(0, 0);
			break;			
		case KeyEvent.VK_D:
			scene.camera.ChangePosition(0, 0);
			break;
		case KeyEvent.VK_Q:
			scene.camera.ChangePosition(1, 0);
			break;
		case KeyEvent.VK_E:
			scene.camera.ChangePosition(1, 0);
			break;
			
		// obroty
		case KeyEvent.VK_UP:
			scene.camera.ChangeAngle(0, 0);
			break;
		case KeyEvent.VK_DOWN:
			scene.camera.ChangeAngle(0, 0);
			break;
		case KeyEvent.VK_LEFT:
			scene.camera.ChangeAngle(1, 0);
			break;
		case KeyEvent.VK_RIGHT:
			scene.camera.ChangeAngle(1, 0);
			break;
		case KeyEvent.VK_COMMA:
			scene.camera.ChangeAngle(2, 0);
			break;
		case KeyEvent.VK_PERIOD:
			scene.camera.ChangeAngle(2, 0);
			break;

		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
