package cisc181.PenguinSamurai;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
/*
    Name: Naveen Paramesh
    Section Number: CISC181011-030L
 */


public class SplashScreen extends Activity {

    Bitmap background; // the background for the splash
    BitmapDrawable mainMenuBackground; // the  background drawable
    LinearLayout layout; // layout the splash screen in in
    DisplayMetrics metrics; // the screen details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);// sets view
        // initializes everything and scales backdround to device screen
        metrics = getResources().getDisplayMetrics();
        layout = (LinearLayout)findViewById(R.id.splash);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.splashscreen);
        background = Bitmap.createScaledBitmap(background, metrics.widthPixels, metrics.heightPixels, true);
        mainMenuBackground = new BitmapDrawable(getResources(), background);
        layout.setBackgroundDrawable(mainMenuBackground);

        // makes a thread that runs this activity for a while then launches another activity
        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                    Intent intent = new Intent(SplashScreen.this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        thread.start();// starts this thread

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
