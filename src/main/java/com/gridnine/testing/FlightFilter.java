package com.gridnine.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FlightFilter {
	List<FiltrationRule> rules;

	public FlightFilter() {
		rules = new ArrayList<>();
	}

	public FlightFilter(Collection<FiltrationRule> rules) {
		this.rules = new ArrayList<>();
		addRules(rules);
	}

	public FlightFilter(FiltrationRule... rules) {
		this.rules = new ArrayList<>();
		addRules(rules);
	}

	public void addRule(FiltrationRule rule) {
		if (rule != null)
			rules.add(rule);
	}

	public void addRules(Collection<FiltrationRule> rules) {
		if (rules != null && rules.size() > 0)
			this.rules.addAll(rules);
	}

	public void addRules(FiltrationRule... rules) {
		if (rules != null && rules.length > 0)
			this.rules.addAll(Arrays.asList(rules));
	}
	public void clearRules(){
		rules.clear();
	}

	public Collection<Flight> filter(Collection<Flight> flights){
		List<Flight> result = new ArrayList<>();
		for (var flight: flights){
			if (filterFlight(flight)) result.add(flight);
		}
		return result;
	}
	public Collection<Flight> excludeFilter(Collection<Flight> flights){
		List<Flight> result = new ArrayList<>();
		for (var flight: flights){
			if (!filterFlight(flight)) result.add(flight);
		}
		return result;
	}

	private boolean filterFlight(Flight flight){
		for (FiltrationRule rule: rules){
			if (!rule.filter(flight)) return false;
		}
		return true;
	}
}
