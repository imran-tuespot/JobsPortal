package com.tuespotsolutions.util;

import org.springframework.stereotype.Component;

import com.tuespotsolutions.customexception.InvalidFileFormatException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.bind.DatatypeConverter;

@Component
public class FileUpload {

	
	// images upload method
	public static String uploadFile(String file, String fileUploadUrl) {

		String fileName = null;

		String[] strings = file.split(",");
		String extension;
		switch (strings[0]) {// check image's extension
		case "data:image/jpeg;base64":
			extension = "jpeg";
			break;
		case "data:image/png;base64":
			extension = "png";
			break;
		case "data:image/jpg;base64":
			extension = "jpg";
			break;
		default:// should write cases for more images types
			extension = "invalid";
			break;
		}

		/**
		 * check file format
		 */
		if (extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png")) {
			// convert base64 string to binary data

			byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
			String modifyFile = "studentProfile" + System.currentTimeMillis() + "." + extension;
			// String upload_dir_file = env.getProperty("file.local.path");
			String path = fileUploadUrl + modifyFile;
			fileName = modifyFile;
//			studentDetailEntity.setUploadPhoto(modifyFile);
			File file1 = new File(path);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file1))) {
				outputStream.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new InvalidFileFormatException("Only jpg, jpeg and png extensions are valid");
		}

		return fileName;

	}

	
	
	// Resume upload method
	public static String uploadResume(String file, String fileUploadUrl) {

		String fileName = null;

		String[] strings = file.split(",");
		String extension;
		switch (strings[0]) {// check image's extension
		case "data:application/pdf;base64":
			extension = "pdf";
			break;
		case "data:application/docx;base64":
			extension = "docx";
			break;
			
		default:// should write cases for more images types
			extension = "invalid";
			break;
		}

		/**
		 * check file format
		 */
		if (extension.equals("pdf") || extension.equals("docx")) {
			// convert base64 string to binary data

			byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
			String modifyFile = "studentProfile" + System.currentTimeMillis() + "." + extension;
			// String upload_dir_file = env.getProperty("file.local.path");
			String path = fileUploadUrl + modifyFile;
			fileName = modifyFile;
//			studentDetailEntity.setUploadPhoto(modifyFile);
			File file1 = new File(path);
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file1))) {
				outputStream.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new InvalidFileFormatException("Only pdf and docx extensions are valid");
		}

		return fileName;

	}

}
