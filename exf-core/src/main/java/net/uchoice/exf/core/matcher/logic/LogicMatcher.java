package net.uchoice.exf.core.matcher.logic;

import net.uchoice.exf.model.matcher.MatchAble;

public abstract class LogicMatcher implements MatchAble {
	
	protected MatchAble right;
	
	protected MatchAble left;
	
	public MatchAble getRight() {
		return right;
	}

	public void setRight(MatchAble right) {
		this.right = right;
	}

	public MatchAble getLeft() {
		return left;
	}

	public void setLeft(MatchAble left) {
		this.left = left;
	}

}
