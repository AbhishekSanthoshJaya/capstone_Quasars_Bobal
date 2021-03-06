package com.aby.capstone_quasars_bobal;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FlashCardActivity extends AppCompatActivity {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    TextView txtFront, txtBack;
    String word, meaning, pos;
    int correctAnswers = 0, wrongAnswers = 0;

    ArrayList<String> wordList = new ArrayList<>();
    ArrayList<String> meaningList = new ArrayList<>();
    ArrayList<String> posList = new ArrayList<>();
    ArrayList<String> randomList = new ArrayList<>();
    LinearLayout btnLayout;
    Button btnCorrect, btnWrong;
    TextView txtScore, txtScore2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        getSupportActionBar().hide();

        txtFront = findViewById(R.id.txtFront);
        txtBack = findViewById(R.id.txtBack);
        btnCorrect = findViewById(R.id.btnCorrect);
        btnWrong = findViewById(R.id.btnWrong);
        txtScore = findViewById(R.id.txtScore);
        txtScore2 = findViewById(R.id.txtScore2);
        findViews();
        loadAnimations();
        changeCameraDistance();
        try {
            parseJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final int randomNumber = generateRandom();
        txtFront.setText(wordList.get(randomNumber));
        txtBack.setText(wordList.get(randomNumber)+"("+(posList.get(randomNumber))+")" + ": " + meaningList.get(randomNumber));
        btnLayout = findViewById(R.id.btnLayout);

        btnCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctAnswers++;
                txtScore.setText(correctAnswers + " RIGHT");
                getCorrectWords();
                int rand2 = generateRandom();
                if(getCorrectWords().contains(rand2))
                {
                    rand2 = generateRandom();
                }
                findViews();
                loadAnimations();
                changeCameraDistance();
                flipCard();
                txtFront.setText(wordList.get(rand2));
               // txtBack.setText(meaningList.get(rand2));
                txtBack.setText(wordList.get(rand2)+"("+(posList.get(rand2))+")" + ": " + meaningList.get(rand2));
            }
        });
        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrongAnswers++;
                findViews();
                loadAnimations();
                changeCameraDistance();
                flipCard();
                int rand2 = generateRandom();
                txtFront.setText(wordList.get(rand2));
                // txtBack.setText(meaningList.get(rand2));
                txtBack.setText(wordList.get(rand2)+"("+(posList.get(rand2))+")" + ": " + meaningList.get(rand2));
                txtScore2.setText(wrongAnswers + " WRONG");
            }
        });
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    private void findViews() {
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);
    }

    public void flipCard(View view) {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
            btnLayout.setVisibility(View.VISIBLE);
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("words.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void parseJSON() throws JSONException {
        String jsonStr = loadJSONFromAsset();
        JSONArray jsonarray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
             word = jsonobject.getString("word");
             meaning = jsonobject.getString("meaning");
             pos = jsonobject.getString("pos");
             //wordsMap.put(word, meaning);
             wordList.add(word);
             meaningList.add(meaning);
             posList.add(pos);
             Log.i("word", word);
             Log.i("meaning", meaning);
             //Log.i("map", wordsMap.toString());
        }
    }
    public int generateRandom()
    {
        int max = 299;
        int min = 0;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public ArrayList<Integer> getCorrectWords()
    {
        int randNum = generateRandom();
        ArrayList<Integer> correctIndex = new ArrayList<>();
        correctIndex.add(randNum);
        return correctIndex;
    }

    public void flipCard() {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
            btnLayout.setVisibility(View.VISIBLE);
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }
}

