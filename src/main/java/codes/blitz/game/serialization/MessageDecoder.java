package codes.blitz.game.serialization;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import codes.blitz.game.message.GameMessage;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;

public class MessageDecoder implements Decoder.Text<GameMessage> {
  private static final JsonMapper jsonMapper = JsonMapperSingleton.getInstance();

  @Override
  public GameMessage decode(String message) throws DecodeException {
    try {
      var rawMessage = jsonMapper.readValue(message, new TypeReference<Map<String, Object>>() {
      });
      if ("ERROR".equals(rawMessage.get("type"))) {
        System.err.println(rawMessage);
        System.exit(1);
      }
      return jsonMapper.readValue(message, GameMessage.class);
    } catch (JsonProcessingException e) {
      throw new DecodeException(message, e.getMessage(), e);
    }
  }

  @Override
  public boolean willDecode(String s) {
    return (s != null);
  }
}
