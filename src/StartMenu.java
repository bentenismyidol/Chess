
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.swing.LookAndFeel;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.sound.sampled.*;

import java.awt.Dimension;
import java.awt.Color;
import java.net.URL;

import static java.awt.SystemColor.window;
public class StartMenu implements Runnable {
    //play button
    final JButton playbutton = new JButton(new ImageIcon("pic/1.png")); // ẢNH 1
    ImageIcon iconplayin = new ImageIcon("pic/2.png"); // ẢNH 2
    ImageIcon iconplayout = new ImageIcon("pic/1.png"); // ẢNH 1
    //exit button
    final JButton exitbutton = new JButton(new ImageIcon("pic/3.png")); // ẢNH 3
    ImageIcon iconexitin = new ImageIcon("pic/4.png"); // ẢNH 4
    ImageIcon iconexitout = new ImageIcon("pic/3.png"); // ẢNH 3
    //start burron
    final JButton start = new JButton(new ImageIcon("pic/5.png")); // ẢNH 5
    ImageIcon starticonin = new ImageIcon("pic/6.png"); // ẢNH 6
    ImageIcon sraticonout = new ImageIcon("pic/5.png"); // ẢNH 5
    //instr button
    final JButton instr = new JButton(new ImageIcon("pic/7.png")); //ẢNH 7
    ImageIcon instriconin = new ImageIcon("pic/8.png"); // ẢNH 8
    ImageIcon instriconout = new ImageIcon("pic/7.png"); // ẢNH 7
    //back button
    final JButton back = new JButton(new ImageIcon("pic/9.png")); //ẢNH 9
    ImageIcon backiconin = new ImageIcon("pic/10.png"); // ẢNH 10
    ImageIcon backiconout = new ImageIcon("pic/9.png"); // ẢNH 9
  
