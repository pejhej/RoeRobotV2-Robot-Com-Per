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
    //First input in value is the length of the byte[]
    private byte[] value;
    //Default is 1 bytes
    private int nrOfBytes = 1;
    
    //Flag for what controller this command is designated
    //public boolean linearRobot = false;
    //public boolean elevatorRobot = false;
    

    
    //Constructor
    public Commando(byte commandAddress)
    {
        this.commandAddress = commandAddress;
        //Creates value(byte[]) with default nr of bytes inside
        this.value = null;
        
        
    }

  
    
    /**
     * Returns the command address for this commando object
     * @return Returns the command address for this commando in byte
     */
    public byte getCmdAddr()
    {
        return this.commandAddress;
    }
    
    
    /**INTEGER VALUES SETTER / GETTER **/
    /**
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setIntValue(int intValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Integer.BYTES);
        dbuf.putInt(intValue);
         value = dbuf.array(); // { 0, 1 }
         setNrOfBytes(dbuf.capacity());
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
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setShortValue(short intValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Short.BYTES);
        dbuf.putShort(intValue);
         setValue(dbuf.array()); // { 0, 1 }
    }

    /**
     * Returns byte[] value as short
     * @return  Returns byte[] value as short 
     */
     public short getShortValue()
    {
        byte[] arr = value;
        ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
        short num = wrapped.getShort();// 1
        return num;
    }
 
        
   
     /**
      * Set byte[] value as byte[]
      * @param value Set byte[] value as byte[] 
      */
      public void setValue(byte[] value)
    {
        //The length of the given byte[]
        byte incSize = (byte) value.length;
        //Create big enough byte[] to store the inc []
        this.value = new byte[incSize+1];
        //Save the size of the byte in the first byte
        this.value[0] = incSize;
        
        this.setNrOfBytes(incSize);
        //Save the incomming byte[] value in the class value
        for(int i=1; i<=incSize; ++i)
            this.value[i] = value[(i-1)];
    }
      
      /**
       * Returns byte[] that is value for this commando
       * @return    Returns byte[] that is value for this commando 
       */
      public byte[] getValue()
    {
       return this.value;
    }
      
      
      /**
       * Returns the byte from the given index value, Null if value not presetn
       * @param byteNr the index of where to return byte
       * @return 
       */
       public byte getByteIndexValue(int byteNr)
    {
        if(getNrOfBytes()>= byteNr)
            return this.value[byteNr];
       
        return 0;
    }

       
       
/**
 * Return the nr of bytes set in nrOfBytes
 * @return Return the nr of bytes in value as int
 */
    public int getNrOfBytes()
    {
        return nrOfBytes;
    }
    
    /**
 * Return the nr of bytes in the value
 * @return Return the nr of bytes in value as int
 */
    public byte getNrOfBytesInByte()
    {
        int counter;
        
         return (byte) this.getValue().length; // { 0, 1 }
    }
    
    /**
     * Set the number of bytes in the value[]
     * @param nrOfBytes The integer to set nrOfBytes to
     */
    public void setNrOfBytes(int nrOfBytes)
    {
        this.nrOfBytes = nrOfBytes;
    }

    /**
     * Return the payload of this command, with first byte as number of bytes in payload
     * @return Return the payload including its size
     */
    public byte[] getByteWithSize()
            {
                //Create new byte array for added size to the value
                byte[] returnByte = new byte[this.value.length + 1];
                //Set the size in the spot in the byte array
                returnByte[0] = this.getNrOfBytesInByte();
                System.arraycopy(this.value, 0, returnByte, 1, this.value.length);
             
                
                return returnByte;
            }
   
    
    
    /**
     * Return the payload of this command, with first byte as number of bytes in payload
     * @return Return the payload including its size
     */
    public byte[] makeCompleteByte()
            {
                //Create new byte array for added size to the value
                byte[] returnByte = new byte[this.value.length + 2];
                
                //Set the size in the spot in the byte array
                returnByte[0] = this.getCmdAddr();
                returnByte[1] = (byte) this.getValue().length;
                System.arraycopy(this.getValue(), 0, returnByte, 2, this.value.length);
                
                return returnByte;
            }
}
