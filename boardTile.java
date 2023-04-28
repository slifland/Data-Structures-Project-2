import java.util.*;

public class boardTile{
   private Car leftOccupant;
   private Car rightOccupant;
   private String type;
   private int row, col;
   //INTERSECTION ONLY
   private boolean canPass;
   private MyQueue<Car> line;
   
   //constructor with random type
   public boardTile(){
      leftOccupant = null;
      rightOccupant = null;
      if(Math.random() < 0.5)
         type = "road";
      else
         type = "grass";
   }
   //constructor with specified type
   public boardTile(String s, int r, int c){
      row = r;
      col = c;
      leftOccupant = null;
      rightOccupant = null;
      type = s;
      if(type.equals("intersection")){
         line = new MyQueue<Car>();
         canPass = false;
      }
   }
   
   //get methods
   public String getType() {return type;}
   public boolean getPass() {return canPass;}
   public Car getLeft() {return leftOccupant;}
   public Car getRight() {return rightOccupant;}
   public int getRow() {return row;}
   public int getCol() {return col;}
   
   //information methods
   public boolean isEmpty() {return leftOccupant == null && rightOccupant == null;} //returns if the space is empty
   public boolean rightIsEmpty() {return rightOccupant == null;}
   public boolean leftIsEmpty() {return leftOccupant == null;}
   public boolean isRoad() {return type.equals("road");} //checks if tile is a road

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
   public void flip() {canPass = !canPass;} //flips intersection
   public void enterLine (Car c){ //car joins line for intersection
      line.add(c);
   }
   public void exitLine(){
      Car temp = line.remove();
      temp.move();
      temp.exit();
   }

}