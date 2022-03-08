package com.example.detector007;

import static com.example.detector007.recoginse.CAMERA_PERMISSION_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

public class MainActivity extends AppCompatActivity {

    private Button button,button1;
    //
    private static final int REQUEST_CODE_SCAN_ONE = 0X01;
    static final int CAMERA_PERMISSION_CODE = 1001;
    private static final String TAG = "MyActivity";
    String a;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermission();
        }
        button=(Button) findViewById(R.id.button3);
        button1=(Button) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openmainActivity();
            }
        });
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openmainActivity2();
            }
        });
    }
    public void openmainActivity(){
        Intent intent= new Intent(this, recoginse.class);
        startActivity(intent);
    }
    public void openmainActivity2(){
        //Intent intent= new Intent(this, scanner.class);
        //startActivity(intent);
        ScanUtil.startScan(this, REQUEST_CODE_SCAN_ONE, new HmsScanAnalyzerOptions.Creator().create());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode!=RESULT_OK || data==null){
            return;
        }
        if(requestCode==REQUEST_CODE_SCAN_ONE){
            Object obj =data.getParcelableExtra(ScanUtil.RESULT);
            a=((HmsScan) obj).getOriginalValue();
            Intent intent= new Intent(this, result.class);
            intent.putExtra(result.NAME,a);
            startActivity(intent);
            //if(obj instanceof HmsScan){
            //if(!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())){
            //Toast.makeText(this,((HmsScan) obj).getOriginalValue(), Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),a,Toast.LENGTH_LONG).show();
            //}
            //   return;
            // }
        }
    }
    private void requestPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != CAMERA_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if(grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "camera permission granted");//camera permission granted
        }
    }
}