package cs2114.photomapper;

import java.util.Observable;
import student.android.ImageOverlay;

//---------------------------------------------------------
/**
 * This class Is the model for the photomapper app.
 * It only keeps track of the photo that the user has last clicked or added.
 *
 *
 *
 * @author Eric Lewis (airshp12)
 * @version 2012.2.24
 *
 */
public class PhotoMapper
    extends Observable
{
    private ImageOverlay currentOverlay;


    /**The constructor for the PhotoMapper model.
     *
     */
    public PhotoMapper()
    {
        currentOverlay = null;
    }

    /**
     * This method sets the chosen overlay
     * @param newOverlay the new Overlay that is clicked or added
     */
    public void setCurrentOverlay(ImageOverlay newOverlay)
    {
        currentOverlay = newOverlay;
    }

    /**
     * This method gets the photo that the user currently has selected.
     * @return the overlay that is currently selected by the user.
     */
    public ImageOverlay getCurrentOverlay()
    {
        return currentOverlay;
    }
}
