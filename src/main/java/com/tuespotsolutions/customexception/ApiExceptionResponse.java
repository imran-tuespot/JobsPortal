package com.tuespotsolutions.customexception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiExceptionResponse {

		private String message;
		private boolean status;
}
