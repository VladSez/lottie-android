package com.airbnb.lottie.model.animatable;

import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.animation.Keyframe;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PathKeyframe;
import com.airbnb.lottie.animation.keyframe.PathKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimatablePathValue implements AnimatableValue<PointF, PointF> {
  public static AnimatableValue<PointF, PointF> createAnimatablePathOrSplitDimensionPath(
      JsonReader reader, LottieComposition composition) throws IOException {

    AnimatablePathValue pathAnimation = null;
    AnimatableFloatValue xAnimation = null;
    AnimatableFloatValue yAnimation = null;

    reader.beginObject();
    while (reader.peek() != JsonToken.END_OBJECT) {
      String name = reader.nextName();
      switch (name) {
        case "k":
          pathAnimation = new AnimatablePathValue(reader, composition);
          break;
        case "x":
          xAnimation = AnimatableFloatValue.Factory.newInstance(reader, composition);
          break;
        case "y":
          yAnimation = AnimatableFloatValue.Factory.newInstance(reader, composition);
          break;
      }
    }
    reader.endObject();

    if (pathAnimation != null) {
      return pathAnimation;
    }
    return new AnimatableSplitDimensionPathValue(xAnimation, yAnimation);
  }

  private final List<Keyframe<PointF>> keyframes = new ArrayList<>();

  /**
   * Create a default static animatable path.
   */
  AnimatablePathValue() {
    keyframes.add(new Keyframe<>(new PointF(0, 0)));
  }

  AnimatablePathValue(Object json, LottieComposition composition) throws IOException {
    if (hasKeyframes(json)) {
      JSONArray jsonArray = (JSONArray) json;
      int length = jsonArray.length();
      for (int i = 0; i < length; i++) {
        JSONObject jsonKeyframe = jsonArray.optJSONObject(i);
        PathKeyframe keyframe = PathKeyframe.Factory.newInstance(jsonKeyframe, composition,
            ValueFactory.INSTANCE);
        keyframes.add(keyframe);
      }
      Keyframe.setEndFrames(keyframes);
    } else {
      keyframes.add(
          new Keyframe<>(JsonUtils.pointFromJsonArray((JSONArray) json, composition.getDpScale())));
    }
  }

  private boolean hasKeyframes(Object json) {
    if (!(json instanceof JSONArray)) {
      return false;
    }

    Object firstObject = ((JSONArray) json).opt(0);
    return firstObject instanceof JSONObject && ((JSONObject) firstObject).has("t");
  }

  @Override
  public BaseKeyframeAnimation<PointF, PointF> createAnimation() {
    if (keyframes.get(0).isStatic()) {
      return new PointKeyframeAnimation(keyframes);
    }
    return new PathKeyframeAnimation(keyframes);
  }

  private static class ValueFactory implements AnimatableValue.Factory<PointF> {
    private static final Factory<PointF> INSTANCE = new ValueFactory();

    private ValueFactory() {
    }

    @Override public PointF valueFromObject(JsonReader reader, float scale) throws IOException {
      return JsonUtils.pointFromJsonArray(reader, scale);
    }
  }
}
