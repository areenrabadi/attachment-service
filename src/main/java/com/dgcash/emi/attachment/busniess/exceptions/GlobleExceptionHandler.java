package com.dgcash.emi.attachment.busniess.exceptions;

import com.dgcash.common.core.data.dtos.translation.ErrorMessageDto;
import com.dgcash.common.core.exception.DigitalCashException;
import com.dgcash.emi.attachment.client.translation.TranslationClientHandlerImpl;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobleExceptionHandler extends ResponseEntityExceptionHandler {


	private final TranslationClientHandlerImpl translationClientHandler;

	@ExceptionHandler({UploadFileException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleUploadFileException(DigitalCashException ex, WebRequest request) {
		try {
			return new ResponseEntity<>(getErrorMsg(ex, request), HttpStatus.BAD_REQUEST);
		}catch (FeignException e){
			return handleFeignClientException(e);
		}}


	@ExceptionHandler({InvalidFileType.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleInvalidFileType(InvalidFileType ex, WebRequest request) {
		try {
			return new ResponseEntity<>(getErrorMsg(ex, request), HttpStatus.BAD_REQUEST);
		}catch (FeignException e){
			return handleFeignClientException(e);
		}}

	@ExceptionHandler({FileNotFoundOnFTPException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> handleFileNotFoundOnFTPException(FileNotFoundOnFTPException ex, WebRequest request) {
		try {
			return new ResponseEntity<>(getErrorMsg(ex, request), HttpStatus.NOT_FOUND);
		} catch (FeignException e){
			return handleFeignClientException(e);
	}
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<String> handleFeignClientException(FeignException e) {
		return ResponseEntity.status(e.status()).body(e.contentUTF8());
	}

	@SneakyThrows(FeignException.class)
	private ErrorMessageDto getErrorMsg(DigitalCashException ex, WebRequest request) {
		String header = Optional.ofNullable(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))
				  .filter(StringUtils::isNotBlank)
				  .map(h -> h.contains("ar") ? "ar" : h.contains("en") ? "en" : "en")
				  .orElse("en");
		return translationClientHandler.getErrorMessageByLanguageAndCodeWithParameters(
				  header, ex.getErrorCode(), ex.getArgs());
	}

}