    //sound effect
    public void SoundEffects() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("pic/13.wav"); //VIDEO 13
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }
    public void run() {
        final JFrame startWindow = new JFrame("Chess");
        // Set window properties
        startWindow.setLocation(300, 100);
        startWindow.setResizable(false);
        startWindow.setSize(700, 500);
        //set window background
        try {
            startWindow.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("pic/12.png"))))); //ẢNH 12
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        //empty border
        Border emptyBorder = BorderFactory.createEmptyBorder();
        // Game title
        final JLabel titleLabel = new JLabel(new ImageIcon("pic/11.png")); // ẢNH 11
        titleLabel.setBounds(90,40,500,100);
        startWindow.add(titleLabel);
        // Black player selections
        final JLabel blackPiece = new JLabel();
        try {
            Image blackImg = ImageIO.read(getClass().getResource("bp.png"));
            blackPiece.setIcon(new ImageIcon(blackImg));
            blackPiece.setBounds(50,50,20,20);
            blackPiece.setVisible(false);
            startWindow.add(blackPiece);
        } catch (Exception e) {
            System.out.println("Required game file bp.png missing");
        }
        final JTextField blackInput = new JTextField("Black", 10);
        blackInput.setBounds(70,50,100,20);
        blackInput.setVisible(false);
        blackInput.setBackground(Color.BLACK);
        blackInput.setForeground(Color.lightGray);
        blackInput.setBorder(emptyBorder);
        startWindow.add(blackInput);

        // White player selections
        final JLabel whitePiece = new JLabel();
        try {
            Image whiteImg = ImageIO.read(getClass().getResource("wp.png"));
            whitePiece.setIcon(new ImageIcon(whiteImg));
            whitePiece.setBounds(480,50,20,20);
            whitePiece.setVisible(false);
            startWindow.add(whitePiece);
        }  catch (Exception e) {
            System.out.println("Required game file wp.png missing");
        }
        final JTextField whiteInput = new JTextField("White", 10);
        whiteInput.setBounds(500,50,100,20);
        whiteInput.setVisible(false);
        whiteInput.setBackground(Color.BLACK);
        whiteInput.setForeground(Color.lightGray);
        whiteInput.setBorder(emptyBorder);
        startWindow.add(whiteInput);

        // Timer settings
        final String[] minSecInts = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minSecInts[i] = "0" + Integer.toString(i);
            } else {
                minSecInts[i] = Integer.toString(i);
            }
        }
        //label text for h,m,s
        final JLabel hourstxt = new JLabel("H");
        hourstxt.setBounds(290,50,20,20);
        hourstxt.setVisible(false);
        hourstxt.setForeground(Color.WHITE);
        startWindow.add(hourstxt);
        final JLabel minutestxt = new JLabel("M");
        minutestxt.setBounds(290,150,20,20);
        minutestxt.setVisible(false);
        minutestxt.setForeground(Color.WHITE);
        startWindow.add(minutestxt);
        final JLabel secondstxt = new JLabel("S");
        secondstxt.setBounds(290,250,20,20);
        secondstxt.setVisible(false);
        secondstxt.setForeground(Color.WHITE);
        startWindow.add(secondstxt);

        //custom combobox properties
        class MyComboBoxUI extends BasicComboBoxUI {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                LookAndFeel.uninstallBorder(comboBox);
            }

            @Override
            protected JButton createArrowButton() {
                final Color background = Color.black.darker();     //Default is UIManager.getColor("ComboBox.buttonBackground").
                final Color pressedButtonBorderColor = Color.WHITE; //Default is UIManager.getColor("ComboBox.buttonShadow"). The color of the border of the button, while it is pressed.
                final Color triangle = Color.WHITE;               //Default is UIManager.getColor("ComboBox.buttonDarkShadow"). The color of the triangle.
                final Color highlight = background;               //Default is UIManager.getColor("ComboBox.buttonHighlight"). Another color to show the button as highlighted.
                final JButton button = new BasicArrowButton(BasicArrowButton.SOUTH, background, pressedButtonBorderColor, triangle, highlight);
                button.setName("ComboBox.arrowButton"); //Mandatory, as per BasicComboBoxUI#createArrowButton().
                return button;
            }

            @Override
            public void configureArrowButton() {
                super.configureArrowButton(); //Do not forget this!
                arrowButton.setBackground(Color.lightGray);
                arrowButton.setForeground(Color.BLACK);
            }
        }
        //combobox for h,m,s
        final JComboBox<String> seconds = new JComboBox<String>(minSecInts);
        seconds.setBounds(300,150,100,20);
        seconds.setVisible(false);
        seconds.setBackground(Color.black);
        seconds.setForeground(Color.lightGray);
        seconds.setUI(new MyComboBoxUI());
        //seconds.setBorder(emptyBorder);
        final JComboBox<String> minutes = new JComboBox<String>(minSecInts);
        minutes.setBounds(300,250,100,20);
        minutes.setVisible(false);
        minutes.setBackground(Color.black);
        minutes.setForeground(Color.lightGray);
        minutes.setUI(new MyComboBoxUI());
        final JComboBox<String> hours =
                new JComboBox<String>(new String[] {"0","1","2","3"});
        hours.setBounds(300,50,100,20);
        hours.setVisible(false);
        hours.setBackground(Color.black);
        hours.setForeground(Color.lightGray);
        hours.setUI(new MyComboBoxUI());
        //Box timerSettings = Box.createHorizontalBox();

        hours.setMaximumSize(hours.getPreferredSize());
        minutes.setMaximumSize(minutes.getPreferredSize());
        seconds.setMaximumSize(minutes.getPreferredSize());

        startWindow.add(hours);
        startWindow.add(minutes);
        startWindow.add(seconds);

        //BUTTONS (play,exit)
        //exit button
        exitbutton.setBorder(emptyBorder);
        exitbutton.setBounds(175,350,350,50);
        exitbutton.setFocusable(false);
        exitbutton.setVisible(true);
        exitbutton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                //System.out.println("test đầu vào");
                exitbutton.setIcon(iconexitin);
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            public void mouseExited(MouseEvent evt) {
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }
                exitbutton.setIcon(iconexitout);
            }
        });
        startWindow.add(exitbutton);
        //play button
        playbutton.setBorder(emptyBorder);
        playbutton.setBounds(175,200,350,50);
        playbutton.setFocusable(false);
        playbutton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                //System.out.println("test đầu vào");
                playbutton.setIcon(iconplayin);
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            public void mouseExited(MouseEvent evt) {
                //System.out.println("test đầu ra");
                playbutton.setIcon(iconplayout);
            }
        });
        startWindow.add(playbutton);
        // Buttons (start,insr,back)
        //inreustion
        instr.setBorder(emptyBorder);
        instr.setBounds(245,370,200,50);
        instr.setVisible(false);
        instr.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                //System.out.println("test đầu vào");
                instr.setIcon(instriconin);
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            public void mouseExited(MouseEvent evt) {
                //System.out.println("test đầu ra");
                instr.setIcon(instriconout);
            }
        });
        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(startWindow,
                        "To begin a new game, input player names\n" +
                                "next to the pieces. Set the clocks and\n" +
                                "click \"Start\". Setting the timer to all\n" +
                                "zeroes begins a new untimed game.",
                        "How to play",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        //start
        start.setBorder(emptyBorder);
        start.setBounds(25,370,200,50);
        start.setVisible(false);
        start.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                //System.out.println("test đầu vào");
                start.setIcon(starticonin);
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            public void mouseExited(MouseEvent evt) {
                //System.out.println("test đầu ra");
                start.setIcon(sraticonout);
            }
        });
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }
                String bn = blackInput.getText();
                String wn = whiteInput.getText();
                int hh = Integer.parseInt((String) hours.getSelectedItem());
                int mm = Integer.parseInt((String) minutes.getSelectedItem());
                int ss = Integer.parseInt((String) seconds.getSelectedItem());

                new GameWindow(bn, wn, hh, mm, ss);
                startWindow.dispose();
            }
        });
        //back
        back.setBorder(emptyBorder);
        back.setVisible(false);
        back.setBounds(465,370,200,50);
        back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                //System.out.println("test đầu vào");
                back.setIcon(backiconin);
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            public void mouseExited(MouseEvent evt) {
                //System.out.println("test đầu ra");
                back.setIcon(backiconout);
            }
        });
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SoundEffects();
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }
                playbutton.setVisible(true);
                exitbutton.setVisible(true);
                titleLabel.setVisible(true);
                start.setVisible(false);
                instr.setVisible(false);
                back.setVisible(false);
                whiteInput.setVisible(false);
                blackInput.setVisible(false);
                hours.setVisible(false);
                minutes.setVisible(false);
                seconds.setVisible(false);
                whitePiece.setVisible(false);
                blackPiece.setVisible(false);
                hourstxt.setVisible(false);
                minutestxt.setVisible(false);
                secondstxt.setVisible(false);

            }
        });
        startWindow.add(start);
        startWindow.add(instr);
        startWindow.add(back);
        //play & exit utilitiies
        playbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==playbutton) {
                    try {
                        SoundEffects();
                    } catch (UnsupportedAudioFileException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (LineUnavailableException ex) {
                        ex.printStackTrace();
                    }
                    playbutton.setVisible(false);
                    exitbutton.setVisible(false);
                    titleLabel.setVisible(false);
                    start.setVisible(true);
                    instr.setVisible(true);
                    back.setVisible(true);
                    whiteInput.setVisible(true);
                    blackInput.setVisible(true);
                    hours.setVisible(true);
                    minutes.setVisible(true);
                    seconds.setVisible(true);
                    whitePiece.setVisible(true);
                    blackPiece.setVisible(true);
                    hourstxt.setVisible(true);
                    minutestxt.setVisible(true);
                    secondstxt.setVisible(true);

                }
            }
        });

        exitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==exitbutton) {
                    try {
                        SoundEffects();
                    } catch (UnsupportedAudioFileException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (LineUnavailableException ex) {
                        ex.printStackTrace();
                    }
                    playbutton.setVisible(false);
                    startWindow.dispose();
                }
            }
        });
        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);
    }

}
