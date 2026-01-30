diff --git a/app/src/main/java/com/brouken/runner/MainActivity.java b/app/src/main/java/com/brouken/runner/MainActivity.java
index 29c7bbd33ed251d5b6ad28d757350368c98ec988..b51032524d63b21b4c0fa57c7482711b20f3be5c 100644
--- a/app/src/main/java/com/brouken/runner/MainActivity.java
+++ b/app/src/main/java/com/brouken/runner/MainActivity.java
@@ -1,53 +1,46 @@
 package com.brouken.runner;
 
 import android.app.Activity;
 import android.content.Intent;
 import android.content.pm.ApplicationInfo;
 import android.content.pm.PackageManager;
 import android.os.Bundle;
-import android.os.Build;
 
 public class MainActivity extends Activity {
 
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
 
         PackageManager packageManager = getPackageManager();
         String ownPackageName = getPackageName();
         for (ApplicationInfo applicationInfo : packageManager.getInstalledApplications(0)) {
             if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                 continue;
             }
-            if (!applicationInfo.enabled) {
-                continue;
-            }
             if (ownPackageName.equals(applicationInfo.packageName)) {
                 continue;
             }
-            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
-                    && isPackageSuspended(packageManager, applicationInfo.packageName)) {
+            if (!isTargetSleepApp(applicationInfo)) {
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
 
-    private boolean isPackageSuspended(PackageManager packageManager, String packageName) {
-        try {
-            return packageManager.isPackageSuspended(packageName);
-        } catch (PackageManager.NameNotFoundException e) {
-            return false;
-        }
+    private boolean isTargetSleepApp(ApplicationInfo applicationInfo) {
+        boolean isSuspended = (applicationInfo.flags & ApplicationInfo.FLAG_SUSPENDED) != 0;
+        boolean isStopped = (applicationInfo.flags & ApplicationInfo.FLAG_STOPPED) != 0;
+        return isSuspended || isStopped;
     }
 }
