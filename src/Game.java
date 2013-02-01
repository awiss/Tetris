import java.awt.*;
import java.awt.event.*;


import javax.swing.*;

public class Game implements Runnable {
   public void run() {
      // Top-level frame
      final JFrame frame = new JFrame("Tetris");
      frame.setLocation(500, 600);
      final TetrisField court = new TetrisField();
      final JPanel panel = new JPanel();
      frame.add(panel, BorderLayout.NORTH);
      final JButton reset = new JButton("Play Again");
      
      reset.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
        	 court.reset();
         }
      });
      panel.add(reset);
      // Main playing area
      frame.add(court, BorderLayout.CENTER);
      
      

     
      // Put the frame on the screen
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);

      // Start the game running
      court.reset();
      }

   /*
    * Get the game started!
    */
   public static void main(String[] args) {
       SwingUtilities.invokeLater(new Game());
   }

}