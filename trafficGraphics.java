//D Oberle 3/27/22
import java.util.HashSet;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;                          //there is a swing timer and an awt timer, so we have to be specific and not use .*
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
   
public class trafficGraphics extends JPanel
{
   private static final int SIZE = 700;            //size of square screen to be drawn
   private static boardTile[][] board;
   private static final int textSize = 25;         //font size
   private static final int DELAY = 1;	            //#miliseconds delay between each time the screen refreshes for the timer
   private static Timer t;							      //used to control what happens each frame (game code)
   private static HashSet<Integer> pressedKeys;    //collection of which keys are pressed down on the keyboard
   private static int playerX, playerY;            //position of a dot that can be moved around
    
   public trafficGraphics(boardTile[][] board)
   {
      this.board = board;
   }

   //displays board
   public void showGraphics(Graphics g)	
   {
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[i].length; k++){
            switch(board[i][k].getType()){
               case 0: //road
                  g.setColor(Color.gray);
                  g.fillRect(i * 7, k * 7, 7, 7);
                  g.setColor(Color.yellow);
                  if(i % 4 == 0)
                     g.drawLine(i * 7 + 3, k * 7, i * 7 + 3, k * 7 + 7);
                  else
                     g.drawLine(i * 7, k * 7 + 3, i * 7 + 7, k * 7 + 3);
                  break;
               case 1: //intersection
                  g.setColor(Color.black);
                  g.fillRect(i * 7, k * 7, 7, 7);
                  if(board[i][k].getPass(0)) //north
                     g.setColor(Color.green);
                  else
                     g.setColor(Color.red);
                  g.fillOval(i * 7 + 4, k * 7, 2, 2); //north 
                  if(board[i][k].getPass(1)) //east
                     g.setColor(Color.green);
                  else
                     g.setColor(Color.red);
                  g.fillOval(i * 7 + 4, k * 7 + 3, 2, 2); //east
                  if(board[i][k].getPass(2)) //south
                     g.setColor(Color.green);
                  else
                     g.setColor(Color.red);
                  g.fillOval(i * 7, k * 7 + 4, 2, 2); //south
                  if(board[i][k].getPass(3)) //west
                     g.setColor(Color.green);
                  else
                     g.setColor(Color.red);
                  g.fillOval(i * 7, k * 7, 2, 2); //west
                  break;
               case 2: //grass
                  g.setColor(Color.green);
                  g.fillRect(i * 7, k * 7, 7, 7);
                  break;
            }
          if(!board[i][k].isEmpty()){
            g.setColor(Color.black);
            if(!board[i][k].rightIsEmpty()){
               if(i % 4 == 0)
                  g.fillOval(i * 7, k * 7, 3, 3);
               else
                  g.fillOval(i * 7, k * 7, 3, 3);
            } 
            if(!board[i][k].leftIsEmpty()){
               if(i % 4 == 0)
                  g.fillOval(i * 7 + 3, k * 7, 3, 3);
               else
                  g.fillOval(i * 7, k * 7 + 3, 3, 3);
            }
          }
         }
      }
   }
   
   public void updateBoard(boardTile[][] board){ this.board = board; } //updates the board to the new state
   
   //pre:   kc is a valid keyCode: https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
	//post:  return a string that describes the key with the keyCode kc
   public static String intToKey(int kc)
   {
      if(kc == KeyEvent.VK_SPACE)
         return "SPACE";
      if(kc == KeyEvent.VK_SHIFT)
         return "SHIFT";
      if(kc == KeyEvent.VK_ENTER)
         return "ENTER";
      if(kc == KeyEvent.VK_CONTROL)
         return "CTRL";
      if(kc == KeyEvent.VK_ALT)
         return "ALT";
      if(kc == KeyEvent.VK_UP)
         return "UP";
      if(kc == KeyEvent.VK_DOWN)
         return "DOWN";
      if(kc == KeyEvent.VK_LEFT)
         return "LEFT";
      if(kc == KeyEvent.VK_RIGHT)
         return "RIGHT";
      return ""+(char)(kc);
   }

   //inherited by JPanel - override to display the graphics we want to show
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      showGraphics(g);					      
   }
   
   //moves the orange dot depending on which arrow keys are being pressed
   // public void moveDot()
//    {
//       int speed = 2;                   //don't move off the screen, son!
//       if(pressedKeys.contains(KeyEvent.VK_UP) && playerY >= speed)   
//          playerY-= speed;
//       else if(pressedKeys.contains(KeyEvent.VK_DOWN) && playerY <= SIZE-(speed + textSize))
//          playerY += speed;     
//       if(pressedKeys.contains(KeyEvent.VK_LEFT) && playerX >= speed)
//          playerX-= speed;
//       else if(pressedKeys.contains(KeyEvent.VK_RIGHT) && playerX <= SIZE-(speed + textSize))
//          playerX += speed;  
//    }
//    
  //  private class Listener implements ActionListener
//    {
//       public void actionPerformed(ActionEvent e)	   //this is called for each timer iteration
//       {
//          //have your game code method calls here - the ones that should be updated every frame
//          moveDot();
//          repaint();                                   //repaint() automatically calls paintComponent(g) which calls showGraphics(g)
//       }
//    }

   //methods we need to define from implementing the KeyListener interface
  //  public void keyTyped(KeyEvent e)                   //don't need this one
//    {}
//       
//    public void keyPressed(KeyEvent e)
//    {
//       if(e.getKeyCode()==KeyEvent.VK_ESCAPE)	         //End the program	
//          System.exit(1);
//       pressedKeys.add(e.getKeyCode());                //add key to list of keys pressed
//    }
//    
//    public void keyReleased(KeyEvent e)
//    {
//       pressedKeys.remove((Integer)(e.getKeyCode()));  //remove key from list of keys pressed
//   }
}
