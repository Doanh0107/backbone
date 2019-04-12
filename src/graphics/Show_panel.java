package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import main.Main;

public class Show_panel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Show_panel(){		
	}		
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		g2.setColor(Color.red);
		g2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		try{
			if (Main.cnt[0] == 0){
				g2.drawString("Graphs will be displayed here!", 280, 300);
				return;
			}
		}catch(Exception e){
			return;
		}
		
		//Draw result
		g2.setColor(Color.black);
		g2.drawString(Main.node_off + "/" + (Main.Core.size() + Main.Edge.size()), 670, 540);
		g2.drawString(Main.link_off + "/" + Main.arcs.size(), 670, 560);		g2.setColor(Color.red);
		
		//Draw cores
		g2.setColor(Color.red);
		for(int i = 0; i < Main.cnt[0]; i ++){
			if (Main.Core.elementAt(i).getStt())
				g2.fillOval(Main.Core.elementAt(i).getPoint().getX() - 8, Main.Core.elementAt(i).getPoint().getY() - 8, 17, 17);
			else{
				g2.setColor(Color.gray);
				g2.fillOval(Main.Core.elementAt(i).getPoint().getX() - 8, Main.Core.elementAt(i).getPoint().getY() - 8, 17, 17);
				g2.setColor(Color.red);
			}
		}
		
		//Draw edges		
		g2.setColor(Color.blue);
		for(int i = 0; i < Main.cnt[1]; i ++){			
			if (!Main.Edge.elementAt(i).getStt()){
				g2.setColor(Color.gray);
				g2.fillOval(Main.Edge.elementAt(i).getPoint().getX() - 7, Main.Edge.elementAt(i).getPoint().getY() - 7, 15, 15);
				g2.setColor(Color.blue);
				continue;
			}
			g2.fillOval(Main.Edge.elementAt(i).getPoint().getX() - 7, Main.Edge.elementAt(i).getPoint().getY() - 7, 15, 15);			
		}
		
		//Draw aggregations
		
		g2.setColor(Color.green);
		for(int i = 0; i < Main.cnt[2]; i ++){			
			g2.fillOval(Main.Aggre.elementAt(i).getPoint().getX() - 6, Main.Aggre.elementAt(i).getPoint().getY() - 6, 12, 12);			
		}
		
		//draw arcs
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(1));
		for(int i = 0; i < Main.arcs.size(); i ++){
			if (!Main.arcs.elementAt(i).getStt()){
				g2.setColor(Color.magenta);
				g2.drawLine(getX(Main.arcs.elementAt(i).x), getY(Main.arcs.elementAt(i).x), getX(Main.arcs.elementAt(i).y), getY(Main.arcs.elementAt(i).y));
				g2.setColor(Color.black);
				continue;
			}
			g2.drawLine(getX(Main.arcs.elementAt(i).x), getY(Main.arcs.elementAt(i).x), getX(Main.arcs.elementAt(i).y), getY(Main.arcs.elementAt(i).y));
		}
	}		

	static int getX(int i){
		if (i < Main.cnt[0]) return Main.Core.elementAt(i).getPoint().getX();
		if (i < Main.cnt[0] + Main.cnt[1]) return Main.Edge.elementAt(i - Main.cnt[0]).getPoint().getX();
		return Main.Aggre.elementAt(i - Main.cnt[0] - Main.cnt[1]).getPoint().getX();
	}
	
	static int getY(int i){
		if (i < Main.cnt[0]) return Main.Core.elementAt(i).getPoint().getY();
		if (i < Main.cnt[0] + Main.cnt[1]) return Main.Edge.elementAt(i - Main.cnt[0]).getPoint().getY();
		return Main.Aggre.elementAt(i - Main.cnt[0] - Main.cnt[1]).getPoint().getY();
	}
}
