package com.example.detector007;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.text.MLLocalTextSetting;
import com.huawei.hms.mlsdk.text.MLText;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;

import java.io.IOException;
import java.util.List;

public class recoginse extends AppCompatActivity {

    static final int CAMERA_PERMISSION_CODE = 1001;
    private static final String TAG = "MyActivity";
    private MLTextAnalyzer analyzer;
    public Bitmap selectedBitmap;
    TextView textview;
    Button set_image,analyze_image;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoginse);

        set_image = findViewById(R.id.button);
        analyze_image = findViewById(R.id.button2);
        textview = findViewById(R.id.textView);
        image = findViewById(R.id.imageView);

        set_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                //launch activity to get result
                GetImage.launch(intent);
            }
        });

        analyze_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAnalyze();
//                createMLTextAnalyzer();
            }
        });

//        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//            requestPermission();
//        }
    } // OnCreate end...

    ActivityResultLauncher<Intent> GetImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result){
                    if (result.getResultCode() == Activity.RESULT_OK) {
//                  There are no request codes
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        try {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            image.setImageBitmap(selectedBitmap);
//                    testAnalyze(selectedBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private String displaySuccess(MLText mlText) {
        String res = "";
        List<MLText.Block> blocks = mlText.getBlocks();
        for (MLText.Block block : blocks) {
            for (MLText.TextLine line : block.getContents()) {
                res += line.getStringValue() + "\n";
            }
        }
        return res;
    }

    public void testAnalyze() {
        MLLocalTextSetting setting = new MLLocalTextSetting.Factory()
                .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE)
//                Specify languages that can be recognized.
                .setLanguage("en")
                .create();
        analyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting);

//        Create an MLFrame object using the bitmap, which is the image data in bitmap format.
        MLFrame frame = MLFrame.fromBitmap(selectedBitmap);
        Task<MLText> task = analyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<MLText>() {
            @Override
            public void onSuccess(MLText text) {
                // Processing for successful recognition.
                textview.setText(displaySuccess(text));
//                textview.setText("success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
//                Processing logic for recognition failure.
                Log.e(TAG, "failed: " + e.getMessage());
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            if (analyzer != null)
                analyzer.stop();
        } catch (IOException e) {
            Log.e(TAG, "Stop failed: " + e.getMessage());
        }
    }

//    private void requestPermission() {
//        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA) &&
//                !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
//                !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode != CAMERA_PERMISSION_CODE) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            return;
//        }
//        if(grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            Log.d(TAG, "camera permission granted");//camera permission granted
//        }
//    }
}