/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;
import java.awt.*;
import javax.swing.*;
import java.lang.Class;
/**
 *
 * @author Jordan
 */
public class Minebutton extends JButton{
    private boolean btnHasMine = false; //if true, button contains a mine
    private int mineCount = 0; //Count of adjacent mines
    private boolean covered = true; //is true until player clicks the button
    private int x; 
    private int y;
    private boolean visited = false;
    private ImageIcon mine = new ImageIcon("mine.png"); // possible icons
    private ImageIcon flag = new ImageIcon("flagged.png");
    private ImageIcon one = new ImageIcon("one.png");
    private ImageIcon two = new ImageIcon("two.png");
    private ImageIcon three = new ImageIcon("three.png");
    private ImageIcon four = new ImageIcon("four.png");
    private ImageIcon five = new ImageIcon("five.png");
    private ImageIcon six = new ImageIcon("six.png");
    private ImageIcon seven = new ImageIcon("seven.png");
    private ImageIcon eight = new ImageIcon("eight.png");
    private ImageIcon blank = new ImageIcon("blank.png");
    private ImageIcon coveredIcon = new ImageIcon("covered.png");
    

    @Override
    protected int checkHorizontalKey(int i, String string) {
        return super.checkHorizontalKey(i, string);
    }
    private ImageIcon greymine = new ImageIcon("greymine.png");
    private boolean flagged = false; //true if flag is on button
    
    
    public void visit()//visit - used for Cascade() in Minesweeper.java
    {
        visited = true;
    }
    public void setGreyMine(){ //sets to grey icon - used when user loses
        this.setIcon(greymine);
    }
    public void resetProps()//resets properties to defaults
    {
        flagged = false;
        visited = false;
        btnHasMine = false;
        mineCount = 0;
        covered = true;
        this.setIcon(coveredIcon);
    }
    public boolean getVisited()//determine if visited - used for Cascade() in Minesweeper.java
    {
        return visited;
    }
    public void rightClick() // handles right click and toggles flagged vs covered icon
    {
        if(flagged)
        {
            flagged = false;
            this.setIcon(coveredIcon);
        }
        else
        {
            flagged = true;
            this.setIcon(flag);
        }
    }
    public boolean isFlagged()
    {
        return flagged;
    }
    public void unvisit()//unvisit - used for Cascade() in Minesweeper.java
    {
        visited = false;
    }
    public Minebutton(int newx, int newy)
    {
        super();
        x = newx;
        y = newy;
        this.setIcon(coveredIcon);
    }
    public void makeMine()//set square to be a mine
    {
        btnHasMine=true;
    }
    public int getXcoord()//x coord in grid
    {
        return x;
    }
    public int getYcoord()//y coord in grid
    {
        return y;
    }
    
    public int getMineCount() {
        return mineCount;
    }
    
    public boolean isCovered()//true if square is covered
    {
        return covered;
    }
    
    public boolean isMine(){//true if square is a mine
        return btnHasMine;
    }
    public void setCount(int count)//set mine count
    {
        mineCount = count;
    }
    public void uncover(){ //uncover and set new icon
        covered = false;
        if(flagged)
        {
            return;
        }
        if(btnHasMine)
        {
            this.setIcon(mine);
        }
        else
        {
            switch(mineCount) {
                case 0:
                    this.setIcon(blank);
                    break;
                case 1:
                    this.setIcon(one);
                    break;
                case 2:
                    this.setIcon(two);
                    break;
                case 3:
                    this.setIcon(three);
                    break;
                case 4:
                    this.setIcon(four);
                    break;
                case 5:
                    this.setIcon(five);
                    break;
                case 6:
                    this.setIcon(six);
                    break;
                case 7:
                    this.setIcon(seven);
                    break;
                case 8:
                    this.setIcon(eight);
                    break;
                
             
            }
        }
        
    }
}
