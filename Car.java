
public class Car{
   private boardTile loc;
   private int dir;
   private boolean atIntersection;
   
   private int NORTH = 0;
   private int EAST = 1;
   private int SOUTH = 2;
   private int WEST = 3;
   
   public Car(){
      trafficDriver.carList.add(this);
      loc = null;
   }
   public Car(boardTile x){
      loc = x;
      if(loc.getCol() == 0)
         dir = SOUTH;
      else if(loc.getRow() == 0)
         dir = EAST;
      else if(loc.getCol() == trafficDriver.board.length - 1)
         dir = NORTH;
      else if(loc.getRow() == trafficDriver.board.length - 1)
         dir = WEST;
      else
         System.out.println("somethins up");
      trafficDriver.carList.add(this);
      atIntersection = false;
   }
   
   public void exit() {atIntersection = false;}
      
   //moves the car forward one space, returns true if successful
   public boolean move(){
      int i = loc.getRow();
      int c = loc.getCol();
      if(loc.getRight() == this){ //car is on the right, can be going south or west
         if(dir == SOUTH){
            if (c + 1 >= trafficDriver.board.length){
               trafficDriver.board[i][c].setRight(null);
               trafficDriver.carList.remove(this);
               return true;
            }
            else if((trafficDriver.board[i][c+1].isRoad() || trafficDriver.board[i][c+1].getPass()) && trafficDriver.board[i][c+1].rightIsEmpty()){
               trafficDriver.board[i][c].setRight(null);
               trafficDriver.board[i][c+1].setRight(this);
               loc = trafficDriver.board[i][c+1];
               return true;
            }
            // else if (!atIntersection){
//                trafficDriver.board[i+1][c].enterLine(this);
//                atIntersection = true;
//                return false;
//             }
         }
         else{ //car is going west
            if (i - 1 <= trafficDriver.board.length){
               trafficDriver.board[i][c].setRight(null);
               trafficDriver.carList.remove(this);
               return true;
            }
            else if((trafficDriver.board[i-1][c].isRoad() || trafficDriver.board[i-1][c].getPass()) && trafficDriver.board[i-1][c].rightIsEmpty()){
               trafficDriver.board[i][c].setRight(null);
               trafficDriver.board[i-1][c].setRight(this);
               loc = trafficDriver.board[i-1][c];
               return true;
            }

         }
      }
      else{ //car is on the left, can be going north or east
         if(dir == NORTH){ //car is going north
            if (c - 1 <= trafficDriver.board.length){
               trafficDriver.board[i][c].setLeft(null);
               trafficDriver.carList.remove(this);
               return true;
            }
            else if((trafficDriver.board[i][c-1].isRoad() || trafficDriver.board[i][c-1].getPass()) && trafficDriver.board[i][c-1].leftIsEmpty()){
               trafficDriver.board[i][c].setLeft(null);
               trafficDriver.board[i][c-1].setLeft(this);
               loc = trafficDriver.board[i][c-1];
               return true;
            }
         }
         else{ //car is going east
         if (i + 1 >= trafficDriver.board.length){
               trafficDriver.board[i][c].setLeft(null);
               trafficDriver.carList.remove(this);
               return true;
         }
         else if((trafficDriver.board[i+1][c].isRoad() || trafficDriver.board[i+1][c].getPass()) && trafficDriver.board[i+1][c].leftIsEmpty()){
               trafficDriver.board[i][c].setLeft(null);
               trafficDriver.board[i+1][c].setLeft(this);
               loc = trafficDriver.board[i+1][c];
               return true;
            }   
         }
      }
      return false;
   }

}