package pong;

/**
 *
 * @author daniele&emil
 */
public class Pong {

    public static void main(String[] args) { 
            final int WIDTH = 800;
            final int HEIGHT = WIDTH / 16 * 9;
            final String TITLE = "Pong Beta";
            MainMenu a=new MainMenu(WIDTH, HEIGHT, TITLE);
    }
}
