package com.example.jacob.otp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {

    private String seed_;
    private int count_;

    private void writeTo(){
        try {
            FileOutputStream fos = openFileOutput("abc.txt", Context.MODE_PRIVATE);
            String toStore = seed_ + "," + count_;
            fos.write(toStore.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readFrom(){
        try {
            FileInputStream fos = openFileInput("abc.txt");
            byte[] ba = new byte[256];
            int many = fos.read(ba);
            String res = new String(ba);
            res = res.substring(0,many);
            String[] seedCount = res.split(",");
            if (seedCount.length < 2){
                System.out.println("not enough");
                seed_ = "";
                count_= -1;
                return;
            }
            seed_ = seedCount[0];
            count_ = Integer.parseInt(seedCount[1]);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
            seed_ = "";
            count_= -1;
        } catch (IOException e){
            e.printStackTrace();
            seed_ = "";
            count_= -1;
        }

    }


    class SettingsListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            startActivity(new Intent(MainActivity.this, Main2Activity.class));

        }
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + 'x', new BigInteger(1, data));
    }

    String myHash(String data){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            return bin2hex(hash).substring(0,4);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }

    String applyHashN(){
        String res = seed_;
        for (int i=0; i<count_; i++){
            res = myHash(res);
        }
        return res;
    }

    class GenerateListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            seed_ = "";
            count_= -1;
            readFrom();
            if(seed_ != "" && count_ != -1){
                String res = applyHashN();
                TextView otp = (TextView)findViewById(R.id.OTPText);
                otp.setText(res);
            }else{
                Context context = getApplicationContext();
                CharSequence text = "No settings saved!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    class AcceptedListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            seed_ = "";
            count_= -1;
            readFrom();
            if(seed_ != "" && count_ != -1){
                count_ --;
                writeTo();
                Context context = getApplicationContext();
                TextView otp = (TextView)findViewById(R.id.OTPText);
                otp.setText("----");
                CharSequence text = "Counter updated";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }else{
                Context context = getApplicationContext();
                CharSequence text = "No settings saved!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button settings = (Button) findViewById(R.id.settingsButton);
        settings.setOnClickListener(new SettingsListener());
        Button generate = (Button) findViewById(R.id.generateButton);
        generate.setOnClickListener(new GenerateListener());
        Button accepted = (Button) findViewById(R.id.acceptButton);
        accepted.setOnClickListener(new AcceptedListener());
    }


}


