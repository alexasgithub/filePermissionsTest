package com.example.khalessi.filepermissionstest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {



    private TextView ausgabeText;
    private EditText eingabeText;
    private Button speichern, auslesen;
    public String daten="Eingabetext";
    private int flagT = 1;
    private String TAG = "permissions";
    private boolean permission_write_external_granted = false;

    private final int REQUEST_CODE = 2325;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();


        eingabeText = (EditText) findViewById(R.id.eingabeText);

        daten = eingabeText.getText().toString();

        ausgabeText = (TextView) findViewById(R.id.ausgabeText);
        speichern = (Button) findViewById(R.id.speichern);
//checkSelfPermission ()....

        speichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        auslesen = (Button) findViewById(R.id.ausgabe);
        auslesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkPermission();
            }
        });


    }

   // public void checkPermission (int flagT){
        public void checkPermission (){


        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(check != PackageManager.PERMISSION_GRANTED) {
            // Rechte anfordern
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            return;
        }
        else {
          //  if (flagT==1) {writeToFile(); }

          //  else { readFile();}
            permission_write_external_granted=true;

        }
    }

   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int [] grantResults) {
        try {
            if (requestCode == REQUEST_CODE && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

                permission_write_external_granted = true;

                  //  ausgabeText.setText(grantResults[0]);

            }
        } catch (SecurityException ex) {
            Log.d("meinApp", ex.getMessage());
            finish();

                    // super.onRequestPermissionsResult(requestCode, permissions, grantRes
        }

    }



public void textSchreiben (String text, File datei) {

    try {
        FileOutputStream fo = new FileOutputStream(datei);
        PrintWriter pw = new PrintWriter(fo);
        pw.write(text);
        pw.close();

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

}



public void onSchreibenClick (View view){
    if (permission_write_external_granted){

    File fileOut;

    fileOut = new File(getExternalFilesDir(null), "testDatei.txt");
    textSchreiben ("gutenMorgen", fileOut);}
}



    private void writeToFile (){


        try {
            // daten = "Eingabetext";

            eingabeText = (EditText) findViewById(R.id.eingabeText);

            daten = eingabeText.getText().toString();

            // Stream erzeugen
            FileOutputStream fos = openFileOutput("text.txt", Context.MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(fos);

            // In Steam schreiben
            pw.println(daten);
            // Stream schließen
            pw.close();
            ausgabeText.setText("Text erfolgreich gespeichert");
        } catch (IOException ex) {
            Log.d("meinApp", ex.getMessage());
            ausgabeText.setText(ex.getMessage());
        }
    }

    private void readFile() {

        try {
            // File-Objekt erzeugen
            File datei = new File
                    (getFilesDir(),"text.txt");
            // Stream erzeugen
            BufferedReader br = new BufferedReader(
                    new FileReader(datei));
            // Aus Stream lesen

            String zeile="";
            String ausgabeString="";
            while ((zeile = br.readLine()) !=null )
            {
                ausgabeString +=zeile;
            }
            ausgabeText.setText(ausgabeString);
            // Stream schließen
            br.close();
        } catch (IOException ex) {
            Log.d("meinApp", ex.getMessage());
            ausgabeText.setText(ex.getMessage());

        }
    }


    public void onBilderClick(View view) {
        if(permission_write_external_granted)
        {
            File bilder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File [] files = bilder.listFiles();
            if(files.length>0){
            TextView tv = (TextView) findViewById(R.id.ausgabe);
            for (File f : files) {
                tv.setText(tv.getText()+f.getName()+"\n");
            }}
            }
    }
}
