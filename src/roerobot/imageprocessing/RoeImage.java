
package roerobot.imageprocessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opencv.core.Mat;


/**
 * Class represents image with the following properties:
 * - timestamp
 * - index of image in tray
 * - 
 *
 * @author KristianAndreLilleindset
 * @version 17-04-2018
 */
public class RoeImage 
{
    // image as Mat file
    private Mat image;
    
    // timestamp of image 
    private long timestamp;
    
    // frame index in tray
    private int pictureIndex;
    
    // list holding position of roe detected in the image
    private ArrayList<Coordinate> roePositions;
    
    public RoeImage()
    {
        // create list of roe positions
         roePositions= new ArrayList();
    }
    
    
    /**
     * Set image
     * 
     * @param image added to RoeImage
     */
    public void SetImage(Mat image)
    {
        this.image = image;
    }
    
    
    /**
     * Get image
     * 
     * @return Mat frame image
     */
    public Mat getImage()
    {
        return image;
    }
    
    
    /**
     * Set timestamp of the image captured
     * 
     * @param timestamp
     */
    public void setTimeStamp(long timestamp)
    {
        this.timestamp = timestamp;
    }
    
    
    /**
     * Get timestamp of image
     * 
     * @return timestamp of image
     */
    public long getTimestamp()
    {
        return this.timestamp;
    }
    
    
    /**
     * Add position of roe.
     * 
     * @param coordinate of roe 
     */
    public void addRoePosition(Coordinate coordinate)
    {
        this.roePositions.add(coordinate);
    }
            
    
    /**
     * Get iterator from list of roe postitions.
     * 
     * @return iterator from list of roe positions. 
     */
    public Iterator getRoePositionIterator()
    {
        return this.roePositions.iterator();
    }
    
    
    /**
     * Set picture index in coordinate system
     * 
     * @param pictureIndex of picture
     */
    public void setPictureIndex(int pictureIndex)
    {
        this.pictureIndex = pictureIndex;
    }
    
    
    /**
     * Get index of picture in coordinatesystem
     * 
     * @return pictureIndex in coordinatesystem
     */
    public int getPictureIndex()
    {
        return this.pictureIndex;
    }
}
