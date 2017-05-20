package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author EulerLoop (Ankit Patel)
 */

public class Minesweeper implements ActionListener{
	
	public JFrame jframe;
	public JOptionPane error;
	public JButton[][] buttons;
	public int colmines, rowmines, mines;
	public int[][] counts;
	public static Minesweeper minesweeper;
	final int mine = 10;
	public boolean won;
	
	JButton reset = new JButton("Reset");
	Container grid = new Container();
	
	
	public Minesweeper() {
		jframe = new JFrame("Minesweeper");
		
		// Using User Input to collect designated size of the game
		Object[] possibilities = {"8x8", "16x16", "32x32", "64x64"};
		String s = (String)JOptionPane.showInputDialog(jframe, possibilities);
		
		// If the user selects 8x8
		if (s != null && s.length() > 0 && s.equals("8x8")) {
			jframe.setSize(8*60, 8*40);
			colmines = 8;
			rowmines= 8;
		}
		
		// If the user selects 16x16
		else if (s != null && s.length() > 0 && s.equals("16x16")) {
			jframe.setSize(16*60, 16*40);
			colmines = 16;
			rowmines = 16;
		}
		
		// If the user selects 32x32
		else if (s != null && s.length() > 0 && s.equals("32x32")) {
			jframe.setSize(32*60, 32*40);
			colmines = 32;
			rowmines = 32;
		}
		
		// If the user selects 64x64
		else if (s != null && s.length() > 0 && s.equals("64x64")) {
			jframe.setSize(64*60, 64*40);
			colmines = 64;
			rowmines = 64;
		}
		
		// If the user inputs an incorrect message
		else {
			JOptionPane.showMessageDialog(error, "The size you have requested is not allowed! Please restart the application");
			System.exit(0);
		}
		
		// # Of Mines
		mines = colmines*rowmines / 8;
		
		
		// Initializing location and properties of layout
		jframe.setLocationRelativeTo(null);
		jframe.setLayout(new BorderLayout());
		jframe.add(reset, BorderLayout.NORTH);
		reset.addActionListener(this);
		
		// Using a 2D array of buttons for selected areas
		buttons = new JButton[colmines][rowmines];
		
		// Using a 2D array of integers for internal controls for selected spots
		counts = new int[colmines][rowmines];
		
		// Creating the Button Grid
		grid.setLayout(new GridLayout(colmines, rowmines));
		for (int a = 0; a < buttons.length; a++) {
			for (int b = 0; b < buttons[0].length; b++) {
				buttons[a][b] = new JButton();
				buttons[a][b].addActionListener(this);
				grid.add(buttons[a][b]);
			}
		}
		jframe.add(grid, BorderLayout.CENTER);
		createRandomMines();
		
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
	}
	
	public static void main(String[] args) {
		minesweeper = new Minesweeper();

	}

