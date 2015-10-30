package cisc181.PenguinSamurai;


/*
    Name: Naveen Paramesh
    Section Number: CISC181011-030L
 */

// We went above and beyond in this app with the extra activities

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainMenuActivity extends Activity {

    Button startBtn; // the start button
    Button highScoreBtn; // the high score button
    Button optionsBtn; // the options button
    Bitmap background; // the background
    BitmapDrawable mainMenuBackground; // the background drawable
    DisplayMetrics metrics; // the screen details
    LinearLayout layout;// the layout from the main menu layout file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);// sets view
        // loads background and used display metrics to scale it and loads all the buttons
        background = BitmapFactory.decodeResource(getResources(), R.drawable.mainmenubackground);
        metrics = getResources().getDisplayMetrics();
        startBtn = (Button)findViewById(R.id.startButton);
        highScoreBtn = (Button)findViewById(R.id.highScoreButton);
        optionsBtn = (Button)findViewById(R.id.optionsButton);
        layout = (LinearLayout)findViewById(R.id.mainmenu);

        // sets all the on click listeners of all the buttons
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, OptionsActivity.class);
                startActivity(intent);
            }
        });


        // scales image to fit screen
        background = Bitmap.createScaledBitmap(background, metrics.widthPixels, metrics.heightPixels, true);
        mainMenuBackground = new BitmapDrawable(getResources(), background);
        layout.setBackgroundDrawable(mainMenuBackground);

        highScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, HighScore.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
