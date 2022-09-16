package org.hyperskill.Battleship.utils;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ListUtils {
    public static <T extends Collection<?>> boolean containsAny(T list, T other) {
        return other.stream().anyMatch(list::contains);
    }

    public static <T> List<T> flatten(List<? extends Collection<T>> collections) {
        return collections.stream().flatMap(Collection::stream).collect(toList());
    }
}
