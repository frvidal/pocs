package com.test;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import org.eclipse.jgit.lib.ObjectId;

public class FixedFile {
	
    @Expose
	public final String objId;

    @Expose
	public final String shortMessage;

    @Expose
	public final String pathFile;

    @Expose
	public boolean evicted = false;

    @Expose
	public List<CommitOperation> modifiedLinesCommit = new ArrayList<>();
	
	public FixedFile(ObjectId objId, String shortMessage, String pathFile) {
		this.objId = objId.name();
		this.shortMessage = shortMessage;
		this.pathFile = pathFile;
	}


}
