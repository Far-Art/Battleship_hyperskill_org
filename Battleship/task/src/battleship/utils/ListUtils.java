package battleship.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtils {

    public static <T extends Collection> boolean containsAny(T list, T other) {
        return other.stream().anyMatch(list::contains);
    }

    public static <T> List<T> flatten(List<? extends Collection<T>> collections) {
        return collections.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
