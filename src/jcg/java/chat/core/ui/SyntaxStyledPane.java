package jcg.java.chat.core.ui;

import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * Created on July 28 2018
 *
 */
public class SyntaxStyledPane {

	JTextPane pane;
	StyledDocument styledDocument;
	
	ArrayList<Keyword> keys;
	
	public SyntaxStyledPane() {
		keys = new ArrayList<Keyword>();
		pane = new JTextPane();
		pane.setEditable(true);
		pane.setText("word");
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
	
	// Find the keys within the string
	public void insertString(String text) throws BadLocationException {
		text += '\n';
		pane.setCaretPosition(pane.getText().length() - 1);
		String split[] = text.split(" ");
		
		
		
		// Super inefficient but don't know a better solution yet
		for(int i = 0; i < split.length; i++) {
			boolean printed = false;
			for(Keyword k : keys) {
				if(split[i].trim() == k.getKeyword().trim()) {	// Shouldn't have duplicate keywords so exit on the first occurrence
					styledDocument.insertString(styledDocument.getLength(), split[i].trim() + " ", k.getSimpleAttributeSet());
					printed = true;
					break;
				} 
			}
			if(printed == false) {
				styledDocument.insertString(styledDocument.getLength(), split[i].trim() + " ", null);
			}
		}
			
	}
	
}
