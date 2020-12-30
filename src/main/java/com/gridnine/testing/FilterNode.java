package com.gridnine.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract  class FilterNode {
	FiltrationRule rule;
	FilttrationCond cond;
	List<FilterNode> children;

	public List<FilterNode> getChildren() {
		return children;
	}

	public FilterNode(FilttrationCond cond, FiltrationRule rule, FilterNode... children) {
		this.rule = rule;
		this.cond = cond;
		if (children == null) this.children = new ArrayList<>();
		else this.children = Arrays.asList(children);
	}

	public boolean checkCond(Collection<Flight> flights) {
		return cond.isCond(flights);
	}

	abstract Collection<Flight> filter(Collection<Flight> flights);
}
