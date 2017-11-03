package xdean.stackoverflow.rx;

import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import rx.Observable;

public class Q47057374 {
  public static void main(String[] args) {
    final ImmutableMap<String, Observable<Integer>> map = ImmutableMap.of(
        "1", Observable.just(1),
        "2", Observable.just(2),
        "3", Observable.just(3));
    System.out.println(toMap(map));
    final ImmutableMap<String, Observable<Integer>> multimap = ImmutableMap.of(
        "1", Observable.just(1, 2, 3),
        "2", Observable.just(4, 5, 6),
        "3", Observable.just(7, 8, 9));
    System.out.println(toMutimap(multimap));
  }

  public static <K, V> Map<K, V> toMap(Map<K, Observable<V>> map) {
    return Maps.transformValues(map, o -> o.toSingle().toBlocking().value());
  }

  public static <K, V> Multimap<K, V> toMutimap(Map<K, Observable<V>> map) {
    ArrayListMultimap<K, V> multimap = ArrayListMultimap.create();
    map.forEach((k, vo) -> vo.forEach(v -> multimap.put(k, v)));
    return multimap;
  }
}
