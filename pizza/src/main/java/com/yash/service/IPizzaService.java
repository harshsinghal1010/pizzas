package com.yash.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;


public interface IPizzaService {
	
	public String readFile(File file)throws IOException;
	public String deleteFile(File file)throws IOException;

}
