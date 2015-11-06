package com.mishobu.animaciones;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    boolean continuar = true;
    float velocidad = 0.5f;
    int dt = 10;
    int tiempo = 0;
    private Thread hilo = null;
    private DinamicaView dinamica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dinamica = new DinamicaView(this);
        setContentView(dinamica);
        hilo = new Thread(dinamica);
        hilo.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        continuar = true;
        if(hilo == null) {
            hilo = new Thread(dinamica);
            hilo.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        continuar = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    class DinamicaView extends View implements Runnable {

        int x, y, ymax;
        Paint paintFondo, paintParticula, paint;

        public DinamicaView(Context context) {
            super(context);
            paintFondo = new Paint();
            paintParticula = new Paint();
            paint = new Paint();
            paintFondo.setColor(Color.WHITE);
            paintParticula.setColor(Color.RED);
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
        }

        @Override
        public void run() {
            while (continuar) {
                tiempo += dt;
                y += (int) (velocidad * dt);
                if(y > ymax) velocidad = -velocidad;
                if(y < 0) velocidad = - velocidad;
                postInvalidate();
                try {
                    Thread.sleep(dt);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            x= w / 2;
            y = 0;
            ymax = h;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawPaint(paintFondo);
            canvas.drawCircle(x, y, 30, paintParticula);
            canvas.drawText("y = " + y, 50, 50, paint);
            canvas.drawText("t = " + tiempo, 50, 90, paint);
        }
    }

}