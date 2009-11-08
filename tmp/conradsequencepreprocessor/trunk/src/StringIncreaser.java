
public class StringIncreaser implements Increaser<String>{
	
	private volatile int cnt = -1;

	public String increase(String t) {
		cnt++;
		return new StringBuilder(t).append(DELIM).append(cnt).toString();
	}

}
