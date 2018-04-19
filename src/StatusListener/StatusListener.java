/*
 * This class is the listener interface for the status classes.
 * It holds methods that can be called on all other classes implementing this
 * interface.
 */
package StatusListener;

import Status.Status;

/**
 *
 * @author KristianAndreLilleindset
 */
public interface StatusListener 
{
    /**
     * Notify new status
     * @param status being triggered
     */
    public void notifyNewStatus(Status status); 
}
