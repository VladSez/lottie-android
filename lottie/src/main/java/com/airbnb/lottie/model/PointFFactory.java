package com.airbnb.lottie.model;

import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;

import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.utils.JsonUtils;

import java.io.IOException;

public class PointFFactory implements AnimatableValue.Factory<PointF> {
  public static final PointFFactory INSTANCE = new PointFFactory();

  private PointFFactory() {
  }

  @Override public PointF valueFromObject(JsonReader reader, float scale) throws IOException {
    JsonToken token = reader.peek();
    if (token == JsonToken.BEGIN_ARRAY) {
      return JsonUtils.pointFromJsonArray(reader, scale);
    } else if (token == JsonToken.BEGIN_OBJECT) {
      return JsonUtils.pointFromJsonObject(reader, scale);
    } else {
      throw new IllegalArgumentException("Cannot convert json to point. Next token is " + token);
    }
  }
}
