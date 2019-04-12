package algorithm;

import java.util.Random;
import java.util.Vector;

public class Node_Opt {
	
	static Vector<Integer> off_node(int current, Vector<Node> Core, Vector<Node> Edge, Vector<Pair> arcs, int[][] x){
		Vector<Integer> res = new Vector<Integer>();
		if(current < Core.size()) Core.elementAt(current).changeStt();
		else Edge.elementAt(current - Core.size()).changeStt();		
		for(int i = 0; i < arcs.size(); i ++)
			if(arcs.elementAt(i).getStt() && (arcs.elementAt(i).x == current || arcs.elementAt(i).y == current)){
				res.addElement(i);
				arcs.elementAt(i).changeStt();
				x[arcs.elementAt(i).x][arcs.elementAt(i).y] = 0;
				x[arcs.elementAt(i).y][arcs.elementAt(i).x] = 0;
			}
		
		return res;
	}
	
	static void on_node(int current, Vector<Node> Core, Vector<Node> Edge, Vector<Pair> arcs, int[][] x, Vector<Integer> off_list){
		int tmp;
		
		if(current < Core.size()) Core.elementAt(current).changeStt();
		else Edge.elementAt(current - Core.size()).changeStt();
		for(int i = 0; i < off_list.size(); i ++){
			tmp = off_list.elementAt(i);
			arcs.elementAt(tmp).changeStt();
			x[arcs.elementAt(tmp).x][arcs.elementAt(tmp).y]= 1;
			x[arcs.elementAt(tmp).y][arcs.elementAt(tmp).x] = 1;
		}
	}
	
	public static void Random_off(Vector<Node> Core, Vector<Node> Edge, Vector<Pair> arcs, int[][] x, double[][] t, int count, int start, int[][] cap, double alpha, double k, double[][] f){
		int cnt = Edge.size() + Core.size();
		int current;
		boolean[] check = new boolean[cnt];
		Random r = new Random();
		Vector<Integer> off_list = new Vector<Integer>();
		
		for(int i = 0; i < cnt; i ++)
			check[i] = true;
		while(cnt > 0){
			current = r.nextInt(Edge.size() + Core.size());
			if (check[current]){
				check[current] = false;
				cnt --;
				off_list = off_node(current, Core, Edge, arcs, x);
				
				f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
				
				if(f[0][0] == -1) 		
					on_node(current, Core, Edge, arcs, x, off_list);
			}
		}
		f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
	}
	
	public static void Least_link(Vector<Node> Core, Vector<Node> Edge, Vector<Pair> arcs, int[][] x, double[][] t, int count, int start, int[][] cap, double alpha, double k, double[][] f){
		int[] queue, label;
		int cnt = Core.size() + Edge.size(), current;
		Vector<Integer> off_list = new Vector<Integer>();
		
		queue = new int[cnt];
		label = new int[cnt];
		for(int i = 0; i < cnt; i ++){
			queue[i] = 0;
			label[i] = i;
			for(int j = 0; j < count; j ++)
				queue[i] += x[i][j];
		}
		for(int i = cnt - 1; i > 0; i --)
			for(int j = 0; j < i; j ++)
				if (queue[j] > queue[j + 1]){
					int tmp = queue[j];
					queue[j] = queue[j + 1];
					queue[j + 1] = tmp;
					tmp = label[j];
					label[j] = label[j + 1];
					label[j + 1] = tmp;
				}
		for(int i = 0; i < cnt; i ++){
			current = label[i];
			off_list = off_node(current, Core, Edge, arcs, x);
			
			f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
			
			if(f[0][0] == -1) 		
				on_node(current, Core, Edge, arcs, x, off_list);
		}
		f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
	}
	
	public static void Least_flow(Vector<Node> Core, Vector<Node> Edge, Vector<Pair> arcs, int[][] x, double[][] t, int count, int start, int[][] cap, double alpha, double k, double[][] f){
		int[] queue, label;
		int cnt = Core.size() + Edge.size(), current;
		Vector<Integer> off_list = new Vector<Integer>();
		
		queue = new int[cnt];
		label = new int[cnt];
		for(int i = 0; i < cnt; i ++){
			queue[i] = 0;
			label[i] = i;
			for(int j = 0; j < count; j ++)
				queue[i] += f[i][j];
		}
		for(int i = cnt - 1; i > 0; i --)
			for(int j = 0; j < i; j ++)
				if (queue[j] > queue[j + 1]){
					int tmp = queue[j];
					queue[j] = queue[j + 1];
					queue[j + 1] = tmp;
					tmp = label[j];
					label[j] = label[j + 1];
					label[j + 1] = tmp;
				}
		for(int i = 0; i < cnt; i ++){
			current = label[i];
			off_list = off_node(current, Core, Edge, arcs, x);
			
			f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
			
			if(f[0][0] == -1) 		
				on_node(current, Core, Edge, arcs, x, off_list);
		}
		f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
	}
	
	public static void Optimize_edge(Vector<Node> Core, Vector<Node> Edge, Vector<Pair> arcs, int[][] x, double[][] t, int count, int start, int[][] cap, double alpha, double k, double[][] f){
		int cnt = Core.size() + Edge.size();
		int current;
		Vector<Integer> off_list = new Vector<Integer>();
		
		for(int i = Core.size(); i < cnt; i ++){
			if (Edge.elementAt((i - Core.size() - 1 + Edge.size()) % Edge.size()).getStt() && Edge.elementAt((i + 1 - Core.size()) % Edge.size()).getStt()){
				current = i;
				
				off_list = off_node(current, Core, Edge, arcs, x);
				
				f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
				
				if(f[0][0] == -1) 		
					on_node(current, Core, Edge, arcs, x, off_list);
			}
		}
		
		for(int i = 0; i < Core.size(); i ++){
			current = i;
			
			off_list = off_node(current, Core, Edge, arcs, x);
			
			f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
			
			if(f[0][0] == -1) 		
				on_node(current, Core, Edge, arcs, x, off_list);
		}
		
		f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
		
	}

}
