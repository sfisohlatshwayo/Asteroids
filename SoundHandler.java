package gh.asteroids1;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Provides utilities for instantiating and using 
 * sound clips.
 */
public class SoundHandler {
    
    private AudioInputStream sound;
	 private Clip clip;
	 private boolean looping = false;  //play continuously
	 private int repeat = 0;  //no. of times to play
	 private String filename = "";  //sound file
	 
	 /**
	  * Default constructor - no args.
	  */
	 public SoundHandler() {
        try {
            //create a sound buffer
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) { }
    }
    
    /**
     * Constructs a <tt>SoundHandler</tt> with 
     * supplied sound file.
     * @param filename the name of the sound sample
     */
    public SoundHandler(String filename) {
        try {
            //create a sound buffer
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) { }
        
        //load the sound file
        load(filename);
    }
    
    /**
     * Accessor method for the internal 
     * sound clip.
     * @return the sound clip
     */
    public Clip getClip() { return clip; }
    
    /**
     * Accessor method for the looping 
     * status of the clip.
     * @return whether the clip plays continuously
     */
    public boolean getLooping() {
    		return looping;
    }
    
    /**
     * Mutator method for the looping status.
     * @param loop to loop or not to loop
     */
    public void setLooping(boolean loop){
    		looping = loop;
    }
    
    /**
     * Accessor method for the repeat property.
     * @return the number of times the sound will repeat
     */
    public int getRepeat(){
    		return repeat;
    }
    
    /**
     * Mutator method for the repeat property
     * @param times the number of times to repeat the sound; 
     * the repeats are over and above the first play of the sound -
     * setting repeat to 1 will play the sound twice.
     */
    public void setRepeat(int times){
    		repeat = times;
    }
    
    /**
     * Accessor method for the name of the sound file.
     * @return the sound file name
     */
    public String getFilename(){
    		return filename;
    }
    
    /**
     * Mutator method for the name of the sound file
     * @param fnam the name of the sound file
     */
    public void setFilename(String fnam){
    		filename = fnam;
    }
    
    /**
     * Verifies when sound is loaded and ready to play
     * @return the loaded status of the sound
     */
    public boolean isLoaded() {
        return (boolean)(sound != null);
    }

    /*
     * Generic method for filename path extraction.
     * @param filename the name of the file
     * @return the file path to filename
     */
    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e) { }
        return url;
   }

    /**
     * Loads the sound file.
     * @param soundfile the name of the sound file to load
     * @return whether the load succeeded
     */
    public boolean load(String soundfile) {
        try {
            //prepare the input stream for an audio file
            setFilename(soundfile);
            //set the sound stream source
            sound = AudioSystem.getAudioInputStream(getURL(filename));
            //load the sound file
            clip.open(sound);
            return true;
        } catch (IOException e) {
            return false;
        } catch (UnsupportedAudioFileException e) {
            return false;
        } catch (LineUnavailableException e) {
            return false;
        }
    }
	 
	 /**
	  * Plays the sound file.
	  */
    public void play() {
        //exit if the sound hasn't been loaded
        if (!isLoaded()){
        		return;
        }

        //reset the sound clip
        clip.setFramePosition(0);

        //play sound with optional looping
        if (looping){
        		clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
        		clip.loop(repeat);
        }
    }
	 
	 /**
	  * Stops the sound playing.
	  */
    public void stop() {
        clip.stop();
    }

}