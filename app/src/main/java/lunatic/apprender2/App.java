package lunatic.apprender2;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by -Lunatic on 04/04/2016.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        SugarContext.init(getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }
}
