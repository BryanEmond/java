package codes.blitz.game;

import codes.blitz.game.client.LocalGameClient;
import codes.blitz.game.client.WebsocketGameClient;

public class Application {
  @SuppressWarnings("resource")
  public static void main(String[] args) throws Exception {
    Solver solver = new Solver();
    if (System.getenv("TOKEN") != null) {
      new WebsocketGameClient(solver).run();
    } else {
      new LocalGameClient(solver).run();
    }
  }
}