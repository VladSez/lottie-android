package com.airbnb.lottie.model.animatable;

import android.util.JsonReader;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.animation.Keyframe;
import com.airbnb.lottie.animation.keyframe.TextKeyframeAnimation;
import com.airbnb.lottie.model.DocumentData;

import java.io.IOException;
import java.util.List;

public class AnimatableTextFrame extends BaseAnimatableValue<DocumentData, DocumentData> {

  AnimatableTextFrame(List<Keyframe<DocumentData>> keyframes) {
    super(keyframes);
  }

  @Override public TextKeyframeAnimation createAnimation() {
    return new TextKeyframeAnimation(keyframes);
  }

  public static final class Factory {
    private Factory() {
    }

    public static AnimatableTextFrame newInstance(
        JsonReader reader, LottieComposition composition) throws IOException {
      // TODO (json)
      // if (json != null && json.has("x")) {
      //   composition.addWarning("Lottie doesn't support expressions.");
      // }
      return new AnimatableTextFrame(
          AnimatableValueParser
              .newInstance(reader, 1, composition, AnimatableTextFrame.ValueFactory.INSTANCE));
    }
  }

  private static class ValueFactory implements AnimatableValue.Factory<DocumentData> {
    private static final AnimatableTextFrame.ValueFactory INSTANCE =
        new AnimatableTextFrame.ValueFactory();

    private ValueFactory() {
    }

    @Override
    public DocumentData valueFromObject(JsonReader reader, float scale) throws IOException {
      // STOPSHIP (json)
      return null;
      // return DocumentData.Factory.newInstance((JSONObject) object);
    }
  }
}
