package com.example.translate;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.common.modeldownload.FirebaseRemoteModel;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner inputlanguage=(Spinner)findViewById(R.id.inputlanguage);
        Spinner outputlanguage=(Spinner)findViewById(R.id.outputlanguage);
        final ArrayList<String> inplanguages=new ArrayList<String>();
        final ArrayList<String> outlanguages=new ArrayList<String>();
        inplanguages.addAll(Arrays.asList("English"));
        outlanguages.addAll(Arrays.asList("English","German","Arabic","French","Hindi","Korean","Telugu","Greek","Japanese","Tamil","Urdu","Gujarati","ChinesePRC","Bengali"));
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,inplanguages);
        ArrayAdapter arrayAdapter1=new ArrayAdapter(this,android.R.layout.simple_spinner_item,outlanguages);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outputlanguage.setAdapter(arrayAdapter1);
        inputlanguage.setAdapter(arrayAdapter);
         final int  startlanguage=FirebaseTranslateLanguage.EN;
        final int[] endlanguage = {FirebaseTranslateLanguage.EN};
        final HashMap<String,Integer>languageshash=new HashMap<String,Integer>();
        languageshash.put("English",FirebaseTranslateLanguage.EN);
        languageshash.put("German",FirebaseTranslateLanguage.DE);
        languageshash.put("Arabic",FirebaseTranslateLanguage.AR);
        languageshash.put("French",FirebaseTranslateLanguage.FR);
        languageshash.put("Hindi",FirebaseTranslateLanguage.HI);
        languageshash.put("Korean",FirebaseTranslateLanguage.KO);
       languageshash.put("Greek",FirebaseTranslateLanguage.EL);
       languageshash.put("Japanese",FirebaseTranslateLanguage.JA);
        languageshash.put("Telugu",FirebaseTranslateLanguage.TE);
        languageshash.put("Bengali",FirebaseTranslateLanguage.BN);
        languageshash.put("Urdu",FirebaseTranslateLanguage.UR);
        languageshash.put("Gujarati",FirebaseTranslateLanguage.GU);
        languageshash.put("ChinesePRC",FirebaseTranslateLanguage.ZH);
        languageshash.put("Tamil",FirebaseTranslateLanguage.TA);

        Spinner downloadlanguagesfrom =(Spinner)findViewById(R.id.downloadlanguages);
        downloadlanguagesfrom.setAdapter(arrayAdapter1);
        final int[] down = {FirebaseTranslateLanguage.EN};
        Button downloadbutton=(Button)findViewById(R.id.download);




        outputlanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

             endlanguage[0] =languageshash.get(outlanguages.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
downloadlanguagesfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        down[0] =languageshash.get(outlanguages.get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});

        final TextView output=(TextView)findViewById(R.id.output);
        final TextView input=(TextView)findViewById(R.id.input);

        Button translate =(Button)findViewById(R.id.button);
        final ProgressDialog progress=new ProgressDialog(this);
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Translating Data...Please Wait");

        progress.setTitle("Translating");
        final ProgressDialog progress1=new ProgressDialog(this);
        progress1.setCancelable(true);
        progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress1.setMessage("Downloading Data...May take Time");
progress1.setCancelable(false);
        progress1.setTitle("Downloading..");
        downloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
progress1.show();

                FirebaseTranslatorOptions options1 =
                        new FirebaseTranslatorOptions.Builder()
                                .setSourceLanguage(startlanguage)
                                .setTargetLanguage(down[0])
                                .build();
                final FirebaseTranslator LanguageTranslator =
                        FirebaseNaturalLanguage.getInstance().getTranslator(options1);

                FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()

                        .build();
                LanguageTranslator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void v) {
                                        progress1.dismiss();

                                        Toast.makeText(getApplicationContext(),"Downloaded Successfully",Toast.LENGTH_SHORT).show();
                                    }

                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        progress1.dismiss();
                                        Toast.makeText(getApplicationContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                    }
                                });




            }});

       translate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final ClipboardManager clipboard = (ClipboardManager)
                       getSystemService(Context.CLIPBOARD_SERVICE);

               progress.show();
               if (TextUtils.isEmpty(input.getText().toString())) {
                   progress.dismiss();
                   Toast.makeText(getApplicationContext(), "Please Enter the Text", Toast.LENGTH_SHORT).show();
               } else {


                   FirebaseTranslatorOptions options =
                           new FirebaseTranslatorOptions.Builder()
                                   .setSourceLanguage(startlanguage)
                                   .setTargetLanguage(endlanguage[0])
                                   .build();

                   final FirebaseTranslator LanguageTranslator =
                           FirebaseNaturalLanguage.getInstance().getTranslator(options);

                   FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()

                           .build();

                   LanguageTranslator.downloadModelIfNeeded(conditions)
                           .addOnSuccessListener(
                                   new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void v) {

                                           LanguageTranslator.translate(input.getText().toString())
                                                   .addOnSuccessListener(
                                                           new OnSuccessListener<String>() {
                                                               @Override
                                                               public void onSuccess(@NonNull String translatedText) {
                                                                   progress.dismiss();
                                                                   output.setText(translatedText);
                                                                   ClipData clip = ClipData.newPlainText("Translated Text", translatedText);
                                                                   clipboard.setPrimaryClip(clip);
                                                                   Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();

                                                               }
                                                           })
                                                   .addOnFailureListener(
                                                           new OnFailureListener() {
                                                               @Override
                                                               public void onFailure(@NonNull Exception e) {
                                                                   progress.dismiss();
                                                                   Toast.makeText(getApplicationContext(), "Download Model First", Toast.LENGTH_SHORT).show();

                                                               }
                                                           });
                                       }
                                   })
                           .addOnFailureListener(
                                   new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           // Model couldn’t be downloaded or other internal error.
                                           // ...
                                           progress.dismiss();
                                           Toast.makeText(getApplicationContext(), "Error in Downloading ", Toast.LENGTH_SHORT).show();
                                       }
                                   });


               }
           }
       });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.about:{
            Intent i=new Intent( getApplicationContext(),about.class);
            startActivity(i);
            break;}

            case R.id.help:{
                Intent i=new Intent( getApplicationContext(),help.class);
                startActivity(i);
                break;}

        }
        return true;
    }
}