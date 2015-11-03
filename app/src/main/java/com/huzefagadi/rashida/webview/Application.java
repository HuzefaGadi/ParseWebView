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
        Parse.initialize(this, "eLLqwAD7bBoSa2NC2LGCZfGA3OJuJBvaucmUgw6k", "JXVfRrMkFtEroBKhq3cVramzgNIiuhiuGLiCLunr");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // Specify an Activity to handle all pushes by default.
       // PushService.setDefaultPushCallback(this, MainActivity.class);
    }
}
