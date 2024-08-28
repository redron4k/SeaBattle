package com.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Random;
import android.content.Context;

import static com.main.Logic.*;


public class PlayFragment extends Fragment implements View.OnTouchListener {

    private int n1 = 4, n2 = 3, n3 = 2, n4 = 1, t2 = 1, t3 = 1;
    private int[] ships = new int[]{4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    private RelativeLayout eFieldLayout, layout;
    private ImageView shot;
    private static int boxSize;
    private int[][] eFieldArr = new int[11][11];
    private int shotType;
    private CreateActivity c;
    private static ImageView eField;
    private Animation anim;
    private int a = 0, x1 = 0, y1 = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int fieldSize = CreateActivity.getFieldSize();
        auto();
        boxSize = CreateActivity.getBoxSize();
        RelativeLayout.LayoutParams MyParamsF = new RelativeLayout.LayoutParams(fieldSize, fieldSize);

        View v = inflater.inflate(R.layout.play_fragment, container, false);

        eField = v.findViewById(R.id.eField);
        eField.setLayoutParams(MyParamsF);
        eFieldLayout = v.findViewById(R.id.eFieldLayout);
        RadioGroup changeShot = v.findViewById(R.id.changeShot);
        changeShot.check(R.id.r1);
        shotType = 1;
        changeShot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.r1:
                        shotType = 1;
                        break;
                    case R.id.r2:
                        shotType = 2;
                        break;
                    case R.id.r3:
                        shotType = 3;
                        break;
                }
            }
        });
        eField.setOnTouchListener(this);
        layout = v.findViewById(R.id.layout);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_anim);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.c = (CreateActivity) getActivity();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int shotX = ((int) (event.getX() / boxSize) * boxSize);
        int shotY = ((int) (event.getY() / boxSize) * boxSize);
        int Xc = shotX / boxSize;
        int Yc = shotY / boxSize;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            shotType(Xc, Yc, shotX, shotY);
        }
        return true;
    }

    public void doShot(int shotX, int shotY, int x, int y){
        shot = new ImageView(getContext());
        if (shotCheck(x, y)) {
            eFieldLayout.addView(shot, boxSize, boxSize);
            shot.setX(shotX);
            shot.setY(shotY);
            shot.startAnimation(anim);
        }
    }

    boolean q = true;
    public void shotType(int x, int y, int shotX, int shotY){
        switch (shotType){
            case 1:
                doShot(shotX, shotY, x, y);
                if (q)
                    c.createShot();
                break;
            case 2:
                if (t2 > 0) {
                doShot(shotX, shotY, x, y);
                y++; shotY+=boxSize; doShot(shotX, shotY, x, y);
                x--; shotX-=boxSize; doShot(shotX, shotY, x, y);
                y--; shotY-=boxSize; doShot(shotX, shotY, x, y);
                y--; shotY-=boxSize; doShot(shotX, shotY, x, y);
                x++; shotX+=boxSize; doShot(shotX, shotY, x, y);
                x++; shotX+=boxSize; doShot(shotX, shotY, x, y);
                y++; shotY+=boxSize; doShot(shotX, shotY, x, y);
                y++; shotY+=boxSize; doShot(shotX, shotY, x, y);
                t2--;
                c.createShot();
                }
                break;
            case 3:
                if (t3 > 0) {
                    if (a != 1) {
                        a = 1;
                        x1 = x;
                        y1 = y;
                        break;
                    } else {
                        if (x1 == x) {
                            y = 1;
                            shotY = boxSize;
                            for (int i = 1; i < 11; i++) {
                                doShot(shotX, shotY, x, y);
                                y++;
                                shotY += boxSize;
                            }
                        } else if (y1 == y) {
                            x = 1;
                            shotX = boxSize;
                            for (int i = 1; i < 11; i++) {
                                doShot(shotX, shotY, x, y);
                                x++;
                                shotX += boxSize;
                            }
                        }
                        a = 0;
                    }
                    t3--;
                    c.createShot();
                }
                break;
        }
    }

    public boolean shotCheck(int x, int y) {
        //проверка на попадание
        boolean a = true;
        if (x == 0 || y == 0 || x > 10 || y > 10 || eFieldArr[x][y] < 0) {
            a = false;
            q = false;
        } else {
            q = true;
            if (eFieldArr[x][y] != 1) {
                // мимо
                shot.setImageResource(R.drawable.spot);
                eFieldArr[x][y] = -2;
            } else {
                // попал
                shot.setImageResource(R.drawable.cross);
                eFieldArr[x][y] = -1;
                checkKilled(x, y);
            }
        }
        return a;
    }

    public boolean checkKilled(int x, int y){
        //метод для проверки корабля на уничтожение
        int decks = 1;
        int killedDecks = 1;
        int a = 0;
        boolean k = false;
        int minX = x;
        int minY = y;
        int r = 1;
        for (int i = 0; i < 4; i++) {
            if (x == 10) break;
            else {
                x++; a++;
                if (eFieldArr[x][y] == 1 || eFieldArr[x][y] == -1) {
                    decks++;
                    if (eFieldArr[x][y] == -1) killedDecks++;
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
                if (eFieldArr[x][y] == 1 || eFieldArr[x][y] == -1) {
                    decks++;
                    if (x<minX) minX = x;
                    if (eFieldArr[x][y] == -1) killedDecks++;
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
                if (eFieldArr[x][y] == 1 || eFieldArr[x][y] == -1) {
                    decks++;
                    r = -1;
                    if (eFieldArr[x][y] == -1) killedDecks++;
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
                if (eFieldArr[x][y] == 1 || eFieldArr[x][y] == -1) {
                    decks++;
                    r = -1;
                    if (y<minY) minY = y;
                    if (eFieldArr[x][y] == -1) killedDecks++;
                }
                else break;
            }
        }
        if (killedDecks == decks) {
            int w, h;
            k = true;
            int X = minX*boxSize;
            int Y = minY*boxSize;
            ImageView ship = new ImageView(getContext());
            switch (decks){
                case 1:
                    n1--;
                    ship.setImageResource(R.drawable.ship_1);
                    w = boxSize; h = boxSize;
                    eFieldLayout.addView(ship, w, h);
                    ship.setX(X);
                    ship.setY(Y);
                    if (r == -1) ship.setRotation(90);
                    break;
                case 2:
                    n2--;
                    ship.setImageResource(R.drawable.ship_2);
                    w = boxSize*2; h = boxSize;
                    eFieldLayout.addView(ship, w, h);
                    ship.setX(X);
                    ship.setY(Y);
                    if (r == -1) {
                        ship.setRotation(90);
                        ship.setX(X-boxSize/2);
                        ship.setY(Y+boxSize/2);
                    }
                    break;
                case 3:
                    n3--;
                    ship.setImageResource(R.drawable.ship_3);
                    w = boxSize*3; h = boxSize;
                    eFieldLayout.addView(ship, w, h);
                    ship.setX(X);
                    ship.setY(Y);
                    if (r == -1) {
                        ship.setRotation(90);
                        ship.setX(X - boxSize);
                        ship.setY(Y + boxSize);
                    }
                    break;
                case 4:
                    n4--;
                    ship.setImageResource(R.drawable.ship_4);
                    w = boxSize*4; h = boxSize;
                    eFieldLayout.addView(ship, w, h);
                    ship.setX(X);
                    ship.setY(Y);
                    if (r == -1) {
                        ship.setRotation(90);
                        ship.setX(X-boxSize*3/2);
                        ship.setY(Y+boxSize*3/2);
                    }
                    break;
            }
            if(r == 1)
            fillLayout(decks, minX+decks, minY, r);
            else
            fillLayout(decks, minX, minY+decks, r);
            if(n4 == 0 && n3 == 0 && n2 == 0 && n1 == 0) {
                gameEnded();
                TextView youWin = new TextView(getContext());
                youWin.setText(getResources().getString(R.string.win));
                youWin.setTextColor(Color.GREEN);
                youWin.setTextSize(45);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.CENTER_HORIZONTAL);
                youWin.setLayoutParams(p);
                layout.addView(youWin);
            }
        }
        return k;
    }

    static void gameEnded(){
        eField.setOnTouchListener(null);
        CreateActivity.setPlaying(false);
    }

    public void auto() {
        Random rnd = new Random();
        int rX, rY, orientation;
        for (int b : ships) {
            orientation = rnd.nextInt(2);
            switch (orientation){
                case 0: orientation = 1; break;
                case 1: orientation = -1; break;
            }
            do {
                rX = rnd.nextInt(11);
                rY = rnd.nextInt(11);
            } while (!check(b, rX, rY, orientation, eFieldArr));
            fillArr(b, rX, rY, orientation, eFieldArr);
        }
    }

    public void abc(int x, int y){
        ImageView shot = new ImageView(getContext());
        shot.setImageResource(R.drawable.spot);
        int w = boxSize, h = boxSize;
        eFieldLayout.addView(shot, w, h);
        shot.setX(x*boxSize);
        shot.setY(y*boxSize);
    }

    public void fillLayout(int a, int x, int y, int r) {
        switch (r) {
            case 1:
                if (x != 11) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                y++;
                if (x != 11 && y != 11) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                for (int i = 0; i < a; i++) {
                    x--;
                    if (y != 11) {
                        eFieldArr[x][y] = -2;
                        abc(x,y);
                    }
                }
                x--;
                if (x != 0 && y != 11) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                y--;
                if (x != 0) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                y--;
                if (x != 0 && y != 0) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }

                for (int i = 0; i < a; i++) {
                    x++;
                    if (y != 0) {
                        eFieldArr[x][y] = -2;
                        abc(x,y);
                    }
                }
                x++;
                if (x != 11 && y != 0) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                break;
            case -1:
                if (y != 11) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                x++;
                if (y != 11 && x != 11) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                for (int i = 0; i < a; i++) {
                    y--;
                    if (x != 11) {
                        eFieldArr[x][y] = -2;
                        abc(x,y);
                    }
                }
                y--;
                if (y != 0 && x != 11) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                x--;
                if (y != 0) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                x--;
                if (y != 0 && x != 0) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                for (int i = 0; i < a; i++) {
                    y++;
                    if (x != 0) {
                        eFieldArr[x][y] = -2;
                        abc(x,y);
                    }
                }
                y++;
                if (y != 11 && x != 0) {
                    eFieldArr[x][y] = -2;
                    abc(x,y);
                }
                break;
        }
    }
}