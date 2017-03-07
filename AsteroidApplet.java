package gh.asteroids1;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/**
 * A primitive Applet Asteroids-type game that runs in an applet window
 * 600 x 440. This Applet is designed to be a first step in the 
 * teaching of Java applet gaming.
 */
public class AsteroidApplet extends Applet implements Runnable, KeyListener {

    //the main thread becomes the game loop
    Thread gameloop;
    //use this as a double buffer
    BufferedImage backbuffer;
    //the main drawing object for the back buffer
    Graphics2D g2d;
    //toggle for drawing bounding boxes
    boolean showBounds = false;  //, started = false;

    //create the asteroid array
    final int ASTEROIDS = 10;
    Asteroid[] ast = new Asteroid[ASTEROIDS];

    //create the bullet array
    final int BULLETS = 10;
    Bullet[] bullet = new Bullet[BULLETS];
    int currentBullet = 0;

    //the player's ship
    Ship ship = new Ship();

    //create the identity transform (0,0)
    AffineTransform identity = new AffineTransform();

    //create a random number generator
    Random rand = new Random();

    //sound effects objects
    SoundHandler shoot;
    SoundHandler explode;
    
    /**
     * Initialises the instance variables of the applet.
     */
    @Override
    public void init() {
        //create the back buffer for smooth graphics
        backbuffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();

        //set up the ship
        ship.setX(320);
        ship.setY(240);

        //set up the bullets
        for (int n = 0; n<BULLETS; n++) {
            bullet[n] = new Bullet();
        }

        //set up the asteroids
        for (int n = 0; n<ASTEROIDS; n++) {
            ast[n] = new Asteroid();
            ast[n].setrVelocity(rand.nextInt(3)+1);
            ast[n].setX((double)rand.nextInt(600)+20);
            ast[n].setY((double)rand.nextInt(440)+20);
            ast[n].setMoveAngle(rand.nextInt(360));
            double ang = ast[n].getMoveAngle() - 90;
            ast[n].setVelX(calcAngleMoveX(ang));
            ast[n].setVelY(calcAngleMoveY(ang));
        }

        //load sound files
        shoot = new SoundHandler("shoot.wav");
        explode = new SoundHandler("explode.wav");

        //start the user input listener
        addKeyListener(this);
    }
    
    /**
     * Draws the <tt>Ship</tt> in the coordinate system; sets 
     * the position from an affine transform identity 0,0.
     */
    public void drawShip() {
        //draw the ship
        g2d.setTransform(identity);
        g2d.translate(ship.getX(), ship.getY());
        g2d.rotate(Math.toRadians(ship.getFaceAngle()));
        g2d.setColor(Color.ORANGE);
        g2d.fill(ship.getShape());
        //draw bounding rectangle around ship
        if (showBounds) {
            g2d.setTransform(identity);
            g2d.setColor(Color.BLUE);
            g2d.draw(ship.getBounds());
        }
    }
    
    /**
     * Draws the <tt>Bullet</tt>s in the coordinate system; sets 
     * the position from an affine transform identity 0,0. 
     * <tt>Bullet</tt>'s with <tt>this.isAlive() == false</tt>
     * are not drawn.
     */
    public void drawBullets() {
        for (int n = 0; n < BULLETS; n++) {
            if (bullet[n].isAlive()) {
                //draw the bullet
                g2d.setTransform(identity);
                g2d.translate(bullet[n].getX(), bullet[n].getY());
                g2d.setColor(Color.MAGENTA);
                g2d.draw(bullet[n].getShape());
            }
        }
    }
    
    /**
     * Draws the <tt>Asteroid</tt>s in the coordinate system; sets 
     * the position from an affine transform identity 0,0. 
     * <tt>Asteroid</tt>'s with <tt>this.isAlive() == false</tt>
     * are not drawn.
     */
    public void drawAsteroids() {
        for (int n = 0; n < ASTEROIDS; n++) {
            if (ast[n].isAlive()) {
                //draw the asteroid
                g2d.setTransform(identity);
                g2d.translate(ast[n].getX(), ast[n].getY());
                g2d.rotate(Math.toRadians(ast[n].getMoveAngle()));
                g2d.setColor(Color.DARK_GRAY);
                g2d.fill(ast[n].getShape());

                //draw bounding rectangle
                if (showBounds) {
                    g2d.setTransform(identity);
                    g2d.setColor(Color.BLUE);
                    g2d.draw(ast[n].getBounds());
                }
            }
        }
    }
    
