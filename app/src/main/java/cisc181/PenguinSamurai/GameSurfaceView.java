package cisc181.PenguinSamurai;

/*
    Name: Naveen Paramesh
    Section Number: CISC181011-030L
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


// the view that shows up when the game activity is launched
public class GameSurfaceView extends SurfaceView{


    GameThread gameThread;// needed for animation
    Paint mPaint;// needed for drawing
    Bitmap snowBall;// the snowball image
    Bitmap fish;//the fish image
    Bitmap throwingStar;// the throwing star image
    Bitmap mainGameBackground;// the background
    Bitmap penguin;// the penguin
    Bitmap icicle;// the icicle
    DisplayMetrics metrics = getContext().getResources().getDisplayMetrics(); // screen details
    float xPos = 100; // x pos of item
    float yPos = metrics.heightPixels; // y pos of item
    double speed; // the speed of the item
    double theta = 88; // the angle the item is launched
    double time = 0; // time of the projectile item
    double gravity; // force that brings item back down
    double xDelta; // change in item position x
    double yDelta;// change in item position y
    Random random; // random generator
    boolean xRight = true; // determines if the item goes left or right
    long startTime; // time to see elapsed time
    boolean touchDown = false; // boolean to see if touched
    boolean touchStarted = false; // to see if touch started
    boolean touchHit = false; // to see if item hit
    float touchX, touchY; // x and y of touch
    float angleDelta = 15; // change in angle rotated of item
    float angle = 0; // angle of item
    float throwingAngle = 0; // angle of other item
    float throwingDelta = 30; // angle change of other item
    int score; // score of the game
    String livesLeft; // lives remaining
    boolean posted = false; // if score is recorded
    boolean gameOver = false; // if game is over
    boolean alreadyHit = false; // if item already hit
    float startTouchX = 0; // where the user started the touch in x
    float startTouchY = 0; // where the user started the touch in y
    boolean throwSnow = true; // if snow should be throwed
    boolean throwFish = false;// if fish should be throwed
    boolean throwPenguin = false;// if penguin should be throwed
    boolean throwIcicle = false;// if icicle should be throwed
    int highScoreCount = 1; // to keep track of which high score is being recorded
    boolean highScoreSaved = false; // determines if the score was already saved or not



    public GameSurfaceView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        // if the difficults was set
        if(OptionsActivity.easyBtn != null &&
           OptionsActivity.mediumBtn != null &&
           OptionsActivity.hardBtn != null){
            if(OptionsActivity.easyBtn.isChecked()){ // if easy
                gravity = 0.15;
                speed = 3.35;
            }
            else if(OptionsActivity.mediumBtn.isChecked()){ //if medium
                gravity = 0.18;
                speed = 3.35;
            }
            else if(OptionsActivity.hardBtn.isChecked()){//if hard
                gravity = 0.24;
                speed = 3.35;
            }
        }
        else { // if difficulty not set
            gravity = 0.15;
            speed = 3.35;
        }

        // creates new thread for animation and sets all the default values
        // also sets and scales background to device dimensions
        gameThread = new GameThread(this);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        snowBall = BitmapFactory.decodeResource(getResources(), R.drawable.snowball);
        fish = BitmapFactory.decodeResource(getResources(), R.drawable.fish);
        throwingStar = BitmapFactory.decodeResource(getResources(), R.drawable.throwingstar);
        penguin = BitmapFactory.decodeResource(getResources(), R.drawable.penguinobj);
        icicle = BitmapFactory.decodeResource(getResources(), R.drawable.icicle);
        mainGameBackground = BitmapFactory.decodeResource(getResources(), R.drawable.maingamebackground);
        mainGameBackground = Bitmap.createScaledBitmap(mainGameBackground,metrics.widthPixels, metrics.heightPixels, true);
        SurfaceHolder holder = getHolder();
        random = new Random();
        startTime = System.currentTimeMillis();
        score = 0;
        livesLeft = "XXXXX";

        // adds the callback to the class to be called in the given states
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameThread.setRunning(true);// starts the thread
                gameThread.start();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // destroys thread
                boolean retry = true;
                gameThread.setRunning(false);
                while(retry){
                    try{
                        gameThread.join();
                        retry = false;
                    }
                    catch(InterruptedException e){

                    }
                }
            }
        });

    }



    // method to draw everything to the canvas which gets displayed on the screen
    public void myDraw(Canvas canvas){
        // draws the background and the score and lives left
        canvas.drawBitmap(mainGameBackground, 0, 0, mPaint);
        mPaint.setTextSize(60);
        mPaint.setColor(Color.parseColor("#FFFF66"));
        canvas.drawText("Score: " + Integer.toString(score), metrics.widthPixels * 0.1f, metrics.heightPixels * 0.1f, mPaint);
        canvas.drawText(livesLeft, metrics.widthPixels * 0.7f, metrics.heightPixels * 0.1f, mPaint);
        canvas.save();
        drawBitmaps(canvas);


        canvas.restore();

        // makes the user wait 2 seconds before the game starts
        if(yPos == metrics.heightPixels && xPos == 100){
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            if(elapsedTime < 2000){
                return;
            }
        }

        // updates the position
        time += 1;
        if(xRight){
            xDelta = speed * Math.cos(Math.toRadians(theta)) * time;
        }
        else {
            xDelta = (speed * Math.cos(Math.toRadians(theta)) * time) * -1;
        }
        yDelta = ((speed * Math.sin(Math.toRadians(theta)) * time) * -1) + ((0.5) * gravity * Math.pow(time, 2));
        if(!gameOver){
            xPos += xDelta;
            yPos += yDelta;
        }

        // if hit plays sound and adds score, or removes life, or adds life
        if(touchHit && !touchStarted && !alreadyHit){
            alreadyHit = true;
            gameThread.SP.play(gameThread.sound_hit, 1f, 1f, 0, 0, 1f);
            if(throwSnow || throwIcicle){
                score += 1;
                yPos = metrics.heightPixels * 2;
            }
            else if(throwFish){
                if(livesLeft.length() < 5){
                    addLife();
                }
                yPos = metrics.heightPixels * 2;
            }
            else if(throwPenguin){
               if(livesLeft.length() > 0){
                   subLife();
               }
                yPos = metrics.heightPixels * 2;
            }

        }

        // if not hit and drops below screen, subs life and redraws the score and lives left
         if(yPos >= (metrics.heightPixels + snowBall.getHeight()) && !gameOver) {
             if(!touchHit && livesLeft.length() > 0 && (throwSnow || throwIcicle)){
                 subLife();
                 mPaint.setColor(Color.parseColor("#FFFF66"));
                 canvas.drawBitmap(mainGameBackground, 0, 0, mPaint);
                 canvas.drawText("Score: " + Integer.toString(score), metrics.widthPixels * 0.1f, metrics.heightPixels * 0.1f, mPaint);
                 canvas.drawText(livesLeft, metrics.widthPixels * 0.7f, metrics.heightPixels * 0.1f, mPaint);
                 gameThread.gameSurfaceView.getHolder().unlockCanvasAndPost(canvas);
                 gameThread.gameSurfaceView.getHolder().lockCanvas();
                 posted = true;

             }
             // redraws the score
             canvas.drawBitmap(mainGameBackground, 0, 0, mPaint);
             canvas.drawText("Score: " + Integer.toString(score), metrics.widthPixels * 0.1f, metrics.heightPixels * 0.1f, mPaint);
             canvas.drawText(livesLeft, metrics.widthPixels * 0.7f, metrics.heightPixels * 0.1f, mPaint);
             drawBitmaps(canvas);
             gameThread.gameSurfaceView.getHolder().unlockCanvasAndPost(canvas);
             gameThread.gameSurfaceView.getHolder().lockCanvas();
             posted = true;

            // makes the user wait a while before the next item is generated
            try{
                gameThread.sleep(1000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }


            // resets a lot of variables for the next projectie motion
            time = 0;
            xPos = random.nextInt(metrics.widthPixels - 99) + 50;
            yPos = metrics.heightPixels;
            if(xPos >  (metrics.widthPixels / 2) - 100){
                //Log.d("The X POS", Integer.toString((metrics.widthPixels / 2) - 100));
                xRight = false;
            }
            else {
                xRight = true;
            }
             alreadyHit = false;
             touchHit = false;

             generateItem(); // determines new item to be thrown

        }
        // draws throwing star where ever the user finger moves
        mPaint.setColor(Color.WHITE);
        if(touchDown) {
            canvas.save();
            canvas.rotate(throwingAngle, touchX + throwingStar.getWidth() / 2, touchY + throwingStar.getHeight() / 2);
            canvas.drawBitmap(throwingStar, touchX, touchY, mPaint);
            canvas.restore();
        }
        // handles if game is over and records score to be compared to all the high scores
        mPaint.setColor(Color.MAGENTA);
        if(livesLeft.length() == 0){
            canvas.drawText("GAME OVER", metrics.widthPixels/2 - (metrics.widthPixels * (1.0f/5)), metrics.heightPixels/2, mPaint);
            gameOver = true;
            yPos = metrics.heightPixels + 500;
            if(!highScoreSaved){
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("highscores.txt", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                for(;;){
                    if(sharedPreferences.getInt("highScore" + Integer.toString(highScoreCount), -1) == -1){
                        editor.putInt("highScore" + Integer.toString(highScoreCount), score);
                        editor.commit();
                        highScoreSaved = true;
                        break;
                    }
                    highScoreCount += 1;
                }
            }

        }


        // updates both item angle and throwingStar angle
        angle += angleDelta;
        throwingAngle += throwingDelta;
    }



    public boolean onTouchEvent(MotionEvent e) {

        // determine whether touch is still active or has just ended
        // if the item hasn't already been hit, register all of this
        if(!alreadyHit){
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                touchDown = true;        // finger down
                touchStarted = true;     // finger JUST went down
                startTouchX = e.getX();       // where finger is
                startTouchY = e.getY();

                // is touch close enough to object to count as hit?


            }

            // finger down and dragging

            else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                touchStarted = false;    // finger has been down for awhile now
                touchX = e.getX();
                touchY = e.getY();
                // determines if any of the items below have been hit
                if(throwSnow){
                    if (touchX >= (xPos - 30) && touchX <= (xPos + snowBall.getWidth() + 30) && touchY >= (yPos - 30) && touchY < (yPos + snowBall.getHeight() + 30)) {
                        if(startTouchX < xPos || startTouchX > xPos + snowBall.getWidth()){
                            if(startTouchY < yPos || startTouchY > yPos + snowBall.getHeight()){
                                touchHit = true;
                            }
                        }
                    }
                    else {
                        touchHit = false;
                    }
                }
                else if(throwFish){
                    if (touchX >= (xPos - 30) && touchX < (xPos + fish.getWidth() + 30) && touchY >= (yPos - 30) && touchY < (yPos + fish.getHeight() + 30)) {
                        if(startTouchX < xPos || startTouchX > xPos + fish.getWidth()){
                            if(startTouchY < yPos || startTouchY > yPos + fish.getHeight()){
                                touchHit = true;

                            }
                        }
                    }
                    else {
                        touchHit = false;
                    }
                }
                else if(throwIcicle){
                    if (touchX >= (xPos - 30) && touchX < (xPos + icicle.getWidth() + 30) && touchY >= (yPos - 30) && touchY < (yPos + icicle.getHeight() + 30)) {
                        if(startTouchX < xPos || startTouchX > xPos + icicle.getWidth()){
                            if(startTouchY < yPos || startTouchY > yPos + icicle.getHeight()){
                                touchHit = true;
                            }
                        }
                    }
                    else {
                        touchHit = false;
                    }
                }
                else if(throwPenguin){
                    if (touchX >= (xPos - 30) && touchX < (xPos + penguin.getWidth() + 30) && touchY >= (yPos - 30) && touchY < (yPos + penguin.getHeight() + 30)) {
                        if(startTouchX < xPos || startTouchX > xPos + penguin.getWidth()){
                            if(startTouchY < yPos || startTouchY > yPos + penguin.getHeight()){
                                touchHit = true;

                            }
                        }
                    }
                    else {
                        touchHit = false;
                    }
                }

            }

            // finger is up after being down

            else if (e.getAction() == MotionEvent.ACTION_UP) {
                touchDown = false;       // finger up
                touchStarted = false;    // so a touch cannot have just been started
            }
            // unrecognized motion event

            else {
                return false;
            }
        }
        else { // if the item has already been hit, just do this
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                touchDown = true;
                touchX = e.getX();
                touchY = e.getY();
            }
            else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                touchX = e.getX();
                touchY = e.getY();

            }
            else if (e.getAction() == MotionEvent.ACTION_UP) {
                touchDown = false;       // finger up
            }
        }


        // do NOT force a redraw -- just wait for GameThread's next draw call
        return true;
    }

    // method to properly draw the determined item on the canvas
    private void drawBitmaps(Canvas canvas){
        if(throwSnow){
            canvas.rotate(angle, xPos + snowBall.getWidth() / 2, yPos + snowBall.getHeight() / 2);
            canvas.drawBitmap(snowBall, xPos, yPos, mPaint);
        }
        else if (throwFish){
            canvas.rotate(angle, xPos + fish.getWidth() / 2, yPos + fish.getHeight() / 2);
            canvas.drawBitmap(fish, xPos, yPos, mPaint);
        }
        else if(throwIcicle){
            canvas.rotate(angle, xPos + icicle.getWidth() / 2, yPos + icicle.getHeight() / 2);
            canvas.drawBitmap(icicle, xPos, yPos, mPaint);
        }
        else {
            canvas.rotate(angle, xPos + penguin.getWidth() / 2, yPos + penguin.getHeight() / 2);
            canvas.drawBitmap(penguin, xPos, yPos, mPaint);
        }
    }

    // method to sub a life from the user
    private void subLife(){
        if(livesLeft.length() > 0){
            this.livesLeft = livesLeft.substring(0, livesLeft.length() - 1);
        }

    }

    // method to add a life for the user
    private void addLife(){
        this.livesLeft = livesLeft + "X";
    }

    // method to generate a the next item thrown
    private void generateItem(){
        int item = random.nextInt(31);
        if(OptionsActivity.easyBtn != null &&
                OptionsActivity.mediumBtn != null &&
                OptionsActivity.hardBtn != null){
            if(OptionsActivity.easyBtn.isChecked()){
                if(item < 10){
                    throwSnow = true;
                    throwIcicle = false;
                    throwFish = false;
                    throwPenguin = false;
                }
                else if(item < 20) {
                    throwSnow = false;
                    throwIcicle = true;
                    throwFish = false;
                    throwPenguin = false;
                }
                else if(item < 25){
                    throwSnow = false;
                    throwIcicle = false;
                    throwFish = false;
                    throwPenguin = true;
                }
                else {
                    throwSnow = false;
                    throwIcicle = false;
                    throwFish = true;
                    throwPenguin = false;
                }
            }
            else if(OptionsActivity.mediumBtn.isChecked()){
                item = random.nextInt(41);
                if(item < 10){
                    throwSnow = true;
                    throwIcicle = false;
                    throwFish = false;
                    throwPenguin = false;
                }
                else if(item < 20) {
                    throwSnow = false;
                    throwIcicle = true;
                    throwFish = false;
                    throwPenguin = false;
                }
                else if(item < 30){
                    throwSnow = false;
                    throwIcicle = false;
                    throwFish = false;
                    throwPenguin = true;
                }
                else {
                    throwSnow = false;
                    throwIcicle = false;
                    throwFish = true;
                    throwPenguin = false;
                }
            }
            else {
                item = random.nextInt(41);
                if(item < 10){
                    throwSnow = true;
                    throwIcicle = false;
                    throwFish = false;
                    throwPenguin = false;
                }
                else if(item < 20) {
                    throwSnow = false;
                    throwIcicle = true;
                    throwFish = false;
                    throwPenguin = false;
                }
                else if(item < 37){
                    throwSnow = false;
                    throwIcicle = false;
                    throwFish = false;
                    throwPenguin = true;
                }
                else {
                    throwSnow = false;
                    throwIcicle = false;
                    throwFish = true;
                    throwPenguin = false;
                }
            }

        }
        else {
            if(item < 10){
                throwSnow = true;
                throwIcicle = false;
                throwFish = false;
                throwPenguin = false;
            }
            else if(item < 20) {
                throwSnow = false;
                throwIcicle = true;
                throwFish = false;
                throwPenguin = false;
            }
            else if(item < 25){
                throwSnow = false;
                throwIcicle = false;
                throwFish = false;
                throwPenguin = true;
            }
            else {
                throwSnow = false;
                throwIcicle = false;
                throwFish = true;
                throwPenguin = false;
            }
        }

    }

}
