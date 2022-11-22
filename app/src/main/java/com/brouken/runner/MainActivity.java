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
        for (ApplicationInfo applicationInfo : packageManager.getInstalledApplications(0)) {
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                if (!applicationInfo.enabled) {
                    Intent launchIntent = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
                    if (launchIntent != null) {
                        startActivity(launchIntent);
                    }
                }
            }
        }

        Intent intent = new Intent("com.google.android.finsky.VIEW_MY_DOWNLOADS");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        finish();
    }
}