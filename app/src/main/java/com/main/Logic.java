package com.main;

public class Logic {


    public static boolean check(int a, int x, int y, int r, int[][] FieldArr){
        // проверка возможности поставить корабль
        // a - палубы; x и y - координаты; r - ориентация
        boolean b = true;
        switch (r){
            case 1:
                if (x>0 && y>0 && x<=11-a) {
                    for (int i = 0; i < a; i++) {
                        if (FieldArr[x][y] != 0) {
                            b = false;
                        }
                        x++;
                    }
                } else b = false;
                break;
            case -1:
                if (x>0 && y>0 && y<=11-a){
                    for (int i = 0; i < a; i++) {
                        if (FieldArr[x][y] != 0) {
                            b = false;
                        }
                        y++; }
                } else b = false;
                break;
        }
        return b;
    }

    public static void fillArr(int a, int x, int y, int r, int[][] FieldArr){
        //заполнение массива числами:
        //      1 - корабль
        //      2 - обводка вокруг корабля
        switch (r) {
            case 1: //горизонтально
                //корабль
                for (int i = 0; i < a; i++) {
                    FieldArr[x][y] = 1;
                    x++;
                }
                //---
                //обводка
                if (x != 11) {
                    FieldArr[x][y] = 2;
                }
                y++;
                if (x != 11 && y != 11) {
                    FieldArr[x][y] = 2;
                }
                for (int i = 0; i < a; i++) {
                    x--;
                    if (y != 11) {
                        FieldArr[x][y] = 2;
                    }
                }
                x--;
                if (x != 0 && y != 11) {
                    FieldArr[x][y] = 2;
                }
                y--;
                if (x != 0) {
                    FieldArr[x][y] = 2;
                }
                y--;
                if (x != 0 && y != 0) {
                    FieldArr[x][y] = 2;
                }

                for (int i = 0; i < a; i++) {
                    x++;
                    if (y != 0) {
                        FieldArr[x][y] = 2;
                    }
                }
                x++;
                if (x != 11 && y != 0) {
                    FieldArr[x][y] = 2;
                }
                //---
                break;
            case -1: //вертикально
                //корабль
                for (int i = 0; i < a; i++) {
                    FieldArr[x][y] = 1;
                    y++;
                }
                //---
                //обводка
                if (y != 11) {
                    FieldArr[x][y] = 2;
                }
                x++;
                if (y != 11 && x != 11) {
                    FieldArr[x][y] = 2;
                }
                for (int i = 0; i < a; i++) {
                    y--;
                    if (x != 11) {
                        FieldArr[x][y] = 2;
                    }
                }
                y--;
                if (y != 0 && x != 11) {
                    FieldArr[x][y] = 2;
                }
                x--;
                if (y != 0) {
                    FieldArr[x][y] = 2;
                }
                x--;
                if (y != 0 && x != 0) {
                    FieldArr[x][y] = 2;
                }
                for (int i = 0; i < a; i++) {
                    y++;
                    if (x != 0) {
                        FieldArr[x][y] = 2;
                    }
                }
                y++;
                if (y != 11 && x != 0) {
                    FieldArr[x][y] = 2;
                }
                //---
                break;
        }
    }
}
