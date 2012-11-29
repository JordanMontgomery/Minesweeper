/*
 * Minesweeper.Java - Implements the basic minesweeper screen and handles game logic
 * The individual button logic is handled in Minebutton.java
 *  CS335 Homework 1 "Minesweeer"
 */
package minesweeper;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class Minesweeper extends JFrame {

    private final static String versionString = "1.0.1"; //version string
    private static int length; //variables to store user input
    private static int width;
    private static int nMines;
    private static int nFlags;
    private static Scanner userInput = new Scanner(System.in); //stdin scanner
    private Random randnums = new Random();//random numbers used to populate mines
    private boolean inprogress; //flag whether game is in progress or not
    private Minebutton[][] buttons; //array of all squares on board
    private static JMenuBar topMenu = new JMenuBar(); //menu bar
    private JMenu gameMenu = new JMenu("Game"); //"Game" menu
    private JMenu helpMenu = new JMenu("Help"); //"Help" menu
    private JMenuItem newGame = new JMenuItem("New game"); //starts a new game
    private JMenuItem exit = new JMenuItem("Exit"); //exits program
    private JMenuItem about = new JMenuItem("About"); //displays program info
    private JButton smiley = new JButton(); //Status smiley at top of screen
    private static ImageIcon smileyglasses = new ImageIcon("sunglasses.png"); //smiley won icon
    private static ImageIcon smileyface = new ImageIcon("smiley.png"); //regular smiley icon
    private static ImageIcon smileydead = new ImageIcon("deadsmiley.png");//smiley lose icon
    private static ImageIcon displayZero = new ImageIcon("displayzero.png");//digit icons for timer and mine counter
    private static ImageIcon displayOne = new ImageIcon("displayOne.png");
    private static ImageIcon displayTwo = new ImageIcon("displayTwo.png");
    private static ImageIcon displayThree = new ImageIcon("displayThree.png");
    private static ImageIcon displayFour = new ImageIcon("displayFour.png");
    private static ImageIcon displayFive = new ImageIcon("displayFive.png");
    private static ImageIcon displaySix = new ImageIcon("displaySix.png");
    private static ImageIcon displaySeven = new ImageIcon("displaySeven.png");
    private static ImageIcon displayEight = new ImageIcon("displayEight.png");
    private static ImageIcon displayNine = new ImageIcon("displayNine.png");
    private static ImageIcon negative = new ImageIcon("negative.png");
    private static final JLabel timerHundreds = new JLabel(displayZero);//labels for digits of mine counter and timer
    private static final JLabel timerTens = new JLabel(displayZero);
    private static final JLabel timerOnes = new JLabel(displayZero);
    private static final JLabel mineHundreds = new JLabel(displayZero);
    private static final JLabel mineTens = new JLabel(displayZero);
    private static final JLabel mineOnes = new JLabel(displayZero);
    private static int seconds = 0; //seconds counter
    private static boolean timerStarted = false;
    Timer mineTimer = new Timer(true); //Timer object to update timer

    public Minesweeper(String name) {
        super(name);
        setResizable(false);
    }

    /*
     * Adds add components to the panel
     */
    private void addComponentsToPane(final Container pane) {
        nFlags = nMines;
        final JPanel timer = new JPanel(); //timer at top of screen
        final JPanel minesCount = new JPanel(); //mine counter at top of screen

        timer.setLayout(new BorderLayout()); //setup timer display
        timer.add(timerHundreds, BorderLayout.WEST);
        timer.add(timerTens, BorderLayout.CENTER);
        timer.add(timerOnes, BorderLayout.EAST);

        minesCount.setLayout(new BorderLayout()); //setup mine counter display
        minesCount.add(mineHundreds, BorderLayout.WEST);
        minesCount.add(mineTens, BorderLayout.CENTER);
        minesCount.add(mineOnes, BorderLayout.EAST);

        updateMineCount();//update display values
        updateTimer();

        final JPanel topPanel = new JPanel(); //setup panel for counters and smiley
        topPanel.setLayout(new BorderLayout());
        JPanel mineGrid = new JPanel();//setup panel for buttons
        mineGrid.setLayout(new GridLayout(length, width));

        buttons = new Minebutton[length][width]; //array to store all buttons in an easily-accessible way
        Minebutton b = new Minebutton(-1, -1);//temp button to help determine window size
        Dimension buttonSize = b.getMinimumSize();
        topPanel.setPreferredSize(new Dimension(50, 30)); //setup top panel size
        //determined by trial and error
        for (int x = 0; x < length; x++)//loop through and create each button
        {
            for (int y = 0; y < width; y++) {
                buttons[x][y] = new Minebutton(x, y);
                buttons[x][y].setBorder(null);//so the buttons show up as minesweeper squares
                buttons[x][y].setSize(17, 17);
                buttons[x][y].setOpaque(false);
                buttons[x][y].addMouseListener(new MouseAdapter()//Listener to determine when clicked
                {
                    //listen for mouse pressed and act accordingly
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Minebutton current = (Minebutton) e.getSource();//minebutton event is from
                        if (SwingUtilities.isLeftMouseButton(e))//if left mouse click
                        {
                            if (inprogress == true) {
                                if (!timerStarted)//start timer if not yet started
                                {
                                    timerStarted = true;
                                    mineTimer.scheduleAtFixedRate(new updateTimerTask(), 1000, 1000);//start timer updater
                                }
                                if (current.isCovered() && !(current.isFlagged()))//if button is not covered or is flagged do nothing
                                {
                                    if (current.isMine())//if it's a mine user loses
                                    {
                                        lose();
                                        current.uncover();
                                    } else {
                                        current.uncover();
                                        if (current.getMineCount() == 0) {
                                            cascade(current);//cascade through emptys, described below
                                        }
                                        if (hasWon()) //if only squares left are mines user wins
                                        {
                                            win();
                                        }

                                    }
                                }
                            }
                        } else if (SwingUtilities.isRightMouseButton(e))//if right mouse clicked
                        {
                            if (inprogress == true) //do nothing if between games
                            {
                                if (current.isCovered()) {
                                    current.rightClick();
                                    if (current.isFlagged()) {
                                        nFlags--;
                                    } else {
                                        nFlags++;
                                    }
                                    updateMineCount();
                                }
                            }
                        }
                    }
                });
                mineGrid.add(buttons[x][y]);//add mine squares to grid
            }
        }
        placeMines();//add mines to the grid
        countMines();//add mine counts to each square


        //Add listeners to menu to start new game, exit game or show info
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        gameMenu.add(newGame); //build menu
        gameMenu.add(exit);
        helpMenu.add(about);
        topMenu.add(gameMenu);
        topMenu.add(helpMenu);
        topMenu.setVisible(true);
        this.setJMenuBar(topMenu);
        smiley.setIcon(smileyface); //create "smiley" button and add listener
        smiley.setBorder(null);//set smiley to only show icon
        smiley.setMaximumSize(new Dimension(smileyface.getIconHeight(), smileyface.getIconWidth()));
        smiley.setOpaque(false);
        smiley.setContentAreaFilled(false);
        smiley.setBorderPainted(false);
        smiley.addActionListener(new ActionListener()//new game on smiley click
        {
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
        topPanel.add(smiley, BorderLayout.CENTER);//build top panel using BorderLayout
        topPanel.add(timer, BorderLayout.EAST);
        topPanel.add(minesCount, BorderLayout.WEST);
        pane.add(topPanel, BorderLayout.NORTH);//add top panel, separator and mine grid to frame
        pane.add(new JSeparator(), BorderLayout.CENTER);
        pane.add(mineGrid, BorderLayout.SOUTH);
        inprogress = true; //start game
    }

    private class updateTimerTask extends TimerTask //Task to keep timer updating
    {

        public void run() {
            seconds += 1;
            updateTimer();
        }
    }

    private static void updateTimer() //update timer graphically
    {
        char hundreds, tens, ones;
        if (seconds < 999) {
            hundreds = (char) (((seconds / 100) % 10) + 30);
            tens = (char) (((seconds / 10) % 10) + 30);
            ones = (char) ((seconds % 10) + 30);
        } else {
            hundreds = tens = ones = '9';
        }

        setDisplayLabel(timerHundreds, hundreds);
        setDisplayLabel(timerTens, tens);
        setDisplayLabel(timerOnes, ones);
    }

    private static void updateMineCount() //set mine counter graphically
    {
        if (nFlags >= 0) {
            char hundreds = (char) (((nFlags / 100) % 10) + 30);
            char tens = (char) (((nFlags / 10) % 10) + 30);
            char ones = (char) ((nFlags % 10) + 30);
            setDisplayLabel(mineHundreds, hundreds);
            setDisplayLabel(mineTens, tens);
            setDisplayLabel(mineOnes, ones);
        } else {
            int tmpFlags = nFlags * -1;
            char tens = (char) (((tmpFlags / 10) % 10) + 30);
            char ones = (char) ((tmpFlags % 10) + 30);
            mineHundreds.setIcon(negative);
            setDisplayLabel(mineTens, tens);
            setDisplayLabel(mineOnes, ones);
        }

    }

    private static void setDisplayLabel(JLabel label, char num)//used by updateMines and updateTimer to determine icons for labels
    {
        switch (num) {//this seems weird but is determining label based on ASCII value
            case 30:
                label.setIcon(displayZero);
                break;
            case 31:
                label.setIcon(displayOne);
                break;
            case 32:
                label.setIcon(displayTwo);
                break;
            case 33:
                label.setIcon(displayThree);
                break;
            case 34:
                label.setIcon(displayFour);
                break;
            case 35:
                label.setIcon(displayFive);
                break;
            case 36:
                label.setIcon(displaySix);
                break;
            case 37:
                label.setIcon(displaySeven);
                break;
            case 38:
                label.setIcon(displayEight);
                break;
            default://default to label 9
                label.setIcon(displayNine);
                break;

        }
    }

    private void showAbout()//show about dialog
    {
        JOptionPane.showMessageDialog(this, "Minesweeper for CS 335 by Elijah Jordan Montgomery\n<elijah.montgomery@uky.edu>\nVersion: " + versionString, "About Minesweeper", JOptionPane.INFORMATION_MESSAGE);
    }

    private void win()//stops game and timer upon user win
    {
        inprogress = false;
        mineTimer.cancel();
        smiley.setIcon(smileyglasses);
    }

    private void lose()//stops game and timer upon user lose
    {
        mineTimer.cancel();
        inprogress = false;
        smiley.setIcon(smileydead);//smiley has died :(
        for (int x = 0; x < length; x++)//display all hidden mines as grey mines
        {
            for (int y = 0; y < width; y++) {
                if (buttons[x][y].isMine() && buttons[x][y].isCovered()) {
                    buttons[x][y].setGreyMine();
                }
            }
        }
    }

    private boolean hasWon()//check if user has won - true if only squares left uncovered are mines
    {
        if (!(inprogress))//do nothing if game is not in progress
        {
            return false;
        }
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < width; y++) {
                if (!(buttons[x][y].isMine()) && buttons[x][y].isCovered()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Create the GUI and show it. For thread safety, this method is invoked
     * from the event dispatch thread.
     */
    private static void createAndShowGUI() { //create GUI and make frame visible
        //Create and set up the window.
        Minesweeper frame = new Minesweeper("MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void startNewGame() {//resets all objects to beginning of game states, redistributes mines and recalculates minecounts
        mineTimer.cancel();
        mineTimer = new Timer(false);
        nFlags = nMines;
        seconds = 0;
        updateTimer();
        updateMineCount();
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                buttons[x][y].resetProps();
            }
        }
        placeMines();
        countMines();
        smiley.setIcon(smileyface);
        timerStarted = false;
        inprogress = true;

    }

    private void cascade(Minebutton target) {
        /* Poor man's implementation of Microsoft's Cascade algorithm
         * Microsoft implementation described at http://www.techuser.net/minecascade.html
         * I couldn't get their exact algorithm to work right, so this one
         * recursively visits all adjacent squares cascading through and 
         * uncovering the blanks and edge squares
         */
        if (target.getVisited() == true || target.isFlagged())//do nothing if already visited this square
        {
            return;
        }
        target.visit();//mark visited
        target.uncover();//uncover
        int x = target.getXcoord();//get x and y coordinates to cascade through adjacent squares
        int y = target.getYcoord();
        if (target.getMineCount() == 0) {
            if ((x - 1) != -1) {
                if ((y - 1) != -1) {
                    if (!(buttons[x - 1][y - 1].isMine()) && !(buttons[x - 1][y - 1].getMineCount() == 0))//check button top-left
                    {

                        cascade(buttons[x - 1][y - 1]);
                    }
                }
                if (!(buttons[x - 1][y].isMine()))//check button mid-left
                {
                    cascade(buttons[x - 1][y]);
                }
                if (y + 1 < width) {
                    if (!(buttons[x - 1][y + 1].isMine()) && !(buttons[x - 1][y + 1].getMineCount() == 0))//check button bottom left
                    {
                        cascade(buttons[x - 1][y + 1]);
                    }
                }

            }
            if ((y - 1) != -1) {
                if (!(buttons[x][y - 1].isMine()))//check button top-middle
                {
                    cascade(buttons[x][y - 1]);
                }
                if ((x + 1) < length) {
                    if (!(buttons[x + 1][y - 1].isMine()) && !(buttons[x + 1][y - 1].getMineCount() == 0))//check button top-right
                    {
                        cascade(buttons[x + 1][y - 1]);
                    }
                }
            }
            if ((x + 1) < length) {
                if (!(buttons[x + 1][y].isMine()))//check button mid-right
                {
                    cascade(buttons[x + 1][y]);
                }
                if (y + 1 < width) {
                    if (!(buttons[x + 1][y + 1].isMine()) && !(buttons[x + 1][y + 1].getMineCount() == 0))//check button bottom-right
                    {
                        cascade(buttons[x + 1][y + 1]);
                    }
                }
            }
            if ((y + 1) < width) {
                if (!(buttons[x][y + 1].isMine())) //check button bottom-middle
                {
                    cascade(buttons[x][y + 1]);
                }
            }
        }
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        System.out.print("Please enter the length of the board: ");
        length = userInput.nextInt();
        if (length < 1 || length >= 50) {
            System.out.print("Length must be greater than 0, less than 50");
            System.exit(1);
        }
        System.out.print("Please enter the width of the board: ");
        width = userInput.nextInt();
        if (width < 1 || width >= 50) {
            System.out.print("Width must be greater than 0, less than 50");
            System.exit(1);
        }
        System.out.print("Please enter the number of mines on the board: ");
        nMines = userInput.nextInt();
        if (nMines < 1 || nMines >= (width * length)) {
            System.out.print("Number of mines must be greater than 0, less than width * length");
            System.exit(1);
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void countMines() {
        //function to set minecounts of squares
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < width; y++) {
                int nmines = 0;
                if ((x - 1) != -1) {
                    if ((y - 1) != -1) {
                        if (buttons[x - 1][y - 1].isMine())//check button top-left
                        {
                            nmines++;
                        }
                    }
                    if (buttons[x - 1][y].isMine())//check button mid-left
                    {
                        nmines++;
                    }
                    if (y + 1 < width) {
                        if (buttons[x - 1][y + 1].isMine())//check button bottom left
                        {
                            nmines++;
                        }
                    }

                }
                if ((y - 1) != -1) {
                    if (buttons[x][y - 1].isMine())//check button top-middle
                    {
                        nmines++;
                    }
                    if ((x + 1) < length) {
                        if (buttons[x + 1][y - 1].isMine())//check button top-right
                        {
                            nmines++;
                        }
                    }
                }
                if ((x + 1) < length) {
                    if (buttons[x + 1][y].isMine())//check button mid-right
                    {
                        nmines++;
                    }
                    if (y + 1 < width) {
                        if (buttons[x + 1][y + 1].isMine())//check button bottom-right
                        {
                            nmines++;
                        }
                    }
                }
                if ((y + 1) < width) {
                    if (buttons[x][y + 1].isMine()) //check mid-bottom
                    {
                        nmines++;
                    }
                }
                buttons[x][y].setCount(nmines);
            }
        }
    }

    private void placeMines() {
        //Randomly place nMines mines on board
        int ctr = 0;
        while (ctr != nMines) {
            int tempx = randnums.nextInt(length);
            int tempy = randnums.nextInt(width);
            if (!buttons[tempx][tempy].isMine()) //keep looping if random selection is already a mine
            {
                buttons[tempx][tempy].makeMine();
                ctr++;
            }
        }
    }
}
