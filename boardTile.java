import java.util.*;

public class boardTile{
   private Car leftOccupant;
   private Car rightOccupant;
   private int type;
   private int ROAD = 0;
   private int INTERSECTION = 1;
   private int GRASS = 2;
   private int STOP = 3;
   private int row, col;
   //INTERSECTION ONLY
   private boolean hasCarWaiting;
   private boolean[] canPass; //0 = NORTH, 1 = EAST, 2 = SOUTH, 3 = WEST
   private int counter;
   //STOP SIGN ONLY
   private MyQueue<Car> stopSign;
   
   
   //constructor with random type
   public boardTile(){
      leftOccupant = null;
      rightOccupant = null;
      if(Math.random() < 0.5)
         type = ROAD;
      else
         type = INTERSECTION;
   }
   //constructor with specified type
   public boardTile(int s, int r, int c){
      row = r;
      col = c;
      leftOccupant = null;
      rightOccupant = null;
      type = s;
      if(type == INTERSECTION){
         hasCarWaiting = false;
         counter = 0;
         canPass = new boolean[4];
         if(Math.random() < 0.5){
            canPass[0] = true;
            canPass[1] = false;
            canPass[2] = true;
            canPass[3] = false;
         }
         else{
            canPass[0] = false;
            canPass[1] = true;
            canPass[2] = false;
            canPass[3] = true;
         }
      }
      else if (type == STOP){
         //stopSign = new MyQueue<Car>();
      }
   }
   public boardTile(boardTile c){ //copy constructor
      row = c.getRow();
      col = c.getCol();
      type = c.getType();
      if(!c.leftIsEmpty())
         leftOccupant = new Car(c.getLeft(), this);
      if(!c.rightIsEmpty())
         rightOccupant = new Car(c.getRight(), this);
      if(type == INTERSECTION){
         canPass = new boolean[4];
         counter = c.getCounter();
         for(int i = 0; i < 4; i++)
            canPass[i] = c.getPass(i);
         hasCarWaiting = (c.hasLine() == 1) ? true : false;
         trafficDriver.intersectionList.add(this);
      }
   }
   
   public boardTile(String left, String right, int r, int c, int t, boolean carW, int count, boolean pass0, boolean pass1, boolean pass2, boolean pass3){ //for reconstructing an intersection from a code
      type = t;
      row = r;
      col = c;
      boolean st;
      if(left.length() > 0){
//          if(left.substring(0,1).equals(0)){
//             st = false;
//          }
//          else{
//             st = true;
//          }
         leftOccupant = new Car(this, Integer.parseInt(left.substring(0,1)));
      }
      if(right.length() > 0){
//          if(right.substring(0,1).equals(0)) {st = false;}
//          else {st = true;}
         rightOccupant = new Car(this, Integer.parseInt(right.substring(0,1)));
      }
      hasCarWaiting = carW;
      counter = count;
      canPass = new boolean[4];
      canPass[0] = pass0;
      canPass[1] = pass1;
      canPass[2] = pass2;
      canPass[3] = pass3;
   }
   public boardTile(String left, String right, int r, int c, int t){ //for reconstructing a non-intersection from code
      row = r;
      col = c;
      boolean st;
      if(left.length() > 0){
//          if(left.substring(0,1).equals(0)){
//             st = false;
//          }
//          else{
//             st = true;
//          }
         leftOccupant = new Car(this, Integer.parseInt(left.substring(0,1)));
      }
      if(right.length() > 0){
//          if(right.substring(0,1).equals(0)){
//             st = false;
//          }
//          else{
//             st = true;
//          }
         rightOccupant = new Car(this, Integer.parseInt(right.substring(0,1)));
      }
      type = t;   
   }
   //get methods
   public int getCounter() {
      return counter;}
   public int getType() {
      return type;}
   public boolean getPass(int a) {
      try {
         return canPass[a]; } 
      catch(NullPointerException e) { 
         return false;}}
   public Car getLeft() {
      return leftOccupant;}
   public Car getRight() {
      return rightOccupant;}
   public int getRow() {
      return row;}
   public int getCol() {
      return col;}
   public int hasLine(){
      if(hasCarWaiting)
         return 1;
      return 0;
   }
   public boolean contains(Car c){
      return leftOccupant == c || rightOccupant == c;
   }
   
   //information methods
   public boolean isEmpty() {
      return leftOccupant == null && rightOccupant == null;} //returns if the space is empty
   public boolean rightIsEmpty() {
      return rightOccupant == null;}
   public boolean leftIsEmpty() {
      return leftOccupant == null;}
   public boolean isRoad() {
      return type == ROAD || type == STOP;} //checks if tile is a road
   public boolean isStop() {
      return type == STOP;}
   public boolean canGo() {
      return stopSign == null || stopSign.size() == 0;}

   //maintenance and set methods
   public void clear(){  //clears the tile
      this.leftOccupant = null;
      this.rightOccupant = null;
   }
   public void setRight(Car c){
      if(type == GRASS)
         return;
      this.rightOccupant = c;
   }
   public void setLeft(Car c){
      if(type == GRASS)
         return;
      this.leftOccupant = c;
   }
   public void wait(Car c){
      if(stopSign == null)
         stopSign = new MyQueue<Car>();
      stopSign.add(c);
   }
   public Car allow(){
      Car temp = stopSign.remove();
      if(stopSign.size() == 0)
         stopSign = null;
      return temp;
   }
   public void flip() { //flips intersection
      for(int i = 0; i < 4; i++) {
         canPass[i] = !canPass[i];
      }
      hasCarWaiting = false;
   }
   public void enterLine(){
      hasCarWaiting = true;
   }
   public void calculate(){
      if(type == STOP && stopSign.size() > 1){
         allow().move();
      }
      if(!isEmpty()){
         counter++;
         return;
      }
      if(counter > 5 && hasCarWaiting){
         if(Math.random() > 0.5){
            this.flip();
            counter = 0;
            return;
         }
      }
      else if(counter > 10){
         if(Math.random() > 0.5){
            this.flip();
            counter = 0;
            return;
         }
      }
      counter++;
   }
   
   public String inCode(){
      String code = "";
      if(leftOccupant == null)
         code += "0";
      else
         code = code + "1&" + leftOccupant.carInfo() + "&";
      if(rightOccupant == null)
         code += "0";
      else
         code = code + "1&" + rightOccupant.carInfo() + "&";
      code += Integer.toString(type);
      if(type == INTERSECTION){
         if(hasCarWaiting)
            code += "1";
         else
            code += "0";
         for(int i = 0; i < 4; i++){
            if(canPass[i])
               code += "1";
            else
               code += "0";
         }
         code = code + Integer.toString(counter) + "*"; 
      }
      return code;
   }

}