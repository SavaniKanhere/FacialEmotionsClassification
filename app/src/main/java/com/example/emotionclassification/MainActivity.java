package com.example.emotionclassification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.emotionclassification.ml.Model;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;
    private Button select, predict, capture, seemore;
    private TextView resultTextView;
    private Bitmap img;
    private String item = "";
    public String result;
    private LinearLayout resultLayout;
    //private LinearLayout mBottomSheetLayout;
    //private BottomSheetBehavior sheetBehavior;
    //private ImageView header_Arrow_Image;

    private void showBottomSheetDialog(String result) {

//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(R.layout.activity_bottom_sheet_layout);
//        bottomSheetDialog.show();
//        Log.d("PRINTING: ",result);
//        Log.d("TEXTVIEW: ",resultTextView.getRootView().toString());
//        resultTextView.setText("Result is: "+result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView)findViewById(R.id.imageView);
        //tv = (TextView)findViewById(R.id.textView);
        select = (Button)findViewById(R.id.button);
        predict = (Button)findViewById(R.id.button2);
        capture = (Button) findViewById(R.id.button3);
        //mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        //sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        //header_Arrow_Image = findViewById(R.id.bottom_sheet_arrow);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item = "Gallery";
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
                }

                item = "Camera";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result = "Result is here";

                View v1 = getLayoutInflater().inflate(R.layout.activity_bottom_sheet_layout,null);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
                bottomSheetDialog.setContentView(v1);

                try {

                    img = Bitmap.createScaledBitmap(img,224,224,true);

                    Model model = Model.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

                    TensorImage tensorImage = new TensorImage(DataType.UINT8);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Releases model resources if no longer used.
                    model.close();

                    result = "Angry: " + Math.round(outputFeature0.getFloatArray()[0]*0.3921) + "%\n\n" + "Disgust: " + Math.round(outputFeature0.getFloatArray()[1]*0.3921) + "%\n\n" + "Fear: " + Math.round(outputFeature0.getFloatArray()[2]*0.3921) + "%\n\n" + "Happy: " + Math.round(outputFeature0.getFloatArray()[3]*0.3921) + "%\n\n" + "Neutral: " + Math.round(outputFeature0.getFloatArray()[4]*0.3921) + "%\n\n" + "Sad: " + Math.round(outputFeature0.getFloatArray()[5]*0.3921) + "%\n\n" + "Surprise: " + Math.round(outputFeature0.getFloatArray()[6]*0.3921) + "%";

                    //tv.setText("Angry: " + Math.round(outputFeature0.getFloatArray()[0]*0.3921) + "%\n" + "Disgust: " + Math.round(outputFeature0.getFloatArray()[1]*0.3921) + "%\n" + "Fear: " + Math.round(outputFeature0.getFloatArray()[2]*0.3921) + "%\n" + "Happy: " + Math.round(outputFeature0.getFloatArray()[3]*0.3921) + "%\n" + "Neutral: " + Math.round(outputFeature0.getFloatArray()[4]*0.3921) + "%\n" + "Sad: " + Math.round(outputFeature0.getFloatArray()[5]*0.3921) + "%\n" + "Surprise: " + Math.round(outputFeature0.getFloatArray()[6]*0.3921) + "%");
                    //tv.setText(result);
                    //showBottomSheetDialog(result);

                } catch (Exception e) {
                    // TODO Handle the exception
                    result = "Something went wrong :(";
                    e.printStackTrace();
                }

                resultTextView = v1.findViewById(R.id.resultTextId);
                resultTextView.setText(result);
                seemore = v1.findViewById(R.id.seemore);

                seemore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,MoreInfoActivity.class);
                        startActivity(intent);
                    }
                });

                bottomSheetDialog.show();

            }
        });

        /*header_Arrow_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                header_Arrow_Image.setRotation(slideOffset * 180);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            if(item.equals("Gallery")) {
                imgView.setImageURI(data.getData());

                Uri uri = data.getData();
                try {
                    img = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {

                img = (Bitmap) data.getExtras().get("data");
                imgView.setImageBitmap(img);
            }

            item = "";
        }
    }
}