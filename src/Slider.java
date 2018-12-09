/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

/**
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public class Slider extends Note{

    /**
     * Constructor
     */
    public Slider(int px, int width, int courtWidth, int courtHeight) {
        super(0, 0, 0);
    }

}