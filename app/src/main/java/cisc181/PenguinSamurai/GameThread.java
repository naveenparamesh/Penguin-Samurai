package cisc181.PenguinSamurai;


import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;

/*
    Name: Naveen Paramesh
    Section Number: CISC181011-030L
 */

public class GameThread extends Thread{

    GameSurfaceView gameSurfaceView; // the surface view of the game
    private boolean running = false; // if the thread is running or not
    public long sleepMillis = 0; // determines fps of game, the lower the better
    SoundPool SP; // plays music
    int sound_hit; // sound of item hit
    Canvas canvas; // the canvas


    // loads everything
    public GameThread(GameSurfaceView gameSurfaceView){
        super();
        this.gameSurfaceView = gameSurfaceView;
        SP = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);  // deprecated from API level 21 on
        sound_hit = SP.load(gameSurfaceView.getContext(), R.raw.snowballsplat, 1);
    }

    // sets thread running to true
    public void setRunning(boolean running){
        this.running = running;
    }

    @Override
    public void run(){
        while(running){

            // basically locks canvas and posts it
            // only if it hasn't been done already
            if(!gameSurfaceView.posted){
                canvas = gameSurfaceView.getHolder().lockCanvas();

            }
            else {
                gameSurfaceView.posted = false;
            }

            if(canvas != null){
                synchronized (gameSurfaceView.getHolder()){
                    gameSurfaceView.myDraw(canvas);
                }
                if(!gameSurfaceView.posted){
                    gameSurfaceView.getHolder().unlockCanvasAndPost(canvas);
                }



            }

            try{
                sleep(sleepMillis); // makes thread wait before drawing again
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }


        }
    }

}
