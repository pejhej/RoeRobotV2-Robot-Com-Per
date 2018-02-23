/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Status;

import java.nio.ByteBuffer;

/**
 *
 * @author PerEspen
 */
public class Parameters extends Status
{
         //Status name for this class
    private static final String STATUS = "PARAMETERS";
    
    //ADDRESS For this status command
    private static final byte COMMAND_ADDRESS = 0x70;
    
    private static final byte defaultByteRange = 2;
   
    
    /**PARAMETERS**/
    private byte[] xRange;
    private byte[] yRange;
    private byte[] zRange;
    
    
    
    public Parameters( )
    {
        super(COMMAND_ADDRESS, STATUS);
    }

    
    
    
    
    /*****************************X VALUES*****************************/
   /**NUMBERS VALUES SETTER / GETTER **/
    /**
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setIntXValue(int intValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Integer.SIZE/8);
        dbuf.putInt(intValue);
         xRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * Returns byte[] value as int
     * @return  Returns byte[] value as int 
     */
     public int getIntXValue()
    {
        byte[] arr = xRange;
        ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
        int num = wrapped.getInt();// 1
        
        return num;
    }
     
     /**
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setShortXValue(short shortValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Short.BYTES);
        dbuf.putShort(shortValue);
         xRange = dbuf.array(); // { 0, 1 }
    }
    
    
    
        /*****************************Y VALUES*****************************/
   /**NUMBERS VALUES SETTER / GETTER **/
    /**
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setIntYValue(int intValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Integer.SIZE/8);
        dbuf.putInt(intValue);
         yRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * Returns byte[] value as int
     * @return  Returns byte[] value as int 
     */
     public int getIntYValue()
    {
        byte[] arr = yRange;
        ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
        int num = wrapped.getInt();// 1
        
        return num;
    }
     
     /**
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setShortYValue(short shortValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Short.BYTES);
        dbuf.putShort(shortValue);
         yRange = dbuf.array(); // { 0, 1 }
    }
    
    
    
          /*****************************Z VALUES*****************************/
   /**NUMBERS VALUES SETTER / GETTER **/
    /**
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setIntZValue(int intValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Integer.SIZE/8);
        dbuf.putInt(intValue);
         zRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * Returns byte[] value as int
     * @return  Returns byte[] value as int 
     */
     public int getIntZValue()
    {
        byte[] arr = zRange;
        ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
        int num = wrapped.getInt();// 1
        
        return num;
    }
     
     /**
     * Set the byte[] value with an int of 2 significant numbers
     * @param intValue The int to set to value
     */
    public void setShortZValue(short shortValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Short.BYTES);
        dbuf.putShort(shortValue);
         zRange = dbuf.array(); // { 0, 1 }
    }
    
    
    
    
    /*************************BYTE METHODS****************/
    
    public void setXByteArr(byte[] byteArr)
    {
        this.xRange = byteArr;
    }
     
    
     public void setYByteArr(byte[] byteArr)
    {
        this.yRange = byteArr;
    }
     
     
      public void setZByteArr(byte[] byteArr)
    {
        this.zRange = byteArr;
    }
    
        public byte[] getXByteArr()
    {
        return this.xRange;
    }
     
    
         public byte[] getYByteArr()
    {
        return this.xRange;
    }
     
        public byte[] getZByteArr()
    {
        return this.xRange;
    }
      
        
        
    public static byte getCMD()
    {
        return COMMAND_ADDRESS;
    }
    
    
    
    
    @Override
    public void putValue(byte[] inputVal)
    {
        int lenghtCnt = inputVal.length;
        //Checks if there are multiple values, multiple values means its both x and y, maybe all
        if(inputVal.length > defaultByteRange)
        {
            /*Copying and setting the X byte[]*/
            byte[] copy = new byte[inputVal.length/2];
            System.arraycopy(inputVal, 0, copy, 0, defaultByteRange);
            this.setXByteArr(copy);
            lenghtCnt = lenghtCnt+defaultByteRange;
            
              /*Copying and setting the Y byte[]*/
            copy = new byte[inputVal.length/2];
            System.arraycopy(inputVal, defaultByteRange, copy, 0, defaultByteRange);
            this.setYByteArr(copy);
            lenghtCnt = lenghtCnt+defaultByteRange;
            
            if((lenghtCnt+defaultByteRange) >= inputVal.length)
            {
                  /*Copying and setting the Y byte[]*/
                copy = new byte[inputVal.length/2];
                System.arraycopy(inputVal, (defaultByteRange*2), copy, 0, defaultByteRange);
                this.setZByteArr(copy);
                lenghtCnt = lenghtCnt+defaultByteRange;
            }
        }
        else if(inputVal.length > 0)
        {
            /**Only 1 value means Z range**/
            /*Copying and setting the X byte[]*/
            byte[] copy = new byte[inputVal.length/2];
            System.arraycopy(inputVal, 0, copy, 0, defaultByteRange);
            this.setZByteArr(copy);
            lenghtCnt = lenghtCnt+defaultByteRange;
        }
    }

      
}
