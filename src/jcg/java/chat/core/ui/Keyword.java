package jcg.java.chat.core.ui;

import javax.swing.text.SimpleAttributeSet;

public class Keyword {
	
	private String keyword;
	private SimpleAttributeSet set;

	public Keyword(String keyword, SimpleAttributeSet set) {
		this.keyword = keyword;
		this.set = set;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public SimpleAttributeSet getSimpleAttributeSet() {
		return set;
	}
	
}
