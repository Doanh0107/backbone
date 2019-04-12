package algorithm;

public class Node {
	private int number;
	private boolean stt;
	private Cordi p;
	
	public Node(int x){
		this.stt = true;
		this.p = new Cordi(0,0);
		this.number = x;
	}
	
	public int getNum(){
		return this.number;
	}
	
	public void changeStt(){
		this.stt = ! this.stt;
	}
	
	public boolean getStt(){
		return this.stt;
	}
	
	public void setPoint(int x, int y){
		this.p.set(x, y);		
	}
	
	public Cordi getPoint(){
		return this.p;
	}
}
