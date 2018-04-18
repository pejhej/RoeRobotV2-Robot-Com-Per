
package roerobot.imageprocessing;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;

/**
 * A class handling handle camera activity, open camera, take picture
 *
 * @author Kristian Andre Lilleindset
 * @version 13-02-2018
 */

public class Camera 
{
    // Which connected camera to open
    private final int camToOpen = 1;
    
    // Picture frame
    private final Mat frame;
    
    // Timestamp of last image
    private long timestamp;
    
    // Camera
    private final VideoCapture cam;
    
    
    public Camera() 
    {
        // Open a camerasource
        this.cam = new VideoCapture(this.camToOpen);
        
        // create a Mat frame variable
        this.frame = new Mat();
        
        // set the desired width and height of the pictures taken
        this.cam.set(CAP_PROP_FRAME_WIDTH, 1920);
        this.cam.set(CAP_PROP_FRAME_HEIGHT, 1080);
    }
   

    
    /**
     * Take a picture and return the Mat frame.
     * 
     * @return RoeImage with picture and properties
     */
    public RoeImage takePicture() 
    {
        // return variable
        RoeImage result = new RoeImage();
        
        // take picture 
        this.cam.read(this.frame);
        
        // update timestamp for image capturing
        this.timestamp = System.currentTimeMillis();
        
        // add picture to result
        result.SetImage(this.frame);
        
        // add timestamp of captured image
        result.setTimeStamp(this.timestamp);

        // the image captured with properties
        return result;
    }
}
