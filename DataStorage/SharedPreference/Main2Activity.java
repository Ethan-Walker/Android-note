package com.example.ethanwalker.sharepre;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ethanwalker.a03_datestorage.R;

public class Main2Activity extends AppCompatActivity {
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mcontext = this;
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("shared1", MODE_PRIVATE);
//                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//              3.  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
//                SharedPreferences preferences = getSharedPreferences("shared1", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("name", "ethan");
                editor.putInt("age", 20);
                editor.putBoolean("man?", true);
                editor.putFloat("salary", new Float(2132.221));
                editor.apply();
            }
        });


        Button obtainData = (Button) findViewById(R.id.obtain_data);
        obtainData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences sharedPreferences = getSharedPreferences("shared1",MODE_PRIVATE);
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

                String name = sharedPreferences.getString("name", "");
                int age = sharedPreferences.getInt("age", 0);
                boolean man = sharedPreferences.getBoolean("man?", false);
                float salary = sharedPreferences.getFloat("salary", 0);
                Toast.makeText(mcontext, name + "==>" + age + "==>" + man + "==>" + salary, Toast.LENGTH_LONG).show();

            }
        });
    }


}
