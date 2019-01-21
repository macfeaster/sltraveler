package io.sektor.sltraveler;

import android.app.Application;

public class SLApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the app state with the application context
        // since this outlives individual activities and we don't
        // want an individual activity's lifecycle to power the app state
        ApplicationState.createInstance(getApplicationContext());
    }
}
