package pong;

import MGui.GraphSet;
import MGui.MFrame;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author daniele&emil
 */
public class AIGame extends MFrame implements ActionListener{
    
    
    private Timer timer,_timer; 
    private File sourceimage;
    private Image immagineIc;
    private int movement,points,_points,score,scorePX,scoreCX,scoreY;
    private final int width,height;
    private Ball ball;
    private final AIPaddle ai,_ai;
    private boolean right,down,up,ballMovement;
    private final URL url,_url; 
    private final AudioClip clipContact,clipScore;
    private final double SPEED_AI= 7;
    
    public AIGame(int width, int height, String title) {        
        super(width, height);
        super.setTitle(title);
        super.setResizable(false); 
        super.setLocationRelativeTo(null);
        super.setCanvasBackground(Color.BLACK);
        try {
            sourceimage=new java.io.File("src\\icon\\pong.gif");
            Image image = ImageIO.read(sourceimage);
            immagineIc=Toolkit.getDefaultToolkit().getImage(sourceimage.toString());
            super.setIconImage(image);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        score=0;
        points=0;
        _points=0;
        movement=10;
        scorePX=width/6;
        scoreCX=430;
        scoreY=30;
        if(System.getProperty("os.name").startsWith("Windows"))
        {
            this.width=width+11;
            ai=new AIPaddle(width,0,SPEED_AI,width,height,true);
        }
        else if(System.getProperty("os.name").startsWith("Linux"))
        {
            this.width=width;
            ai=new AIPaddle(width-10,0,SPEED_AI,width,height,true);
        }
        else
        {
            this.width=width;
            ai=new AIPaddle(width,0,SPEED_AI,width,height,true);
        }
        this.height=height;
        ball=new Ball(100,40,20,20);
        ball.setSpeed(6);
        _ai=new AIPaddle(0,0,SPEED_AI-0.8,width,height,false);
        url=Pong.class.getResource("pongLimit.wav");
        _url=Pong.class.getResource("pongScore.wav");
        clipContact=Applet.newAudioClip(url);
        clipScore=Applet.newAudioClip(_url);
        ballMovement=true;
        timer=new Timer(10,this); 
        timer.start();
    }

    @Override
    public void mpaint ( Graphics2D g2 ){
        GraphSet.setColor(g2, 255, 255, 255);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        try
        {
            g2.draw(ball);
            g2.fill(ball);
            g2.draw(_ai);
            g2.fill(_ai);
            g2.draw(ai);
            g2.fill(ai);
            g2.setFont(new Font("8BIT WONDER", Font.BOLD, 20));        //OLD WILD 8 Bit FONT :D
            setDimension(g2);
            g2.drawString(""+_points,width/2+30,scoreY);
            g2.drawString(""+points, width/2-45,scoreY);
            for(int i=0;i<height+10;i+=10)
            {
                Rectangle2D.Double r=new Rectangle2D.Double(width/2,i,5,5);
                g2.draw(r);
                g2.fill(r);
            }
        }
        catch(NullPointerException exc)
        {
            
        }
        if(score!=0)
        {
            if(score==1)
                g2.drawString("AI 00 score",scorePX,height/2);
            else
                g2.drawString("AI 01 score",scoreCX,height/2);
    }
  }
 
    void moveBall()
    {   
        if(points==10)
            jackpot("Game Over\nWanna continue playing?","Your computer wins!");
        else if(_points==10)
            jackpot("Game Over\nWanna continue playing?","Skinet(AKA The Machine) wins!");
        
        if(ball.getBounds().intersectsLine(new Line2D.Double(0,height,width,height)))
        {
            movement=0;
            clipContact.play();
        }
        else if(ball.getBounds().intersectsLine(new Line2D.Double(0,0,width,0)))
            {
            movement=1;
            clipContact.play();
        }
        else if(ball.getBounds().intersectsLine(new Line2D.Double(width,0,width,height)))    //+11 offset
        {
            if(!ball.getBounds().intersects(ai))
            {
                   score=1;
                   points++;
                   printScore();
                   new GameScore(clipScore).run(); //utilizzo oggetto esterno causa thread. 
                   movement=2;
            }
            else
            {
                if(ball.getBounds().intersectsLine(ai.getX(),ai.getY(),ai.getX(),(ai.getY()+20)))
                    movement=4;
                else if(ball.getBounds().intersectsLine(ai.getX(),ai.getY()+20,ai.getX(),(ai.getY()+40)))
                    movement=5;
                else if(ball.getBounds().intersectsLine(ai.getX(),ai.getY()+40,ai.getX(),(ai.getY()+60)))
                    movement=6;
            }
                   clipContact.play();
               
        }
        else if(ball.getBounds().intersectsLine(new Line2D.Double(-5,0,-5,height)))
        {
               if(!ball.getBounds().intersects(_ai)) 
               {
                   score=2;
                   _points++;
                   printScore();
                   new GameScore(clipScore).run(); //utilizzo oggetto esterno causa thread.
                   movement=3;
               }
               else
              {
                if(ball.getBounds().intersectsLine(_ai.getX(),_ai.getY(),_ai.getX(),(_ai.getY()+20)))
                    movement=7;
                else if(ball.getBounds().intersectsLine(_ai.getX(),_ai.getY()+20,_ai.getX(),(_ai.getY()+40)))
                    movement=8;
                else if(ball.getBounds().intersectsLine(_ai.getX(),_ai.getY()+40,_ai.getX(),(_ai.getY()+60)))
                    movement=9;
              }
                   clipContact.play(); 
        }
        
        switch(movement)
        {
            case 0: ball.moveY(false); ball.moveX(right);  down=false;  break;
            case 1: ball.moveY(true); ball.moveX(right);  down=true;   break;
            case 2: ball.moveX(false); ball.moveY(down);  right=false; break;
            case 3: ball.moveX(true); ball.moveY(down);  right=true; break;
            case 4: /*if(down&&!up)
                    {   
                        down=false;
                        up=true;
                    }
                    else if(!down&&up)
                    {
                        down=true;
                        up=false;
                    }
                    else
                    {
                        down=false;
                        up=true;
                    }*/
                    right=false; if(ball.getX()>=ai.getX()) down=false; ball.moveY(down); ball.moveX(right); break;
            case 5: right=false; ball.setSpeed(9); ball.moveX(right); ball.setSpeed(6); break;
            case 6: right=false; if(ball.getX()>=ai.getX()) down=false; ball.moveY(down); ball.moveX(right); break;
            case 7: right=true; if(ball.getX()<=_ai.getX()) down=true; ball.moveY(down); ball.moveX(right); break;
            case 8: right=true; ball.setSpeed(9); ball.moveX(right); ball.setSpeed(6); break;
            case 9: right=true; if(ball.getX()<=_ai.getX()) down=true; ball.moveY(down); ball.moveX(right); break;
            default:ball.moveX(true);
                    ball.moveY(true);  
                    down=true;
                    right=true;
                    break;
        }
    }

    
    private void printScore()
    {
        timer.stop();
        _timer=new Timer(1200,this);
        _timer.start();
        ballMovement=false;
    }

    private void reset()
    {
        ball=new Ball(350,80,20,20);
        ball.setSpeed(6);
        points=0;
        _points=0;
        mrepaint();
    }
    
    private void jackpot(String msg,String ttl)
    {
        if(JOptionPane.showConfirmDialog(this,msg,ttl,
           JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
              reset();
        else
        {
              dispose();
              System.exit(0);
        }
    }
    
    void setDimension(Graphics2D g2)                                 //check the presence of a custom font
    {
        if(!g2.getFont().getName().equals("8BIT WONDER"))
            {
                g2.setFont(new Font("Serif", Font.BOLD, 50));
                scoreY=40;
                scorePX=width/5;
                scoreCX=450;
            }
    }
    
    /*private void updateStatus()
    {
        if(player.getY()>=2)
			if(keys[KeyEvent.VK_UP])
                        {
                           player.move(false);
                        }
        if(player.getY()<=(height-50))
                        if(keys[KeyEvent.VK_DOWN])
                        {
                           player.move(true);
                        }
    }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(timer)) {
            if(ballMovement)
            {
                //updateStatus();
                _ai.moveAi(ball.x,ball.y);
                ai.moveAi(ball.x,ball.y);
                moveBall();
                mrepaint();
            }
        }
        else if(e.getSource().equals(_timer))
        {  
            mrepaint();
            score=0;
            ballMovement=true;
            _timer.stop();
            timer.start();
        }
    }
    
}


