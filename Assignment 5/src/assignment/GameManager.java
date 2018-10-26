package assignment;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class GameManager implements BoggleGame {
	
	ArrayList<String> cubes = new ArrayList<String>();
	char[][] board;
	ArrayList<ArrayList<String>> playerLists;
	GameDictionary dictionary = new GameDictionary();
	//Iterator<String> itr = GameDictionary.iterator();
	SearchTactic currentSearchTactic = BoggleGame.SEARCH_DEFAULT;
	int [] scores;
	String lastAddedWord;
	
	@Override
	public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {
		
		board = new char[size][size];
		
		//read from cubes.txt into a 2D array
		Scanner scan = new Scanner(new File (cubeFile));
		while (scan.hasNextLine()) {
			cubes.add(scan.nextLine());
		}
		
		//put random pieces on the board - cubes should be empty after this
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				int pos = (int) (Math.random() * cubes.size());
				board[i][j] = cubes.get(pos).charAt((int) (Math.random() * 6));
				cubes.remove(pos);
			}
		}	
		
		scores = new int[numPlayers];
		playerLists = new ArrayList<ArrayList<String>>(numPlayers);
		dictionary = (GameDictionary) dict;
	}

	@Override
	public char[][] getBoard() {
		return board;
	}

	@Override
	public int addWord(String word, int player) {
		//1. check if word is on the board - using getAllWords
		//2. if word is valid, need to check if been used before
		//3. if still good, then score it - 1 point for 4 letters, 2 points for 5 etc.
		//add word to player list if valid
		Collection<String> allWords = getAllWords();
		for (int i = 0; i < allWords.size(); i++) {
			 if (allWords.contains(word)) {
				 for (int j = 0; j < playerLists.get(player - 1).size(); j++) {
					// if ()
				 }
				 
			 }
			 
		}
		
//		if (valid) {
//			playerList
//		}
		return 0;
	}

	@Override
	public List<Point> getLastAddedWord() {
		
		// check if word is in getAllWords
			
		return null;
	}

	@Override
	public void setGame(char[][] board) {
		this.board = board;		//check this - do we need a for loop?
		//set player lists to empty
		//set scores to empty
		
	}

	@Override
	public Collection<String> getAllWords() {
		// search through board using currentSearchTactic 
		
		ArrayList<String> allWords = new ArrayList<String>();
		Trie words = dictionary.getDictionary();
		boolean [][]visited = new boolean[board.length][board[0].length];
		int r = 0; 
		int c = 0;
		String boardWord = "";
		boolean foundWord = false;
		String currentWord = "";
		Iterator <String> iteratorWords = words.iterator();
		
		if (currentSearchTactic == SearchTactic.SEARCH_BOARD) {
			
			while (iteratorWords.hasNext()) {
				currentWord = iteratorWords.next();
				
				//resetting all variables for next word
				r = 0; 
				c = 0;
				boardWord = "";
				foundWord = false;
				
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board.length; j++) {
						visited[i][j] = false;
					}
				}
				
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board.length; j++) {
						if (Character.toLowerCase(board[i][j]) == Character.toLowerCase(currentWord.charAt(0))) {
							foundWord |= searchBoard(board, visited, i, j, boardWord, currentWord, 0);
						}
					}
				}
				
				if (foundWord) {
					allWords.add(currentWord);
				}
			}
		}
		else if (currentSearchTactic == SearchTactic.SEARCH_DICT) {
			
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					allWords = searchDictionary(words, board, visited, i, j, boardWord, allWords);
				}
			}
			
			
		}
		return allWords;
	}

	@Override
	public void setSearchTactic(SearchTactic tactic) {
		currentSearchTactic = tactic;
		
	}

	@Override
	public int[] getScores() {
		return scores;
	}
	
	//gets all words from dictionary and checks if in board
	public boolean searchBoard(char [][] board, boolean [][] visited, int r, int c, String boardWord, String currentWord, int letterPos) {
		char letter = ' ';
		boolean wordFound = false;
		if(letterPos < currentWord.length())
			letter = Character.toLowerCase(currentWord.charAt(letterPos));
		
		if (currentWord.toLowerCase().equals(boardWord.toLowerCase())) {
			return true;
		}
		
		for(int i = r - 1; i <= r + 1; i++) {
			for(int j = c - 1; j <= c + 1; j++) {
				if(i >= 0 && j >= 0 && i < board.length && j < board[0].length && !visited[i][j]) {
					if(letter == Character.toLowerCase(board[i][j])) {
						r = i; 
						c = j;
						visited[i][j] = true;
						wordFound |= searchBoard(board, visited, r, c, boardWord + letter, currentWord, letterPos + 1);
						visited[i][j] = false;
					}
				}
			}
		}
		return wordFound;
	}
	
	//gets all words from board and checks if in dictionary
	public ArrayList<String> searchDictionary(Trie words, char [][] board, boolean [][] visited, int r, int c, String word, ArrayList<String> allWords) {
		//traverse through dictionary, check if each word is on board
		//if on board, add to list
		
		visited[r][c] = true;
		word = word + Character.toLowerCase(board[r][c]);
		
		if(words.search(word))
			allWords.add(word);
		
		for(int i = r - 1; i <= r + 1; i++) {
			for(int j = c - 1; j <= c + 1; j++) {
				if(i >= 0 && j >= 0 && i < board.length && j < board[0].length && !visited[i][j])
					searchDictionary(words, board, visited, i, j, word.toLowerCase(), allWords);
			}
		}
		
		word = word.substring(0, word.length()-1);
		visited[r][c] = false;
		return allWords;
	}
	
	public void print() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				System.out.print(board[i][j] + "| ");
			}
			System.out.println();
		}
	}
	
	//errors: size of allWords is 0
	
	//never recursively calls function if first letter is not first letter of board

}
