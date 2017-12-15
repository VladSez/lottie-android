package com.airbnb.lottie.model.content;

import android.util.JsonReader;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;

import java.io.IOException;

public class Mask {
  public enum MaskMode {
    MaskModeAdd,
    MaskModeSubtract,
    MaskModeIntersect,
    MaskModeUnknown;
  }

  private final MaskMode maskMode;
  private final AnimatableShapeValue maskPath;
  private final AnimatableIntegerValue opacity;

  private Mask(MaskMode maskMode, AnimatableShapeValue maskPath, AnimatableIntegerValue opacity) {
    this.maskMode = maskMode;
    this.maskPath = maskPath;
    this.opacity = opacity;
  }

  public static class Factory {
    private Factory() {
    }

    public static Mask newMask(
        JsonReader reader, LottieComposition composition) throws IOException {
      MaskMode maskMode = null;
      AnimatableShapeValue maskPath = null;
      AnimatableIntegerValue opacity = null;

      reader.beginObject();
      while (reader.hasNext()) {
        switch (reader.nextName()) {
          case "mode":
            switch (reader.nextString()) {
              case "a":
                maskMode = MaskMode.MaskModeAdd;
                break;
              case "s":
                maskMode = MaskMode.MaskModeSubtract;
                break;
              case "i":
                maskMode = MaskMode.MaskModeIntersect;
                break;
              default:
                maskMode = MaskMode.MaskModeUnknown;
            }
            break;
          case "pt":
            maskPath = AnimatableShapeValue.Factory.newInstance(reader, composition);
            break;
          case "o":
            AnimatableIntegerValue.Factory.newInstance(reader, composition);
            break;
        }
      }
      reader.endObject();

      return new Mask(maskMode, maskPath, opacity);
    }
  }

  public MaskMode getMaskMode() {
    return maskMode;
  }

  public AnimatableShapeValue getMaskPath() {
    return maskPath;
  }

  public AnimatableIntegerValue getOpacity() {
    return opacity;
  }
}
