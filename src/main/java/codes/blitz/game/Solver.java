package codes.blitz.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import codes.blitz.game.message.Answer;
import codes.blitz.game.message.CoordinatePair;
import codes.blitz.game.message.GameMessage;
import codes.blitz.game.message.Question;
import codes.blitz.game.message.Totem;
import codes.blitz.game.message.TotemAnswer;
// import codes.blitz.game.message.TotemQuestion;
import codes.blitz.game.serialization.JsonMapperSingleton;

public class Solver {
  private final JsonMapper jsonMapper = JsonMapperSingleton.getInstance();
  private List<CoordinatePair> I, L, J, T, Z, S, O;

  public Solver() {
    // This method should be use to initialize some variables you will need
    // throughout the challenge.
    I = List.of(new CoordinatePair(0, 0), new CoordinatePair(1, 0), new CoordinatePair(2, 0), new CoordinatePair(3, 0));
    L = List.of(new CoordinatePair(0, 2), new CoordinatePair(0, 1), new CoordinatePair(0, 0), new CoordinatePair(1, 0));
    J = List.of(new CoordinatePair(0, 1), new CoordinatePair(0, 0), new CoordinatePair(1, 0), new CoordinatePair(0, 2));
    T = List.of(new CoordinatePair(0, 0), new CoordinatePair(1, 0), new CoordinatePair(2, 0), new CoordinatePair(1, 1));
    Z = List.of(new CoordinatePair(0, 1), new CoordinatePair(1, 1), new CoordinatePair(1, 0), new CoordinatePair(2, 0));
    S = List.of(new CoordinatePair(0, 0), new CoordinatePair(0, 1), new CoordinatePair(1, 1), new CoordinatePair(2, 1));
    O = List.of(new CoordinatePair(1, 0), new CoordinatePair(1, 1), new CoordinatePair(0, 0), new CoordinatePair(1, 0));

  }

  /*
   * Here is where the magic happens, for now the answer is a single 'I'. I bet
   * you can do better ;)
   */
  public Answer getAnswer(GameMessage gameMessage) throws JsonProcessingException {
    Question question = gameMessage.payload();
    System.out.println("Received Question: " + jsonMapper.writeValueAsString(question));
    // var totems = List.of(new TotemAnswer(Totem.I, List.of(new CoordinatePair(0,
    // 0), new CoordinatePair(1, 0),
    // new CoordinatePair(2, 0), new CoordinatePair(3, 0))));
    var totems = placeTotems(question);
    Answer answer = new Answer(totems);
    System.out.println("Sending Answer: " + jsonMapper.writeValueAsString(answer));
    System.out.println(answer);
    return answer;
  }

  public List<TotemAnswer> placeTotems(Question question) {
    List<CoordinatePair> currentTotems;
    List<TotemAnswer> totems = new ArrayList<TotemAnswer>();
    Totem totemForm;
    int height = 0;
    for (int i = 0; question.totems().size() > i; i++) {
      switch (question.totems().get(i).shape().toString().toUpperCase()) {
      case "I":
        currentTotems = this.I;
        totemForm = Totem.I;
        break;
      case "L":
        currentTotems = this.L;
        totemForm = Totem.L;
        break;
      case "J":
        currentTotems = this.J;
        totemForm = Totem.J;
        break;
      case "Z":
        currentTotems = this.Z;
        totemForm = Totem.Z;
        break;
      case "S":
        currentTotems = this.S;
        totemForm = Totem.S;
        break;
      case "T":
        currentTotems = this.T;
        totemForm = Totem.T;
        break;
      case "O":
        currentTotems = this.O;
        totemForm = Totem.O;
        break;
      default:
        currentTotems = List.of(new CoordinatePair(0, 0), new CoordinatePair(0, 0), new CoordinatePair(0, 0),
            new CoordinatePair(0, 0));
        totemForm = Totem.O;
      }

      currentTotems = currentTotems.stream().map(cord -> new CoordinatePair(cord.x(), cord.y() + height)).collect(Collectors.toList());

      totems.add(new TotemAnswer(totemForm, currentTotems));
      
      switch (question.totems().get(i).shape().toString().toUpperCase()) {
      case "I":
        height += 1;
        break;
      case "L":
        height += 3;
        break;
      case "J":
        height += 2;
        break;
      case "Z":
        height += 2;
        break;
      case "S":
        height += 2;
        break;
      case "T":
        height += 2;
        break;
      case "O":
        height += 2;
        break;
      default:
        height += 0;
      }
    }
    return totems;
  }
}