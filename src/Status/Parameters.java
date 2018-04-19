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

    private static final byte DEFAULT_BYTE_RANGE = Short.BYTES;

    /**
     * PARAMETERS*
     */
    private byte[] xRange;
    private byte[] yRange;
    private byte[] zRange;
    
    private byte numberOfTrays;

    public Parameters()
    {
        //Put superclass params
        super(COMMAND_ADDRESS, STATUS);
        //Initiate the values
        this.setShortXValue((short) 0);
        this.setShortYValue((short) 0);
        this.setShortZValue((short) 0);
        
        
    }

    /**
     * ***************************X VALUES****************************
     */
    /**
     * NUMBERS VALUES SETTER / GETTER *
     */
    /**
     * Set the byte[] value with an int of 2 significant numbers
     *
     * @param intValue The int to set to value
     */
    public void setIntXValue(int intValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Integer.SIZE / 8);
        dbuf.putInt(intValue);
        xRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * Returns byte[] value as int
     *
     * @return Returns byte[] value as int
     */
    public int getIntXValue()
    {
        int num = 0;
        if (Byte.compare(xRange[0], (byte) 0) != 0)
        {
            byte[] arr = xRange;
            ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
            num = wrapped.getInt();// 1
        }

        return num;
    }

    /**
     * Set the byte[] value with an Short of 2 significant numbers
     *
     * @param shortValue The Short to set to value
     */
    public void setShortXValue(short shortValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Short.BYTES);
        dbuf.putShort(shortValue);
        xRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * ***************************Y VALUES****************************
     */
    /**
     * NUMBERS VALUES SETTER / GETTER *
     */
    /**
     * Set the byte[] value with an int of 2 significant numbers
     *
     * @param intValue The int to set to value
     */
    public void setIntYValue(int intValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Integer.SIZE / 8);
        dbuf.putInt(intValue);
        yRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * Returns byte[] value as int
     *
     * @return Returns byte[] value as int
     */
    public int getIntYValue()
    {
        int num = 0;
        if (Byte.compare(yRange[0], (byte) 0) != 0)
        {
            byte[] arr = yRange;
            ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
            num = wrapped.getInt();// 1
        }
        return num;
    }

    /**
     * Set the byte[] value with an Short of 2 significant numbers
     *
     * @param shortValue The Short to set to value
     */
    public void setShortYValue(short shortValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Short.BYTES);
        dbuf.putShort(shortValue);
        yRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * ***************************Z VALUES****************************
     */
    /**
     * NUMBERS VALUES SETTER / GETTER *
     */
    /**
     * Set the byte[] value with an Short of 2 significant numbers
     *
     * @param shortValue The Short to set to value
     */
    public void setIntZValue(int shortValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Integer.SIZE / 8);
        dbuf.putInt(shortValue);
        zRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * Returns byte[] value as int
     *
     * @return Returns byte[] value as int
     */
    public int getIntZValue()
    {
        int num = 0;
        if (Byte.compare(zRange[0], (byte) 0) != 0)
        {
            byte[] arr = zRange;
            ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
            num = wrapped.getInt();// 1
        }
        return num;
    }

    /**
     * Set the byte[] value with an Short of 2 significant numbers
     *
     * @param shortValue The int to set to value
     */
    public void setShortZValue(short shortValue)
    {
        ByteBuffer dbuf = ByteBuffer.allocate(Short.BYTES);
        dbuf.putShort(shortValue);
        zRange = dbuf.array(); // { 0, 1 }
    }

    /**
     * Returns byte[] value as Short
     * @return Returns byte[] value as Short
     */
    public short getShortXValue()
    {
        short num = 0;
        if (xRange != null)
        {
            byte[] arr = xRange;
            ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
            num = wrapped.getShort();// 1
        }
        return num;
    }

    /**
     * Returns byte[] value as Short
     * @return Returns byte[] value as Short
     */
    public short getShortYValue()
    {     
        short num = 0;
        
        if (yRange != null)
        {
            byte[] arr = yRange;
            ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
            num = wrapped.getShort();// 1
        }
        
        return num;
    }

    /**
     * Returns byte[] value as Short
     *
     * @return Returns byte[] value as Short
     */
    public short getShortZValue()
    {
        short num = 0;
        if (zRange != null)
        {
            byte[] arr = zRange;
            ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
            num = wrapped.getShort();// 1
        }

        return num;
    }

    /**
     * ***********************BYTE METHODS***************
     * 
     * 
     * @param byteArr
     */
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
        ByteBuffer bb; 
        int lenghtCnt = 0;
        System.out.println(lenghtCnt);
      

        //Checks if there are multiple values, multiple values means its both x and y, maybe all
        if (inputVal.length >= DEFAULT_BYTE_RANGE * 2)
        {
            /*Copying and setting the X byte[]*/
            byte[] copy = new byte[DEFAULT_BYTE_RANGE];
            System.arraycopy(inputVal, 0, copy, 0, DEFAULT_BYTE_RANGE);
            this.setXByteArr(copy);
            lenghtCnt = lenghtCnt + DEFAULT_BYTE_RANGE;

            /*Copying and setting the Y byte[]*/
            copy = new byte[DEFAULT_BYTE_RANGE];
            System.arraycopy(inputVal, DEFAULT_BYTE_RANGE, copy, 0, DEFAULT_BYTE_RANGE);
            this.setYByteArr(copy);
            lenghtCnt = lenghtCnt + DEFAULT_BYTE_RANGE;
            
            //Means there are values After the XY-Bytes
            //Most certainly 
            if ((lenghtCnt + DEFAULT_BYTE_RANGE) <= inputVal.length)
            {
                /*Copying and setting the Y byte[]*/
                copy = new byte[DEFAULT_BYTE_RANGE];
                System.arraycopy(inputVal, (DEFAULT_BYTE_RANGE * 2), copy, 0, DEFAULT_BYTE_RANGE);
                this.setZByteArr(copy);
                lenghtCnt = lenghtCnt + DEFAULT_BYTE_RANGE;
            }
        } 
        //Only 1 value means its only z range
        else if (inputVal.length >= DEFAULT_BYTE_RANGE)
        {
            
            /*Copying and setting the X byte[]*/
            byte[] copy = new byte[inputVal.length];
            System.arraycopy(inputVal, 0, copy, 0, DEFAULT_BYTE_RANGE);
            this.setZByteArr(copy);
            lenghtCnt = lenghtCnt + DEFAULT_BYTE_RANGE;
        }
    }
    
    
    
    @Override
     public void trigger(byte[] val)
     {
         System.out.print("X value: ");
         System.out.println(this.getShortXValue());
         
          System.out.print("Y value: ");
         System.out.println(this.getShortYValue());
         
          System.out.print("Z value: ");
         System.out.println(this.getShortZValue());
     }
}
