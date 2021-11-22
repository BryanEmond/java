package codes.blitz.game.message;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public record CoordinatePair(int x, int y) {
}
