package com.example.jacob.otp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    private void writeTo(String seed, int count){
        try {
            FileOutputStream fos = openFileOutput("abc.txt", Context.MODE_PRIVATE);
            String toStore = seed + "," + count;
            fos.write(toStore.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    class SubmitListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            TextView seed = (TextView)findViewById(R.id.seedInput);
            TextView count = (TextView)findViewById(R.id.countInput);
            String seedVal = seed.getText().toString();
            String countVal = count.getText().toString();
            if(!seedVal.isEmpty() && !countVal.isEmpty()){
                int countValInt = Integer.parseInt(countVal);
                writeTo(seedVal,countValInt);
                System.out.println(seedVal + " " + countVal);
                Context context = getApplicationContext();
                CharSequence text = "Settings Updated";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();//success
                startActivity(new Intent(Main2Activity.this, MainActivity.class));

            }else{
                Context context = getApplicationContext();
                CharSequence text = "Complete empty fields";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new SubmitListener());
    }
}
