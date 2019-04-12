package algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;

import main.Main;

public class Link_Opt {
	
	public static void Random_off(Vector<Pair> arcs, int[][] x, double[][] t, int count, int start, int[][] cap, double alpha, double k, double[][] f){
		Random r = new Random();
		boolean[] check = new boolean[arcs.size()];
		int cnt = 0, current;
		
		for(int i = 0; i < arcs.size(); i ++){
			check[i] = arcs.elementAt(i).getStt();
			if (check[i]) cnt ++;
		}
		
		while(cnt > 0){
			current = r.nextInt(arcs.size());
			if (check[current]){
				check[current] = false;
				cnt --;
				arcs.elementAt(current).changeStt();
				x[arcs.elementAt(current).x][arcs.elementAt(current).y] = 0;
				x[arcs.elementAt(current).y][arcs.elementAt(current).x] = 0;
				
				f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
				if (f[0][0] != -1) continue;
				
				arcs.elementAt(current).changeStt();
				x[arcs.elementAt(current).x][arcs.elementAt(current).y] = 1;
				x[arcs.elementAt(current).y][arcs.elementAt(current).x] = 1;
			}
		}
		f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
	}
	
	public static void least_flow(Vector<Pair> arcs, int[][] x, double[][] t, int count, int start, int[][] cap, double alpha, double k, double[][] f){
		Vector<Integer> queue = new Vector<Integer>();
		int current;
		
		for(int i = 0; i < arcs.size(); i ++)
			if (arcs.elementAt(i).getStt()) queue.addElement(i);
		
		Collections.sort(queue, new Comparator<Integer>(){

			@Override
			public int compare(Integer arg0, Integer arg1) {
				if (Main.f[arcs.elementAt(arg0).x][arcs.elementAt(arg0).y] <= Main.f[arcs.elementAt(arg1).x][arcs.elementAt(arg1).y])
					return -1;
				return 0;
			}
		});
		
		//System.out.println(f[arcs.elementAt(queue.elementAt(0)).x][arcs.elementAt(queue.elementAt(0)).y] + "   " + f[arcs.elementAt(queue.elementAt(50)).x][arcs.elementAt(queue.elementAt(50)).y]);
		
		for(int i = 0; i < queue.size(); i++){
			current = queue.elementAt(i);
			arcs.elementAt(current).changeStt();
			x[arcs.elementAt(current).x][arcs.elementAt(current).y] = 0;
			x[arcs.elementAt(current).y][arcs.elementAt(current).x] = 0;
			
			f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
			if (f[0][0] != -1) continue;
			
			arcs.elementAt(current).changeStt();
			x[arcs.elementAt(current).x][arcs.elementAt(current).y] = 1;
			x[arcs.elementAt(current).y][arcs.elementAt(current).x] = 1;
		}
		
		f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
	}

}
