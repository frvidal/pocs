package com.test;

import java.util.ArrayList;
import java.util.List;

public class Record {

	final String shortCommitMessage;

	public List<String> paths = new ArrayList<>();

	public Record(String shortCommitMessage) {
		this.shortCommitMessage = shortCommitMessage;
	}
	
}
