package com.brouken.runner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager packageManager = getPackageManager();
        String ownPackageName = getPackageName();
        for (ApplicationInfo applicationInfo : packageManager.getInstalledApplications(0)) {
            if (!applicationInfo.enabled) {
                continue;
            }
            if (ownPackageName.equals(applicationInfo.packageName)) {
                continue;
            }
            if (!isTargetSleepApp(applicationInfo)) {
                continue;
            }
            Intent launchIntent = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        }

        Intent intent = new Intent("com.google.android.finsky.VIEW_MY_DOWNLOADS");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        finish();
    }

    private boolean isTargetSleepApp(ApplicationInfo applicationInfo) {
        // Check if app is in sleeping or deep sleeping state
        // Sleeping apps are typically marked as suspended or stopped by Samsung
        boolean isSuspended = (applicationInfo.flags & ApplicationInfo.FLAG_SUSPENDED) != 0;
        boolean isStopped = (applicationInfo.flags & ApplicationInfo.FLAG_STOPPED) != 0;
        return isSuspended || isStopped;
    }
}
