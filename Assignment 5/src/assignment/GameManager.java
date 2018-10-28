package assignment;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GameManager implements BoggleGame {
	
	ArrayList<String> cubes = new ArrayList<String>();
	char[][] board;
	List<List<String>> playerLists;
	GameDictionary dictionary = new GameDictionary();
	SearchTactic currentSearchTactic = BoggleGame.SEARCH_DEFAULT;
	int [] scores;
	String lastAddedWord;
	HashMap<String, ArrayList<Point>> points = new HashMap<String, ArrayList<Point>>();
	
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
		playerLists = new ArrayList<List<String>>(numPlayers);
		for(int i = 0; i < numPlayers; i++)
			playerLists.add(new ArrayList<String>());
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
		//4. add word to player list if valid
		
		ArrayList<String> allWords = new ArrayList<String> (getAllWords());
		
		//if word is valid (in allWords) and has not already been added to playerList, and is longer than 4 characters, then add to playerList
		if(allWords.contains(word.toLowerCase()) && !(playerLists.get(player).contains(word.toLowerCase())) && word.length() >= 4) {
			playerLists.get(player).add(word);
			scores[player] += word.length()-3;
			lastAddedWord = word;
			List<Point> coordinates = getLastAddedWord();
			
			char[][] board = getBoard();
			for (Point p: coordinates) {
				Character.toLowerCase(board[(int) p.getX()][(int) p.getY()]);
			}
		}
		
		return scores[player];
	}

	@Override
	public List<Point> getLastAddedWord() {
		return points.get(lastAddedWord);
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
				ArrayList <Point> wordPoints = new ArrayList <Point>();
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
							foundWord |= searchBoard(board, visited, i, j, boardWord, currentWord, 0, wordPoints);
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
	public boolean searchBoard(char [][] board, boolean [][] visited, int r, int c, String boardWord, String currentWord, int letterPos, ArrayList <Point> wordPoints) {
		char letter = ' ';
		boolean wordFound = false;
		
		if(letterPos < currentWord.length())
			letter = Character.toLowerCase(currentWord.charAt(letterPos));
		
		if (currentWord.toLowerCase().equals(boardWord.toLowerCase())) {
			for(int i = wordPoints.size() - 1; i > currentWord.length(); i--) {
				wordPoints.remove(i);
			}
			
			points.put(currentWord, wordPoints);
			return true;
		}
		
		for(int i = r - 1; i <= r + 1; i++) {
			for(int j = c - 1; j <= c + 1; j++) {
				if(i >= 0 && j >= 0 && i < board.length && j < board[0].length && !visited[i][j]) {
					if(letter == Character.toLowerCase(board[i][j])) {
						r = i; 
						c = j;
						visited[i][j] = true;

						ArrayList <Point> temp = new ArrayList <Point>();
						for(Point p:wordPoints)
							temp.add(p);
						temp.add(new Point(j,i));
						
						wordFound |= searchBoard(board, visited, r, c, boardWord + letter, currentWord, letterPos + 1, temp);
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
	
	//clean this up
	public void print() {
		List<Point> coordinates = getLastAddedWord();
		
		//these are the same: coordinates and points.get(lastAddedWord)
		
		if(coordinates != null && !coordinates.isEmpty()) {
			for (Point p: coordinates) {
				board[(int) p.getY()][(int) p.getX()] = Character.toLowerCase(board[(int) p.getY()][(int) p.getX()]);
			}
		}
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				System.out.print(board[i][j] + "| ");
			}
			System.out.println();
		}
		
		if(coordinates != null && !coordinates.isEmpty()) {
			for (Point p: coordinates) {
				board[(int) p.getY()][(int) p.getX()] = Character.toUpperCase(board[(int) p.getY()][(int) p.getX()]);
			}
		}
	}
	
	public void print(int playerNum) {
		List<Point> coordinates = getLastAddedWord();
		
		if(coordinates != null && !coordinates.isEmpty()) {
			for (Point p: coordinates) {
				board[(int) p.getY()][(int) p.getX()] = Character.toUpperCase(board[(int) p.getY()][(int) p.getX()]);
			}
		}
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				System.out.print(board[i][j] + "| ");
			}
			System.out.println();
		}
	}
}
