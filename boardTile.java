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
         stopSign = new MyQueue<Car>();
      }
   }
   
   //get methods
   public int getType() {return type;}
   public boolean getPass(int a) {try {return canPass[a]; } catch(NullPointerException e) { return false;}}
   public Car getLeft() {return leftOccupant;}
   public Car getRight() {return rightOccupant;}
   public int getRow() {return row;}
   public int getCol() {return col;}
   public int hasLine(){
      if(hasCarWaiting)
         return 1;
      return 0;
   }
   
   //information methods
   public boolean isEmpty() {return leftOccupant == null && rightOccupant == null;} //returns if the space is empty
   public boolean rightIsEmpty() {return rightOccupant == null;}
   public boolean leftIsEmpty() {return leftOccupant == null;}
   public boolean isRoad() {return type == ROAD || type == STOP;} //checks if tile is a road
   public boolean isStop() {return type == STOP;}
   public boolean canGo() {return stopSign.size() == 0;}

   //maintenance and set methods
   public void clear(){  //clears the tile
      this.leftOccupant = null;
      this.rightOccupant = null;
   }
   public void setRight(Car c){
      this.rightOccupant = c;
   }
   public void setLeft(Car c){
      this.leftOccupant = c;
   }
   public void wait(Car c){
      stopSign.add(c);
   }
   public Car allow(){
      return stopSign.remove();
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

}