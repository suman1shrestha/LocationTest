package location.com.locationtest;

import android.Manifest;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShowLocation = (Button) findViewById(R.id.button);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Geocoder gCoder = new Geocoder(getApplicationContext());
                    ArrayList<Address> addresses = null;
                    try {
                        addresses = (ArrayList<Address>) gCoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses != null && addresses.size() > 0) {
                        Toast.makeText(getApplicationContext(), addresses.get(0).getAddressLine(0) +","+addresses.get(0).getLocality() +","+addresses.get(0).getAdminArea()+","+addresses.get(0).getPostalCode()+","+ addresses.get(0).getCountryName(), Toast.LENGTH_LONG).show();
                    }

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
    }
}