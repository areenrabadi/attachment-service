package com.dgcash.emi.attachment.busniess.exceptions;

import com.dgcash.common.core.exception.DigitalCashException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class GlobleExceptionHandler
{
	@ExceptionHandler({UploadFileException.class})
	public ResponseEntity<String> handleUploadFileException(DigitalCashException ex) {
		return new ResponseEntity<>(ex.getErrorCode() , HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({InvalidFileType.class})
	public ResponseEntity<String> handleInvalidFileType(InvalidFileType ex) {
		return new ResponseEntity<>(ex.getErrorCode() , HttpStatus.BAD_REQUEST);
	}
}
