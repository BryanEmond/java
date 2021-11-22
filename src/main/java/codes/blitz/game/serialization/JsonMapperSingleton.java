package codes.blitz.game.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public final class JsonMapperSingleton {
  private static JsonMapper instance;

  public static synchronized JsonMapper getInstance() {
    if (instance == null) {
      instance = JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();
    }
    return instance;
  }
}
