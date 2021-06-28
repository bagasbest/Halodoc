package com.halodoc.halodoc.ui.buatjanji;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.halodoc.halodoc.R;

public class BuatJanjiDetailActivity extends AppCompatActivity {


    public static final String EXTRA_HOSPITAL = "hospital" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_janji_detail);
    }
}