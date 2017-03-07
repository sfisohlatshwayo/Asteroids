
package gh.asteroids1;

import java.awt.Rectangle;

/**
 * Models the bullets fired by the <tt>Ship</tt> in the Asteroid 
 * Applet game.
 */
public class Bullet extends BaseVectorShape {
   /**
    * Constructs a <tt>Bullet</> shape.
    */
   public Bullet(){
      this.setShape(new Rectangle(0, 0, 1, 1));
      this.setAlive(false);
   }
   
   /**
    * Creates and returns the bounding <tt>java.awt.Rectangle</tt>
    * of the <tt>Bullet</tt>.
    * @return the bounding rectangle
    */
   public Rectangle getBounds(){
      Rectangle r;
      r = new Rectangle((int)this.getX(), (int)this.getY(), 1, 1);
      return r;
   }
}
