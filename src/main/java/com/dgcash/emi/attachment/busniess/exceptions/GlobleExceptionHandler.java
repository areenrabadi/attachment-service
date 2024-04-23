package com.dgcash.emi.attachment.busniess.exceptions;

import com.dgcash.common.core.exception.DigitalCashException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobleExceptionHandler
{
	@ExceptionHandler({UploadFileException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleUploadFileException(DigitalCashException ex) {
		return new ResponseEntity<>(ex.getErrorCode() , HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({InvalidFileType.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleInvalidFileType(InvalidFileType ex) {
		return new ResponseEntity<>(ex.getErrorCode() , HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({FileNotFoundOnFTPException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleFileNotFoundOnFTPException(FileNotFoundOnFTPException ex) {
		return new ResponseEntity<>(ex.getErrorCode() , HttpStatus.NOT_FOUND);
	}
}
