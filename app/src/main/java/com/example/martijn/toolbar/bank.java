package com.example.martijn.toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class bank extends AppCompatActivity {
    private TextView actualScores;
    private TextView actualRank;
    private String fileName = "userName.txt";
    private Button savetoFirebaseButton;
    private TextView nothingMatttersTx;
    private ArrayList<String> namesfirst;
    private ArrayList<String> nameslast;
    private String username;


    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<String> list;
    private ArrayList<User> userList;
    private ArrayAdapter<String> adapter;
    private User user;
    private int teller;
    private ListView deListView;
    private ArrayList<String> trySortList;
    private TextView urs;

    private Button sharebt;
//    private Intent sharingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        nothingMatttersTx = (TextView) findViewById(R.id.nothingMattters);

        //Initialise toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");

        //Top scores er in zettennnn
        fillListToppers();


        //Score ophalen met parameter passing
        actualScores = (TextView) findViewById(R.id.actualScore);
        actualScores.setText(setScore().substring(0, setScore().length() - 2));
        actualRank = (TextView) findViewById(R.id.actualRank);
//        actualRank.setText(String.valueOf(getRank()).substring(0, String.valueOf(getRank()).length() - 2));



        //If username is already decided
        try{
            if(readFile(fileName).length() > 0){
                Toast.makeText(bank.this, "File exists", Toast.LENGTH_SHORT).show();
            }
            else{
                giveUserName();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //Waarom doet ie dit niet op het laden?
        try{
            nothingMatttersTx.setText(readFile(fileName));
        }catch (Exception e){
            e.getMessage();
        }

//        fillListToppers();
//        Save to firebase
//        sharebt = (Button) findViewById(R.id.imageButton38);
//        sharebt.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = "Your body here";
//                String shareSub = "Your subject here";
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "Share using"));
//            }
//        });
//        sharebt = (Button) findViewById(R.id.imageButton38);
//        sharebt.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(bank.this, "Clean file", Toast.LENGTH_SHORT).show();
//                saveFile("score.txt", null);//Hij maakt de score niet 0??
//                giveUserName();
//                saveFile("dinoFotoFile.txt", null);
//
//            }
//        });

        savetoFirebaseButton = (Button) findViewById(R.id.imageButton34);
        savetoFirebaseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                nothingMatttersTx.setText(readFile(fileName));
                //Zet adapter eerst op null
                list = new ArrayList<>();
                userList = new ArrayList<>();
                trySortList= new ArrayList<>();
                list.clear();
                userList.clear();
                adapter.clear();
                deListView.setAdapter(null);
                Toast.makeText(bank.this, "Upload", Toast.LENGTH_SHORT).show();


                uploadFire();
                fillListToppers();



//                deListView.setAdapter(adapter);
            }
        });




    }
    public String setScore(){
        Bundle b = getIntent().getExtras();

        if(b != null){
            String value = b.getString("ScoreDino");
            String tempString = String.valueOf(value);
//            actualScore.setText(tempString.substring(0, tempString.length() - 2));
//            Toast.makeText(bank.this, String.valueOf(value), Toast.LENGTH_SHORT).show();
            return tempString;
        }
        else{
            Toast.makeText(bank.this, "Error in parameter pass", Toast.LENGTH_SHORT).show();
            return "error";
        }
    }
    //Score local opslaan
    public void saveFile(String file, String scoreString){
        try{
            FileOutputStream fos = openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(scoreString.getBytes());
            fos.close();
            Toast.makeText(bank.this, "Username saved mate", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(bank.this, "Error saving score!", Toast.LENGTH_LONG).show();
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
            Toast.makeText(bank.this, "Error reading file", Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(MainActivity.this, scoreDinoLok, Toast.LENGTH_LONG).show();
        return scoreDinoLok;
    }
    public void giveUserName(){
        namesfirst = new ArrayList<>();
        namesfirst.add("Monster");
        namesfirst.add("Anxiety");
        namesfirst.add("Fast");
        namesfirst.add("Great");
        namesfirst.add("Giant");
        namesfirst.add("Small");
        namesfirst.add("Rapid");
        namesfirst.add("Free");
        namesfirst.add("God");
        namesfirst.add("Rarri");
        namesfirst.add("Range");
        namesfirst.add("Killer");
        namesfirst.add("Weird");
        namesfirst.add("Techno");
        namesfirst.add("Range");
        namesfirst.add("Cypher");
        namesfirst.add("Suicidal");
        namesfirst.add("X");

        nameslast = new ArrayList<>();
        nameslast.add("ranger");
        nameslast.add("dino");
        nameslast.add("flyer");
        nameslast.add("-rex");
        nameslast.add("zilla");
        nameslast.add("plant");
        nameslast.add("dino");
        nameslast.add("potato");
        nameslast.add("friend");
        nameslast.add("ugly");
        nameslast.add("weirdo");
        nameslast.add("repticus");
        nameslast.add("septicus");
        nameslast.add("norangtus");

//        Create random name
        Random Dice = new Random();
        int n = Dice.nextInt(namesfirst.size());
        Random Dice2 = new Random();
        int p = Dice2.nextInt(nameslast.size());
        username = namesfirst.get(n) + nameslast.get(p);

        saveFile(fileName, String.valueOf(username));

    }

    public void fillListToppers(){
        //Pull from firebase
        deListView = (ListView) findViewById(R.id.listV);
        user = new User();
        database =  FirebaseDatabase.getInstance();
//
        ref = database.getReference("Users");//Reference naam mijn userstable in Firebase
        list = new ArrayList<>();
        userList = new ArrayList<>();
        trySortList= new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.da_items, R.id.userinfo, list);
        list.clear();
        userList.clear();
        adapter.clear();
        deListView.setAdapter(null);




        teller = 0;
//
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    user = ds.getValue(User.class);
                    userList.add(user);
                    list.add(String.valueOf(user.getScore()) + " " + String.valueOf(user.getName()));
                    teller = teller + 1;
                }
                deListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.userList = userList;
    }
    public void uploadFire(){
        //Upload to firebae
        Toast.makeText(bank.this, "Upload firebase()", Toast.LENGTH_SHORT).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.child(String.valueOf(readFile(fileName))).child("Name").setValue(String.valueOf(readFile(fileName)));
        myRef.child(String.valueOf(readFile(fileName))).child("Score").setValue(readFile("score.txt").substring(0, (readFile("score.txt").length() - 2)));
    }
//    public double getRank(){
////        fillListToppers();
//        double rank = 1;
////        Toast.makeText(bank.this, "Size " + userList.get(0).getName(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(bank.this, String.valueOf(Double.parseDouble(setScore().substring(0, setScore().length() - 2))), Toast.LENGTH_SHORT).show();
//        Toast.makeText(bank.this, "Size " + userList.size(), Toast.LENGTH_SHORT).show();
//
//        for(int i = 0; i<userList.size(); i++){
//            if (Double.parseDouble(setScore().substring(0, setScore().length() - 2)) <= Double.parseDouble(userList.get(i).getScore())){
//                rank = rank + 1;
//            }
//            Toast.makeText(bank.this, String.valueOf(Double.parseDouble(userList.get(i).getScore())), Toast.LENGTH_SHORT).show();
//        }
//        return rank;
//    }

}