    /**
     * Updates the game state and prints some info
     * on the game <tt>Ship</tt> object to the screen.
     * @param g the <tt>java.awt.Graphics</tt> object that 
     * does the drawing
     */
    @Override
    public void update(Graphics g) {
        //start off transforms at identity
        g2d.setTransform(identity);

        //erase the background
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, getSize().width, getSize().height);

        //print some status information
        g2d.setColor(Color.WHITE);
        g2d.drawString("Ship: " + Math.round(ship.getX()) + "," +
            Math.round(ship.getY()) , 5, 10);
        g2d.drawString("Move angle: " + Math.round(
            ship.getMoveAngle())+90, 5, 25);
        g2d.drawString("Face angle: " +  Math.round(
            ship.getFaceAngle()), 5, 40);

        //draw the game graphics
        drawShip();
        drawBullets();
        drawAsteroids();

        //repaint the applet window
        paint(g);
    }
    
    /**
     * Updates the position of all live the game objects
     * and performs a collision check.
     */
    private void gameUpdate() {
        updateShip();
        updateBullets();
        updateAsteroids();
        checkCollisions();
    }
    
    /**
     * Updates the position of the game <tt>Ship</tt>
     * object; performs a screen position wrap if the 
     * ship is moving off the screen.
     */
    public void updateShip() {
        //update ship's X position, wrap around left/right
        ship.incX(ship.getVelX());
        if (ship.getX() < -10)
            ship.setX(getSize().width + 10);
        else if (ship.getX() > getSize().width + 10)
            ship.setX(-10);
        //update ship's Y position, wrap around top/bottom
        ship.incY(ship.getVelY());
        if (ship.getY() < -10)
            ship.setY(getSize().height + 10);
        else if (ship.getY() > getSize().height + 10)
            ship.setY(-10);
    }
    
    /**
     * Updates the position of the <tt>Bullet</tt>s.
     */
    public void updateBullets() {
        //move the bullets
        for (int n = 0; n < BULLETS; n++) {
            if (bullet[n].isAlive()) {
                //update bullet's x position
                bullet[n].incX(bullet[n].getVelX());
                //bullet disappears at left/right edge
                if (bullet[n].getX() < 0 ||
                    bullet[n].getX() > getSize().width)
                {
                    bullet[n].setAlive(false);
                }
                //update bullet's y position
                bullet[n].incY(bullet[n].getVelY());
                //bullet disappears at top/bottom edge
                if (bullet[n].getY() < 0 ||
                    bullet[n].getY() > getSize().height)
                {
                    bullet[n].setAlive(false);
                }
            }
        }
    }
    
    /**
     * Updates the position of the live <tt>Asteroid</tt>s in 
     * the game. Performs a screen position wrap if they
     * are moving off the screen, and updates the rotation of
     * the asteroids.
     */
    public void updateAsteroids() {
        //move and rotate the asteroids
        for (int n = 0; n < ASTEROIDS; n++) {
            if (ast[n].isAlive()) {
                //update the asteroid's X value
                ast[n].incX(ast[n].getVelX());
                if (ast[n].getX() < -20)
                    ast[n].setX(getSize().width + 20);
                else if (ast[n].getX() > getSize().width + 20)
                    ast[n].setX(-20);

                //update the asteroid's Y value
                ast[n].incY(ast[n].getVelY());
                if (ast[n].getY() < -20)
                    ast[n].setY(getSize().height + 20);
                else if (ast[n].getY() > getSize().height + 20)
                    ast[n].setY(-20);

                //update the asteroid's rotation
                ast[n].incMoveAngle(ast[n].getrVelocity());
                if (ast[n].getMoveAngle() < 0)
                    ast[n].setMoveAngle(360 - ast[n].getrVelocity());
                else if (ast[n].getMoveAngle() > 360)
                    ast[n].setMoveAngle(ast[n].getrVelocity());
            }
        }
    }
    
    /**
     * Checks all live game objects for collisions. Bullets
     * and asteroids that have suffered collisions have
     * their <tt>alive</tt> property set to <tt>false</tt>.
     * Ship/asteroid collisions reset the ship to the 
     * centre of the screen and its velocity to zero.
     */
    public void checkCollisions() {
        //check for ship and bullet collisions with asteroids
        for (int m = 0; m<ASTEROIDS; m++) {
            if (ast[m].isAlive()) {
                //check for bullet collisions
                for (int n = 0; n < BULLETS; n++) {
                    if (bullet[n].isAlive()) {
                        //perform the collision test
                        if (ast[m].getBounds().contains(
                                bullet[n].getX(), bullet[n].getY()))
                        {
                            bullet[n].setAlive(false);
                            ast[m].setAlive(false);
                            explode.play();
                            continue;
                        }
                    }
                }

                //check for ship collision
                if (ast[m].getBounds().intersects(ship.getBounds())) {
                    ast[m].setAlive(false);
                    explode.play();
                    ship.setX(320);
                    ship.setY(240);
                    ship.setFaceAngle(0);
                    ship.setVelX(0);
                    ship.setVelY(0);
                    continue;
                }
            }
        }
    }
    
    /**
     * Not implemented in this class.
     * @param k the key event
     */
    @Override
    public void keyReleased(KeyEvent k) { }
    /**
     * Not implemented in this class.
     * @param k the key event
     */
    @Override
    public void keyTyped(KeyEvent k) { }
    
    /**
     * Handles the key presses that move the
     * ship around the screen and fire the bullets.
     * @param k the key event to handle
     */
    @Override
    public void keyPressed(KeyEvent k) {
        int keyCode = k.getKeyCode();

        switch (keyCode) {

        case KeyEvent.VK_LEFT:
            //left arrow rotates ship left 5 degrees
            ship.incFaceAngle(-5);
            if (ship.getFaceAngle() < 0) ship.setFaceAngle(360-5);
            break;

        case KeyEvent.VK_RIGHT:
            //right arrow rotates ship right 5 degrees
            ship.incFaceAngle(5);
            if (ship.getFaceAngle() > 360) ship.setFaceAngle(5);
            break;

        case KeyEvent.VK_UP:
            //up arrow adds thrust to ship (1/10 normal speed)
            ship.setMoveAngle(ship.getFaceAngle() - 90);
            ship.incVelX(calcAngleMoveX(ship.getMoveAngle()) * 0.1);
            ship.incVelY(calcAngleMoveY(ship.getMoveAngle()) * 0.1);
            break;

        //Ctrl, Enter, or Space can be used to fire weapon
        /*case KeyEvent.VK_CONTROL:
        case KeyEvent.VK_ENTER:*/
        case KeyEvent.VK_SPACE:
            //fire a bullet
            currentBullet++;
            if (currentBullet > BULLETS - 1) currentBullet = 0;
            bullet[currentBullet].setAlive(true);
            //point bullet in same direction ship is facing
            bullet[currentBullet].setX(ship.getX());
            bullet[currentBullet].setY(ship.getY());
            bullet[currentBullet].setMoveAngle(ship.getFaceAngle() - 90);
            //fire bullet at angle of the ship
            double angle = bullet[currentBullet].getMoveAngle();
            double svx = ship.getVelX();
            double svy = ship.getVelY();
            bullet[currentBullet].setVelX(svx + calcAngleMoveX(angle) * 2);
            bullet[currentBullet].setVelY(svy + calcAngleMoveY(angle) * 2);
            //play shoot sound
            shoot.play();
            break;
            
        /*case KeyEvent.VK_S:
            started = true;
            start();*/

        case KeyEvent.VK_B:
            //toggle bounding rectangles
            showBounds = !showBounds;
            break;
        }
    }
    
    /**
     * Calculates the movement angle x-component of an
     * on-screen live object. Uses <code>V<sub>x</sub> = Vcos(angle)</code>
     * @param angle the movement angle
     * @return the x-component of the movement angle
     */
    public double calcAngleMoveX(double angle) {
        return (double) (Math.cos(angle * Math.PI / 180));
    }
    
    /**
     * Calculates the movement angle y-component of an
     * on-screen live object. Uses <code>V<sub>y</sub> = Vsin(angle)</code>
     * @param angle the movement angle
     * @return the y-component of the movement angle
     */
    public double calcAngleMoveY(double angle) {
        return (double) (Math.sin(angle * Math.PI / 180));
    }
    
    /**
     * Initialises and runs the game thread.
     */
    @Override
    public void run(){
        //acquire the current thread
        Thread t = Thread.currentThread();
        //keep going as long as the thread is alive
        while (t == gameloop) {
            try {
                //update the game loop
                gameUpdate();
                //shoot for 50 fps
                Thread.sleep(20);
            }
            catch(InterruptedException e) {}
            repaint();
        }
    }
    
    /**
     * Starts the game thread.
     */
    @Override
    public void start() {
        /*if (gameloop == null)
            gameloop = new Thread(this);
        if(started)
            gameloop.start();*/
        gameloop = new Thread(this);
        gameloop.start();
    }
    
    /**
     * Draws the game window by painting the
     * screen buffer to the window.
     * @param g the graphics object that does the drawing
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(backbuffer, 0, 0, this);
    }
    
    /**
     * Sets the game thread to <tt>null</tt>.
     */
    @Override
    public void stop() {
        gameloop = null;
    }
    // TODO overwrite destroy() method?
}
