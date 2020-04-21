
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

public class Main {



    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze runner");
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(new EmptyBorder(15,15,15,15));


        MazePanel mazePanel = new MazePanel(new Maze(21, 21*4/3));

//        panel.add(mazePanel,BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Action");

        JMenuItem restart = new JMenuItem("Generate maze");
        restart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazePanel.reset();
            }
        });
        menu.add(restart);
        JMenuItem solve = new JMenuItem("Solve");
        solve.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazePanel.solve();
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        menu.add(solve);
        menu.add(exit);
        menuBar.add(menu);
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About ..");
        about.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panel, "Developed by Walid BOUDEBOUDA\n walid.boudebouda@gmail.com\nhttps://www.github.com/walid-git");
            }
        });
        help.add(about);
        menuBar.add(help);
        panel.add(menuBar,BorderLayout.NORTH);
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setBorder(new EmptyBorder(15,15,15,15));
        panel1.setBackground(new Color(0x8FDCDC));
        panel1.add(mazePanel,BorderLayout.CENTER);
        panel.add(panel1,BorderLayout.CENTER);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }


}