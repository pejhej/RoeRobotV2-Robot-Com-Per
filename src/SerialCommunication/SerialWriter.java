/*
 * This class is responsible for sending data from the Odroid to the Arduino
 */
package SerialCommunication;


import gnu.io.SerialPort;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author kristianandrelilleindset, edited by Fredrik Midtbø
 */
public class SerialWriter {

    // variable holding an instance of the serialport
    private final SerialPort serialPort;

    // variable holding the output
    private OutputStream output;

    // variable holding the data to be sent to the Arduino
    private byte[] dataToBeSent;

    // variable holding the number of the first byte being sent to the Arduino
    private final int startByteNr = 0;

    // variable holding the number of which expected to be the last byte in a 
    // message sent to the Arduino
    private final int stopByteNr = 7;

    
    // variable holding if there is new data available for sending 
    private volatile boolean dataAvailable;

    /**
     * creating an instance of a SerialWriter
     *
     * @param serialPort port found which is connected to the Arduino
     */
    public SerialWriter(SerialPort serialPort) {
        this.dataAvailable = false;
        this.serialPort = serialPort;
        
        dataToBeSent = new byte[20];
        // run the initialization of the serialwriter
        this.initialize();
    }

    /**
     * Create a
     */
    private void initialize() {
        // adding itself as a listener to the calculation
  

        // create an outputstream for sending data
        try {
            this.output = this.serialPort.getOutputStream();
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * sends the data received from the function call
     */
    public void sendData(byte[] bytesToSend) {
        // try to send the readied data
        try 
        {
            
            this.setDataToBeSent(bytesToSend);
            this.output.write(this.getDataToSend());
            output.flush();
        } catch (IOException ex) 
        {
            ex.printStackTrace();
            System.out.println("Serial: "+ex.toString());
        }
    }

    /**
     * Add calculator and add itself as listener to the calculator data
     * available
     *
     * @param calculator
     */
    /*
    public void addCalculator(DataCalculation calculator) 
    {
        this.calculator = calculator;
        this.calculator.addListener(this);
    }

    /**
     * Getting notified on data available from the calculation
     * @param val
     */
    /*
    @Override
    public synchronized void setCalculations(boolean val) {
        this.dataAvailable = val;
    }
    */
    
    /**
     * Getter method for the dataAvailable variable.
     */
    private synchronized boolean getCaluculations() {
        return this.dataAvailable;
    }
    
    private synchronized void setDataToBeSent(byte[] dataByte)
    {
        // setting the start and stopbytes of the data to be sent
        // making it easy for the Arduino to reecognize i this.calculator.getCalculatedData()f the message
        // is at the beginning when it starts to receive.
        
        int byteLength = dataByte.length;
        reziseSendData(byteLength);
        
        //Iterate through the incomming databyte and set it to the send byte
        for(int i=0; i < byteLength; ++i)
        {
            this.dataToBeSent[i] = dataByte[i];
        }
      
    }
    
    private synchronized byte[] getDataToSend()
    {
        return this.dataToBeSent;
    }
    
    private void reziseSendData(int byteLength)
    {
        this.dataToBeSent = new byte[byteLength];
    }
    
    private void resetSendData()
    {
         int byteLength = this.dataToBeSent.length;
        //Iterate through the incomming databyte and set it to the send byte
        for(int i=0; i < byteLength; ++i)
        {
            this.dataToBeSent[i] = 0;
        }
    }

 
            
    /**
     * 
     */
    /*
    @Override
    public void run() 
    {
        while (true) 
        {
            // check if there is new data available from the calculation
            if (this.dataAvailable)
            {
                // get the calculated data from the calculator and send it
                // with the sendData method.
                this.setDataToBeSent(this.calculator.getCalculatedData());

                this.sendData();
                // reset the dataAvailable variable to false
               // this.setCalculations(false);
            }
        }
    }
*/
}
