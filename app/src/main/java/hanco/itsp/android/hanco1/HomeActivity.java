package hanco.itsp.android.hanco1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    public static CameraManager cameraManager;

    public static CameraCharacteristics frontCamerachars;

    public static CameraCharacteristics rearCamerachars;
    public static Size rearsize;
    public static Size frontsize;



    private static final int REQUEST_CAMERA_PERMISSION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button ocrbutton=findViewById(R.id.home_ocr);
        Button logobutton=findViewById(R.id.home_logo);
        Button camerabutton=findViewById(R.id.home_camera);
        cameraManager=(CameraManager)getSystemService(CAMERA_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_PERMISSION);
        }
        try {
            frontCamerachars=cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[1]);
            StreamConfigurationMap frontconfigs=frontCamerachars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            frontsize=frontconfigs.getOutputSizes(ImageFormat.JPEG)[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            rearCamerachars=cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[0]);
            StreamConfigurationMap rearconfigs=rearCamerachars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            rearsize=rearconfigs.getOutputSizes(ImageFormat.JPEG)[0];


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        ocrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,OCRActivity.class);
                startActivity(intent);
            }
        });
        logobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,LogoActivity.class);
                startActivity(intent);
            }
        });
        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });
    }
}
