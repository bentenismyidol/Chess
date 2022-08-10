

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameWindow {
    private JFrame gameWindow = new JFrame("Chess");
    public Clock blackClock;
    public Clock whiteClock;
    private Timer timer;
    private Board board;
    private JPanel panel;

    public GameWindow(String blackName, String whiteName, int hh, int mm, int ss)
    {
        blackClock = new Clock(hh, mm,ss);
        whiteClock = new Clock(hh, mm,ss);

        try{
            Image whiteImg = ImageIO.read(getClass().getResource("wp.png"));
            gameWindow.setIconImage(whiteImg);
        } catch(Exception e) {System.out.println("Game file wp.png not found!");}


        gameWindow.setLocation(400,100);

        gameWindow.setLayout(new BorderLayout(20,20));

        panel = gameDataPanel (blackName ,whiteName, hh,mm,ss);

        gameWindow.setSize(panel.getPreferredSize());
        gameWindow.add(panel, BorderLayout.NORTH);

        this.board = new Board(this);

        gameWindow.add(board, BorderLayout.CENTER);

        gameWindow.add(buttons(), BorderLayout.SOUTH);


        gameWindow.setSize(gameWindow.getPreferredSize());
        gameWindow.setResizable(false);

        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel gameDataPanel(final String bn, final String wn, final int hh, final int mm, final int ss)
    {
        JPanel gameData = new JPanel();
        gameData.setLayout(new GridLayout(3,2));

        //Player names
        JLabel w = new JLabel(bn);
        JLabel b = new JLabel(wn);

        w.setHorizontalAlignment(JLabel.CENTER);
        w.setVerticalAlignment(JLabel.CENTER);
        b.setHorizontalAlignment(JLabel.CENTER);
        b.setVerticalAlignment(JLabel.CENTER);

        gameData.add(w);
        gameData.add(b);
        //CLocks
        final JLabel bTime = new JLabel(blackClock.getTime());
        final JLabel wTime = new JLabel(whiteClock.getTime());

        bTime.setHorizontalAlignment(JLabel.CENTER);
        bTime.setVerticalAlignment(JLabel.CENTER);
        wTime.setHorizontalAlignment(JLabel.CENTER);
        wTime.setVerticalAlignment(JLabel.CENTER);

        if (!(hh == 0 && mm == 0 && ss == 0))
        {
            timer = new Timer(1000, null);
            timer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean turn = board.getTurn();

                    if (turn) {
                        whiteClock.decr();
                        wTime.setText(whiteClock.getTime());

                        if (whiteClock.outOfTime()) {
                            timer.stop();
                            int n = JOptionPane.showConfirmDialog(
                                    gameWindow,
                                    bn + " wins by time! Play a new game? \n" +
                                            "Choosing \"No\" quits the game.",
                                    bn + " wins!",
                                    JOptionPane.YES_NO_OPTION);

                            if (n == JOptionPane.YES_OPTION) {
                                new GameWindow(bn, wn, hh, mm, ss);
                                gameWindow.dispose();
                            } else gameWindow.dispose();
                        }
                    } else {
                        blackClock.decr();
                        bTime.setText(blackClock.getTime());

                        if (blackClock.outOfTime()) {
                            timer.stop();
                            int n = JOptionPane.showConfirmDialog(
                                    null,
                                    wn + " wins by time! Play a new game? \n" +
                                            "Choosing \"No\" quits the game.",
                                    wn + " wins!",
                                    JOptionPane.YES_NO_OPTION);

                            if (n == JOptionPane.YES_OPTION) {
                                new GameWindow(bn, wn, hh, mm, ss);
                                gameWindow.dispose();
                            } else gameWindow.dispose();
                        }
                    }
                }
            });
            timer.start();
        } else {
            wTime.setText("Unlimited time");
            bTime.setText("Unlimited time");
        }

        gameData.add(wTime);
        gameData.add(bTime);

        gameData.getMinimumSize();

        return gameData;
    }

    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3,0,0));

        final JButton quit = new JButton("Quit");

        quit.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) {
                                       int n = JOptionPane.showConfirmDialog(gameWindow, "Are you sure you want to quit?", "Confirm quit", JOptionPane.YES_NO_OPTION);

                                       if (n == JOptionPane.YES_OPTION) {gameWindow.dispose();}
                                   }
                               }
        );

        final JButton nGame = new JButton("New game");

        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(gameWindow, "Are you sure you want to begin a new game?", "Confirm new game", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    gameWindow.dispose();
                    SwingUtilities.invokeLater(new StartMenu());
                }
            }
        });


        final JButton instr = new JButton("How to play");

        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(gameWindow,
                        "Move the chess pieces on the board by clicking\n"
                                + "and dragging. You can win either by your opponent running\n"
                                + "out of time or by checkmating your opponent.\n"
                                + "\nGood luck!",
                        "How to play", -1);
            }
        });

        buttons.add(instr);
        buttons.add(nGame);
        buttons.add(quit);


        buttons.getMinimumSize();

        return buttons;
    }
    public void incheckOccurred (int c){
        if(c==0){
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                     "Black is in check! Be careful!! \n" +
                            "Choosing \"OK\" to keep on playing." +
                            "Choosing \"Cancel\" to quit.",
                    "White checks!",JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.CANCEL_OPTION) {
                SwingUtilities.invokeLater(new StartMenu());
                gameWindow.dispose();
            }
        }
        else {
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "White is in check! Be careful!! \n" +
                            "Choosing \"OK\" to keep on playing." +
                            "Choosing \"Cancel\" to quit.",
                    "Black checks!",JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.CANCEL_OPTION) {
                SwingUtilities.invokeLater(new StartMenu());
                gameWindow.dispose();
            }
        }
    }
    public void checkmateOccurred (int c) {
        if (c == 0) {
            if (timer != null) timer.stop();
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "White wins by checkmate! Set up a new game? \n" +
                            "Choosing \"No\" lets you look at the final situation.",
                    "White wins!",
                    JOptionPane.YES_NO_OPTION);

            if (n == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new StartMenu());
                gameWindow.dispose();
            }
        } else {
            if (timer != null) timer.stop();
            int n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Black wins by checkmate! Set up a new game? \n" +
                            "Choosing \"No\" lets you look at the final situation.",
                    "Black wins!",
                    JOptionPane.YES_NO_OPTION);

            if (n == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new StartMenu());
                gameWindow.dispose();
            }
        }
    }
}
