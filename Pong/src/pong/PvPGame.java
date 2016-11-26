package pong;
import java.awt.*;
import MGui.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class PvPGame extends MFrame implements ActionListener {
    
    private Timer timer,_timer; 
    private File sourceimage;
    private Image immagineIc;
    private int movement,points,_points,score,scorePX,scoreCX,scoreY;
    private final int width,height;
    private Ball ball;
    private final Paddle player,_player;
    private boolean right,down,up,ballMovement;
    private boolean[] keys;
    private final URL url,_url; 
    private final AudioClip clipContact,clipScore;
    private final KeyListener listener;
    private final double SPEED_AI= 5.5;
    
    public PvPGame(int width, int height, String title) {        
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
        listener = new MyKeyListener();
        keys=new boolean[256];
        for(int i=0;i<keys.length;i++)
            keys[i]=false;
	addKeyListener(listener);
	setFocusable(true);
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
            _player=new Paddle(width,0,10,60);
        }
        else if(System.getProperty("os.name").startsWith("Linux"))
        {
            this.width=width;
            _player=new Paddle(width-10,0,10,60);
        }
        else
        {
            this.width=width;
            _player=new Paddle(width,0,10,60);
        }
        _player.setSpeed(7);
        this.height=height;
        ball=new Ball(100,40,20,20);
        ball.setSpeed(6);
        player=new Paddle(0,0,10,60);
        player.setSpeed(7);
        url=Pong.class.getResource("pongLimit.wav");
        _url=Pong.class.getResource("pongScore.wav");
        clipContact=Applet.newAudioClip(url);
        clipScore=Applet.newAudioClip(_url);
        timer=new Timer(10,this); 
        timer.start();
        ballMovement=true;
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
            g2.draw(player);
            g2.fill(player);
            g2.draw(_player);
            g2.fill(_player);
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
                g2.drawString("Player one score",scorePX,height/2);
            else
                g2.drawString("Player two score",scoreCX,height/2);
    }
  }
 
    void moveBall()
    {   
        if(points==10)
            jackpot("Game Over\nWanna continue playing?","Player one wins!");
        else if(_points==10)
            jackpot("Game Over\nWanna continue playing?","Player two wins!");
        
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
            if(!ball.getBounds().intersects(_player))
            {
                   score=1;
                   points++;
                   printScore();
                   new GameScore(clipScore).run(); //utilizzo oggetto esterno causa thread. 
                   movement=2;
            }
            else
            {
                if(ball.getBounds().intersectsLine(_player.getX(),_player.getY(),_player.getX(),(_player.getY()+20)))
                    movement=4;
                else if(ball.getBounds().intersectsLine(_player.getX(),_player.getY()+20,_player.getX(),(_player.getY()+40)))
                    movement=5;
                else if(ball.getBounds().intersectsLine(_player.getX(),_player.getY()+40,_player.getX(),(_player.getY()+60)))
                    movement=6;
            }
                   clipContact.play();
               
        }
        else if(ball.getBounds().intersectsLine(new Line2D.Double(-5,0,-5,height)))
        {
               if(!ball.getBounds().intersects(player)) 
               {
                   score=2;
                   _points++;
                   printScore();
                   new GameScore(clipScore).run(); //utilizzo oggetto esterno causa thread.
                   movement=3;
               }
               else
              {
                if(ball.getBounds().intersectsLine(player.getX(),player.getY(),player.getX(),(player.getY()+20)))
                    movement=7;
                else if(ball.getBounds().intersectsLine(player.getX(),player.getY()+20,player.getX(),(player.getY()+40)))
                    movement=8;
                else if(ball.getBounds().intersectsLine(player.getX(),player.getY()+40,player.getX(),(player.getY()+60)))
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
                    right=false; if(ball.getX()>=_player.getX()) down=false; ball.moveY(down); ball.moveX(right); break;
            case 5: right=false; ball.setSpeed(9); ball.moveX(right); ball.setSpeed(6); break;
            case 6: right=false; if(ball.getX()>=_player.getX()) down=false; ball.moveY(down); ball.moveX(right); break;
            case 7: right=true; if(ball.getX()<=player.getX()) down=true; ball.moveY(down); ball.moveX(right); break;
            case 8: right=true; ball.setSpeed(9); ball.moveX(right); ball.setSpeed(6); break;
            case 9: right=true; if(ball.getX()<=player.getX()) down=true; ball.moveY(down); ball.moveX(right); break;
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
        player.setY(0);
        _player.setY(0);
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
    
    private void updateStatus()
    {
        if(player.getY()>=2)
			if(keys[KeyEvent.VK_W])
                        {
                           player.move(false);
                        }
        if(player.getY()<=(height-50))
                        if(keys[KeyEvent.VK_S])
                        {
                           player.move(true);
                        }
        if(_player.getY()>=2)
                        if(keys[KeyEvent.VK_UP])
                        {
                           _player.move(false);
                        }
        if(_player.getY()<=(height-50))
                        if(keys[KeyEvent.VK_DOWN])
                        {
                           _player.move(true);
                        }
                        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(timer)) {
            if(ballMovement)
            {
                updateStatus();
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
    
    private class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
                    keys[e.getKeyCode()]=true;
		}

		@Override
		public void keyPressed(KeyEvent e) {
                    keys[e.getKeyCode()]=true;
		}

		@Override
		public void keyReleased(KeyEvent e) {
                    keys[e.getKeyCode()]=false;
		}
	}
}
