package com.example.mathgeniuses.model;

public class OperationObject {
	private long mId;
	private String mName;
	
	public OperationObject(long id, String name) {
		mId = id;
		mName = name;
	}
	
	
	public long getId() {
		return mId;
	}
	
	public void setId(long id) {
		this.mId = id;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
}
