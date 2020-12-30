package com.gridnine.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class CommonNode extends FilterNode {
	public CommonNode(FilttrationCond cond, FiltrationRule rule, FilterNode... children) {
		super(cond, rule, children);
	}

	@Override
	public Collection<Flight> filter(Collection<Flight> flights) {
			return flights.stream().filter(rule::filter).collect(Collectors.toList());
	}
}
