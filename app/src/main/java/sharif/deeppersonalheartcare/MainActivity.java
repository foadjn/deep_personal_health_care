package sharif.deeppersonalheartcare;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.round_activity_main);
        TextView mTextView = findViewById(R.id.text);

        Context context = getApplicationContext();
        List<ApplicationInfo> packages;
        PackageManager pm = getPackageManager();
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        String myPackage = getApplicationContext().getPackageName();

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            if (packageInfo.packageName.equals(myPackage)) continue;
            mActivityManager.killBackgroundProcesses(packageInfo.packageName);
        }


        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}