
public class Pair<A,B> {
	private A _item1;
	private B _item2;
	
	public Pair(A item1, B item2) {
		_item1 = item1;
		_item2 = item2;
	}
	public A getFirst() {
		return _item1;
	}
	public B getSecond() {
		return _item2;
	}

}
