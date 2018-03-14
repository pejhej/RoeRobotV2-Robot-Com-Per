/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Commands.Commando;

/**
 *
 * @author PerEspen
 */
public class Light extends Commando
{
    //Payload in this command
     private byte[] value;
    //Command address
    private static final byte COMMAND_ADDRESS = 0x11;
    
    public Light( )
    {
        super(COMMAND_ADDRESS);
    }
    
    
 
     @Override
      public void setValue(byte[] value)
    {
        //The length of the given byte[]
        byte incSize = (byte) value.length;
        //Create big enough byte[] to store the inc []
        this.value = new byte[incSize];
        //Save the size of the byte in the first byte
        for(int i = 0; i< incSize; ++i)
        {
            this.value[i] = value[i];
        }
        this.setNrOfBytes(incSize);
        //Save the incomming byte[] value in the class value
    
    }
    
      
      
      public void setOn()
      {
         byte[] controlByte = new byte[1];
        controlByte[0] = 1;
       
        this.setValue(controlByte);
      }
      
       public void setOff()
      {
         byte[] controlByte = new byte[1];
        controlByte[0] = 1;
       
        this.setValue(controlByte);
      }
}
