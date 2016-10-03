package src.chess;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;


/**
 * This is the Player Class
 * It provides the functionality of keeping track of all the users
 * Objects of this class is updated and written in the Game's Data Files after every Game
 *
 */
public class Player implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer gamesplayed;
	private Integer gameswon;
	
	//Constructor
	private Player(String name)
	{
		this.name = name.trim();
		//this.lname = lname.trim();
		gamesplayed = new Integer(0);
		gameswon = new Integer(0);
	}
	
	//Name Getter
	public String name()
	{
		return name;
	}
	
	//Returns the number of games played
	public Integer gamesplayed()
	{
		return gamesplayed;
	}
	
	//Returns the number of games won
	public Integer gameswon()
	{
		return gameswon;
	}
	
	//Calculates the win percentage of the player
	public Integer winpercent()
	{
		return new Integer((gameswon*100)/gamesplayed);
	}
	
	//Increments the number of games played
	public void updateGamesPlayed()
	{
		gamesplayed++;
	}
	
	//Increments the number of games won
	public void updateGamesWon()
	{
		gameswon++;
	}
	
	
	public static ArrayList<Player> fetch_players()         //Function to fetch the list of the players
	{
		Player tempplayer;
		ObjectInputStream input = null;
		ArrayList<Player> players = new ArrayList<Player>();
		try
		{
			File infile = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
			input = new ObjectInputStream(new FileInputStream(infile));
			try
			{
				while(true)
				{
					tempplayer = (Player) input.readObject();
					players.add(tempplayer);
				}
			}
			catch(EOFException e)
			{
				input.close();
			}
		}
		catch (FileNotFoundException e)
		{
			players.clear();
			return players;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			try {input.close();} catch (IOException e1) {}
			JOptionPane.showMessageDialog(null, "Unable to read the required Game files !!");
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click Ok to Continue Builing New File");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return players;
	}
	
	public static Player createPlayer(String name) {
		return new Player(name);
	}

	public void Update_Player()            //Function to update the statistics of a player
	{

		File inputfile=null;
		File outputfile=null;
		String input_file_name = System.getProperty("user.dir")+ File.separator + "chessgamedata.dat";
		String output_file_name = System.getProperty("user.dir")+ File.separator + "tempfile.dat";
		try
		{
			inputfile = new File(input_file_name);
			outputfile = new File(output_file_name);
		} catch (SecurityException e)
		{
			JOptionPane.showMessageDialog(null, "Read-Write Permission Denied !! Program Cannot Start");
			System.exit(0);
		} 
		try
		{
			file_the_player_data(inputfile, outputfile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to read/write the required Game files !! Press ok to continue");
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click Ok to Continue Builing New File");
		}
		catch (Exception e)
		{
			
		}
	}

	public void file_the_player_data(File inputfile, File outputfile)
			throws IOException, FileNotFoundException, ClassNotFoundException {
		ObjectInputStream input;
		ObjectOutputStream output;
		Player temp_player;
		boolean playerexists;
		if(!outputfile.exists())
			outputfile.createNewFile();
		if(!inputfile.exists())
		{
				output = new ObjectOutputStream(new java.io.FileOutputStream(outputfile,true));
				output.writeObject(this);
		}
		else
		{
			input = new ObjectInputStream(new FileInputStream(inputfile));
			output = new ObjectOutputStream(new FileOutputStream(outputfile));
			playerexists = false;
			try
			{
				while(true)
				{
					temp_player = (Player)input.readObject();
					if (temp_player.name().equals(name()))
					{
						output.writeObject(this);
						playerexists = true;
					}
					else
					{
						output.writeObject(temp_player);
					}
				}
			}
			catch(EOFException e){
				input.close();
			}
			if(!playerexists)
			{
				output.writeObject(this);
			}
		}
		inputfile.delete();
		output.close();
		File newf = new File(System.getProperty("user.dir")+ File.separator + "chessgamedata.dat");
		if(!outputfile.renameTo(newf))
		{
			System.out.println("File Renameing Unsuccessful");
		}
	}
}
