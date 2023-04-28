import java.util.*;
public class MyQueue<anyType>{
   private LinkedList list;
   public MyQueue(){
      list = new LinkedList();
   }
   public boolean isEmpty(){
      return list.size() == 0;
   }
   public void add(anyType x){
      list.add(x);
   }
   public anyType remove(){
      if(this.isEmpty())
         return null;
      return (anyType) list.remove(0);
   }
   public anyType peek(){
      if(this.isEmpty())
         return null;
      return (anyType) list.get(0);
   } 
   public int size(){
      return list.size();
   }
   public String toString(){
      return list.toString();
   }
}