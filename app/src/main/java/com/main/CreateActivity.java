package com.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

import static com.main.Logic.*;

public class CreateActivity extends MyActivity implements View.OnTouchListener {
    static int displayWidth, displayHeight, fieldSize, boxSize, w, h, r;
    int n1 = 4, n2 = 3, n3 = 2, n4 = 1;
    int[][] pFieldArr = new int[11][11];
    int[] ships = new int[]{4,3,3,2,2,2,1,1,1,1};
    ImageView pField, ship, shipPreview;
    ImageView shot;
    RelativeLayout pFieldLayout, MainLayout;
    RadioGroup changeShip;
    Animation anim;
    static boolean playing;
    int shotStage = 0;
    int hitR = 0;
    int x = 0, y = 0;

    boolean c, l = false;

    public static void setPlaying(boolean b) {
        playing = b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        layoutParams();

        playing = false;
        MainLayout = findViewById(R.id.mainLayout);
        pFieldLayout = findViewById(R.id.pFieldLayout);
        changeShip = findViewById(R.id.changeShip);
        ship = new ImageView(this);
        changeShip.check(R.id.four);
        r = 1;
        shipPreview = findViewById(R.id.shipPreview);
        shipPreview.setAdjustViewBounds(true);
        shipPreview.setMaxWidth(boxSize*8);
        shipPreview.setMaxHeight(boxSize*2);
        changeShip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.four:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.fourDecks)+" "+n4, Toast.LENGTH_SHORT)
                                .show();
                        shipPreview.setImageResource(R.drawable.ship_4);
                        shipPreview.setMaxWidth(boxSize*8);
                        shipPreview.setMaxHeight(boxSize*2);
                        break;
                    case R.id.three:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.threeDecks)+" "+n3, Toast.LENGTH_SHORT)
                                .show();
                        shipPreview.setImageResource(R.drawable.ship_3);
                        shipPreview.setMaxWidth(boxSize*6);
                        shipPreview.setMaxHeight(boxSize*2);
                    break;
                    case R.id.two:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.twoDecks)+" "+n2, Toast.LENGTH_SHORT)
                                .show();
                        shipPreview.setImageResource(R.drawable.ship_2);
                        shipPreview.setMaxWidth(boxSize*4);
                        shipPreview.setMaxHeight(boxSize*2);;
                        break;
                    case R.id.one:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.oneDeck)+" "+n1, Toast.LENGTH_SHORT)
                                .show();
                        shipPreview.setImageResource(R.drawable.ship_1);
                        shipPreview.setMaxWidth(boxSize*2);
                        shipPreview.setMaxHeight(boxSize*2);
                        break;
                }
            }});
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b1:
                if (c) {
                    MainLayout.removeView(findViewById(R.id.createTxt));
                    MainLayout.removeView(findViewById(R.id.b1));
                    MainLayout.removeView(findViewById(R.id.createTools));
                    PlayFragment playFragment = new PlayFragment();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.eContainer, playFragment).commit();
                    pField.setOnTouchListener(null);
                    n1 = 4; n2 = 3; n3 = 2; n4 = 1;
                    playing = true;
                } else Toast.makeText(getApplicationContext(), getResources().getString(R.string.arrangeAll), Toast.LENGTH_SHORT).show();
                break;
            case R.id.b2:
                if (!playing){
                finish();
                } else {
                    startActivity(new Intent(CreateActivity.this, DialogActivity.class));
                }
                break;
            case R.id.rotate:
                r*=-1;
                switch (r){
                    case -1: shipPreview.animate().rotation(90); break;
                    case 1: shipPreview.animate().rotation(0); break;
                }
                break;
            case R.id.autoCreate:
                if (!c && !l)
                auto(); else
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.clearAll), Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (!playing){
        finish();
    } else {
        startActivity(new Intent(CreateActivity.this, DialogActivity.class));
        }
    }

    public void layoutParams() {
        Display display = getWindowManager().getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();
        fieldSize = (int)displayWidth * 7 / 12;
        boxSize = fieldSize / 11;
        pField = findViewById(R.id.pField);
        RelativeLayout.LayoutParams MyParams = new RelativeLayout.LayoutParams(fieldSize, fieldSize);
        MyParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        MyParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        pField.setLayoutParams(MyParams);
        pField.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
                if (!c) {
                    float touchX = event.getX();
                    int shipX = ((int) (touchX / boxSize) * boxSize);
                    float touchY = event.getY();
                    int shipY = ((int) (touchY / boxSize) * boxSize);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        switch (changeShip.getCheckedRadioButtonId()) {
                            case R.id.four:
                                if (check(4, (int) (touchX / boxSize), (int) (touchY / boxSize), r, pFieldArr) && n4 > 0) {
                                    four(shipX, shipY);
                                    l = true;
                                    fillArr(4, (int) (touchX / boxSize), (int) (touchY / boxSize), r, pFieldArr);
                                } else
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.impossible), Toast.LENGTH_SHORT)
                                            .show();
                                break;
                            case R.id.three:
                                if (check(3, (int) (touchX / boxSize), (int) (touchY / boxSize), r, pFieldArr) && n3 > 0) {
                                    three(shipX, shipY);
                                    l = true;
                                    fillArr(3, (int) (touchX / boxSize), (int) (touchY / boxSize), r, pFieldArr);
                                } else
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.impossible), Toast.LENGTH_SHORT)
                                            .show();
                                break;
                            case R.id.two:
                                if (check(2, (int) (touchX / boxSize), (int) (touchY / boxSize), r, pFieldArr) && n2 > 0) {
                                    two(shipX, shipY);
                                    l = true;
                                    fillArr(2, (int) (touchX / boxSize), (int) (touchY / boxSize), r, pFieldArr);
                                } else
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.impossible), Toast.LENGTH_SHORT)
                                            .show();
                                break;
                            case R.id.one:
                                if (check(1, (int) (touchX / boxSize), (int) (touchY / boxSize), r, pFieldArr) && n1 > 0) {
                                    one(shipX, shipY);
                                    l = true;
                                    fillArr(1, (int) (touchX / boxSize), (int) (touchY / boxSize), r, pFieldArr);
                                } else
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.impossible), Toast.LENGTH_SHORT)
                                            .show();
                                break;
                        }
                    }
                    if (n1 == 0 && n2 == 0 && n3 == 0 && n4 == 0) c = true;
                }
        return true;
    }

    public static int getFieldSize() {
        return fieldSize;
    }
    public static int getBoxSize() {
        return boxSize;
    }

    public void auto() {
        Random rnd = new Random();
        int rX, rY;
        for (int b : ships) {
            ship = new ImageView(this);
            r = rnd.nextInt(2);
            switch (r){
                case 0: r = 1; break;
                case 1: r = -1; break; }

            do {
            rX = rnd.nextInt(11);
            rY = rnd.nextInt(11);} while (!check(b, rX, rY, r, pFieldArr));
            switch (b){
                case 4: four(rX*boxSize, rY*boxSize); break;
                case 3: three(rX*boxSize, rY*boxSize); break;
                case 2: two(rX*boxSize, rY*boxSize); break;
                case 1: one(rX*boxSize, rY*boxSize); break; }
            fillArr(b, rX, rY, r, pFieldArr);
        }
        c = true;
    }

    public void four(int X, int Y){
            ship = new ImageView(this);
            ship.setImageResource(R.drawable.ship_4);
            w = boxSize * 4; h = boxSize;
            n4--;
            pFieldLayout.addView(ship, w, h);
            ship.setX(X);
            ship.setY(Y);
            if (r == -1){
                ship.setRotation(90);
                ship.setX(X-boxSize*3/2);
                ship.setY(Y+boxSize*3/2);
        }
    }
    public void three(int X, int Y){
            ship = new ImageView(this);
            ship.setImageResource(R.drawable.ship_3);
            w = boxSize * 3; h = boxSize;
            pFieldLayout.addView(ship, w, h);
            ship.setX(X);
            ship.setY(Y);
            if (r == -1){
                ship.setRotation(90);
                ship.setX(X - boxSize);
                ship.setY(Y + boxSize);
        }
            n3--;
    }
    public void two(int X, int Y){
            ship = new ImageView(this);
            ship.setImageResource(R.drawable.ship_2);
            w = boxSize * 2; h = boxSize;
            pFieldLayout.addView(ship, w, h);
            ship.setX(X);
            ship.setY(Y);
            if (r == -1){
                ship.setRotation(90);
                ship.setX(X-boxSize/2);
                ship.setY(Y+boxSize/2);
        }
            n2--;
    }
    public void one(int X, int Y){
            ship = new ImageView(this);
            ship.setImageResource(R.drawable.ship_1);
            w = boxSize; h = boxSize;
            pFieldLayout.addView(ship, w, h);
            ship.setX(X);
            ship.setY(Y);
            if (r == -1) {
                ship.setRotation(90);
                ship.setX(X);
                ship.setY(Y);
        }
            n1--;
    }

    int f = 0;
    int x1, y1;
    int count = 0;
    int ls = 0;
    public void createShot(){
        if (playing) {
            if (shotStage == 0) {
                killed = false;
                f = 0;
                Random rnd = new Random();
                x = rnd.nextInt(11);
                y = rnd.nextInt(11);
                int shotX = x * boxSize;
                int shotY = y * boxSize;
                doShot(shotX, shotY, x, y);
            } else if (shotStage == 1){
                   if (f == 0) {
                       f++; doShot(++x*boxSize, y*boxSize, x, y);
                   } else {
                    if (f == 1) {
                        x = x1; f++; doShot(x*boxSize, ++y*boxSize, x, y);
                    } else {
                    if (f == 2) {
                        y = y1; f++; doShot(--x*boxSize, y*boxSize, x, y);
                    } else {
                    if (f == 3) {
                        x = x1; doShot(x*boxSize, --y*boxSize, x, y);
                    } else f = 0;
                   } } }
                if (pFieldArr[x][y] == -1) {
                    if (x == x1) hitR = -1;
                    if (y == y1) hitR = 1;
                }
            } else if (shotStage == 2){
                int shotX, shotY;
                switch (hitR) {
                    case 1:
                        if (ls == 0) x++;
                        else x--;
                            shotX = x * boxSize;
                            shotY = y * boxSize;
                            doShot(shotX, shotY, x, y);
                        break;
                    case -1:
                        if (ls == 0) y++;
                        else y--;
                            shotX = x * boxSize;
                            shotY = y * boxSize;
                            doShot(shotX, shotY, x, y);
                        break;
                }
            }
        }
    }

    boolean killed = false;

    public void doShot(int shotX, int shotY, int x, int y){
        if (shotCheck(x, y)) {
            count = 0;
            pFieldLayout.addView(shot, boxSize, boxSize);
            shot.setX(shotX);
            shot.setY(shotY);
            shot.startAnimation(anim);
            if (pFieldArr[x][y] == -2)
                ls = pFieldArr[x][y];
            else
            if (pFieldArr[x][y] == -1) {
                ls = 0;
                if (shotStage == 0) {
                    x1 = x; y1 = y; }
                if (shotStage<2 && !killed) {
                    shotStage++; }
                createShot();
            }
        } else {
            count++;
            if (count >= 5) {
                shotStage = 0;
                count = 0;
            }
            createShot();
        }
    }

    public boolean shotCheck(int x, int y) {
        boolean a = true;
        if (x>10 || y>10 || pFieldArr[x][y]<0) ls = -2;
        if (x < 1 || y < 1  || x>10 || y>10 || pFieldArr[x][y]<0) a = false;
        else if (pFieldArr[x][y] != 1) {
            shot = new ImageView(this);
            shot.setImageResource(R.drawable.spot);
            pFieldArr[x][y] = -2;
        } else {
            shot = new ImageView(this);
            shot.setImageResource(R.drawable.cross);
            pFieldArr[x][y] = -1;
            checkKilled(x,y);
        }
        return a;
    }

    public void checkKilled(int x, int y){
        int killedDecks = 1;
        int decks = 1;
        int a = 0;
        int minX = x;
        int minY = y;
        int r = 1;
        for (int i = 0; i < 4; i++) {
            if (x == 10) break;
            else {
                x++; a++;
                if (pFieldArr[x][y] == 1 || pFieldArr[x][y] == -1) {
                    decks++;
                    if (pFieldArr[x][y] == -1) killedDecks++;
                }
                else break;
            }
        }
        x = x-a;
        a = 0;
        for (int i = 0; i < 4; i++) {
            if (x == 1) break;
            else {
                x--; a++;
                if (pFieldArr[x][y] == 1 || pFieldArr[x][y] == -1) {
                    decks++;
                    if (x<minX) minX = x;
                    if (pFieldArr[x][y] == -1) killedDecks++;
                }
                else break;
            }
        }
        x = x+a;
        a = 0;
        for (int i = 0; i < 4; i++) {
            if (y == 10) break;
            else {
                y++; a++;
                if (pFieldArr[x][y] == 1 || pFieldArr[x][y] == -1) {
                    decks++;
                    r = -1;
                    if (pFieldArr[x][y] == -1) killedDecks++;
                }
                else break;
            }
        }
        y = y-a;
        a = 0;
        for (int i = 0; i < 4; i++) {
            if (y == 1) break;
            else {
                y--; a++;
                if (pFieldArr[x][y] == 1 || pFieldArr[x][y] == -1) {
                    decks++;
                    r = -1;
                    if (y<minY) minY = y;
                    if (pFieldArr[x][y] == -1) killedDecks++;
                }
                else break;
            }
        }
        if (killedDecks == decks) {
            shotStage = 0; f = 0; count = 0; ls = 0;
            hitR = 0;
            killed = true;
            switch (decks){
                case 1:
                    n1--;
                    break;
                case 2:
                    n2--;
                    break;
                case 3:
                    n3--;
                    break;
                case 4:
                    n4--;
                    break;
            }
            if(r == 1)
                fillLayout(decks, minX+decks, minY, r);
            else
                fillLayout(decks, minX, minY+decks, r);
            if(n4 == 0 && n3 == 0 && n2 == 0 && n1 == 0) {
                PlayFragment.gameEnded();
                TextView youLose = new TextView(this);
                youLose.setText(getResources().getString(R.string.lose));
                youLose.setTextColor(Color.RED);
                youLose.setTextSize(45);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.CENTER_IN_PARENT);
                youLose.setLayoutParams(p);
                MainLayout.addView(youLose);

            }
        }
    }

    public void abc(int x, int y){
        ImageView shot = new ImageView(this);
        shot.setImageResource(R.drawable.spot);
        int w = boxSize, h = boxSize;
        pFieldLayout.addView(shot, w, h);
        shot.setX(x*boxSize);
        shot.setY(y*boxSize);
        shot.startAnimation(anim);
    }

    public void fillLayout(int a, int x, int y, int r) {
        switch (r) {
            case 1:
                if (x != 11) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                y++;
                if (x != 11 && y != 11) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                for (int i = 0; i < a; i++) {
                    x--;
                    if (y != 11) {
                        pFieldArr[x][y] = -2;
                        abc(x,y);
                    }
                }
                x--;
                if (x != 0 && y != 11) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                y--;
                if (x != 0) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                y--;
                if (x != 0 && y != 0) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }

                for (int i = 0; i < a; i++) {
                    x++;
                    if (y != 0) {
                        pFieldArr[x][y] = -2;
                        abc(x,y);
                    }
                }
                x++;
                if (x != 11 && y != 0) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                break;
            case -1:
                if (y != 11) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                x++;
                if (y != 11 && x != 11) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                for (int i = 0; i < a; i++) {
                    y--;
                    if (x != 11) {
                        pFieldArr[x][y] = -2;
                        abc(x,y);
                    }
                }
                y--;
                if (y != 0 && x != 11) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                x--;
                if (y != 0) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                x--;
                if (y != 0 && x != 0) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                for (int i = 0; i < a; i++) {
                    y++;
                    if (x != 0) {
                        pFieldArr[x][y] = -2;
                        abc(x,y);
                    }
                }
                y++;
                if (y != 11 && x != 0) {
                    pFieldArr[x][y] = -2;
                    abc(x,y);
                }
                break;
        }
    }
}