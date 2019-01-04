package com.yash.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yash.model.PizzaOrder;
import com.yash.service.IPizzaService;
import com.yash.service.impl.PizzaServiceImpl;
import com.yash.utill.ApiStatus;
import com.yash.utill.Endpoints;
import com.yash.utill.Utills;

@RestController
@RequestMapping(Endpoints.PIZZA)
public class PizzaController {

	@Autowired
	private ApiStatus status;

	private List<PizzaOrder> pizzaList;

	private Integer count;

	private File inputFile;
	private FileOutputStream outputStream;

	@Autowired
	private IPizzaService service;

	@PostMapping(path = Endpoints.ORDERS, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<?> getFile(@RequestParam("file") MultipartFile uploadfile) throws IOException {

		if (uploadfile.getContentType().equals("text/plain")) {

			createDirectory();

			inputFile = new File(Utills.FILE_PATH + uploadfile.getOriginalFilename());
			inputFile.createNewFile();

			outputStream = new FileOutputStream(inputFile);
			outputStream.write(uploadfile.getBytes());
			String response = service.readFile(inputFile);
			outputStream.close();

			inputFile.delete();

			return showResponse("success", "output file: " + Utills.FILE_PATH + Utills.OUTPUT_FILE, 200);
		} else

			return showResponse("error", "Wrong input file", 400);

	}

	private void createDirectory() {
		// TODO Auto-generated method stub
		File file = new File(Utills.FILE_PATH);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}

		}
	}

	@RequestMapping(Endpoints.OUTPUT)
	private ResponseEntity<InputStreamResource> downloadFile1() throws IOException {

		File file = new File(Utills.FILE_PATH + Utills.OUTPUT_FILE);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				// Content-Length
				.contentLength(file.length()).body(resource);
	}

	private ResponseEntity<?> showResponse(String stat, String message, int code) {
		status.setStatus(stat);
		status.setMessage(message);
		return ResponseEntity.status(code).body(status);
	}

	@ExceptionHandler({ Exception.class })
	private ResponseEntity<?> handleException(Exception e) throws IOException {
		status.setStatus("error");
		if (e.getMessage().equals("1")) {
			outputStream.close();
			inputFile.delete();
			status.setMessage("Wrong input file");

		} else
			status.setMessage(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
	}

}
