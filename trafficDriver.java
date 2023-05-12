//D Oberle 4/27/22
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;

public class trafficDriver					   
{
   public static trafficGraphics screen;	
   private static int ROAD = 0;
   private static int INTERSECTION = 1;
   private static int GRASS = 2;
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
      int numWaiting;
      int numStationary;
      int turns = 0;
      int totCarsWaiting = 0;
      for(int i = 0; i < 100; i++){
         generateCar();
      }
      while(true){
         numWaiting = 0;
         screen.updateBoard(board);
         frame.repaint();
         numStationary = 0;
         for(int i = 0; i < carList.size(); i++){
            if(Math.random() < 0.25 && carList.get(i).turnRight())
               continue;
            else if(!carList.get(i).move())
               numStationary++;
         }
         for(boardTile x : intersectionList){
            x.calculate();
            numWaiting += x.hasLine();
         }
         Thread.sleep(1);
         // generateCar();
         frame.setTitle("Number of intersections with a line: " + numWaiting + ". Number of cars that are stationary: " + numStationary + ". Number of cars: " + carList.size());
         if(turns > 50){
            totCarsWaiting += numStationary;
            System.out.println("Average number of cars waiting: " + totCarsWaiting / (turns - 50));
         }
         turns++;
      }
   } 
   
   //set up traffic grid
   public static boardTile[][] initializeBoard(boardTile[][] board){
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[i].length; k++){
            board[i][k] = (i % 4 == 0 || k % 4 == 0) ? ((i % 8 == 0 && k % 8 == 0 || (k % 4 == 0 && k % 8 != 0 && i % 4 == 0 && i % 8 != 0)) ? new boardTile(INTERSECTION, i, k) : new boardTile(ROAD, i, k)) : new boardTile(GRASS, i , k);
            if(board[i][k].getType() == INTERSECTION)
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
