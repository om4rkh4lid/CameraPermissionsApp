package com.example.camerapermissionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button camButton;
    final int CAM_REQUEST_CODE = 1;
    Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init widget variables
        camButton = findViewById(R.id.button);

        //add listener to button
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked();
            }
        });

    }


    void onButtonClicked() {
        // TODO(2) - check that the android version is marshmallow or later because o.w the permission has already been granted at install-time or simply use Requiresapi(m)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // TODO: check that the HARDWARE FEATURE is available (in case the uses feature isn't used or if the required value is set to false)
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                // TODO(3) - check if the permission is already granted (checkselfpermission)
                /* if the permission isn't granted it's either because:
                    1. the first time being asked (case A)
                    2. the user denied (case B)
                    3. the user denied and said don't ask again (case C)
                    4. the permission is disabled on the device from settings (case D)
                */
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d("omar", "useCamera: here4");
                    useCamera();
                }
                // TODO(4) - if the permission isn't granted (returned false from checkSelfPermission statement)
                // TODO(4.1) - check for cases C and D and show the user a message that let's them know a permission is required (shouldShowPermissionRequestRationale)
                else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Camera permission required to use this feature", Toast.LENGTH_LONG).show();
                }
                // TODO(4.2) - request permission for cases A and B (will not do anything if it's case C or D) using (requestPermission)
                else{
                    //an array in case more than one permission is required
                    String[] permissions = {Manifest.permission.CAMERA};
                    //this request code will be used to specify the permission we are referring to in the onRequestPermissionsResult
                    requestPermissions(permissions, CAM_REQUEST_CODE);
                }

            }
        }
        // permission was already granted at runtime
        else {
            useCamera();
        }
    }


    // TODO(5) - check for the result of the requestPermission dialogue by overriding the (onRequestPermissionsResult) callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults //empty if the request permission dialogue was interrupted before an answer was given
    ) {
        // TODO: check that grantResults is not empty
        if(grantResults.length > 0){
            // TODO: check for the different request codes
            switch (requestCode){
                case CAM_REQUEST_CODE:
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        useCamera();

                    }else{
                        Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG);
                    }
                    break;
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void useCamera(){
        //get and release the camera instantly because this app is about permissions only
        mCamera = getCameraInstance();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            Toast.makeText(getApplicationContext(), "SUCCESSFUL", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Toast.makeText(getApplicationContext(), "FAILED", Toast.LENGTH_LONG).show();
        }
        return c; // returns null if camera is unavailable
    }

    /** releasing the camera */
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


}