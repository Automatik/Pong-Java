package pong;
import java.awt.geom.Rectangle2D;
/**
 *
 * @author Emil
 */
public class AIPaddle extends Rectangle2D.Double
{
    private final int gameHeight,gameWidth;
    private final double WIDTH=10,HEIGHT=60,SPEED;
    private final boolean right;
    public AIPaddle(int x,int y,double speed,int gameWidth,int gameHeight,boolean right){
        SPEED=speed;
        this.x=x;
        this.y=y;
        this.height=HEIGHT;
        this.width=WIDTH;
        this.gameHeight=gameHeight;
        this.gameWidth=gameWidth;
        this.right=right;
    }
    
   public void moveAi(double ballX, double ballY)
    {
        if(right)
        {
            if(ballX >= gameWidth/2){    
                if(ballY < y && y>=2)
                    y-=SPEED;
                if(ballY >y && y+height <=gameHeight+10)
                    y+=SPEED;
            }
        }
        else 
        {        
            if(ballX < gameWidth/1.5){
                 if(ballY < y && y>=2)
                     y-=SPEED;
                 if(ballY >y && y+height <=gameHeight+10)
                     y+=SPEED;
            }
        } 
    }
}