package com.tpofof.core.data.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

public class ResultsSetTest {

	@Test
	public void testBuilder() {
		ResultsSet.builder()
			.limit(100)
			.offset(200)
			.total(1000L)
			.results(new ArrayList<Object>())
			.build();
	}
	
	@Test(expected=Exception.class)
	public void testNullLimit() {
		ResultsSet.builder()
			.offset(200)
			.total(1000L)
			.results(new ArrayList<Object>())
			.build();
	}
	
	@Test(expected=Exception.class)
	public void testNullOffset() {
		ResultsSet.builder()
			.limit(100)
			.total(1000L)
			.results(new ArrayList<Object>())
			.build();
	}
	
	@Test(expected=Exception.class)
	public void testNullResults() {
		ResultsSet.builder()
			.limit(100)
			.offset(200)
			.total(1000L)
			.build();
	}

	@Test
	public void testEquals() {
		ResultsSet<String> expected = ResultsSet.<String>builder()
				.limit(100)
				.offset(200)
				.total(1000L)
				.results(Lists.newArrayList("", "test"))
				.build();
		ResultsSet<String> actual = ResultsSet.<String>builder()
				.limit(100)
				.offset(200)
				.total(1000L)
				.results(Lists.newArrayList("", "test"))
				.build();
		assertEquals(expected, actual);
	}

	@Test
	public void testNotEqualLimit() {
		ResultsSet<String> expected = ResultsSet.<String>builder()
				.limit(50)
				.offset(200)
				.total(1000L)
				.results(Lists.newArrayList("", "test"))
				.build();
		ResultsSet<String> actual = ResultsSet.<String>builder()
				.limit(100)
				.offset(200)
				.total(1000L)
				.results(Lists.newArrayList("", "test"))
				.build();
		assertNotEquals(expected, actual);
	}

	@Test
	public void testNotEqualOffset() {
		ResultsSet<String> expected = ResultsSet.<String>builder()
				.limit(100)
				.offset(300)
				.total(1000L)
				.results(Lists.newArrayList("", "test"))
				.build();
		ResultsSet<String> actual = ResultsSet.<String>builder()
				.limit(100)
				.offset(200)
				.total(1000L)
				.results(Lists.newArrayList("", "test"))
				.build();	
		assertNotEquals(expected, actual);
	}

	@Test
	public void testNotEqualResults() {
		ResultsSet<String> expected = ResultsSet.<String>builder()
				.limit(100)
				.offset(200)
				.total(1000L)
				.results(Lists.newArrayList("first", "test"))
				.build();
		ResultsSet<String> actual = ResultsSet.<String>builder()
				.limit(100)
				.offset(200)
				.total(1000L)
				.results(Lists.newArrayList("first", "not the same", "extra"))
				.build();
		assertNotEquals(expected, actual);
	}
}
