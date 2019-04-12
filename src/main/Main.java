package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import algorithm.Algorithms;
import algorithm.Initialize;
import algorithm.Link_Opt;
import algorithm.Node;
import algorithm.Node_Opt;
import algorithm.Pair;
import graphics.Show_panel;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Main {
	
	public static Vector<Node> Core;
	public static Vector<Node> Edge;
	public static Vector<Node> Aggre;
	public static Vector<Pair> arcs;
	public static int[] cnt;
	private int[][] x, pos, cap;
	private double[][] t;
	public static double[][] f;
	public static int count, start, node_off, link_off;
	private double alpha, k;

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		cnt = new int[3];
		arcs = new Vector<Pair>();
		initialize();		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Quang - Backbone network");
		frame.setSize(1024, 640);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Show_panel show_panel = new Show_panel();
		show_panel.setBackground(Color.white);
		show_panel.setBorder(new LineBorder(new Color(128, 128, 128), 2, true));
		show_panel.setSize(750, 580);
		show_panel.setLocation(250, 10);
		frame.getContentPane().add(show_panel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.GRAY, 2, true));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.GRAY, 2, true));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 228, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
					.addGap(770))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JLabel lblAlgorithm = new JLabel("Algorithms");
		lblAlgorithm.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Algorithms.reset(Core, Edge, arcs, x);
				f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
				show_panel.repaint();
			}
		});
		
		JLabel lblNodeHeuristic = new JLabel("Node heuristic");
		
		JLabel lblLinkHeuristic = new JLabel("Link heuristic");
		
		JLabel lblAlpha = new JLabel("Alpha");
		
		JLabel lblOffpeak = new JLabel("Off-peak");
		
		JSpinner spinner_3 = new JSpinner();
		SpinnerNumberModel spm_3 = new SpinnerNumberModel(1, 0.5, 1.0, 0.1);
		spinner_3.setModel(spm_3);
		
		JRadioButton rdbtnOffpeak = new JRadioButton("Off-peak");				
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"OE", "LL", "LF", "R"}));
		comboBox.setSelectedIndex(0);
		
		JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {"LF", "R"}));
		comboBox_1.setSelectedIndex(0);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int node_h, link_h;
				node_h = comboBox.getSelectedIndex();
				link_h = comboBox_1.getSelectedIndex();
				alpha = spm_3.getNumber().doubleValue();
				if (rdbtnOffpeak.isSelected()) k = 0.2;
				else k = 1;				
				
				try{
					if (Core.isEmpty()) return;
				}catch(Exception e){
					return;
				}
				
				Algorithms.reset(Core, Edge, arcs, x);
				
				//Node heuristic
				if (node_h == 1) Node_Opt.Least_link(Core, Edge, arcs, x, t, count, start, cap, alpha, k, f);
				else if (node_h == 2) Node_Opt.Least_flow(Core, Edge, arcs, x, t, count, start, cap, alpha, k, f);
				else if (node_h == 3) Node_Opt.Random_off(Core, Edge, arcs, x, t, count, start, cap, alpha, k, f);
				else Node_Opt.Optimize_edge(Core, Edge, arcs, x, t, count, start, cap, alpha, k, f); 
				
				node_off = 0;
				for(int i = 0 ; i < Core.size(); i ++)
					node_off += (Core.elementAt(i).getStt() ? 0 : 1);
				for(int i = 0 ; i < Edge.size(); i ++)
					node_off += (Edge.elementAt(i).getStt() ? 0 : 1);
				
				//Link heuristic
				if(link_h == 0) Link_Opt.least_flow(arcs, x, t, count, start, cap, alpha, k, f);
				else Link_Opt.Random_off(arcs, x, t, count, start, cap, alpha, k, f);
				
				link_off = 0;
				for(int i = 0; i < arcs.size(); i ++)
					link_off += (arcs.elementAt(i).getStt() ? 0 : 1);
				
				
				show_panel.repaint();
			}
		});
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(81)
					.addComponent(lblAlgorithm, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(89))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(lblLinkHeuristic, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblNodeHeuristic, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(lblAlpha)
						.addComponent(lblOffpeak))
					.addGap(43)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnOffpeak)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(spinner_3, Alignment.LEADING)
							.addComponent(comboBox_1, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(comboBox, Alignment.LEADING, 0, 61, Short.MAX_VALUE)))
					.addContainerGap(44, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(72)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnReset, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
						.addComponent(btnApply, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
					.addGap(91))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(lblAlgorithm)
					.addGap(22)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNodeHeuristic)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLinkHeuristic)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAlpha)
						.addComponent(spinner_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOffpeak)
						.addComponent(rdbtnOffpeak))
					.addGap(33)
					.addComponent(btnApply)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnReset)
					.addGap(48))
		);
		panel_1.setLayout(gl_panel_1);
		
		JLabel lblCore = new JLabel("Core");
		lblCore.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		SpinnerNumberModel spm = new SpinnerNumberModel(5,1,10,1);
		JSpinner spinner = new JSpinner(spm);
		
		JLabel lblEdge = new JLabel("Edge");
		lblEdge.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		SpinnerNumberModel spm1 = new SpinnerNumberModel(20,1,30,1);
		JSpinner spinner_1 = new JSpinner(spm1);
		
		JLabel lblAggre = new JLabel("Aggregation");
		lblAggre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		SpinnerNumberModel spm2 = new SpinnerNumberModel(80,1,120,1);
		JSpinner spinner_2 = new JSpinner(spm2);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cnt[0] = spm.getNumber().intValue();
				cnt[1] = spm1.getNumber().intValue();
				cnt[2] = spm2.getNumber().intValue();				
				Core = new Vector<Node>();
				Edge = new Vector<Node>();
				Aggre = new Vector<Node>();
				
				Initialize.InitCordi(Core, Edge, Aggre, cnt);
				
				count = cnt[0] + cnt[1] + cnt[2];
				x = new int[count][count];
				pos = new int[count][count];
				cap = new int[count][count];
				t = new double[count][count];
				
				arcs = Initialize.InitGraph(cnt, x, pos, t, cap);
				
				node_off = 0;
				link_off = 0;
				alpha = 1;
				k = 1;
				start = cnt[0] + cnt[1];
				//System.out.println("Find Paths ...");
				f = Algorithms.findPath(x, t, count, start, cap, alpha, k);
				//System.out.println("Find Paths completed!");
				
				for(int i = 0; i < count; i ++)
					for(int j = 0; j < count; j ++)
						cap[i][j] = 0;
				
				int[] val = new int[3];
				val[0] = 15;
				val[1] = 5;
				val[2] = 1;
				for(int i = 0; i < arcs.size(); i ++){
					if (arcs.elementAt(i).cap == 15) val[0] = Math.max(val[0], (int)f[arcs.elementAt(i).x][arcs.elementAt(i).y] * 2);
					else if (arcs.elementAt(i).cap == 5) val[1] = Math.max(val[1], (int)f[arcs.elementAt(i).x][arcs.elementAt(i).y] * 2);
					else val[2] = Math.max(val[2], (int)f[arcs.elementAt(i).x][arcs.elementAt(i).y] * 2);
				}
				
				for(int i = 0; i < arcs.size(); i ++){
					if (arcs.elementAt(i).cap == 15) arcs.elementAt(i).cap = val[0];
					else if (arcs.elementAt(i).cap == 5) arcs.elementAt(i).cap = val[1];
					else arcs.elementAt(i).cap = val[2];
					cap[arcs.elementAt(i).x][arcs.elementAt(i).y] = arcs.elementAt(i).cap;
					cap[arcs.elementAt(i).y][arcs.elementAt(i).x] = arcs.elementAt(i).cap;
				}
				
				show_panel.repaint();
			}
		});
		
		JLabel lblInitialization = new JLabel("Initialization");
		lblInitialization.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCore, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblEdge, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblAggre, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
							.addGap(33)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(spinner_2, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(67)
							.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(77)
							.addComponent(lblInitialization)))
					.addContainerGap(40, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(8)
					.addComponent(lblInitialization)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCore, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addGap(31)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEdge, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(34)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAggre, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(spinner_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addComponent(btnCreate)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}
}
