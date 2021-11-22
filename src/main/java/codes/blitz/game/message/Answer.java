package codes.blitz.game.message;

import java.util.List;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Answer(List<TotemAnswer> totems) {
    @Override
    public String toString() {
        List<CoordinatePair> coordinates =
            totems.stream().map(TotemAnswer::coordinates).flatMap(Collection::stream).collect(Collectors.toList());
        int maxX = coordinates.stream().mapToInt(CoordinatePair::x).max().getAsInt()+1;

        Map<Integer, List<BitSet>> collect = coordinates
            .stream()
            .collect(
                Collectors.groupingBy(
                    CoordinatePair::y,
                    Collectors.mapping(
                        coord -> {
                            BitSet sb = new BitSet();
                            sb.set(coord.x());
                            return sb;
                        },
                        Collectors.toList())));

        return collect
            .entrySet()
            .stream()
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .map(entry-> entry.getValue().stream().reduce(new BitSet(maxX), (a, b) -> {
                    a.or(b);
                    return a;
                }))
            .map(bs-> IntStream.range(0, bs.length())
                .mapToObj(b -> String.valueOf(bs.get(b) ? '*' : ' '))
                .collect(Collectors.joining()))
            .collect(Collectors.joining("\n"));
    }   
}
