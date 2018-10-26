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
		String currentWord;
		
		if (currentSearchTactic == SearchTactic.SEARCH_BOARD) {
			
			while (words.iterator().hasNext()) {
				currentWord = words.iterator().next();
				
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
						if (board[i][j] == currentWord.charAt(0)) {
							foundWord = searchBoard(board, visited, r, c, boardWord, currentWord, 0);
							
						}
					}
				}
				
				if (foundWord) {
					allWords.add(currentWord);
				}
			}
			
//			String word = words.iterator().next();
//			boolean firstCase = true;
//			allWords = searchBoard(board, visited, r, c, boardWord, currentWord, letterPos);
//			for (int i = 0; i < allWords.size(); i++) {
//				System.out.println("j" + allWords.get(i));
//			}
		}
		else if (currentSearchTactic == SearchTactic.SEARCH_DICT) {
			allWords = searchDictionary(words, board, visited, r, c, boardWord, allWords);
		}
		
		//return list
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
		if(letterPos < currentWord.length())
			letter = currentWord.charAt(letterPos);
		
		if (currentWord.equals(boardWord)) {
			System.out.println("found word!");
			return true;
		}
		
		for(int i = r - 1; i < r + 1; i++) {
			for(int j = c - 1; j < c + 1; j++) {
				if(i >= 0 && j >= 0 && i < board.length && j < board[0].length && !visited[i][j]) {
					if(letter == board[i][j]) {
						System.out.println("got next letter");
						boardWord+=letter;
						r = i; 
						c = j;
						letterPos++;
						visited[i][j] = true;
						searchBoard(board, visited, r, c, boardWord, currentWord, letterPos);
					}
				}
			}
		}
		System.out.println("before false");
		return false;
		
//		char letter;
//		if(letterPos < currentWord.length())
//			letter = currentWord.charAt(letterPos);
//		//int boardCount = 0;
//		System.out.println(currentWord);
//		System.out.println(boardWord);
//		
//		if (currentWord.equals(boardWord)) {
//			System.out.println("sfdhtzs");
//			allWords.add(currentWord);
//			if(words.iterator().hasNext()) {
//				currentWord = words.iterator().next();
//				letter = currentWord.charAt(0);
//			}
//		}
//		
//		if(boardCount == (board.length * board.length)) {
//			if (words.hasNext())
//				currentWord = words.next();
//			else {
//				System.out.println("No words on the board");
//			}
//		}
//		
//		//ERROR: boardWord is not updating - not calling the recursion, only exiting after first loop
//		
//		if (firstCase) {
//			int count = 0;
//			for (int i = 0; i < board.length; i++ ) {
//				for (int j = 0; j < board.length; j++) {
//					System.out.println(board[i][j]);
//					System.out.println(letter);
//					if (Character.toLowerCase(board[i][j]) == letter) {
//						System.out.println("entered first case");
//						firstCase = false;
//						boardWord += letter;
//						letter = currentWord.charAt(1);
//						visited[i][j] = true;
//						r = i; 
//						c = j;
//						letterPos++;
//						searchBoard(words, board, visited, r, c, boardWord, currentWord, allWords, letterPos, firstCase);
//					}
//					count++;
//				}
//			}
//			if(count == (board.length * board.length)) {
//				if (words.iterator().hasNext()) {
//					currentWord = words.iterator().next();
//					searchBoard(words, board, visited, r, c, boardWord, currentWord, allWords, letterPos, firstCase);
//				}
//				else {
//					System.out.println("No words on the board");
//				}
//			}
//		}
//		else {
//			System.out.println("entered else - not first case");
//			for(int i = r - 1; i < r + 1; i++) {
//				for(int j = c - 1; j < c + 1; j++) {
//					if(i >= 0 && j >= 0 && i < board.length && j < board[0].length && !visited[i][j] && letter == board[i][j]) {
//						System.out.println("got next letter");
//						boardWord+=letter;
//						r = i; 
//						c = j;
//						letterPos++;
//						visited[i][j] = true;
//						searchBoard(words, board, visited, r, c, boardWord, currentWord, allWords, letterPos, firstCase);
//					}
//				}
//			}
//		}
//		
//		visited[r][c] = false;
//		return allWords;
		
		//edge cases to fix: if loops through entire dictionary and board has no valid words
		//if finds first letter, but can't find consecutive letters, should move to next word
	}
	
	//gets all words from board and checks if in dictionary
	public ArrayList<String> searchDictionary(Trie words, char [][] board, boolean [][] visited, int r, int c, String word, ArrayList<String> allWords) {
		//traverse through dictionary, check if each word is on board
		//if on board, add to list
		
		visited[r][c] = true;
		word = word + board[r][c];
		
		if(words.search(word))
			allWords.add(word);
		
		for(int i = r - 1; i < r + 1; i++) {
			for(int j = c - 1; j < c + 1; j++) {
				if(i >= 0 && j >= 0 && i < board.length && j < board[0].length && !visited[i][j])
					searchDictionary(words, board, visited, r, c, word, allWords);
			}
		}
		
		word = word.substring(0, word.length()-2);
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
