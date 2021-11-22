package codes.blitz.game;

import java.util.ArrayList;
import java.util.List;

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
  private final ArrayList<Integer> matrix;
  private int[][] I, L, J, T, Z, S, O;

  public Solver() {
    // This method should be use to initialize some variables you will need
    // throughout the challenge.
    this.matrix = new ArrayList<Integer>();
    I = new int[][] { { 1, 1, 1, 1 } };
    L = new int[][] { { 2, 0 }, { 2, 0 }, { 2, 2 } };
    J = new int[][] { { 0, 3 }, { 0, 3 }, { 3, 3 } };
    T = new int[][] { { 0, 4, 0 }, { 4, 4, 4 } };
    Z = new int[][] { { 5, 5, 0 }, { 0, 5, 5 } };
    S = new int[][] { { 0, 6, 6 }, { 6, 6, 0 } };
    O = new int[][] { { 7, 7 }, { 7, 7 } };

  }

  /*
   * Here is where the magic happens, for now the answer is a single 'I'. I bet
   * you can do better ;)
   */
  public Answer getAnswer(GameMessage gameMessage) throws JsonProcessingException {
    Question question = gameMessage.payload();
    // System.out.println("Received Question: " +
    // jsonMapper.writeValueAsString(question));
    var totems = List.of(new TotemAnswer(Totem.I, List.of(new CoordinatePair(0, 0), new CoordinatePair(1, 0),
        new CoordinatePair(2, 0), new CoordinatePair(3, 0))));
    placeTotems(question);
    Answer answer = new Answer(totems);
    System.out.println("Sending Answer: " + jsonMapper.writeValueAsString(answer));
    return answer;
  }

  public List<Object> placeTotems(Question question) {
    int[][] currentTotems;
    var totems = List.of();
    for (int i = 0; question.totems().size() > i; i++) {
      switch (question.totems().get(i).shape().toString().toUpperCase()) {
      case "I":
        currentTotems = this.I;
        break;
      case "L":
        currentTotems = this.L;
        break;
      case "J":
        currentTotems = this.J;
        break;
      case "Z":
        currentTotems = this.Z;
        break;
      case "S":
        currentTotems = this.S;
        break;
      case "T":
        currentTotems = this.T;
        break;
      case "O":
        currentTotems = this.O;
        break;
      default:
        currentTotems = new int[][] { { 0, 0, 0, 0 } };
      }

      if (currentTotems == this.I) {
        for (int j = 0; currentTotems.length > j; j++) {
          for (int x = 0; currentTotems[j].length > x; x++) {
            if(currentTotems[j][x] != 0){
              matrix.add(i + 1);
            }else{
              matrix.add(0);
            }
          }
        }
      } 
      else {
        // for (int j = 0; currentTotems.length > j; j++) {
        //   for (int x = 0; currentTotems[j].length > x; x++) {
        //     matrix.add(currentTotems[j]);
        //   }
        // }
      }
    }
  // totems.add(new TotemAnswer(totemForm, List.of(pair1, pair2, pair3, pair4)));
  return totems;
}}