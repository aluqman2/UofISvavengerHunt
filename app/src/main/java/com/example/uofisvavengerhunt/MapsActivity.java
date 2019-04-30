package com.example.uofisvavengerhunt;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {



    private GoogleMap mMap;
    private Location lastLocation;
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;
    private GeofencingClient geofencingClient;

    private PendingIntent geofencePendingIntent;
    private ArrayList<Geofence> geofences = new ArrayList<>();

    private static String triviaMessage = "Welcome to Trivia";
    private static boolean answer = true;
    private static int score = 0;
    private GoogleSignInClient googleSignInClient;

    private RequestQueue q;

    private String currentTemp = "64";
    final TextView temperature = findViewById(R.id.temp);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermissions();
        createLocationRequest();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);

        Button jeff;
        Button signIn;

        jeff = findViewById(R.id.button);
        jeff.setOnClickListener(v -> {
            Log.d(TAG, "Test Dialog");
            triviaTest();
        });

        signIn = findViewById(R.id.signInButton);
        signIn.setOnClickListener(v -> {
            Log.e(TAG, "bruh, sign in shit");
            signIn();
        });

        temperature.setText(setTempText());
        temperature.setVisibility(View.VISIBLE);

        buildGeofences();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        try {
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
        } catch (SecurityException e) {
            Log.e(TAG, "no perms");
        }

        q = Volley.newRequestQueue(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.e(TAG, "fuck you");
        }
        // Add a marker in Sydney and move the camera
    }

    /**
     * creates new Dialog
     */
    private void triviaTest() {
        TriviaDialog trivia = new TriviaDialog();
        System.out.println(answer);
        System.out.println(score);
        trivia.show(getSupportFragmentManager(), "test");
        notification();
    }

    private String setTempText() {
        String set_text = currentTemp;
        return set_text;
    }
    //remember to at some point update the trivia message based on location of user

    /**
     * supporting method for TriviaDialog class
     * gives the string in triviaMessage to TriviaDialog to display to user
     * @return the string currently stored in triviaMessage
     */
    public static String getTriviaMessage() {
        return triviaMessage;
    }

    public static void setTriviaMessage(String setTrivia) {
        triviaMessage = setTrivia;
    }

    public static void setAnswer(boolean bool) {

        answer = bool;
    }

    public static boolean correctAnswer() {
        return answer;
    }

    public static void upScore() {
        score++;
    }

    public static int getScore() {
        //notification()
        return score;
    }

    /**
     * displays Snackbar popup
     */
    public void notification() {
        Snackbar notify = Snackbar.make(findViewById(R.id.myCoordinatorLayout), R.string.notify, Snackbar.LENGTH_LONG);
        notify.show();
    }

    /**
     * Make sure app has permissions
     */
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder request = new GeofencingRequest.Builder();
        request.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        request.addGeofences(geofences);
        return request.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionIntentService.class);
        geofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    /**
     * gedit those geofences
     */
    private void buildGeofences() {
        geofences.add(new Geofence.Builder()
                .setRequestId("Siebel")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(40.1138, -88.2252, 200)
                .setLoiteringDelay(10000)
                .build());

        geofences.add(new Geofence.Builder()
                .setRequestId("Union")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(40.1092, -88.2272, 200)
                .setLoiteringDelay(10000)
                .build());

        geofences.add(new Geofence.Builder()
                .setRequestId("UGL")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(40.1047, -88.2272, 200)
                .setLoiteringDelay(10000)
                .build());

        geofences.add(new Geofence.Builder()
                .setRequestId("Krannert")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                .setCircularRegion(40.1080, -88.2225, 50)
                .setLoiteringDelay(10000)
                .build());

        geofences.add(new Geofence.Builder()
                .setRequestId("ARC")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(40.1013, -88.2361, 50)
                .setLoiteringDelay(10000)
                .build());

        geofences.add(new Geofence.Builder()
                .setRequestId("Foellinger")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(40.1059, -88.2273, 200)
                .setLoiteringDelay(10000)
                .build());

        geofences.add(new Geofence.Builder()
                .setRequestId("Bookstore")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(40.1083, -88.2292, 200)
                .setLoiteringDelay(10000)
                .build());

        geofences.add(new Geofence.Builder()
                .setRequestId("Armory")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(40.1047, -88.2319, 200)
                .setLoiteringDelay(10000)
                .build());

        geofences.add(new Geofence.Builder()
                .setRequestId("McKinley")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(40.1028, -88.2199, 200)
                .setLoiteringDelay(10000)
                .build());
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 69);
    }
}
