/*
 * This class is the listener interface for the serial reader
 * implement method serialDataAvailable and add as listener to reader 
 * object for getting notified when message is recieved. 
 */
package SerialCommunication;

/**
 *
 * @author kristianandrelilleindset
 */
public interface SerialInputListener 
{
    /**
     * Method called by classes implementing this interface.
     */
    public void serialDataAvailable(String[] data);
}

