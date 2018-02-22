/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;



/**
 * This class functions as superclass to all commandoes. 
 * All commandoes have register and value attached.
 * 
 * 
 * byte[] arr = { 0x00, 0x01 };
    ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
    short num = wrapped.getShort(); // 1

    ByteBuffer dbuf = ByteBuffer.allocate(2);
    dbuf.putShort(num);
    byte[] bytes = dbuf.array(); // { 0, 1 }
    * 
    * 
 * @author PerEspen
 */


import java.nio.ByteBuffer;

public class Commando
{
    //Class fields 
    private final byte commandAddress;
    private byte[] value;
    private int nrOfBytes = 1;
    
    //Flag for what controller this command is designated
    //public boolean linearRobot = false;
    //public boolean elevatorRobot = false;
    

    
    //Constructor
    public Commando(byte commandAddress)
    {
        this.commandAddress = commandAddress;
        //Creates value(byte[]) with default nr of bytes inside
        this.value = new byte[nrOfBytes];
        
        
    }

  
    
    /**
     * Returns the command address for this commando object
     * @return Returns the command address for this commando in byte
     */
    public byte getCmdAddr()
    {
        return this.commandAddress;
    }
    
    /**
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setIntValue(int intValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Integer.SIZE/8);
        dbuf.putInt(intValue);
         value = dbuf.array(); // { 0, 1 }
    }

    /**
     * Returns byte[] value as int
     * @return  Returns byte[] value as int 
     */
     public int getIntValue()
    {
        byte[] arr = value;
        ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
        int num = wrapped.getInt();// 1
        
        return num;
    }
        
   
     /**
      * Set byte[] value as byte[]
      * @param value Set byte[] value as byte[] 
      */
      public void setValue(byte[] value)
    {
       this.value = value;
    }
      
      /**
       * Returns byte[] that is value for this commando
       * @return    Returns byte[] that is value for this commando 
       */
      public byte[] getValue()
    {
       return this.value;
    }
      
      
       public byte getByteValue(int byteNr)
    {
       return this.value[byteNr];
    }
       
       
    public void setValue(byte b)
    {
       this.value[0] = b;
    }
    
    
    



    public int getNrOfBytes()
    {
        return nrOfBytes;
    }

    public void setNrOfBytes(int nrOfBytes)
    {
        this.nrOfBytes = nrOfBytes;
    }

    
   
    
}
