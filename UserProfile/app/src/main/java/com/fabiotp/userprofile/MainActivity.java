package com.fabiotp.userprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ImageView profile_picture = findViewById(R.id.profile_picture);
    profile_picture.setImageResource(R.drawable.profile_pic);

    TextView name = findViewById(R.id.name);
    name.setText("Fabio");

    TextView birthday = findViewById(R.id.birthday);
    birthday.setText("12/08/1991");


    TextView country = findViewById(R.id.country);
    country.setText("Portugal");

  }
}
