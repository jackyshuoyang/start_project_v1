package com.demo.dao;

import java.io.File;

import com.demo.bean.Document;

public interface DocumentDAO {

	public int save(Document d);
	
	public String getFileNameById(Integer docId);
	
	public Document getFileById(Integer docId);
	
	
}
