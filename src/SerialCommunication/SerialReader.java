/*
 * This class is responsible for reading the incoming data from the Arduino
 * When the input data has been read it is checked for the correct data. When 
 * the data has been accepted the listeners for the data is being notified. 
 */
package SerialCommunication;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TooManyListenersException;

/**
 * 
 * @author kristianandrelilleindset
 */
public class SerialReader implements SerialPortEventListener {

    // variable holding the serialport
    private final SerialPort serialPort;

    // variable holding the bufferedreader
    private BufferedReader input;

    // byte array holding the data being received
    private String[] inputData = null;
    
  //  private char[] inputString = null;

    // byte array holding the data that has been received and not being correupted
    private String[] acceptedData;

    // variable holding the number of the position of the first byte being received
    private final int firstStringNr = 0;

    // variable holding the number of the position of the last byte being received
    private final int lastStringNr = 3;

    // variable holding the value of the start-string
  //  private final String startString = "device";

    // variable holding the value of the stop-string
    private final String stopString = "stop";
    
    //New input data is recieved
    private boolean newDataRecieved = false;

    // arrayList holding all of the listeners interested in the serial communication
    private final ArrayList<SerialInputListener> listeners;

    /**
     *
     * @param serialPort, the serialport being used for serial communication
     * with the Arduino
     * @throws TooManyListenersException
     */
    public SerialReader(SerialPort serialPort) throws TooManyListenersException {
        // creating the arrayList of listeners 
        this.listeners = new ArrayList<>();

        this.inputData = new String[20];
        this.acceptedData = new String[20];

        this.serialPort = serialPort;
        this.initialize();
    }

    /**
     * Add a listener interested in the input data to the list. 
     * listener has to implement the CalculationListener interface
     *
     * @param listener
     */
    public synchronized void addListener(SerialInputListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Method adding eventlistener to the serialport and creating a buffered reader
     *
     * @throws TooManyListenersException
     */
    private void initialize() throws TooManyListenersException 
    {
        // add eventlisteners   
        this.serialPort.addEventListener(this);
        
        // add method for getting notified when data is recieved
        this.serialPort.notifyOnDataAvailable(true);
        
        // creates an inputstream for reading data 
        try 
        {
            this.input = new BufferedReader(new InputStreamReader(this.serialPort.getInputStream()));
        } catch (IOException ex) 
        {
            System.out.println("reader initialize" + ex.toString());
        }
    }

    /**
     * Handles event on the serial port. Checks if there is data coming in from
     * the serial port. Reads incoming data and saves it to an byte array. Then
     * checks if the message received contains the correct order of startbyte
     * and stopbyte and number of received bytes.
     *
     * @param oEvent
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        // check if the event is data being received on the serial port
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) 
        {
            try {
                
                // saving the input data to an string array, each element seperated by an "/"
                this.inputData = this.input.readLine().split(",");
                      
                    // notify all the listeners of data available for reading
                    this.notifyListeners(inputData);
                    // Fill the input data with null, so no stored values will 
                    //be carried over to next message
                    Arrays.fill(inputData, null);
              
            } catch (IOException ex) 
            {
            //    System.out.println("reader lesing " + ex.toString());
            }
        }
    }

    /**
     * Method notifying 
     * notify all listeners of data now available for reading listener has to
     * implement the CalculationListener interface
     */
    private synchronized void notifyListeners(String[] input) 
    {
        if (this.listeners != null) 
        {
            for (SerialInputListener listener : this.listeners) 
            {
                listener.serialDataAvailable(input);
            }
        }
    }

    /**
     * return the data received from the serial communication with the Arduino
     *
     * @return the data that has been accepted from the Arduino
     */
    public synchronized String[] getSerialData() {
        // return the accepted data.
        return this.acceptedData;
    }
}
