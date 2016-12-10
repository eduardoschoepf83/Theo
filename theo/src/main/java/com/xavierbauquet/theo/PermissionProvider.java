package com.xavierbauquet.theo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class PermissionProvider {

    private Context context;
    private Activity activity;

    PermissionProvider(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    void requestPermissions(String[] permissions) {
        List<String> permissionsToCheck = new ArrayList<>();

        // Get the list of requested permissions that are not permission granted
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToCheck.add(permission);
            }
        }

        // Request the permissions
        if (!permissionsToCheck.isEmpty() && Build.VERSION.SDK_INT >= 23) {
            String[] permissionsToRequest = permissionsToCheck.toArray(new String[permissionsToCheck.size()]);
            activity.requestPermissions(permissionsToRequest, Theo.REQUEST_CODE);
        }
    }

    boolean isPermissionGranted(String[] permissions, boolean snackbar){
        boolean result = true;
        List<String> permissionstoAsk = new ArrayList<>();
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                permissionstoAsk.add(permission);
                result = false;
            }
        }
        if(!result){
            String[] permissionsArray = new String[permissionstoAsk.size()];
            permissionsArray = permissionstoAsk.toArray(permissionsArray);
            makeSnackbar(permissionsArray).show();
        }
        return result;
    }

    private Snackbar makeSnackbar(final String[] permissions){
        String snackBarText = activity.getApplicationContext().getResources().getString(R.string.snack_bar_text);
        String snackBarButtonText = activity.getApplicationContext().getResources().getString(R.string.snack_bar_button);

        return Snackbar.make(activity.findViewById(android.R.id.content), snackBarText, Snackbar.LENGTH_LONG)
                .setAction(snackBarButtonText, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(permissions);
            }
        });
    }

    Context getContext() {
        return context;
    }

    void setContext(Context context) {
        this.context = context;
    }

    Activity getActivity() {
        return activity;
    }

    void setActivity(Activity activity) {
        this.activity = activity;
    }
}
