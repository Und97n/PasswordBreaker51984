package org.axiom.streamTest;

/**
 * <br><br>
 * Created 06.10.2019 19:40
 *
 * @author Zizitop
 */
public class Pair<K, V> {
	private K key;
	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
}
