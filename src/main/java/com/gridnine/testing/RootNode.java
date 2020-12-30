package com.gridnine.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class RootNode extends FilterNode {
	public RootNode(FilttrationCond cond, FiltrationRule rule, FilterNode... children) {
		super(cond, rule, children);
	}

	//все это сделано, чтобы избежать рекурсии, т.к. в условии сказано, что датасет может быть очень большой
	@Override
	public Collection<Flight> filter(Collection<Flight> flights) {
		if (!cond.isCond(flights)) return flights;
		Collection<Flight> current = flights.stream().filter(rule::filter).collect(Collectors.toList());
		if (children == null || children.size() == 0) return current;
		FilterNode ptr = this;
		boolean notExit = true;
		while (notExit) {
			notExit = false;
			for (FilterNode node : ptr.getChildren()) {
				if (node.checkCond(current)) {
					ptr = node;
					current = ptr.filter(current);
					notExit = true;
					break;
				}
			}
		}
		return current;

	}


}
