package com.example.uofisvavengerhunt;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

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
            List<Geofence> current = event.getTriggeringGeofences();
            System.out.println(current);
            //find current location
            //is current location located in one of the geofences? which one
            if (current.toString().contains("Siebel")) {
                MapsActivity.setTriviaMessage("The Thomas M. Siebel Center for Computer Science" +
                        " finished constuction in an astounding two days due to" +
                        " the advancements made in the department prior");
                MapsActivity.setAnswer(false);
                TriviaDialog trivia = new TriviaDialog();
            }
            if (current.toString().contains("Union")) {
                MapsActivity.setTriviaMessage("The Illini Union burned down due to a kitchen fire" +
                        " in Einstein Bros Bagels but was rebuilt in 1941");
                MapsActivity.setAnswer(false);
                TriviaDialog trivia = new TriviaDialog();
            }
            if (current.toString().contains("UGL")) {
                MapsActivity.setTriviaMessage("");
                MapsActivity.setAnswer(true);
                TriviaDialog trivia = new TriviaDialog();
            }
            if (current.toString().contains("Krannert")) {
                MapsActivity.setTriviaMessage("The floor of Krannert is made of teak wood" +
                        " from Thailand and marble walls from Italy!");
                MapsActivity.setAnswer(true);
                TriviaDialog trivia = new TriviaDialog();
            }
            if (current.toString().contains("ARC")) {
                MapsActivity.setTriviaMessage("The ARC takes up almost 20 square feet of space," +
                        " making it one of the largest facilities of its kind in the country");
                MapsActivity.setAnswer(false);
                TriviaDialog trivia = new TriviaDialog();
            }
            if (current.toString().contains("Foellinger")) {
                MapsActivity.setTriviaMessage("The Illinois Physics Department helped to fix a major" +
                        " sound problem in the auditorium");
                MapsActivity.setAnswer(true);
                TriviaDialog trivia = new TriviaDialog();
            }
            if (current.toString().contains("Bookstore")) {
                MapsActivity.setTriviaMessage("");
                MapsActivity.setAnswer(true);
                TriviaDialog trivia = new TriviaDialog();
            }
            if (current.toString().contains("Armory")) {
                MapsActivity.setTriviaMessage("");
                MapsActivity.setAnswer(true);
                TriviaDialog trivia = new TriviaDialog();
            }
            if (current.toString().contains("McKinley")) {
                MapsActivity.setTriviaMessage("");
                MapsActivity.setAnswer(true);
                TriviaDialog trivia = new TriviaDialog();
            }
        }
    }
}
