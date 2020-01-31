package com.sih2020.railwayreservationsystem.Utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import java.util.ArrayList;

public class GenerateBackground {
    public static GradientDrawable generateBackground() {
        ArrayList<String> colorsList = new ArrayList<>();

        for (int i = 47; i <= 107; i++) {
            String colorCode = "#" + convertToHex(i) + convertToHex(i + 90) + convertToHex(i + 3);
            colorsList.add(colorCode);
        }

        int colors[] = new int[colorsList.size()];
        int i = 0;
        for (int j = 0; j < colorsList.size(); j++)
            colors[i++] = Color.parseColor(colorsList.get(j));

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors);

        gd.setCornerRadius(0f);
        return gd;
    }

    private static String convertToHex(int i) {
        String hex = "";
        while (i > 0) {
            int d = i % 16;
            String u = "";
            if (d <= 9)
                u = Integer.toString(d);
            else
                u = Character.toString((char) (d + 55));
            hex = u + hex;
            i = i / 16;
        }
        if (hex.length() == 1)
            hex = "0" + hex;
        return hex;
    }
}
