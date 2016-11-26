package pong;
import MGui.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.*;
/**
 *
 * @author daniele&emil
 */
public final class MainMenu extends MFrame implements ActionListener{
    private final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = SCREEN_WIDTH/16*10;
    private final int BTN_WIDTH = 100, BTN_HEIGHT = 40;
    private int width, height,x,y;
    private String title;
    
    public MainMenu(int WIDTH, int HEIGHT, String TITLE){      
        super.setCanvasBackground(Color.BLACK);
        super.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        super.setVisible(true);
        super.setTitle(TITLE);
        super.setResizable(false); 
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(MFrame.EXIT_ON_CLOSE);
        this.width = WIDTH;
        this.height = HEIGHT;
        this.title = TITLE;
        Timer timer=new Timer(250,this); 
        timer.start();
    }
    
    @Override
    public void mpaint ( Graphics2D g2 ){
        GraphSet.setColor(g2, 255, 255, 255);
        Rectangle2D.Double r1=new Rectangle2D.Double(370,height/2,150,30),r2=new Rectangle2D.Double(370,r1.getY()+50,150,30),
                           r3=new Rectangle2D.Double(370,r2.getY()+50,150,30),r4=new Rectangle2D.Double(370,r3.getY()+50,70,30);
        g2.draw(r1);
        g2.draw(r2);
        g2.draw(r3);
        g2.draw(r4);
        g2.setFont(new Font("8BIT WONDER", Font.BOLD, 10));
        g2.drawString("Play PvsAI",380,height/2+20);
        g2.drawString("Play AivsAI", 380,(int)r2.getY()+20);
        g2.drawString("Play PvP",380,(int)r3.getY()+20);
        g2.drawString("Exit",380,(int)r4.getY()+20);
        g2.setFont(new Font("8BIT WONDER", Font.BOLD, 40));
        g2.drawString("Pong",320,40);
        Rectangle2D.Double[] r=new Rectangle2D.Double[100];
        for(int i=0;i<r.length;i++)
        {
            r[i]=new Rectangle2D.Double(new Random().nextInt(width)+1,new Random().nextInt(height)+1,5,5);
            g2.draw(r[i]);
            g2.fill(r[i]);
        }
        if(r1.contains(new Point2D.Double(x,y)))
            start(0);
        else if(r2.contains(new Point2D.Double(x,y)))
            start(1);
        else if(r3.contains(new Point2D.Double(x,y)))
            start(2);
        else if(r4.contains(new Point2D.Double(x,y)))
            exit();
    }
    
    @Override
    public void mouseClicked (MouseEvent e) {
        x = e.getX();
        y = e.getY();
        mrepaint();
    }
    
    void start(int choice)
    {
        dispose();
        if(choice==0)
            new Game(width,height,title);
        else if(choice==1)
            new AIGame(width,height,title);
        else if(choice==2)
            new PvPGame(width,height,title);
    }
    
    void exit()
    {
        dispose();
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mrepaint();
    }
}
