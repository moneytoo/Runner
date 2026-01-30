package com.brouken.runner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Build;

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
            if (!isTargetSleepApp(packageManager, applicationInfo)) {
                continue;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    && isPackageSuspended(packageManager, applicationInfo.packageName)) {
                continue;
            }
            if (!isTargetSleepApp(packageManager, applicationInfo)) {
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

    private boolean isPackageSuspended(PackageManager packageManager, String packageName) {
        try {
            return packageManager.isPackageSuspended(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean isTargetSleepApp(PackageManager packageManager, ApplicationInfo applicationInfo) {
        CharSequence label = applicationInfo.loadLabel(packageManager);
        if (label == null) {
            return false;
        }
        String appName = label.toString();
        return "Sleeping".equalsIgnoreCase(appName)
                || "Deep sleeping".equalsIgnoreCase(appName);
    }
}
