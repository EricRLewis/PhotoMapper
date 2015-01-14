package cs2114.photomapper;

import student.TestCase;
import student.android.ImageOverlay;


/**
 * The PhotoMapperTest class tests the model PhotoMapper
 * It simply runs J Unit tests.
 * @author Eric Lewis (airshp12)
 * @version 2012.2.24
 */
public class PhotoMapperTest
    extends TestCase
{
    private PhotoMapper mapper;
    private ImageOverlay overlay;

    /**
     * This method sets up the PhotoMapper and some overlays to be used
     * in the tests
     */
    public void setUp()
    {
        mapper = new PhotoMapper();
        overlay = new ImageOverlay("/mnt/sdcard/download/photomapper1.jpg",
            new com.google.android.maps.GeoPoint(37217000,  -80402167));
    }

    /**
     * This method tests the constructor to make sure it sets everything up
     * correctly. It also tests the getters and setters of the data fields
     */
    public void testSetterAndGetter()
    {
        assertEquals(null, mapper.getCurrentOverlay());
        mapper.setCurrentOverlay(overlay);
        assertEquals (overlay, mapper.getCurrentOverlay());
    }
}
