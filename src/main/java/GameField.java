import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;

    private Image dot;
    private Image apple;

    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];

    private int n = 3;
    private int t = 150;

    private int[] appleX = new int[n];
    private int[] appleY = new int[n];

    private int dots;
    private Timer timer;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;

    private boolean inGame = true;

    public void loadImage() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    public void createApple() {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            appleX[i] = random.nextInt(20)*DOT_SIZE;
            appleY[i] = random.nextInt(20)*DOT_SIZE;

        }
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            y[i] = 48;
            x[i] = 48 - i*DOT_SIZE;
        }

        timer = new Timer(t, this);
        timer.start();
        createApple();
    }

    public void checkApple(){
        for (int i = 0; i < n; i++) {

            if (x[0] == appleX[i] && y[0] == appleY[i]) {
                dots++;
                int tempX = appleX[i];
                int tempY = appleY[i];
                appleX[i] = appleX[n-1];
                appleY[i] = appleY[n-1];
                appleX[n-1] = tempX;
                appleY[n-1] = tempY;

                n--;
                if (dots % 3 == 0) {
                    n = 3;
                    createApple();
                }
                if (dots % 15 == 0) {
                    dots = 3;
                    n = 3;
                    t -= 10;
                    timer = new Timer(t, this);
                    timer.start();
                    createApple();
                }

            }

        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (inGame){
            for (int i = 0; i < n; i++) {
                g.drawImage(apple, appleX[i], appleY[i], this);
            }
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            String str = "Game Over!";
            g.setColor(Color.CYAN);
            g.drawString(str, SIZE/6, SIZE/2);
        }
    }
    public void checkCollision(){
        for (int i = dots; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        if (x[0] > SIZE)
            x[0] = 0;
        if (x[0] < 0)
            x[0] = SIZE;
        if (y[0] > SIZE)
            y[0] = 0;
        if (y[0] < 0)
            y[0] = SIZE;

    }

    @Override
    public void actionPerformed(ActionEvent a){
        if (inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    public GameField(){
        setBackground(Color.BLACK);
        loadImage();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if (left)
            x[0] -= DOT_SIZE;
        if (right)
            x[0] += DOT_SIZE;
        if (up)
            y[0] -= DOT_SIZE;
        if (down)
            y[0] += DOT_SIZE;

    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent k){
            super.keyPressed(k);
            int key = k.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down){
                up = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up){
                down = true;
                left = false;
                right = false;
            }
        }
    }


}
