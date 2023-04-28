//D Oberle 4/27/22
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;

public class trafficDriver					   
{
   public static trafficGraphics screen;					            //Our Custom JPanel
   public static boardTile[][] board;
   static int SIZE = 700;
   public static List<Car> carList;
   public static List<boardTile> intersectionList;
   
   public static void main(String[]args) throws InterruptedException
   {
      carList = new ArrayList<Car>();
      intersectionList = new ArrayList<boardTile>();
      board = new boardTile[SIZE/7][SIZE/7];
      initializeBoard(board);
      screen = new trafficGraphics(board);
      JFrame frame = new JFrame("Panel with Keyboard Input");	//window title
      frame.setSize(SIZE + 100, SIZE + 100);			                        //Size of game window
      frame.setLocation(1, 1);				                     //location of game window on the screen
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      while(true){
         screen.updateBoard(board);
         frame.repaint();
         // for(Car x : carList){
//             x.move();
//          }
         for(int i = 0; i < carList.size(); i++){
            carList.get(i).move();
         }
         for(boardTile x : intersectionList){
            if(Math.random() < 0.2)
               x.flip();
         }
         Thread.sleep(10);
         generateCar();
      }
   } 
   
   //set up traffic grid
   public static boardTile[][] initializeBoard(boardTile[][] board){
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[i].length; k++){
            board[i][k] = (i % 4 == 0 || k % 4 == 0) ? ((i % 4 == 0 && k % 4 == 0) ? new boardTile("intersection", i, k) : new boardTile("road", i, k)) : new boardTile("grass", i , k);
            if(board[i][k].getType().equals("intersection"))
               intersectionList.add(board[i][k]);
         }
      }
      return board;
   }
   
   public static void generateCar(){
      int i, k;
      double ran = Math.random();
      if(ran < 0.25){ //spawn on top edge
         k = 0;
         do{
            i = (int) (Math.random() * board.length);
         }
         while(i % 4 != 0);
         board[i][k].setRight(new Car(board[i][k]));
      }
      else if(ran < 0.5){ //spawn on right edge
         i = board.length -1;
         do{
            k = (int) (Math.random() * board.length);
         }
         while(k % 4 != 0);
         board[i][k].setRight(new Car(board[i][k]));
      }
      else if(ran < 0.75){ //spawn on bottom edge
         k = board.length -1;
         do{
            i = (int) (Math.random() * board.length);
         }
         while(i % 4 != 0);
         board[i][k].setLeft(new Car(board[i][k]));
      }
      else{ //spawn on left edge
         i = 0;
         do{
            k = (int) (Math.random() * board.length);
         }
         while(k % 4 != 0);
         board[i][k].setLeft(new Car(board[i][k]));
      }
   }
}
