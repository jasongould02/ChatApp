package jcg.java.chat.core.client;

import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * Created on July 28 2018
 */
public class SyntaxStyledPane {

	JTextPane pane;
	StyledDocument styledDocument;
	
	ArrayList<Keyword> keys;
	
	public SyntaxStyledPane() {
		keys = new ArrayList<Keyword>();
		pane = new JTextPane();
		pane.setEditable(false);
		styledDocument = pane.getStyledDocument();
	}
	
	public JTextPane getJTextPane() {
		return pane;
	}
	
	public void addKeyword(Keyword newKeyword) {
		if(newKeyword != null) {
			keys.add(newKeyword);
		}
	}
	
	public StyledDocument getStyledDocument() {
		return styledDocument;
	}
	
	
	private void parseString(String string) {
		int amtOpen = 0;
		int positionOpened = 0;
		int positionClosed = 0;
		
		String posOpened = "";
		String posClosed = "";
		char[] s = string.trim().toCharArray();
		char prev = 0, cur = 0, next = 0;
		
		for(int i = 0; i < s.length; i++) {
			if((i-1) >= 0) { prev = s[i-1]; } else  { prev = 0; }
			if((i+1) > s.length) { next = s[i+1]; } else { next = 0; }
			cur = s[i];
			
			if(cur == '<') {
				//positionOpened = i;
				amtOpen += 1;
			} else if(cur == '>' && prev == '/') {
				//positionClosed = i;
				amtOpen -= 1;
				
				if(amtOpen >= 1) {
					// keep going on the loop until it is all closed;
				} else {
					break;
				}
			}
			
		}
		
	}
	
	
	// Find the keys within the string
	public void println(String text) throws BadLocationException {
		text += '\n';
		String split[] = text.split(" ");
		
		// Super inefficient but don't know a better solution yet
		for(int i = 0; i < split.length; i++) {
			boolean printed = false;
			for(Keyword k : keys) {
				if(split[i].trim().equals(k.getKeyword().trim())) {	// Shouldn't have duplicate keywords so exit on the first occurrence
					if(i == split.length - 1) {
						styledDocument.insertString(styledDocument.getLength(), split[i].trim() + "\n", k.getSimpleAttributeSet());
					} else {
						styledDocument.insertString(styledDocument.getLength(), split[i].trim() + " ", k.getSimpleAttributeSet());
					}
					
					printed = true;
					break;
				} 
			}
			if(printed == false) {
				if(i == split.length - 1) {
					styledDocument.insertString(styledDocument.getLength(), split[i].trim() + "\n", null);
				} else {
					styledDocument.insertString(styledDocument.getLength(), split[i].trim() + " ", null);
				}
			}
		}
			
	}
	
}
