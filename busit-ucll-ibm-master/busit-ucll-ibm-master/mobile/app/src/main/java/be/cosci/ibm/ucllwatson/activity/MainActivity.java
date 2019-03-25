package be.cosci.ibm.ucllwatson.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;

import be.cosci.ibm.ucllwatson.CameraPreview;
import be.cosci.ibm.ucllwatson.R;
import be.cosci.ibm.ucllwatson.db.PhotosRepository;
import be.cosci.ibm.ucllwatson.db.item.PhotoItem;
import be.cosci.ibm.ucllwatson.utils.ConnectionUtil;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int MY_PERMISSIONS_EXTERNAL_STORAGE = 2;

    private Camera mCamera;
    private CameraPreview mPreview;
    private PhotosRepository photosRepository;
    private String photoPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        requestPermissions();
        this.photosRepository = new PhotosRepository(this);
    }

    /**
     *
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            initializeCamera();
        }
    }

    /**
     * @param view
     */
    public void takeAPicture(View view) {
        if (ConnectionUtil.showNetworkInfoSnackbar(this, R.id.main_content)) {
            return;
        }
        mCamera.takePicture(null, null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                if (data != null) {
                    FileOutputStream outStream;
                    try {
                        String fName = System.currentTimeMillis() + ".jpg";
                        String savePath = getSaveFolder();
                        photoPath = savePath + File.separator + fName;
                        outStream = new FileOutputStream(photoPath);
                        outStream.write(data);
                        outStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();
                    PhotoItem photoItem = new PhotoItem("", photoPath, System.currentTimeMillis());
                    photosRepository.add(photoItem);
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * @return
     */
    public static String getSaveFolder() {
        String path = Environment.getExternalStorageDirectory().toString() + "/UCLLWatson";
        File f = new File(path);
        if (f.exists())
            return path;
        else
            f.mkdir();
        return path;
    }

    /**
     *
     */
    private void initializeCamera() {
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    /**
     * @param view
     */
    public void openHistory(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeCamera();
                } else {
                    requestPermissions();
                }
                break;
            }
            case MY_PERMISSIONS_EXTERNAL_STORAGE: {
                // TODO: 27-Mar-18
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    requestPermissions();
                }
            }
            break;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        return camera; // returns null if camera is unavailable
    }
}