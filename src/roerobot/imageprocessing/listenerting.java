/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roerobot.imageprocessing;

import roerobotyngve.Coordinate;
import java.util.Iterator;

/**
 *
 * @author KristianAndreLilleindset
 */
public class listenerting implements ImageProcessingListener
{
    
    @Override
    public void notifyImageProcessed(RoeImage processedImage) 
    {
       Iterator myIt = processedImage.getRoePositionIterator();
        
        while(myIt.hasNext())
        {
            Coordinate corrd = (Coordinate) myIt.next();
            System.out.println(corrd.getxCoord() + " , " + corrd.getyCoord());
        }
    }
    
}
