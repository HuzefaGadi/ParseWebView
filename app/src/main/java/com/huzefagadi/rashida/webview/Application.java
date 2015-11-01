package com.huzefagadi.rashida.webview;

/**
 * Created by Rashida on 15/06/15.
 */
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class Application extends android.app.Application {

    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Parse SDK.
        Parse.initialize(this, "doxU7c7PGNa6lBNMuCwBpF1Btxu1VriGE2IGkrgT", "1J3SFE9UTA1LrcCx7tBnyxzG7r1ub1vSP6n6U3Xi");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // Specify an Activity to handle all pushes by default.
       // PushService.setDefaultPushCallback(this, MainActivity.class);
    }
}
