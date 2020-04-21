import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class MazePanel extends JPanel {

    private Maze maze;

    public MazePanel(Maze maze) {
        this.maze = maze;
        this.setSize(640, 480);
        this.setFocusTraversalKeysEnabled(false);
        this.setFocusable(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                System.out.println(keyEvent.getKeyCode());
                int x = MazePanel.this.maze.getPlayer().getX();
                int y = MazePanel.this.maze.getPlayer().getY();

                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        System.out.println("up");
                        MazePanel.this.maze.setPlayerPosition(x,y-1);
                        break;
                    case KeyEvent.VK_DOWN :
                        System.out.println("down");
                        MazePanel.this.maze.setPlayerPosition(x,y+1);
                        break;
                    case KeyEvent.VK_LEFT :
                        System.out.println("left");
                        MazePanel.this.maze.setPlayerPosition(x-1,y);
                        break;
                    case KeyEvent.VK_RIGHT :
                        System.out.println("right");
                        MazePanel.this.maze.setPlayerPosition(x+1,y);
                        break;
                    default:
                        return;
                }
                if (MazePanel.this.maze.getPlayer().getX() == 1 && MazePanel.this.maze.getPlayer().getY() == 0) {
                    JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(MazePanel.this), "You won the game, Congrats !");
                    MazePanel.this.maze = new Maze(MazePanel.this.maze.getHeight()+7, MazePanel.this.maze.getWidth()+11);
                }
                MazePanel.this.repaint();

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0,0,getWidth(),getHeight());
        g2.setColor(new Color(0x8FDCDC));
        g2.fillRect(0,0,getWidth(),getHeight());
        g2.setColor(Color.BLACK);
        for (int i = 0; i < maze.getMaze().length; i++) {
            for (int j = 0; j < maze.getMaze()[0].length; j++) {
                if (maze.getMaze()[i][j] == Maze.MazeCell.EMPTY)
                    continue;
                else if (maze.getMaze()[i][j] == Maze.MazeCell.WALL){
                    g2.fillRect( j *  (getWidth()/maze.getMaze()[0].length) + (getWidth()%maze.getMaze()[0].length)/2 ,i  * (getHeight()/maze.getMaze().length) + (getHeight()%maze.getMaze().length)/2,getWidth()/maze.getMaze()[0].length, getHeight()/maze.getMaze().length);
                }
                else if (maze.getMaze()[i][j] == Maze.MazeCell.PLAYER) {
                    g2.setColor(Color.RED);
                    g2.fillRect( j *  (getWidth()/maze.getMaze()[0].length) + (getWidth()%maze.getMaze()[0].length)/2 ,i  * (getHeight()/maze.getMaze().length) + (getHeight()%maze.getMaze().length)/2,getWidth()/maze.getMaze()[0].length, getHeight()/maze.getMaze().length);
                    g2.setColor(Color.black);
                } else if (maze.getMaze()[i][j] == Maze.MazeCell.PATH) {
                    g2.setColor(Color.YELLOW);
                    g2.fillRect( j *  (getWidth()/maze.getMaze()[0].length) + (getWidth()%maze.getMaze()[0].length)/2 ,i  * (getHeight()/maze.getMaze().length) + (getHeight()%maze.getMaze().length)/2,getWidth()/maze.getMaze()[0].length, getHeight()/maze.getMaze().length);
                    g2.setColor(Color.BLACK);
                }
            }
        }

    }

    public void reset() {
        MazePanel.this.maze = new Maze(MazePanel.this.maze.getHeight()+10, (MazePanel.this.maze.getHeight()+10)*4/3);
        System.out.println("new map "+(MazePanel.this.maze.getHeight()+10)+":"+ (MazePanel.this.maze.getWidth()+15));
        repaint();
    }

    public void solve() {
        int playerX = maze.getPlayer().getX();
        int playerY = maze.getPlayer().getY();
        maze.djikstra(playerX+playerY*maze.getWidth(),1);
        this.repaint();
    }
}
