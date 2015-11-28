package org.nameless.morckx.cmprofiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import cyanogenmod.app.IProfileManager;
import cyanogenmod.app.Profile;
import cyanogenmod.app.ProfileManager;

public class MainActivity extends Activity {

    private static final String TAG = "CMSetProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String profileName=getIntent().getDataString();
        IProfileManager ps = ProfileManager.getService();
        if(profileName == null || profileName.isEmpty()) {

            new AlertDialog.Builder(this)
                    .setTitle("CMSetProfile")
                    .setMessage("Activate CyanogenMod profiles from Tasker by starting CMSetProfile with a 'Launch App' task. Put the profile name you want to activate in the 'Data' argument.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            try {
                if (!ps.profileExistsByName(profileName)) {
                    String existing = "";
                    Profile[] allProfiles = ps.getProfiles();
                    for (int i = 0; i < allProfiles.length; i++) {
                        existing += allProfiles[i].getName();
                        if (i < allProfiles.length - 1) {
                            existing += ", ";
                        }
                    }
                    Log.e(TAG, String.format("Profile '%s' does not exist. Existing profiles are: %s.", profileName, existing));
                }
                ps.setActiveProfileByName(profileName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            finish();
        }
    }
}
