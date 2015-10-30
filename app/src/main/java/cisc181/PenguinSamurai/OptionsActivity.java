package cisc181.PenguinSamurai;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/*
    Name: Naveen Paramesh
    Section Number: CISC181011-030L
 */


public class OptionsActivity extends Activity {

    public static RadioButton easyBtn, mediumBtn, hardBtn; // all the radio buttons
    TextView instructions; // the instructions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options); //sets view
            // initializes all the radio buttons and instructions
            easyBtn = (RadioButton)findViewById(R.id.easy);
            mediumBtn = (RadioButton)findViewById(R.id.medium);
            hardBtn = (RadioButton)findViewById(R.id.hard);
            instructions = (TextView)findViewById(R.id.instructions);

        // sets the instructions
        instructions.setText("The object of the " +
                "game, Penguin Samurai, is to use your " +
                "ninja like reflexes to slice all of the " +
                "snow objects on your screen without dropping " +
                "any. You must avoid hitting fellow penguins, " +
                "or you will lose a life. If you lose " +
                "all 5 lives the game will end. " +
                "Slicing ice objects gains you 1 " +
                "point and hitting a fish will grant " +
                "you an extra life. Slice as many objects " +
                "as you can to gain a highscore!");

        // sets all the listeners for the buttons
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediumBtn.setChecked(false);
                hardBtn.setChecked(false);
            }
        });

        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                easyBtn.setChecked(false);
                hardBtn.setChecked(false);
            }
        });

        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                easyBtn.setChecked(false);
                mediumBtn.setChecked(false);
            }
        });




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
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
