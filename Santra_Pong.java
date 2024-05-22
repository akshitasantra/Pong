import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;

public class Santra_Pong
{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong");
        frame.setSize(1000, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        PongPanel panel = new PongPanel();
        frame.add(panel);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }
    public static class PongPanel extends JPanel implements ActionListener, KeyListener{
        boolean wPressed = false;
        boolean sPressed = false;
        boolean upPressed = false;
        boolean downPressed = false;
        int LpaddleY = 400;
        int RpaddleY = 400;
        int ballX = 500;
        int ballY = 400;
        int ballXspeed = 5;
        int ballYspeed = 5;
        int leftScore = 0;
        int rightScore = 0;
        int LpaddleX = 50;
        int LpaddleW = 25;
        int LpaddleH = 150;
        int RpaddleX = 950;
        int RpaddleW = 25;
        int RpaddleH = 150;
        int ballW = 25;
        int ballH = 25;
        boolean onePressed = false;
        boolean twoPressed = false;
        boolean start = true;
        boolean vsCPU = false;
        Timer timer = new Timer(1000/60, this);
        Clip collisionClip;
        Clip scoreClip;
        public PongPanel(){
            setBackground(new Color(0, 0, 0));
            timer.start();
            setFocusable(true);
            addKeyListener(this);
            try{
                File collisionFile = new File("collision.wav");
                AudioInputStream collisionAudio = AudioSystem.getAudioInputStream(collisionFile);
                collisionClip = AudioSystem.getClip();
                collisionClip.open(collisionAudio);
                
                File scoreFile = new File("score.wav");
                AudioInputStream scoreAudio = AudioSystem.getAudioInputStream(scoreFile);
                scoreClip = AudioSystem.getClip();
                scoreClip.open(scoreAudio);
            }
            catch(Exception e){
                System.out.println("ERROR LOADING SOUND FILES!");
            }
        }
        public void playSound (String s){
            try {
                if(s.equals("collision.wav")){
                    collisionClip.setMicrosecondPosition(0);
                    collisionClip.start();
                }
                if(s.equals("score.wav")){
                    scoreClip.setMicrosecondPosition(0);
                    scoreClip.start();
                }
            }
            catch (Exception e) {
                System.out.println("Can't find file names: " + s);
            }
        }
        public boolean intersects(int px1, int py1, int pw, int ph){
            int bx1 = ballX;
            int by1 = ballY;
            int bx2 = ballX + ballW;
            int by2 = ballY + ballH;
            int px2 = px1 + pw;
            int py2 = py1 + ph;
            if(bx2>px1 && bx1<px2 && by2>py1 && by1<py2){
                return true;
            }
            else{
                return false;
            }
        }
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_W){
                wPressed = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_S){
                sPressed = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_UP){
                upPressed = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN){
                downPressed = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_1){
                onePressed = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_2){
                twoPressed = true;
            }
        }
        public void keyReleased(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_W){
                wPressed = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_S){
                sPressed = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_UP){
                upPressed = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN){
                downPressed = false;
            }
        }
        public void keyTyped(KeyEvent e){}
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if(start){
                g.setColor(new Color(255, 255, 255));
                g.setFont(new Font("IrisUPC", Font.BOLD, 50));
                g.drawString("PONG!", 450, 50);
                g.drawString("Press 1 to play vs CPU", 25, 250);
                g.drawString("Press 2 to play vs HUMAN", 25, 300);
            }
            else{
                if(leftScore < 11 && rightScore < 11){
                    g.setColor(new Color(255, 255, 255));
                    g.setFont(new Font("IrisUPC", Font.BOLD, 100));
                    g.drawString("" + leftScore, 200, 100);
                    g.drawString("" + rightScore, 750, 100);
                    g.fillRect(LpaddleX, LpaddleY, LpaddleW, LpaddleH);
                    g.fillRect(RpaddleX, RpaddleY, RpaddleW, RpaddleH);
                    g.fillRect(ballX, ballY, ballW, ballH);
                }
                else if(leftScore >= 11){
                    timer.stop();
                    g.setColor(new Color(255, 255, 255));
                    g.setFont(new Font("IrisUPC", Font.BOLD, 50));
                    g.drawString("LEFT PLAYER WINS!", 0, 400);
                }
                else if(rightScore >= 11){
                    timer.stop();
                    g.setColor(new Color(255, 255, 255));
                    g.setFont(new Font("IrisUPC", Font.BOLD, 50));
                    g.drawString("RIGHT PLAYER WINS!", 0, 400);
                }
            }
        }
        public void actionPerformed(ActionEvent e){
            if(start){
                if(onePressed){
                    start = false;
                    vsCPU = true;
                }
                if(twoPressed){
                    start = false;
                    vsCPU = false;
                }
            }
            else{
                if(vsCPU){
                    if(RpaddleY > ballY - RpaddleH/2 + ballH/2 && ballX > 250){
                        RpaddleY -= 12;
                    }
                    else if(RpaddleY < ballY - RpaddleH/2 + ballH/2 && ballX > 250){
                    RpaddleY += 12;
                    }
                }
                if(wPressed == true){
                    LpaddleY = LpaddleY - 10;
                }
                if(sPressed == true){
                  LpaddleY = LpaddleY + 10;
                }
                if(upPressed == true){
                    RpaddleY = RpaddleY - 10;
                }
                if(downPressed == true){
                    RpaddleY = RpaddleY + 10;
                }
                if(LpaddleY <= 0){
                    LpaddleY = 0;
                }
                if(LpaddleY >= 625){
                    LpaddleY = 625;
                }
                if(RpaddleY <= 0){
                    RpaddleY = 0;
                }
                if(RpaddleY >= 625){
                    RpaddleY = 625;
                }
                if(ballY <= 0){
                   ballYspeed *= -1;
                }
                if(ballY >= 750){
                   ballYspeed *= -1;
                }
                if(ballX <= 0){
                    ballX = 500;
                    ballY = 400;
                    ballXspeed = 5;
                    ballYspeed = 5;
                    rightScore++;
                    playSound("score.wav");
                    try{
                        Thread.sleep(500);
                    } 
                   catch(Exception ex){}
                }
                if(ballX >= 975){
                    ballX = 500;
                    ballY = 400;
                    ballXspeed = -5;
                    ballYspeed = 5;
                    leftScore++;
                    playSound("score.wav");
                    }
                boolean touchingLeftPaddle = intersects(LpaddleX, LpaddleY,LpaddleW, LpaddleH);
                if(touchingLeftPaddle == true){
                    ballXspeed *= -1;
                    playSound("collision.wav");
                    if(ballXspeed > 0){
                        ballXspeed++;
                    }
                    else{
                        ballXspeed--;
                    }
                    if(ballYspeed > 0){
                        ballYspeed++;
                    }
                    else{
                        ballYspeed--;
                    }
                    if(ballX < LpaddleX + LpaddleW){
                         ballX = LpaddleX + LpaddleW;
                    }
                }
            
                boolean touchingRightPaddle = intersects(RpaddleX, RpaddleY, RpaddleW, RpaddleH);
                if(touchingRightPaddle == true){
                    ballXspeed *= -1;
                    playSound("collision.wav");
                    if(ballXspeed > 0){
                        ballXspeed++;
                    }
                    else{
                        ballXspeed--;
                    }
                    if(ballYspeed > 0){
                        ballYspeed++;
                    }
                    else{
                        ballYspeed--;
                    }
                    if(ballX + ballW > RpaddleX){
                         ballX = RpaddleX - ballW;
                    }
                }
                ballX += ballXspeed;
                ballY += ballYspeed;
                repaint();
            }
        }
    }
}
