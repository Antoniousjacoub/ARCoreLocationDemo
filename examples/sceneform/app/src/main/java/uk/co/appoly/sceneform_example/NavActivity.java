package uk.co.appoly.sceneform_example;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;


public class NavActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    final static private String TAG = "NavActivity";
    private final int SOURCE_PLACE_PICKER_REQUEST = 12;
    private final int DEST_PLACE_PICKER_REQUEST = 22;

    private Intent intent;

    private GoogleApiClient mGoogleApiClient;

    private LatLng srcLatLong;
    private LatLng destLatLong;

    Button destPickBtn;
    Button navStartBtn;
    TextView destResultText;
    private LocationManager locationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        setContentView(R.layout.activity_nav);
        destPickBtn = findViewById(R.id.dest_pick_btn);
        navStartBtn = findViewById(R.id.nav_start_btn);
        destResultText = findViewById(R.id.dest_result_text);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        intent = new Intent();


        destPickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(NavActivity.this),
                            DEST_PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.d(TAG, "onClick: " + e.getMessage());
                }
            }
        });

        navStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationManager == null || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(NavActivity.this, "Please turn on GPS ", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(NavActivity.this,
                            LocationActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case DEST_PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    destResultText.setText(place.getName());
                    destLatLong = place.getLatLng();
                    Constants.DESTINATION_LAT = destLatLong.latitude;
                    Constants.DESTINATION_LOG = destLatLong.longitude;
                    Toast.makeText(this, "Destination selected", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected:  GoogleApiClient");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectedFailed:  GoogleApiClient");
    }
}
