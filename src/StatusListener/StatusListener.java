/*
 * This class is the listener interface for the status classes.
 * It holds methods that can be called on all other classes implementing this
 * interface.
 */
package StatusListener;

/**
 *
 * @author KristianAndreLilleindset
 */
public interface StatusListener 
{
    /**
     * Notify on busy status.
     */
    public void notifyBusy();
    
    /**
     * Notify on EMC status.
     */
    public void notifyEMC();
            
    /**
     * Notify on elevator limit trigged status.
     */
    public void notifyElevatorLimitTrigged();

    /**
     * Notify on linear limit trigged status.
     */
    public void notifyLinearBotLimitTrigged();
    
    /**
     * Notify on calibration parameters status. 
     * 
     * @param calibParam parameters received from calibration
     */
    public void notifyParameters(String calibParam);
    
    /**
     * Notify on ready to recieve status.
     */
    public void notifyReadyToRecieve();
    
    /**
     * Notify on lower safety switch trigged status.
     */
    public void notifySafetySwitchLower();
    
    /**
     * Notify on uppr safgety switch trigged status.
     */
    public void notifySafetySwitchUpper();
    
    /**
     * Notify on stopped status.
     */
    public void notifyStopped();   
}
