/**
 *
 */
package cs2114.photomapper;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import student.android.ImageOverlay;



/**
 * This Class tests the PhotoMapperActivity class.
 * It tests all the methods involving the interaction between the user and the
 * GUI.
 * @author Eric Lewis (airshp12)
 * @version 2012.2.24
 */
public class PhotoMapperActivityTest
    extends student.AndroidTestCase<PhotoMapperActivity>
{
    private PhotoMapper mapper1;
    private PhotoMapper mapper2;
    private ImageOverlay overlay1;
    private ImageOverlay overlay3;
    private static final String PATH1 = "/mnt/sdcard/download/photomapper1.jpg";
    private static final String PATH3 = "/mnt/sdcard/download/photomapper3.jpg";
    private Button addButton;
    private Button removeButton;
    private Button viewButton;
    private MapView mapView1;
    private MapView mapView2;
    private TextView text1;
    private TextView text2;


    /**
     * The constructor for the tester class
     */
    public PhotoMapperActivityTest()
    {
        super(PhotoMapperActivity.class);
    }


    /**
     * This method sets up the instance of the model as well as some sample
     * overlays for the tests
     */
    public void setUp()
    {
        mapper1 = new PhotoMapper();
        mapper2 = new PhotoMapper();
        overlay1 = new ImageOverlay(PATH1, new GeoPoint(37217000, -80402167));
        overlay3 = new ImageOverlay(PATH3, new GeoPoint(37229500, -80419833));
        addButton = getView(Button.class, R.id.addPhoto);
        removeButton = getView(Button.class, R.id.removePhoto);
        viewButton = getView(Button.class, R.id.viewPhoto);
        mapView1 = getView(MapView.class , R.id.mapView);
        mapView2 = getView(MapView.class , R.id.mapView);
        text1 = getView(TextView.class, R.id.statusLabel);
        text2 = getView(TextView.class, R.id.statusLabel);

        getActivity().setPhotoMapper(mapper1);
        getActivity().setMapView(mapView1);
        getActivity().setTextView(text1);
    }

    /**
     * This method tests all the initial declarations when the activity is
     * created
     */

    /**
     * This method tests the getters and setters of the data fields for
     * the activity
     */
    public void testSetterandGetters()
    {
        assertEquals(mapper1, getActivity().getPhotoMapper());
        assertEquals(mapView1, getActivity().getMapView());
        assertEquals(text1, getActivity().getTextView());

        getActivity().setPhotoMapper(mapper2);
        getActivity().setMapView(mapView2);
        getActivity().setTextView(text2);

        assertEquals(mapper2, getActivity().getPhotoMapper());
        assertEquals(mapView2, getActivity().getMapView());
        assertEquals(text2, getActivity().getTextView());

    }

    /**
     * This method tests the addButtonClicked Method.
     */
    public void testAddButton()
    {
        prepareToSelectMediaInChooser("photomapper1.jpg");
        click(addButton);

        assertEquals(1, mapView1.getOverlays().size());
        assertEquals(overlay1.getLocation().getLatitudeE6(),
            mapView1.getMapCenter().getLatitudeE6(), 1.0);

        assertEquals(overlay1.getLocation().getLongitudeE6(),
            mapView1.getMapCenter().getLongitudeE6(), 1.0);

        prepareToSelectMediaInChooser("photomapper4.jpg");
        click(addButton);
        assertEquals(1, mapView1.getOverlays().size());

    }

    /**
     * This method tests the removeButtonClicked method.
     */
    public void testRemoveButton()
    {
        prepareToSelectMediaInChooser("photomapper1.jpg");
        click(addButton);
        prepareToSelectMediaInChooser("photomapper2.jpg");
        click(addButton);
        prepareToSelectMediaInChooser("photomapper3.jpg");
        click(addButton);

        click(removeButton);
        assertFalse(mapView1.getOverlays().contains(overlay3));

        clickCoordinates(overlay1.getLocation());
        click(removeButton);
        assertFalse(mapView1.getOverlays().contains(overlay1));
    }

    /**
     * This method tests the viewButtonClicked method.
     */
    public void testViewButton()
    {
        prepareToSelectMediaInChooser("photomapper1.jpg");
        click(addButton);
        prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(viewButton);
        prepareToSelectMediaInChooser("photomapper4.jpg");
        click(addButton);
        prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(viewButton);
    }

    /**
     * This method tests what happens when an overlay is clicked
     *
     */
    public void testOnClick()
    {
        prepareToSelectMediaInChooser("photomapper1.jpg");
        click(addButton);
        prepareToSelectMediaInChooser("photomapper3.jpg");
        click(addButton);
        assertEquals("photomapper3.jpg",
            getActivity().getPhotoMapper().getCurrentOverlay().getFilePath());
        clickCoordinates(overlay1.getLocation());
        getActivity().getPhotoMapper().setCurrentOverlay(overlay1);

        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        assertEquals(PATH1,
            getActivity().getPhotoMapper().getCurrentOverlay().getFilePath());
    }

    // ----------------------------------------------------------
    /**
     * Centers the map on the specified coordinates and then clicks the middle
     * pixel, to simulate clicking on a particular geographic location on the
     * map.
     *
     * @param location the geographic location to click
     */
    private void clickCoordinates(GeoPoint location)
    {
        mapView1.getController().animateTo(location);

        click(mapView1, mapView1.getWidth() / 2, mapView1.getHeight() / 2);
    }

}
