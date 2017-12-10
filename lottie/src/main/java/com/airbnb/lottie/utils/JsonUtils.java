package com.airbnb.lottie.utils;

import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
  private JsonUtils() {
  }

  public static PointF pointFromJsonObject(JSONObject values, float scale) {

    return new PointF(
        valueFromObject(values.opt("x")) * scale,
        valueFromObject(values.opt("y")) * scale);
  }

  public static PointF pointFromJsonObject(JsonReader reader, float scale) throws IOException {
    reader.beginObject();
    reader.nextName();
    float x = valueFromObject(reader) * scale;
    reader.nextName();
    float y = valueFromObject(reader) * scale;
    return new PointF(x, y);
  }

  public static PointF pointFromJsonArray(JSONArray values, float scale) {
    if (values.length() < 2) {
      throw new IllegalArgumentException("Unable to parse point for " + values);
    }
    return new PointF(
        (float) values.optDouble(0, 1) * scale,
        (float) values.optDouble(1, 1) * scale);
  }

  public static PointF pointFromJsonArray(JsonReader reader, float scale) throws IOException {
    reader.beginArray();
    float x = (float) reader.nextDouble();
    float y = (float) reader.nextDouble();
    while (reader.peek() != JsonToken.END_ARRAY) {
      reader.skipValue();
    }
    reader.endArray();
    return new PointF(x * scale, y * scale);
  }

  public static float valueFromObject(Object object) {
    if (object instanceof Float) {
      return (float) object;
    } else if (object instanceof Integer) {
      return (Integer) object;
    } else if (object instanceof Double) {
      return (float) (double) object;
    } else if (object instanceof JSONArray) {
      return (float) ((JSONArray) object).optDouble(0);
    } else {
      return 0;
    }
  }

  public static float valueFromObject(JsonReader reader) throws IOException {
    try {
      reader.beginArray();
    } catch (IOException e) {
      // This value is either an array or a double. However, peek will throw if it's a double.
      return (float) reader.nextDouble();
    }
    float value = (float) reader.nextDouble();
    while (reader.peek() != JsonToken.END_ARRAY) {
      reader.skipValue();
    }
    reader.endArray();
    return value;
  }

  /** Eventually this should not be used anymore */
  public static JsonReader jsonToReader(Object json) {
    return new JsonReader(new StringReader(json.toString()));
  }

  public static float[] jsonToFloatArray(JsonReader reader) throws IOException {
    FloatArrayList array = new FloatArrayList();
    reader.beginArray();
    while (reader.peek() == JsonToken.NUMBER) {
      array.add((float) reader.nextDouble());
    }
    reader.endArray();
    return array.complete();
  }

  public static List<PointF> jsonToPointArray(JsonReader reader) throws IOException {
    return jsonToPointArray(reader, 1f);
  }

  public static List<PointF> jsonToPointArray(JsonReader reader, float scale) throws IOException {
    List<PointF> points = new ArrayList<>();

    reader.beginArray();
    while (reader.peek() == JsonToken.BEGIN_ARRAY) {
      reader.beginArray();
      float x = (float) reader.nextDouble();
      float y = (float) reader.nextDouble();
      points.add(new PointF(x * scale, y * scale));
      reader.endArray();
    }
    reader.endArray();
    return points;
  }
}
