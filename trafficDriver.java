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
   private static int STOP = 3;
   private static int SPEED = 100;
   public static boardTile[][] board;
   static int SIZE = 700;
   public static List<Car> carList;
   public static List<Car> lastCarList;
   public static List<boardTile> lastIntersectionList;
   public static List<boardTile> intersectionList;
   private static boolean inc;
   private static boolean go;
   private static boardTile[][] lastBoard;
   
   public static void main(String[]args) throws InterruptedException
   {
      lastBoard = new boardTile[SIZE/7][SIZE/7];
      carList = new ArrayList<Car>();
      go = false;
      inc = false;
      intersectionList = new ArrayList<boardTile>();
      lastIntersectionList = new ArrayList<boardTile>();
      lastCarList = new ArrayList<Car>();
      board = new boardTile[SIZE/7][SIZE/7];
      initializeBoard(board);
      screen = new trafficGraphics(board);
      JFrame frame = new JFrame("Panel with Keyboard Input");	//window title
      frame.setSize(SIZE + 200, SIZE + 100);			                        //Size of game window
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
      while(!go){
         Thread.sleep(0);
      }
      while(true){
         if(go){
            copyBoard();
            if(carList.size() < 75){
               int toMake = 25;
               for(int i = 0; i < carList.size(); i++)
                  toMake += checkExistence(carList.get(i));
               for(int i = 0; i < toMake; i++)
                  generateCar();
            }
            numWaiting = 0;
            screen.updateBoard(board);
            frame.repaint();
            numStationary = 0;
            for(int i = 0; i < carList.size(); i++){
               if(Math.random() < 0.25 && carList.get(i).turnLeft())
                  continue;
               else if(Math.random() < 0.25 && carList.get(i).turnRight())
                  continue;
               else if(!carList.get(i).move())
                  numStationary++;
            }
            for(boardTile x : intersectionList){
               x.calculate();
               numWaiting += x.hasLine();
            }
            Thread.sleep(SPEED); //number of milliseconds between turns, 1 for essentially zero delay and 1000 for 1 second delay
            frame.setTitle("Number of intersections with a line: " + numWaiting + ". Number of cars that are stationary: " + numStationary + ". Number of cars: " + carList.size() + ". Speed: " + SPEED);
            if(turns > 50){
               totCarsWaiting += numStationary;
            }
            turns++;
            if(inc == true)
               go = false;
         }
         else
            Thread.sleep(0);
      }
   } 
   
   //set up traffic grid
   public static boardTile[][] initializeBoard(boardTile[][] board){
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[i].length; k++){
            board[i][k] = (i % 4 == 0 || k % 4 == 0) ? (i % 8 == 0 && k % 8 == 0 || (k % 4 == 0 && k % 8 != 0 && i % 4 == 0 && i % 8 != 0) ? new boardTile(INTERSECTION, i, k) : (i % 4 == 0 && k % 4 == 0 ? new boardTile(STOP, i, k) : new boardTile(ROAD, i, k))) : new boardTile(GRASS, i , k);
            if(board[i][k].getType() == INTERSECTION)
               intersectionList.add(board[i][k]);
         }
      }
      return board;
   }
   public static void speedUp(){
      SPEED -= 50;
      if(SPEED < 0)
         SPEED = 0;
   }
   public static void slowDown(){
      SPEED += 50;
   }
   public static void increment(){
      go = true;
      inc = true;
   }
   public static void resume(){
      go = true;
      inc = false;
   }
   public static void pause(){
      go = false;
      inc = false;
   }
   public static void generateCar(){
      int i, k;
      int iterations = 0;
      double ran = Math.random();
      if(ran < 0.25){ //spawn on top edge
         k = 0;
         do{
            i = (int) (Math.random() * board.length);
            iterations++;
            if(iterations > 25)
               return;
         }
         while(i % 4 != 0 || !board[i][k].rightIsEmpty());
         board[i][k].setRight(new Car(board[i][k]));
      }
      else if(ran < 0.5){ //spawn on right edge
         i = board.length -1;
         do{
            k = (int) (Math.random() * board.length);
            iterations++;
            if(iterations > 25)
               return;
         }
         while(k % 4 != 0 || !board[i][k].rightIsEmpty());
         board[i][k].setRight(new Car(board[i][k]));
      }
      else if(ran < 0.75){ //spawn on bottom edge
         k = board.length -1;
         do{
            i = (int) (Math.random() * board.length);
            iterations++;
            if(iterations > 25)
               return;
         }
         while(i % 4 != 0 || !board[i][k].leftIsEmpty());
         board[i][k].setLeft(new Car(board[i][k]));
      }
      else{ //spawn on left edge
         i = 0;
         do{
            k = (int) (Math.random() * board.length);
            iterations++;
            if(iterations > 25)
               return;
         }
         while(k % 4 != 0 || !board[i][k].leftIsEmpty());
         board[i][k].setLeft(new Car(board[i][k]));
      }
   }
   
   public static int checkExistence(Car c){
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[0].length; k++){
            if(board[i][k].contains(c))
               return 0;
         }
      }
      carList.remove(c);
      return 1;
   }
   public static void reverse(){
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[0].length; k++){
            board[i][k] = new boardTile(lastBoard[i][k]);
         }
      }
      carList = lastCarList;
      intersectionList = lastIntersectionList;
      go = false;
      inc = false;
   }
   public static void copyBoard(){
      lastIntersectionList.clear();
      lastCarList.clear();
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[0].length; k++){
            lastBoard[i][k] = new boardTile(board[i][k]);
         }
      }
   }
   
   public static String codify(){
      String code = "";
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[0].length; k++){
            code += board[i][k].inCode();
            code += "8";
          }
      }
      return code;
   }
}
