package cs2114.photomapper;

import java.io.IOException;
import java.util.Observable;
import student.android.ImageOverlay;
import student.android.MediaUtils;
import android.content.Intent;
import student.android.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import cs2114.photomapper.R;
import android.graphics.Canvas;

// -------------------------------------------------------------------------
/**
 *  PhotoMapperActivity is the main screen for the photomapper app.
 *  This class responds to the clicks given by the user and requests
 *  the required information from the model.
 *
 *
 *  @author  Eric Lewis (airshp12)
 *  @version 2012.2.24
 */
public class PhotoMapperActivity extends MapActivity
{
    //~ Instance/static variables .............................................
    private PhotoMapper mapper;
    private static final int IMAGE_PICKED = 1;
    private TextView text;
    private MapView mapView;
    private ExifInterface exif;


  //~ Methods ...............................................................

    /** this method takes the user to a photo selector and adds the selected
     * image to the map. It centers the map on the picture when it is placed.
     * @param view the view that is clicked
     */

    public void addButtonClicked(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");

        startActivityForResult(intent, IMAGE_PICKED);
    }

    /** This method removes the selected picture from the map when the remove
     * button is clicked
     * @param view the view that is clicked
     */
    public void removeButtonClicked(View view)
    {
        if (!(mapper.getCurrentOverlay() == null))
        {
            mapView.getOverlays().remove(mapper.getCurrentOverlay());
            mapView.postInvalidate();
            mapper.setCurrentOverlay(null);
            text.setText("Photo Removed");
        }

        else
        {
            text.setText("Please select or add a photo.");
        }
    }

    /** This is the method brings up the selected picture when the view
     * button is clicked
     * @param view The view that is clicked
     */
    public void viewButtonClicked(View view)
    {
        if (!(mapper.getCurrentOverlay() == null))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(MediaUtils
                .uriForMediaWithFilename(getContentResolver(),
                     mapper.getCurrentOverlay().getFilePath()));

            startActivity(intent);
        }

        else
        {
            text.setText("Please select or add a photo.");
        }
    }

    /**
     * This method decides what to do when a new activity is started.
     * @param requestCode The constant that decides which activity to start.
     * @param resultCode Whether or not the activity is successful.
     * @param data The data taken from the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
        Intent data)
    {
        //if (requestCode == IMAGE_PICKED && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();

            String imagePath = MediaUtils.pathForMediaUri(getContentResolver(),
                uri);

            exif = null;
            try
            {
                exif = new ExifInterface(imagePath);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!(exif == null) && !(exif.getLatLong() == null))
            {

                ImageOverlay newOverlay = new ImageOverlay(imagePath,
                    exif.getLatLong());

                newOverlay.setOnClickListener(new OnClickListener());
                mapper.setCurrentOverlay(newOverlay);
                mapView.getOverlays().add(newOverlay);
                mapView.getController().animateTo(newOverlay.getLocation());
                text.setText(newOverlay.getLocation().toString());
            }
            else
            {
                text.setText("Photo does not have location data.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    /**
     * This class listens for any clicks on an overlay
     * @author Eric Lewis
     * @version 2012.2.24
     *
     */
    private class OnClickListener implements student.android.ImageOverlay
        .OnClickListener
    {

        /**
         *  This method writes in the info label, sets the clicked overlay as
         *  selected, and drags the view over to it when it is clicked on.
         */
        public void onClick(ImageOverlay overlay, MapView map)
        {
            mapper.setCurrentOverlay(overlay);
            text.setText(overlay.getLocation().toString());
            mapView.getController().animateTo(overlay.getLocation());
        }

    }

    /**
     * This class repaints the mapView when the model is updated
     * @author Eric Lewis
     * @version 2012.2.24
     *
     */
    private class MapObserver implements java.util.Observer
    {
        /**
         * This is the method that does the repainting
         */
        public void update(Observable observable, Object data)
        {
            mapView.postInvalidate();
        }
    }

    /** This method draws all the overlays currently on the map.
     * @param canvas the canvas being drawn on
     */
    public void onDraw(Canvas canvas)
    {
        for (Overlay overlay : mapView.getOverlays())
        {
            overlay.draw(canvas, mapView, false);
        }


    }

    //----------------------------------------------------------------
    /**
     * This method just returns the instance of the model it used.
     * @return the PhotoMapper instance
     */
    public PhotoMapper getPhotoMapper()
    {
        return mapper;
    }


    //-----------------------------------------------------------

    /**
     * This method sets the PhotoMapper model
     * @param newPhotoMapper the Photomapper being set to this activity
     */

    public void setPhotoMapper(PhotoMapper newPhotoMapper)
    {
        mapper = newPhotoMapper;
    }

    //-----------------------------------------------------------

    /**
     * This method gets the mapView of the activity
     * @return the MapView
     */
    public MapView getMapView()
    {
        return mapView;
    }


    //-------------------------------------------------------------

    /**
     * This method sets the MapView for the activity
     * @param newMapView the new mapView
     */

    public void setMapView(MapView newMapView)
    {
        mapView = newMapView;
    }

    //----------------------------------------------------------
    /**
     * This method gets the TextView of the activity
     * @return the TextView
     */
    public TextView getTextView()
    {
        return text;
    }


    //-------------------------------------------------------------

    /**
     * This method sets the TextView for the activity
     * @param newTextView the new TextView
     */

    public void setTextView(TextView newTextView)
    {
        text = newTextView;
    }

    // ----------------------------------------------------------
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState information that was saved the last time the
     *     activity was suspended
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        text = (TextView) findViewById(R.id.statusLabel);
        mapView = (com.google.android.maps.MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapper = new PhotoMapper();
        mapper.addObserver(new MapObserver());

    }


    // ----------------------------------------------------------
    /**
     * This method is required by the {@code MapActivity} class. Just return
     * false.
     *
     * @return false
     */
    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }
}