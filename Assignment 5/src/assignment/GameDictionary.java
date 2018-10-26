package assignment;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class GameDictionary implements BoggleDictionary {

	Trie dictionary = new Trie();
	
	@Override
	public Iterator<String> iterator() {
		return dictionary.iterator();
	}

	@Override
	public void loadDictionary(String filename) throws IOException {
		//call insert method for trie for each word in words.txt
		Scanner scan = new Scanner(new File (filename));
		while (scan.hasNextLine()) {
			dictionary.insert(scan.nextLine());
		}
	}

	@Override
	public boolean isPrefix(String prefix) {
		//still use search with trie and check one node above to see if prefix
		return dictionary.startsWith(prefix);
	}

	@Override
	public boolean contains(String word) {
		//use search algorithm with trie
		return dictionary.search(word);
	}
	
	public Trie getDictionary() {
		return dictionary;
	}
}
