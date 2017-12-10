package com.airbnb.lottie.model;

import android.graphics.Color;
import android.util.JsonReader;

import com.airbnb.lottie.model.animatable.AnimatableValue;

import java.io.IOException;

public class ColorFactory implements AnimatableValue.Factory<Integer> {
  public static final ColorFactory INSTANCE = new ColorFactory();

  @Override public Integer valueFromObject(JsonReader reader, float scale) throws IOException {
    reader.beginArray();
    double r = reader.nextDouble();
    double g = reader.nextDouble();
    double b = reader.nextDouble();
    double a = reader.nextDouble();
    reader.endArray();

    if (r <= 1 && g <= 1 && b <= 1 && a <= 1) {
      r *= 255;
      g *= 255;
      b *= 255;
      a *= 255;
    }

    return Color.argb((int) a, (int) r, (int) g, (int) b);
  }
}
