//D Oberle 4/27/22
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class PanelExampleDriver					   
{
   public static PanelExample screen;					            //Our Custom JPanel

   public static void main(String[]args)
   {
      screen = new PanelExample();
      JFrame frame = new JFrame("Panel with Keyboard Input");	//window title
      frame.setSize(700, 700);			                        //Size of game window
      frame.setLocation(1, 1);				                     //location of game window on the screen
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
   } 
}