	// Method for creating mines in random locations
	public void createRandomMines() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y < counts[0].length; y++) {
				list.add(x*100+y);
			}
		}
		// Reinitializing counts to be safe, picks out the # of mines
		counts = new int[colmines][rowmines];
		for (int a = 0; (a < mines); a++){
			int choice = (int)(Math.random() * list.size());
			counts[list.get(choice) / 100][list.get(choice) % 100] = mine;
			list.remove(choice);
		}
		
		// Determining neighbor mines
		
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y <counts[0].length; y++) {
				if (counts[x][y] != mine) {
				
					int neighbormines = 0;
					if (x > 0 && y > 0 && counts[x-1][y-1] == mine) {//up left X
						neighbormines++;
					}
					
					if (y > 0 && counts [x][y-1] == mine) {// up
						neighbormines++;
					}
					
					if (x < counts.length - 1 && y > 0 && counts[x+1][y-1] == mine) {// upright 
						neighbormines++;
					}
					
					if (x > 0 && counts[x-1][y] == mine) { // left
						neighbormines++;
					}
					
					if (x < counts.length - 1 && counts [x+1][y] == mine) { // right
						neighbormines++;
					}
					
					if (x > 0 && y < counts[0].length - 1 && counts[x-1][y+1] == mine) { // downleft
						neighbormines++;
					}
					
					if (y < counts[0].length - 1 && counts[x][y+1] == mine) { //down
						neighbormines++;
					}
					
					if (x < counts.length - 1 && y < counts[0].length - 1 && counts[x+1][y+1] == mine) { // downright
						neighbormines++;
					}
				
				counts[x][y] = neighbormines;
			}
		
		}
	}
	
}
	// Method for procedure if a game has been lost
	public void lostGame() {
		for(int x = 0; x < buttons.length; x++) {
			for (int y = 0; y < buttons[0].length; y++) {
				if (buttons[x][y].isEnabled()) {
					if(counts[x][y] != mine) {
						buttons[x][y].setText(counts[x][y] + "");
						buttons[x][y].setEnabled(false);
					}
					
					else {
						buttons[x][y].setForeground(Color.red);
						buttons[x][y].setText("X");
						buttons[x][y].setEnabled(false);
					}
				}
			}
		}
	}
	// Recursion used to clear the zeroes of repeated non-mines
	public void clearZeros(ArrayList<Integer> toClear) {
		if (toClear.size() == 0) {
			return;
		}
		else {
			int x = toClear.get(0) / 100;
			int y= toClear.get(0) % 100;
			toClear.remove(0);
			
				
				if (x > 0 && y > 0 && buttons[x-1][y-1].isEnabled()) { // upleft
					buttons[x-1][y-1].setText(counts[x-1][y-1] + "");
					buttons[x-1][y-1].setEnabled(false);
					if (counts[x-1][y-1] == 0) {
						toClear.add((x-1) * 100 + (y-1));
					}
				}
				
				if (y > 0 && buttons[x][y-1].isEnabled()) { // up
					buttons[x][y-1].setText(counts[x][y-1] + "");
					buttons[x][y-1].setEnabled(false);
					if (counts[x][y-1] == 0) {
						toClear.add(x * 100 + (y-1));
					}
				}
				
				if (x < counts.length - 1 && y > 0 && buttons[x+1][y-1].isEnabled()) { // upright
					buttons[x+1][y-1].setText(counts[x+1][y-1] + "");
					buttons[x+1][y-1].setEnabled(false);
					if (counts[x+1][y-1] == 0) {
						toClear.add((x+1) * 100 + (y-1));
					}
				}
				
				if (x > 0 && buttons[x-1][y].isEnabled()) { // left
					buttons[x-1][y].setText(counts[x-1][y] + "");
					buttons[x-1][y].setEnabled(false);
					if (counts[x-1][y] == 0) {
						toClear.add((x-1) * 100 + y);
					}
				}
				
				if (x < counts.length - 1 && buttons[x+1][y].isEnabled()) { // right
					buttons[x+1][y].setText(counts[x+1][y] + "");
					buttons[x+1][y].setEnabled(false);
					if (counts[x+1][y] == 0) {
						toClear.add((x+1) * 100 + y);
					}
				}
				
				if (x > 0 && y < counts[0].length - 1 && buttons[x-1][y+1].isEnabled()) { // downleft
					buttons[x-1][y+1].setText(counts[x-1][y+1] + "");
					buttons[x-1][y+1].setEnabled(false);
					if (counts[x-1][y+1] == 0) {
						toClear.add((x-1) * 100 + (y+1));
					}
				}
				
				if ( y < counts[0].length - 1 && buttons[x][y+1].isEnabled()) { // down
					buttons[x][y+1].setText(counts[x][y+1] + "");
					buttons[x][y+1].setEnabled(false);
					if (counts[x][y+1] == 0) {
						toClear.add(x * 100 + (y+1));
					}
				}
				
				if (x < counts.length - 1 &&  y < counts[0].length - 1 && buttons[x+1][y+1].isEnabled()) { // downright
					buttons[x+1][y+1].setText(counts[x+1][y+1] + "");
					buttons[x+1][y+1].setEnabled(false);
					if (counts[x+1][y+1] == 0) {
						toClear.add((x+1) * 100 + (y+1));
					}
				}
				
			}
			clearZeros(toClear);
		}
	
	// Method used to check whether the user has won the game
	public void checkWin() {
		won = true;
		for (int x = 0; x < counts.length; x++) { 
			for (int y = 0; y < counts[0].length; y++) {
				if (counts[x][y] != mine && buttons[x][y].isEnabled() == true) {
					won = false;
				}
			
			}
		}
		if(won == true) {
			JOptionPane.showMessageDialog(jframe, "You have won! Congratulations!");
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		//reset the board
		if (event.getSource().equals(reset)) {
			for (int x = 0; x < buttons.length; x++) {
				for (int y = 0; y < buttons[0].length; y++) {
					buttons[x][y].setEnabled(true);
					buttons[x][y].setText("");
				}
			}
			createRandomMines();
		}
		else {
			for (int x = 0; x < buttons.length; x++) {
				for (int y = 0; y < buttons[0].length; y++) {
					if (event.getSource().equals(buttons[x][y])) {
						if(counts[x][y] == mine) {
							buttons[x][y].setForeground(Color.red);
							buttons[x][y].setText("X");
							lostGame();
						}
						else if (counts[x][y] == 0) {
							buttons[x][y].setText(counts[x][y] + "");
							buttons[x][y].setEnabled(false);
							ArrayList<Integer> toClear = new ArrayList<Integer>();
							toClear.add(x*100 + y);
							clearZeros(toClear);
							checkWin();
						}
						else {
							buttons[x][y].setText(counts[x][y] + "");
							buttons[x][y].setEnabled(false);
							checkWin();
						}
				}
			
		}
		
	}
}
}
}
