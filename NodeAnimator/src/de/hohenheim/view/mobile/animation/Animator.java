package de.hohenheim.view.mobile.animation;


/**
 * @author Marc Fernandes
 * @version 1.0
 * 
 * Interface which must be implemented by all animations.
 */
public interface Animator {
  /**
   * Starts the animation.
   */
  public void start();
  
  /**
   * Stops the animation.
   */
  public void stop();
  
  /**
   * Returns true if the animation is finished, i.e. when the 
   * animation has run once completely
   * @return boolean
   */
  public boolean isFinished();
  
  /**
   * Returns true if the animation was stopped succesfully.
   * @return
   */
  public boolean isStopped();
	
	
}
