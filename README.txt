Minesweeper Program for CS 335 Program 1
by Elijah Jordan Montgomery
<elijah.montgomery@uky.edu>

Synopsis:
This program implements the popular Windows game "Minesweeper" in Java. This program demonstrates the use of various Java Swing GUI layouts and components

Class/file structure:
Minesweeper.java : Implements the core logic of the game and provides the Main() method. This program also sets up the majority of the GUI for the program.
Minebutton.java : Inherits from JButton and implements the minesweeper squares. Very little game logic is included in this class, this is mostly a utility class for displaying the buttons.
All image files are in the root of the netbeans project folder and should be left there as the application will not correctly run without them. This may however be changed in a future revision.

To compile/run: Unzip the zip file to some directory and open the entire directory with Netbeans. To run simply click Run

Notes: 
This project is made to be compiled in Netbeans and the directory structure is setup that way. Other IDEs are unsupported and may require modification to make work correctly.
The images from this project are based on images provided with the open-source Minesweeper clone "Minez" by Andrew Lim. No code or techniques are borrowed, but the images are very similar to the stock Windows Minesweeper(pre-Vista) images.

Bugs/Limitations: 
Buttons do not "depress" when clicked. The outcome is the same on left click as MS Minesweeper, but the initial click animation is different.
Occasionally a button will not work correctly. This happens very seldom, something like 1 in 20 games or so and I believe this to be a timing issue. The button is the correct size but never shows the icon correctly when the bug occurs.
