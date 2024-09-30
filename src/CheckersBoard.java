

import javax.swing.JFrame;

public class CheckersBoard {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Checkers Game");
        frame.setSize(615, 635);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board gameBoard = new Board();  
        frame.add(gameBoard);

        frame.setVisible(true);
    }
}
