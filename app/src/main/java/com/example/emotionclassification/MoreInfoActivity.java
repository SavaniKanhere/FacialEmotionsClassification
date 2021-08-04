package com.example.emotionclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MoreInfoActivity extends AppCompatActivity {

    TextView tv;
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        tv = (TextView)findViewById(R.id.txt);
        myListView = (ListView)findViewById(R.id.myListView);

        ArrayList<String> res = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,res);
        myListView.setAdapter(arrayAdapter);

        res.add("Angry:\nThe best fighter is never angry");
        res.add("Disgust: \nSuccessful people radiate self-esteem, not self-disgust");
        res.add("Fear: \nWe generate fears while we sit, we overcome them by ACTION");
        res.add("Happy: \nBe happy with what you have. Be excited about what you want " );
        res.add("Neutral: \nTime is neutral and does not change things. With courage and initiative,leaders change things.");
        res.add("Sad:\nWhenever you feel sad just remember that there are billions of cells in your body and all they care about YOU" );
        res.add("Surprise: \nEvery day is a  surprise.");
    }
}