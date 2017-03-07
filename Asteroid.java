package gh.asteroids1;

import java.awt.Rectangle;
import java.awt.Polygon;

/**
 * Models the asteroid shape used in the Asteroids Applet game.
 */
public class Asteroid extends BaseVectorShape {
    //the asteroid polygonal shape
    private int[] roidX = {-20, -13, 0, 20, 22, 20, 12, 2, -10, -22, -16};
    private int[] roidY = {20, 23, 17, 20, 16, -20, -22, -14, -17, -20, -5};
    
    protected double rVelocity; //rotation velocity

    /**
     * Constructs an <tt>Asteroid</tt> with no rotational velocity,
     * and <tt>isAlive</tt> by default (see superclass).
     */
    public Asteroid() {
        this.rVelocity = 0.0;
        this.setAlive(true);
        this.setShape(new Polygon(roidX, roidY, roidX.length));
    }

    /**
     * Getter for the rotation velocity of the <tt>Asteroid</tt>.
     * @return the speed of rotation
     */
    public double getrVelocity() {
        return rVelocity;
    }

    /**
     * Setter for the <tt>Asteroid</tt> rotation velocity.
     * @param rVelocity the new rotation velocity
     */
    public void setrVelocity(double rVelocity) {
        this.rVelocity = rVelocity;
    }
    
    /**
     * Getter for the <tt>Asteroid</tt> bounding 
     * <tt>java.awt.Rectangle</tt>.
     * @return the bounding rectangle
     */
    public Rectangle getBounds(){
        Rectangle r = new Rectangle((int)this.getX() - 20, 
                (int)this.getY() - 20, 40, 40);
        return r;
    }
}
