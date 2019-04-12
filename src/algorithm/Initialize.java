package algorithm;

import java.util.Random;
import java.util.Vector;

public class Initialize {	
	
	public static void InitCordi(Vector<Node> Core, Vector<Node> Edge, Vector<Node> Aggre, int[] cnt){
		Node tmp;
		double k, angle;
		
		//Init Core cordinates
		k = 360 / (double)cnt[0];		
		angle = 0;
		for(int i = 0; i < cnt[0]; i ++){
			tmp = new Node(i + 1);
			tmp.setPoint((int)(375 - 120 * Math.sin(Math.toRadians(angle))), (int)(290 - 90 * Math.cos(Math.toRadians(angle))));
			Core.addElement(tmp);
			angle += k;
		}
		
		//Init Edge cordinates
		k = 360 / (double)cnt[1];
		angle = 0;
		for(int i = 0; i < cnt[1]; i ++){
			tmp = new Node(i + cnt[0] + 1);
			tmp.setPoint((int)(375 - 220 * Math.sin(Math.toRadians(angle))), (int)(290 - 160 * Math.cos(Math.toRadians(angle))));
			Edge.addElement(tmp);
			angle += k;
		}
		
		//Init Aggre cordinates
		k = 360 / (double)cnt[2];
		angle = 0;
		for(int i = 0; i < cnt[2]; i ++){
			tmp = new Node(i + cnt[0] + cnt[1] + 1);
			tmp.setPoint((int)(375 - 360 * Math.sin(Math.toRadians(angle))), (int)(290 - 270 * Math.cos(Math.toRadians(angle))));
			Aggre.addElement(tmp);
			angle += k;
		}
	}
	
	public static Vector<Pair> InitGraph(int[] cnt, int[][] x, int[][] pos, double[][] t, int[][] cap){
		Vector<Pair> res = new Vector<Pair>();
		
		int count = cnt[0] + cnt[1] + cnt[2];		
		
		for(int i = 0; i < count; i ++)
			for(int j = 0; j < count; j ++){
				x[i][j] = 0;
				pos[j][j] = -1;
				t[i][j] = 2;
				cap[i][j] = 10000;
			}
		
		int tmp_x, tmp_y;
		Random r = new Random();
		
		for(int i = 0; i < cnt[0]; i ++){
			tmp_y = next(i,cnt[0]);
			res.addElement(new Pair(i, tmp_y, 15));
			turn(x,i,tmp_y,pos,res.size() - 1);
			
			tmp_y = r.nextInt(cnt[0]);
			while (x[i][tmp_y] == 1)
				tmp_y = r.nextInt(cnt[0]);
			res.addElement(new Pair(i,tmp_y, 15));
			turn(x,i,tmp_y,pos,res.size() - 1);
		}
				
		for(int i = 0; i < cnt[1]; i ++){
			tmp_x = cnt[0] + i;
			tmp_y = cnt[0] + next(i, cnt[1]);
			res.addElement(new Pair(tmp_x, tmp_y, 5));
			turn(x, tmp_x, tmp_y, pos, res.size() - 1);
			
			tmp_y = (int)((double)i / cnt[1] * cnt[0]);
			res.addElement(new Pair(tmp_x, tmp_y, 5));
			turn(x,tmp_x,tmp_y,pos,res.size() - 1);
			
			tmp_y = next(tmp_y, cnt[0]);
			res.addElement(new Pair(tmp_x, tmp_y, 5));
			turn(x,tmp_x,tmp_y,pos,res.size() - 1);
		}
				
		for(int i = 0; i < cnt[2]; i ++){
			tmp_x = cnt[0] + cnt[1] + i;
			tmp_y = cnt[0] + (int)((double)i / cnt[2] * cnt[1]);
			res.addElement(new Pair(tmp_x, tmp_y, 1));
			turn(x,tmp_x,tmp_y,pos,res.size() - 1);
			
			tmp_y = cnt[0] + next((int)((double)i / cnt[2] * cnt[1]), cnt[1]);
			res.addElement(new Pair(tmp_x, tmp_y, 1));
			turn(x,tmp_x,tmp_y,pos,res.size() - 1);
		}
		
		return res;
	}
	
	static void turn(int[][] x, int tmp_x, int tmp_y, int[][] pos, int position){
		x[tmp_x][tmp_y] = 1;
		x[tmp_y][tmp_x] = 1;
		pos[tmp_x][tmp_y] = position;
		pos[tmp_y][tmp_x] = position;
	}

	static int prev(int x, int y){
		if (x == 0) return y - 1;
		return x - 1;
	}
	
	static int next(int x, int y){
		if (x == y - 1) return 0;
		return x + 1;
	}
}
