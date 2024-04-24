package com.dgcash.emi.attachment.busniess.exceptions;

import com.dgcash.common.core.data.dtos.translation.ErrorMessageDto;
import com.dgcash.common.core.exception.DigitalCashException;
import com.dgcash.emi.attachment.client.translation.TranslationClientHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobleExceptionHandler extends ResponseEntityExceptionHandler {


	private final TranslationClientHandlerImpl translationClientHandler;

	@ExceptionHandler({UploadFileException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleUploadFileException(DigitalCashException ex, WebRequest request) {
		return new ResponseEntity<>(getErrorMsg(ex, request) , HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler({InvalidFileType.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleInvalidFileType(InvalidFileType ex, WebRequest request) {
		return new ResponseEntity<>(getErrorMsg(ex, request) , HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({FileNotFoundOnFTPException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleFileNotFoundOnFTPException(FileNotFoundOnFTPException ex, WebRequest request) {
		return new ResponseEntity<>(getErrorMsg(ex, request) , HttpStatus.NOT_FOUND);
	}

	private String getErrorMsg(DigitalCashException ex, WebRequest request) {
		String header = Optional.ofNullable(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))
				  .filter(StringUtils::isNotBlank)
				  .orElse("en");
		return translationClientHandler.getErrorMessageByLanguageAndCodeWithParameters(
				  header, ex.getErrorCode(), ex.getArgs()).getMessage().getPlainText();
	}

}
