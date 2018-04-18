
package ImageProcessing;

/**
 * Listener for the image processing.
 * notifyes when there are processed images in queue.
 * Listeners must implement this interface for being notifyed.
 *
 * @author KristianAndreLilleindset
 * @version 17-04-2018
 */
public interface ImageProcessingListener 
{

    /**
     * Notification of image finished processed
     * 
     * @param processedImage from image processing
     */
    public void notifyImageProcessed(RoeImage processedImage);
}
