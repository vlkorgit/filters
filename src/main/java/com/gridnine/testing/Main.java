package com.gridnine.testing;

import org.w3c.dom.traversal.NodeFilter;

import java.time.*;
import java.util.Collection;

public class Main {
	public static void main(String[] args) {
		//testFilterSimple();

		testFilterWithTree();

		//oneMoreExample();
	}
	//пример работы без дерева
	public static void testFilterSimple() {
		var flights = FlightBuilder.createFlights();
		FlightFilter departureAfterNowFilter = new FlightFilter();
		departureAfterNowFilter.addRules((flight) -> {
			return flight.getSegments().get(0).getDepartureDate()
					.compareTo(LocalDateTime.now()) < 0;
		});

		System.out.println("Not filtered");
		flights.forEach(System.out::println);

		var departureAfterNow = departureAfterNowFilter.excludeFilter(flights);
		System.out.println("Flights with departure before now");
		departureAfterNow.forEach(System.out::println);

		FlightFilter noBrokenSegmentsFilter = new FlightFilter((flight) -> {
			return flight.getSegments().stream().filter(
					segment ->
							segment.getDepartureDate().compareTo(segment.getArrivalDate()) > 0
			).count() > 0;
		});
		var noBrokenSegments = noBrokenSegmentsFilter.excludeFilter(flights);
		System.out.println("Flight with no broken segments");
		noBrokenSegments.forEach(System.out::println);


		FlightFilter less2hoursFilter = new FlightFilter((flight) -> {
			var segments = flight.getSegments();
			if (segments.size() <= 1) return true;
			long sum = 0;
			for (int i = 0; i < segments.size(); i++) {
				if (i == 0) {
					sum -= segments.get(i).getArrivalDate().toInstant(ZoneOffset.UTC).getEpochSecond();
				} else if (i == segments.size() - 1) {
					sum += segments.get(i).getDepartureDate().toInstant(ZoneOffset.UTC).getEpochSecond();
				} else {
					sum +=
							segments.get(i).getDepartureDate().toInstant(ZoneOffset.UTC).getEpochSecond()
									- segments.get(i).getArrivalDate().toInstant(ZoneOffset.UTC).getEpochSecond();

				}
			}
			if (sum >= 7200) return false;
			else return true;


		});
		var less2hours = less2hoursFilter.filter(flights);
		System.out.println("Less than 2 hours ");
		less2hours.forEach(System.out::println);
	}
	//пример работы с одним узлом дерева правил
	public static void testFilterWithTree() {
		var flights = FlightBuilder.createFlights();

		System.out.println("Not filtered");
		flights.forEach(System.out::println);

		FilterNode departureAfterNowFilter = new RootNode((f) -> true, (flight) -> {
			return flight.getSegments().get(0).getDepartureDate()
					.compareTo(LocalDateTime.now()) > 0;
		});

		var departureAfterNow = departureAfterNowFilter.filter(flights);

		System.out.println("Flights with departure before now");
		departureAfterNow.forEach(System.out::println);

		FilterNode noBrokenSegmentsFilter = new RootNode(f -> true, (flight) -> {
			return flight.getSegments().stream().filter(
					segment ->
							segment.getDepartureDate().compareTo(segment.getArrivalDate()) < 0
			).count() > 0;
		});
		var noBrokenSegments = noBrokenSegmentsFilter.filter(flights);
		System.out.println("Flight with no broken segments");
		noBrokenSegments.forEach(System.out::println);


		FilterNode less2hoursFilter = new RootNode(f -> true, (flight) -> {
			var segments = flight.getSegments();
			if (segments.size() <= 1) return true;
			long sum = 0;
			for (int i = 0; i < segments.size(); i++) {
				if (i == 0) {
					sum -= segments.get(i).getArrivalDate().toInstant(ZoneOffset.UTC).getEpochSecond();
				} else if (i == segments.size() - 1) {
					sum += segments.get(i).getDepartureDate().toInstant(ZoneOffset.UTC).getEpochSecond();
				} else {
					sum +=
							segments.get(i).getDepartureDate().toInstant(ZoneOffset.UTC).getEpochSecond()
									- segments.get(i).getArrivalDate().toInstant(ZoneOffset.UTC).getEpochSecond();

				}
			}
			if (sum >= 7200) return false;
			else return true;


		});
		var less2hours = less2hoursFilter.filter(flights);
		System.out.println("Less than 2 hours ");
		less2hours.forEach(System.out::println);
	}

	//пример с динамическим выбором правила фильтрации
	//выбирается первое подходящее правило
	public static void oneMoreExample() {
		var flights = FlightBuilder.createFlights();

		System.out.println("Not filtered");
		flights.forEach(System.out::println);

		FilterNode departureAfterNowFilter =
				new RootNode(
						all -> true, //условие выполнения фильтрации
						flight -> //правило фильтрации
								flight.getSegments().get(0).getDepartureDate()
										.compareTo(LocalDateTime.now()) > 0
						,
						new CommonNode(
								all -> all.size() > 10, //условие выполнения фильтрации
								flight ->  //правило фильтрации
										flight.getSegments().size() > 1
						),
						new CommonNode(
								all-> all.size()>1, //условие
								flight-> //правило
										flight.getSegments().size() > 2
						));

		Collection<Flight> filtered = departureAfterNowFilter.filter(flights);
		System.out.println("extra filtered");
		filtered.forEach(System.out::println);
	}
}
