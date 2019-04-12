package algorithm;

public class Pair {
	public int x;
	public int y;
	public int cap;
	private boolean stt;
	
	public Pair(int x, int y, int cap){
		this.x = x;
		this.y = y;
		this.cap = cap;
		this.stt = true;
	}
	
	public void changeStt(){
		this.stt = ! this.stt;
	}
	
	public boolean getStt(){
		return this.stt;
	}
}
