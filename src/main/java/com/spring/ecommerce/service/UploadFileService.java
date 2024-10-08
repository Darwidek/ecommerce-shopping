package com.spring.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	private String folder = "image//"; // crate url of the image file

	public String saveImage(MultipartFile file) throws IOException { // metohd to save image

		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes(); // convert file to bytes type to send at backend
			Path path = Paths.get(folder + file.getOriginalFilename()); /// create a path file with params
			Files.write(path, bytes);
			return file.getOriginalFilename(); // return file name
		}
		return "default.jpg";
	}

	public void deleteImage(String nombre) {
		String ruta = "image//"; // create the url file
		File file = new File(ruta + nombre); // create a file with params
		file.delete(); // delete the file
		
	}
}
