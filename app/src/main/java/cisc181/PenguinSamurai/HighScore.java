package cisc181.PenguinSamurai;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/*
    Name: Naveen Paramesh
    Section Number: CISC181011-030L
 */


public class HighScore extends Activity {

    TextView numberOne; // highest score
    TextView numberTwo; // 2nd highest score
    TextView numberThree;// 3rd highest score
    TextView numberFour;// 4th highest score
    TextView numberFive;// 5th highest score
    ArrayList<Integer> highScores;// all the high scores
    ArrayList<TextView> panels; // all the text views that display the scores
    public SharedPreferences sharedPreferences; // preferences to save all the scores somewhere


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score); // sets view
        sharedPreferences = getSharedPreferences("highscores.txt", Context.MODE_PRIVATE); //for app
        // initializes all the textviews and lists
        numberOne = (TextView)findViewById(R.id.one);
        numberTwo = (TextView)findViewById(R.id.two);
        numberThree = (TextView)findViewById(R.id.three);
        numberFour = (TextView)findViewById(R.id.four);
        numberFive = (TextView)findViewById(R.id.five);
        highScores = new ArrayList<Integer>();
        panels = new ArrayList<TextView>();
        // adds all the textviews to the pannel array
        panels.add(numberOne);
        panels.add(numberTwo);
        panels.add(numberThree);
        panels.add(numberFour);
        panels.add(numberFive);

        // add all the scores stored in preferences to the scores list
        int count = 1;
        loop: for(;;){
            Integer score = sharedPreferences.getInt("highScore" + Integer.toString(count), -1);
            if(score != -1){
                highScores.add(score);


            }
            else {
                break loop;
            }
            count += 1;
        }
        // sorts the list in descending order
        Collections.sort(highScores);
        Collections.reverse(highScores);
        // removes till only 5 scores remain in list
        while (highScores.size() > 5){
            highScores.remove(highScores.size() - 1);
        }
        // displays the scores to the screen
        for(int i = 0; i < highScores.size(); i++){
            String existingText = panels.get(i).getText().toString();
            panels.get(i).setText(existingText + " " + Integer.toString(highScores.get(i)));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
