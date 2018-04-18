
package ImageProcessing;



import roerobotyngve.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 * Class handling image processing of a image. 
 * Searches for roe in a image.
 * 
 * returns the position of roe in the image by x-y direction. 
 * @author KristianAndreLilleindset
 * @version 17-04-2018
 */

public class ImageProcessing implements Runnable
{
    // list of images to process
    private final Stack<RoeImage> processQueue;
    
    // list of listeners to the image processing
    private final ArrayList<ImageProcessingListener> listeners;
    
    // debug variable
    private final boolean debug = false;
    
    public ImageProcessing()
    {
        // load dll file for opencv
        Må fikses link
        System.load("C:\\Users\\krist\\Dropbox\\skole\\6. semester\\Bachelor\\Rognhandteringsrobot-NTNU_Bacheloroppgave\\Programmering\\RoeRobot-ImageProcessing\\opencv\\build\\java\\x64\\opencv_java330.dll");

        
        
        
        // create lists for images and listeners
        this.processQueue = new Stack();
        this.listeners = new ArrayList(); 
    }   
    /**
     * Process image for detecting roe and update coordinates to image.
     */
    private void processImage()
    {
        // get image from processing queue
        RoeImage processingImage = this.processQueue.pop();
        
        // get RGB picture from image object
        Mat rgbImage = processingImage.getImage();
        
        // transform from RGB to grayscale        
        Mat grayImage = new Mat();
        Imgproc.cvtColor(rgbImage, grayImage, Imgproc.COLOR_RGB2GRAY);
        
        // brighten grayscale image
        Mat brightGrayImage = new Mat();
        grayImage.convertTo(brightGrayImage, -1, 5, 10);
        
        // binarize grayscale image 
        Mat bwImage = new Mat();
        Imgproc.threshold(grayImage, bwImage, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        
        // create structure element for eroding and dilating
        // using circular SE for keeping correct shape of desired objects
        Mat structureElement;
        structureElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(35,35));
        
        // erode image for removing unwanter artifacts
        Mat erodedImage = new Mat();
        Imgproc.erode(bwImage, erodedImage, structureElement);
        
        // dilate image restoring objects to correct size, use same SE as 
        // eroding for getting correct size.
        Mat dilatedImage = new Mat();
        Imgproc.dilate(erodedImage, dilatedImage, structureElement);
        
        // find BLOB's in image (Binary Large Objects)
        // finds rectangle surrounding the blob
        List<MatOfPoint> blobContours = new ArrayList();
        Imgproc.findContours(dilatedImage, blobContours, new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);  
        
        // find properties of contours 
        Moments blobProperties;
    
        for(int  i = 0; i < blobContours.size(); i++)
        {
            // get properties from contour of first blob
            blobProperties = Imgproc.moments(blobContours.get(i));
        
            // calculate center of blob in x and y direction, 
            // origo of image at top left corner
            int blobCenterX = (int) (blobProperties.m10/blobProperties.m00);
            int blobCenterY = (int) (blobProperties.m01/blobProperties.m00);
        
            // Recalculate to match robot coordinate system
            // origo being bottom right corner
            blobCenterX = rgbImage.width() - blobCenterX;
            blobCenterY = rgbImage.height() - blobCenterY;
            
            // create coordinate object
            Coordinate blobCentroid = new Coordinate(blobCenterY, blobCenterX, 0);
            
            // add coordinate of blob centroid to image being processed
            processingImage.addRoePosition(blobCentroid);
        }
        
        
        // notify listeners of image finished processing
        // add processed image as parameter
        for(ImageProcessingListener listener : this.listeners)
        {
            listener.notifyImageProcessed(processingImage);
        }
        
        
        //======================== DEBUG =============================//
        
        // add visual rectangle around the blobs detected
        Mat boundedBlobs = dilatedImage;
        Mat boundedBlobsCentroid = dilatedImage;
        if(debug)
        {
            for(int i = 0; i < blobContours.size(); i++)
            {   
                if (Imgproc.contourArea(blobContours.get(i)) > 1 )
                {
                    Rect rect = Imgproc.boundingRect(blobContours.get(i));       
                    if (rect.height > 1)
                    {
                        Imgproc.rectangle(boundedBlobs, new Point(rect.x,rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255,255,255));
                        Imgproc.rectangle(boundedBlobsCentroid, new Point(rect.x,rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255,255,255));

                    }
                }
                
                // get properties from contour of first blob
                blobProperties = Imgproc.moments(blobContours.get(i));
                
                // calculate center of blob in x and y direction, 
                // origo of image at top left corner
                int blobCenterX = (int) (blobProperties.m10/blobProperties.m00);
                int blobCenterY = (int) (blobProperties.m01/blobProperties.m00);
                
                // mark centroid of blobs
                Imgproc.circle(boundedBlobsCentroid, new Point(blobCenterX,blobCenterY), 1, new Scalar(0,0,0));
            }  
        }
    }
    
    
    /**
     * Add image to processing list.
     * 
     * Adds image to bottom of list (FiFo)
     * 
     * @param image to add to list
     */
    public synchronized void addImageToProcessingQueue(RoeImage image)
    {
        this.processQueue.add(image);
    }
    
    
    /**
     * Get image from processing list.
     * Returns image from top of list (FiFo)
     * 
     * @return image from top of list
     */
    public synchronized RoeImage getImageFromProcessingQueue()
    {
        return this.processQueue.pop();
    }
    
    
    /**
     * Get number of elements in processing queue
     * 
     * @return number of elements in processing queue
     */
    private synchronized int nmbrOfElementsInProcessingQueue()
    {
        return this.processQueue.size();
    }
    
    
    /**
     * Add listener to listener list
     * Listener must implement ImageProcessingListener interface
     * 
     * @param listener being added to list
     */
    public void addListener(ImageProcessingListener listener)
    {
        this.listeners.add(listener);
    }
    
    
    @Override
    public void run() 
    {
        //check if processing queue is empty, if not start processing image
        while(this.nmbrOfElementsInProcessingQueue() != 0)
        {
            this.processImage();
        }
    }
}

