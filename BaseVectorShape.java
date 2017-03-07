package gh.asteroids1;

import java.awt.Shape;

/**
 * Represents the commonality that is the Asteroids game objects.
 * Designed to act as the superclass from which all other game objects
 * (vector shapes) are derived.
 */
public class BaseVectorShape {
    private Shape shape;
    private boolean alive;
    private double X, Y;
    private double velX, velY;
    private double moveAngle, faceAngle;
    
    /**
     * Constructs a default <tt>BaseVectorShape</tt> with all instance 
     * variables set to false, zero or null.
     */
     public BaseVectorShape(){
        shape = null;
        alive = false;
        X = 0.0;
        Y = 0.0;
        velX = 0.0;
        velY = 0.0;
        moveAngle = 0.0;
        faceAngle = 0.0;
     }
    
    /**
     * Getter for the shape X-coord.
     * @return the X-coord
     */
    public double getX() {
        return X;
    }
    
    /**
     * Getter for the shape Y-coord.
     * @return the Y-coord
     */
    public double getY() {
        return Y;
    }

    /**
     * Getter for the activity status of the shape:
     * alive or not.
     * @return whether alive or not
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Getter for the angle rotated by the shape.
     * @return the face angle
     */
    public double getFaceAngle() {
        return faceAngle;
    }

    /**
     * Getter for the angle at which the shape is moving.
     * @return the movement angle
     */
    public double getMoveAngle() {
        return moveAngle;
    }

    /**
     * Getter for the shape itself.
     * @return the shape object
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Getter for the X-velocity of the shape.
     * @return the X-velocity component
     */
    public double getVelX() {
        return velX;
    }

    /**
     * Getter for the Y-velocity of the shape.
     * @return the Y-velocity component
     */
    public double getVelY() {
        return velY;
    }

    /**
     * Setter for the X-velocity of the shape.
     * @param X the new X-velocity value
     */
    public void setX(double X) {
        this.X = X;
    }

    /**
     * Setter for the Y-velocity of the shape.
     * @param Y the new Y-velocity value
     */
    public void setY(double Y) {
        this.Y = Y;
    }

    /**
     * Setter for the activity status of the shape:
     * alive or not.
     * @param alive is alive boolean
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Setter for the face angle of the shape.
     * @param faceAngle the new face angle of the shape
     */
    public void setFaceAngle(double faceAngle) {
        this.faceAngle = faceAngle;
    }

    /**
     * Setter for the move angle of the shape.
     * @param moveAngle the new move angle
     */
    public void setMoveAngle(double moveAngle) {
        this.moveAngle = moveAngle;
    }

    /**
     * Setter for the current shape.
     * @param shape the new shape of this object
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * Setter for the shape's X-velocity component.
     * @param velX the new x-Velocity value
     */
    public void setVelX(double velX) {
        this.velX = velX;
    }

    /**
     * Setter for the shape's Y-velocity component.
     * @param velY the new Y-velocity value
     */
    public void setVelY(double velY) {
        this.velY = velY;
    }
    
    /**
     * Increments the X-coord of the shape.
     * @param x the amount to increment by
     */
    public void incX(double x){ this.X += x; }
    
    /**
     * Increments the Y-coord of the shape.
     * @param y the amount to increment by
     */
    public void incY(double y){ this.Y += y; }
    
    /**
     * Increments the X-velocity of the shape.
     * @param x the amount to increment by
     */
    public void incVelX(double x){ this.velX += x; }
    
    /**
     * Increments the Y-velocity of the shape.
     * @param y the amount to increment by
     */
    public void incVelY(double y){ this.velY += y; }
    
    /**
     * Increments the face angle of the shape.
     * @param a the amount to increment by
     */
    public void incFaceAngle(double a){ this.faceAngle += a; }
    
    /**
     * Increments the movement angle of the shape.
     * @param a the amount to increment by
     */
    public void incMoveAngle(double a){ this.moveAngle += a; }
}
