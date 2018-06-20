package com.example.martijn.toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import android.media.MediaPlayer;


public class MainActivity extends AppCompatActivity {

//    ArrayList<String> numberlist = new ArrayList<>();
    private RequestQueue mQueue;

    private double scoreDino;
    private ImageView dinoimg;

    private TextView scoreText;
    private String fileName = "score.txt";
    private String dinoFotoFile = "dinoFotoFile.txt";

    private boolean isOnFire = false;
    private boolean voeg2toe = false;
    private boolean doubleScore = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        mQueue= Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Title
        getSupportActionBar().setTitle("DinoClicks");
        try{
            if(readFile(fileName).length() > 0){
                Toast.makeText(MainActivity.this, "Score bestaat al" + String.valueOf(readFile(fileName)), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "Score aanmaken", Toast.LENGTH_SHORT).show();
                saveFile(fileName, "0.0");
                scoreDino = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


//        Als je op bottom navigatie drukt
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        if(voeg2toe){
                            Toast.makeText(MainActivity.this, "Times 10!", Toast.LENGTH_SHORT).show();
                            plusOne();
                            voeg2toe = false;
                        }
                        else {
                            Toast.makeText(MainActivity.this, "No points", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.action_fire:
                        if(isOnFire){
                            Toast.makeText(MainActivity.this, "Fire", Toast.LENGTH_SHORT).show();
                            fireUp();
                            isOnFire = false;
                        }
                        else {
                            Toast.makeText(MainActivity.this, "No points", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.action_account:
                        passParameter(scoreDino);
//                            startActivity(new Intent(MainActivity.this, bank.class));
                        break;
                }
                return true;
            }
        });


        //initateer button en textfield en img
        dinoimg = (ImageView) findViewById(R.id.imageView2);
//        ImageButton butonParse = findViewById(R.id.imageButton);
        scoreText = (TextView) findViewById(R.id.textView2);


        try {
            //        //Zet text aan score
            String scoreTemp = (readFile(fileName));
            scoreText.setText(scoreTemp.substring(0, scoreTemp.length() - 2));

            //        //Zet score dino gelijk aan de opgeslagen file
            scoreDino = Double.parseDouble(readFile(fileName));
        }catch (Exception e){
            e.getMessage();
        }

        //Zet imga aan score
        try {
            setImg();//Heeft nog geen dino score als je hier komt
        }catch(Exception e){
            e.getMessage();
        }

//        //Als je op de dino klikt
        dinoimg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                playCoinSound();
                scoreOmhoog();
                saveFile(fileName, String.valueOf(scoreDino));

                //Zet text aan nieuwe score
                String scoreTemp = (readFile(fileName));
                scoreText.setText(scoreTemp.substring(0, scoreTemp.length() - 2));

                //Check of je powerups krijgt
                checkIfFire();
                checkIfvoeg2toe();
                setImg();
            }
        });


    }

    public void passParameter(double scoreD){
        //Parameter passing
        Intent intent = new Intent(MainActivity.this, bank.class);
        Bundle b = new Bundle();
        String socrePass = String.valueOf(scoreD);
        b.putString("ScoreDino", socrePass);

        intent.putExtras(b);
        startActivity(intent);
    }

    //Score +1
    public void scoreOmhoog(){
        scoreDino = scoreDino + 1;
        if(doubleScore){
            scoreDino = scoreDino + 1;
        }
    }

    //Fire checken
    public void checkIfFire(){
        if(scoreDino%100 == 0) {
            isOnFire = true;
        }
    }

    //Voeg toe checken
    public void checkIfvoeg2toe(){
        if(scoreDino%1000 == 0) {
            voeg2toe = true;
        }
    }

    //Score local opslaan
    public void saveFile(String file, String scoreString){
        try{
            FileOutputStream fos = openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(scoreString.getBytes());
            fos.close();
//            Toast.makeText(MainActivity.this, "Score saved", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error saving score!", Toast.LENGTH_LONG).show();
        }
    }

    //Score ophalen
    public String readFile(String file){
        String scoreDinoLok = "";

        try{
            FileInputStream fis = openFileInput(file);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            scoreDinoLok = new String(buffer);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error reading file", Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(MainActivity.this, scoreDinoLok, Toast.LENGTH_LONG).show();
        return scoreDinoLok;
    }

    public void setImg(){
        //random dino bij 100 punten
        double fase1 = 10;
        double fase2 = 20;
        double fase3 = 30;

        if(scoreDino == fase1){
            //random nr
            eggCrack();
            Random Dice = new Random();
            int n = Dice.nextInt(3);

            if(n == 0) {
                saveFile(dinoFotoFile, "dinostarteen");
            }
            if(n == 1){
                saveFile(dinoFotoFile, "dinostarttwee");
            }
            else{
                saveFile(dinoFotoFile, "dinostartdrie");
            }
        }
        else if(scoreDino > fase1 & scoreDino <fase2){
            saveFile(dinoFotoFile, String.valueOf(readFile(dinoFotoFile)));
        }
        else if(scoreDino == fase2){
            //random nr
            darkEff();
            Random Dice = new Random();
            int n = Dice.nextInt(3);

            if(n == 0) {
                saveFile(dinoFotoFile, "dinofaseeen");
            }
            if(n == 1){
                saveFile(dinoFotoFile, "dinofasetwee");
            }
            else{
                saveFile(dinoFotoFile, "dinofasedrie");
            }
        }
        else if(scoreDino > fase2 & scoreDino < fase3){
            saveFile(dinoFotoFile, String.valueOf(readFile(dinoFotoFile)));
        }
        else if(scoreDino == fase3){
            //random nr
            darkEff();
            Random Dice = new Random();
            int n = Dice.nextInt(3);

            if(n == 0) {
                saveFile(dinoFotoFile, "dinoeindeen");
            }
            if(n == 1){
                saveFile(dinoFotoFile, "dinoeindtwee");
            }
            else{
                saveFile(dinoFotoFile, "dinoeinddrie");
            }
        }
        else if(scoreDino > fase3){
            saveFile(dinoFotoFile, String.valueOf(readFile(dinoFotoFile)));
        }
        else{
            saveFile(dinoFotoFile, "egg");
        }



        //Zet de juiste dino neer
        Resources res = getResources();
        String mDrawableName = String.valueOf(readFile(dinoFotoFile));
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID );
        dinoimg.setImageDrawable(drawable );
    }
    public void playCoinSound(){
        MediaPlayer mplayer = MediaPlayer.create(this, R.raw.coineffect);
        mplayer.start();
    }
    public void eggCrack(){
        MediaPlayer mplayer = MediaPlayer.create(this, R.raw.eggcrack);
        mplayer.start();
    }
    public void darkEff(){
        MediaPlayer mplayer = MediaPlayer.create(this, R.raw.darkeffect);
        mplayer.start();
    }
    public void fireUp(){
        MediaPlayer mplayer = MediaPlayer.create(this, R.raw.moneybag);
        mplayer.start();
        scoreDino = scoreDino + scoreDino;
        saveFile(fileName, String.valueOf(scoreDino));
        String scoreTemp = (readFile(fileName));
        scoreText.setText(scoreTemp.substring(0, scoreTemp.length() - 2));
    }
    public void plusOne(){
        MediaPlayer mplayer = MediaPlayer.create(this, R.raw.ching);
        mplayer.start();
        scoreDino = scoreDino * 10;
        saveFile(fileName, String.valueOf(scoreDino));
        String scoreTemp = (readFile(fileName));
        scoreText.setText(scoreTemp.substring(0, scoreTemp.length() - 2));
    }













//    private void jsonParce(){
//        String url = "https://api.myjson.com/bins/l0ocu";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("lokaal");
//
//                            for(int i = 0; i < jsonArray.length(); i++){
//                                JSONObject lokaal =  jsonArray.getJSONObject(i);
//
//                                String userName = lokaal.getString("UserName");
//                                int nummer = lokaal.getInt("Number");
//                                Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_LONG).show();
////                                Toast.makeText(getApplicationContext(), String.valueOf(nummer), Toast.LENGTH_LONG).show();
//
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        mQueue.add(request);
//    }
//    public void getJson(){
//        String json;
//
//        try {
//            InputStream is = getAssets().open("src/main/assets/hallo.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            json = new String(buffer, "UTF-8");
//            JSONArray jsonArray = new JSONArray(json);
//
//
//            for(int i = 0; i<jsonArray.length(); i++){
//                JSONObject obj = jsonArray.getJSONObject(i);
//
//                if(obj.getString("UserName").equals("Hallo"))
//                    numberlist.add(obj.getString("Number"));
//
//            }
//            Toast.makeText(getApplicationContext(), numberlist.toString(), Toast.LENGTH_LONG).show();
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch(JSONException e){
//            e.printStackTrace();
//        }
//
//    }
}
