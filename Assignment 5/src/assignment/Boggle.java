package assignment;

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
		if (sizeOfBoard > 4) {
			System.out.println("Size of board cannot be greater than 4. Please re-enter size.");
			sizeOfBoard = scan.nextInt();
		}
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
			
			if (tactic.toLowerCase().equals("b")) {
				System.out.println("Please wait a few seconds while we evaluate your word.");
			}
			
			points = manager.addWord(userEnteredWord, playerNum);
			System.out.println("You have "+points+" point(s)");
			
			manager.print();
			
			System.out.println("Do you want to continue your turn? Enter y or n.");
			String repeat = scan.nextLine();
			while (!(repeat.toLowerCase().equals("n") || repeat.toLowerCase().equals("y"))) {
				System.out.println("Please enter y or n");
				repeat = scan.nextLine();
			}
			if (repeat.toLowerCase().equals("n")) {
				System.out.println("Enter q to quit or s to switch players.");
				String choice = scan.nextLine();
				
				if (choice.equals("q")) {
					keepGoing = false;
					//return score - figure out how to write the method
					System.out.println("Here are all the words you missed!");
					for (int i = 0; i < allWords.size(); i++) {
						if (!(manager.playerLists.get(playerNum).contains(allWords.get(i))) && (allWords.get(i).length() > 3)) {
							System.out.println(allWords.get(i));
						}
					}
					System.out.println("Goodbye!");
				}
				
				if (choice.equals("s")) {
					System.out.println("Please enter player number");
					playerNum = scan.nextInt() - 1;
					scan.nextLine();
					points = 0;
					manager.print(playerNum);
				}
			}
		}
	}
//fix lowercase on board - not picking the right letter if repeated letters
}
