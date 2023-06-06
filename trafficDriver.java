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
   public static List<boardTile> intersectionList;
   public static List<Car> lastCarList;
   public static List<boardTile> lastIntersectionList;
   public static boardTile[][] lastBoard;
   private static boolean inc;
   private static boolean go;
   public static int turns;
   private static boolean needRepaint = false;
   private static Stack<String> past;
   public static boolean needCodify = true;
   
   public static void main(String[]args) throws InterruptedException
   {
      carList = new ArrayList<Car>();
      past = new Stack<String>();
      go = false;
      inc = false;
      intersectionList = new ArrayList<boardTile>();
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
      turns = 0;
      int totCarsWaiting = 0;
      for(int i = 0; i < 100; i++){
         generateCar();
      }
      while(!go){
         Thread.sleep(0);
      }
      while(true){
         if(go){   
            if(needRepaint){
               frame.repaint();
               needRepaint = false;
            } 
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
            frame.setTitle("Number of cars stationary: " + numStationary + ". Number of intersections with a line: " + numWaiting + ". Number of cars: " + carList.size() + ". Speed: " + SPEED);
            turns++;
            if(inc == true)
               go = false;
            if(needCodify)
               past.push(codify());
         }
         else{
            if(needRepaint){
               frame.setTitle("Speed:" + SPEED);
               frame.repaint();
               needRepaint = false;
            }
            Thread.sleep(0);
         }
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
      String newBoard = past.pop();
      decodify(newBoard); 
      turns--;
   }
   
   public static String codify(){
      String code = "";
      for(int i = 0; i < board.length; i++){
         for(int k = 0; k < board[0].length; k++){
            code += board[i][k].inCode();
            code += "n";
         }
      }
      return code;
   }
   public static void decodify(String x){
      board = new boardTile[SIZE/7][SIZE/7];
      System.out.println("::: " + carList.size());
      carList.clear();
      System.out.println("::: " + carList.size());
      intersectionList.clear();
      int r = 0; //keep track of row (start at -1 since it increments at start of if)
      int c = -1; //keep track of column
      boolean carW;
      boolean pass0;
      boolean pass1;
      boolean pass2;
      boolean pass3;
      int type;
      int count;
      String left;
      String right;
      for(String y : x.split("n")){
         if(y.equals("9"))
            break;
         else{
            c++;
            if(c >= board.length){
               r++;
               c=0;
            }
            if(!y.substring(0,1).equals("1")){
               left = "";
               y = y.substring(1,y.length());
            }
            else{
               y = y.substring(1, y.length());
               left = y.split("&")[1];
               y = y.substring(1,y.length()); //cut out &
               y = y.substring(y.indexOf("&") + 1, y.length()); 
            }
            if(!y.substring(0,1).equals("1")){
               right = "";
               y = y.substring(1,y.length());
            }
            else{
               y = y.substring(1, y.length());
               right = y.split("&")[1];
               y = y.substring(1,y.length());
               y = y.substring(y.indexOf("&") + 1, y.length()); 
            } 
            type = Integer.parseInt(y.substring(0,1));
            y = y.substring(1,y.length());
            if(type == 1){
               if(y.substring(0,1).equals("0"))
                  carW = false;
               else
                  carW = true;
               y = y.substring(1, y.length());
               pass0 = (y.substring(0,1).equals("0")) ? false : true;
               pass1 = (y.substring(1,2).equals("0")) ? false : true;
               pass2 = (y.substring(2,3).equals("0")) ? false : true;
               pass3 = (y.substring(3,4).equals("0")) ? false : true;  
               count = Integer.parseInt(y.substring(4,y.indexOf("*"))); 
               board[r][c] = new boardTile(left, right, r, c, type, carW, count, pass0, pass1, pass2, pass3);
               intersectionList.add(board[r][c]);      
            }
            else{
               board[r][c] = new boardTile(left, right, r, c, type);
            }
         }         
      }
      screen.updateBoard(board);
      needRepaint = true;
      System.out.println(carList.size());
      return;
   }
}
