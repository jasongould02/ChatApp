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
	
	private void moveCaretToEnd() {
		if(pane.getText().length() != 0) {
			pane.setCaretPosition(pane.getText().length() - 1);
//			styledDocument.getEndPosition()
		} else {
			pane.setCaretPosition(pane.getText().length());
		}
	}
	
	// Find the keys within the string
	public void println(String text) throws BadLocationException {
		text += '\n';
		//moveCaretToEnd();
		String split[] = text.split(" ");
		
		
		
		// Super inefficient but don't know a better solution yet
		for(int i = 0; i < split.length; i++) {
			boolean printed = false;
			for(Keyword k : keys) {
				if(split[i].contains(k.getKeyword().trim())) {	// Shouldn't have duplicate keywords so exit on the first occurrence
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
			//moveCaretToEnd();
		}
			
	}
	
}
