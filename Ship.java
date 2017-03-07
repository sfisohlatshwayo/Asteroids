package gh.asteroids1;

import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * Models the spaceship used in the Asteroids Applet game.
 */
public class Ship extends BaseVectorShape {
    //the ship shape (polygon)
    private int[] shipXvals = {-6, -3, 0, 3, 6, 0};
    private int[] shipYvals = {6, 7, 7, 7, 6, -7};
    
    /**
     * Default constructor; constructs a <tt>Ship</tt> using the 
     * internal instance variable arrays for the coordinates
     * of the <tt>java.awt.Polygon</tt> that is the ship.
     */
    public Ship(){
        this.setShape(new Polygon(shipXvals, shipYvals, shipXvals.length));
        this.setAlive(true);
    }
    
    /**
     * Accessor for the ship bounding <tt>java.awt.Rectangle</tt>.
     * @return the bounding rectangle
     */
    public Rectangle getBounds(){
        Rectangle r = new Rectangle((int)this.getX() - 6, 
                (int)this.getY() - 6, 12, 12);
        return r;
    }
}
