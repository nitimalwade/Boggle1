package assignment;

import java.util.ArrayList;
import java.util.Iterator;

class TrieNode {
	    TrieNode[] arr;
	    boolean isEnd;
	    // Initialize your data structure here.
	    public TrieNode() {
	        this.arr = new TrieNode[26];
	    }
	}
	 
	public class Trie {
	    private TrieNode root;
	    private ArrayList<String> iterateList = new ArrayList<String>();
	    int currentIndex = -1;
	    int count = 0;
	 
	    public Trie() {
	        root = new TrieNode();
	    }
	 
	    // Inserts a word into the trie.
	    public void insert(String word) {
	        TrieNode p = root;
	        iterateList.add(word);
	        for(int i=0; i<word.length(); i++){
	            char c = word.charAt(i);
	            int index = c-'a';
	            if(p.arr[index]==null){
	                TrieNode temp = new TrieNode();
	                p.arr[index]=temp;
	                p = temp;
	            }
	            else {
	                p=p.arr[index];
	            }
	        }
	        p.isEnd=true;
	    }
	 
	    // Returns if the word is in the trie.
	    public boolean search(String word) {
	        TrieNode p = searchNode(word);
	        if(p==null){
	            return false;
	        }
	        else {
	            if(p.isEnd)
	                return true;
	        }
	 
	        return false;
	    }
	 
	    // Returns if there is any word in the trie
	    // that starts with the given prefix.
	    public boolean startsWith(String prefix) {
	        TrieNode p = searchNode(prefix);
	        if(p==null) {
	            return false;
	        }
	        else {
	            return true;
	        }
	    }
	 
	    public TrieNode searchNode(String s){
	    	TrieNode p = root;
	        for(int i = 0; i < s.length(); i++){
	            char c = Character.toLowerCase(s.charAt(i));
	            int index = c-'a';
	            if(p.arr[index]!=null){
	                p = p.arr[index];
	            }
	            else{
	                return null;
	            }
	        }
	 
	        if(p==root)
	            return null;
	 
	        return p;
	    }

//		@Override
//		public boolean hasNext() {
//			if(iterateList.get(currentIndex + 1) != null)
//				return true;
//			return false;
//			
//			iterateList.iterator()
//		}
//
//		@Override
//		public String next() {
//			currentIndex++;
//			count++;
////			if(currentIndex>0) {
////				iterateList.remove(currentIndex-1);
////				//iterateList.remove(currentIndex-2);
////				//System.out.println(iterateList.size());
////			}
//			//System.out.println(count);
//			return iterateList.get(currentIndex);
//		}
//
//		@Override
//		public void remove() {
//			iterateList.remove(iterateList.size()-1);
//			
//		}
		
		public Iterator<String> iterator() {
			return iterateList.iterator();
		}
	}
