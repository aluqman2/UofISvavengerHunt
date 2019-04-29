package com.example.uofisvavengerhunt;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.content.ContentValues.TAG;

public class GeofenceTransitionIntentService extends IntentService {

    public GeofenceTransitionIntentService() {
        super("GeofenceTransition");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        int geofenceTransition = event.getGeofenceTransition();

        if (event.hasError()) {
            Log.e(TAG, "Network Location Provider isn't fucking here");
        }

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.e(TAG, "holy fuck");
        }
    }
}
