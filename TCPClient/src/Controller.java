import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
public class Controller {
	private static Timer timer = new Timer();
	
	private static int imageNumber=0;
	public static void start(int numberOfFrames){
		  //Viewer.initialize();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				 // new Thread(new Runnable() {
				  //       public void run(){
							  byte[] buf = FrameBuffer.getFromBuffer();
							  if(buf!=null){
								  System.out.println(imageNumber);
							 
							//	FileHandler.convertImages(buf, imageNumber++);
//							
								FileHandler.writeFile(buf, new String(""+imageNumber));
								imageNumber++;
								if(imageNumber>=numberOfFrames)
									this.cancel();
													  
							  }
				//         }
				//  }).start(); 

			  }
			  
			}, 5, 8);
	}
}
