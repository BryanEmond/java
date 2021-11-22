package codes.blitz.game.client;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.glassfish.tyrus.client.ClientManager;

import codes.blitz.game.Solver;
import codes.blitz.game.message.GameMessage;
import codes.blitz.game.serialization.MessageDecoder;
import codes.blitz.game.serialization.MessageEncoder;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ClientEndpoint(decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebsocketGameClient {
  private final Solver solver;
  private final CountDownLatch latch;

  public WebsocketGameClient(Solver solver) {
    this.solver = solver;
    this.latch = new CountDownLatch(1);
  }

  public void run() {
    ClientManager client = ClientManager.createClient();

    try {
      client.connectToServer(this,
          URI.create("ws://127.0.0.1:8765"));
      latch.await();
    } catch (DeploymentException | InterruptedException
        | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @OnOpen
  public void onOpen(Session session) throws IOException, EncodeException {
    var registration = Map.of("type", "REGISTER", "token", System.getenv("TOKEN"));
    session.getBasicRemote().sendObject(registration);
  }

  @OnMessage
  public void processMessageFromServer(GameMessage receivedMessage,
                                       Session session) throws IOException, EncodeException {
    var nextMove = Map.of("type", "COMMAND",
        "tick", receivedMessage.tick(),
        "actions", solver.getAnswer(receivedMessage));

    session.getBasicRemote().sendObject(nextMove);

  }

  @SuppressWarnings("unused")
  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    latch.countDown();
  }
}