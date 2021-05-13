package com.test.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@CrossOrigin("http://localhost:4200")
public class UploadController {

	@ResponseBody
	@RequestMapping(value = "/api/upload/do", method = RequestMethod.POST)
	public boolean upload(@RequestParam("file") MultipartFile file) throws IOException {

		try {
			if (!file.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}