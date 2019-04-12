package algorithm;

import java.util.Vector;

public class Algorithms {		
	
	//Reset graph(network)
	public static void reset(Vector<Node> Core, Vector<Node> Edge, Vector<Pair> arcs, int[][] x){
		try{
			for(int i = 0; i < Edge.size(); i ++){
				if (!Edge.elementAt(i).getStt()) Edge.elementAt(i).changeStt();
			}
		
			for(int i = 0; i < arcs.size(); i ++){
				if (!arcs.elementAt(i).getStt()) arcs.elementAt(i).changeStt();
				x[arcs.elementAt(i).x][arcs.elementAt(i).y] = 1;
				x[arcs.elementAt(i).y][arcs.elementAt(i).x] = 1;
			}
			for(int i = 0; i < Core.size(); i ++)
				if (!Core.elementAt(i).getStt()) Core.elementAt(i).changeStt();
		}catch(Exception ex){
			return;
		}
	}
	
	//Find all paths
	public static double[][] findPath(int[][] x, double[][] t, int count, int start, int[][] cap, double alpha, double k){
		double[][] res = new double[count][count];
		int[] tmp;
		int pos;
		
		for(int i = 0; i < count; i ++)
			for(int j = 0; j < count; j ++)
				res[i][j] = 0;
		
		for(int i = start; i < count - 1; i ++)
			for(int j = i + 1; j < count; j ++){
				//System.out.println("find path: " + i + " and " + j);
				tmp = bfs(i,j,x,count,cap,t[i][j] * k,res, alpha, start);
				if (tmp[0] == -1){
					res[0][0] = - 1;
					return res;
				}
				//System.out.println("bfs is done!" + tmp.length);
				pos = 1;
				while(pos < tmp.length){
					res[tmp[pos]][tmp[pos - 1]] += t[i][j] * k;
					res[tmp[pos - 1]][tmp[pos]] += t[i][j] * k;
					pos ++;
				}
				//System.out.println("find path: " + i + " and " + j + " is done!");
			}
		return res;
	}
	
	//Breadth-first Search
	private static int[] bfs(int fi, int la, int[][] x, int count, int[][] cap, double value, double[][] res, double alpha, int start){
		int[] tr;
		int[] trace = new int[count];
		int[] queue = new int[count + 9];
		int[] check = new int[count];
		int pos, queue_last, current;
		
		for(int i = 0; i < count; i ++){
			check[i] = 0;
			trace[i] = -1;
		}
		pos = 0;
		queue_last = 1;
		check[fi] = 1;
		queue[0] = fi;
		while (pos < queue_last){
			current = queue[pos];
			pos ++;
			for(int i = 0; i < count; i ++)
				if (x[current][i] == 1 && check[i] == 0 && res[current][i] + value <= cap[current][i] * alpha){
					check[i] = check[current] + 1;
					trace[i] = current;
					if (i < start){
						queue[queue_last] = i;					
						queue_last ++;
					}
				}
			if(check[la] > 0) break;
		}
		if (check[la] == 0) {
			tr = new int[1];
			tr[0] = -1;
			return tr;
		}
		tr = new int[check[la]];
		current = la;
		tr[check[current] - 1] = current;
		do{
			current = trace[current];
			tr[check[current] - 1] = current;
		}while(current != fi);
		return tr;
	}
}
