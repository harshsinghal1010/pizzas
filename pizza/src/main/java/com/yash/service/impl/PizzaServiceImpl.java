package com.yash.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yash.model.PizzaOrder;
import com.yash.service.IPizzaService;
import com.yash.utill.PizzaOrderComparator;
import com.yash.utill.Utills;

@Service
public class PizzaServiceImpl implements IPizzaService {



	private List<PizzaOrder> pizzaList;
	private BufferedReader bufferedReader;
	@Override
	public String readFile(File file)  {
		// TODO Auto-generated method stub
		pizzaList = new ArrayList<>();
		FileReader fr;
		try {
			fr = new FileReader(file);
			 bufferedReader = new BufferedReader(fr);
			String line;

			bufferedReader.readLine(); // to remove order and date text from line
			while ((line = bufferedReader.readLine()) != null) {
				// process the line
				readFile1(line);
			}
			
			bufferedReader.close();
			//file.delete();
			Collections.sort(pizzaList, new PizzaOrderComparator());
			writeData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		 finally {
			 try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		//deleteFile(file);
		return "Done";
	}

	@Override
	public String deleteFile(File file) {
		file.delete();
		System.err.println("delete");
		return "file deleted";
	}

	private void readFile1(String data) {

		if (!data.equals("")) {

			data = data.replaceAll("\\s+", " ");
			String arr[] = data.split(" ");

			Long l = Long.parseLong(arr[1]);
			long timeStamp = l * 1000;
			Date date = new Date(timeStamp);
			pizzaList.add(new PizzaOrder(arr[0], date));

		}

	}

	private void writeData() throws IOException {

		String finalData = "Given Pizza Orders with time are : ";
		for (PizzaOrder orders : pizzaList) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(orders.getOrderDate());

			finalData += "Order " + orders.getOrderName() + " at : " + time + " \n";

		}

		File file = new File(Utills.FILE_PATH  + Utills.OUTPUT_FILE);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(finalData);
		bw.close();

	}
	
	
	
	
	
	
	
	
	
	
	
}