package assignment;
//test github

import java.awt.Point;
import java.io.IOException;
import java.util.*;

import assignment.BoggleGame.SearchTactic;

public class Boggle {

	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		GameManager manager = new GameManager ();
		GameDictionary dictionary = new GameDictionary();
		dictionary.loadDictionary("words.txt");
		
		
		System.out.println("Welcome to Boggle!");
		System.out.println();
		System.out.println("Please enter the size of board: ");
		int sizeOfBoard = scan.nextInt();
		System.out.println("Please enter the number of players: ");
		int numPlayers = scan.nextInt();
		System.out.println();
		
		scan.nextLine();
		manager.newGame(sizeOfBoard, numPlayers, "cubes.txt", dictionary);
		ArrayList<String> allWords = new ArrayList<String> (manager.getAllWords());
		
		System.out.println("Please pick a search tactic: a. Search Board b. Search Dictionary");
		String tactic = scan.nextLine();
		if(tactic.toLowerCase().equals("a"))
			manager.setSearchTactic(SearchTactic.SEARCH_BOARD);
		else if(tactic.toLowerCase().equals("b"))
			manager.setSearchTactic(SearchTactic.SEARCH_DICT);
		else
			System.out.println("Invalid Answer :(");
		
		
		System.out.println("Please enter player number");
		int playerNum = scan.nextInt() - 1;
		scan.nextLine();
		
		manager.print();
		
		boolean keepGoing = true;
		int points = 0;
		while (keepGoing) {
			System.out.println("Please enter a word on the board: ");
			String userEnteredWord = scan.nextLine();
			
			points += manager.addWord(userEnteredWord, playerNum);
			System.out.println("You have "+points+" points");
			//ArrayList<String> allWords = new ArrayList<String> (manager.getAllWords());
//			for(String s:allWords)
//				System.out.println(s);
//			System.out.println(allWords.size());
			
			//System.out.println(userEnteredWord);
//			if(allWords.contains(userEnteredWord.toLowerCase())) {
//				System.out.println("Correct!");
//				List<Point> coordinates = manager.getLastAddedWord();
//				char[][] board = manager.getBoard();
//				for (Point p: coordinates) {
//					Character.toLowerCase(board[(int) p.getX()][(int) p.getY()]);
//				}
//			}
//			else {
//				System.out.println("This word was not on the board");
//			}
			
			System.out.println("Do you want to keep playing? Enter y or n.");
			String repeat = scan.nextLine();
			if (repeat.toLowerCase().equals("n")) {
				keepGoing = false;
				//return score - figure out how to write the method
				System.out.println("Here are all the words you missed!");
				for (int i = 0; i < allWords.size(); i++) {
					if (!(manager.playerLists.get(playerNum).contains(allWords.get(i)))) {
						System.out.println(allWords.get(i));
					}
				}
				System.out.println("Goodbye!");
			}
		}
	}

}
