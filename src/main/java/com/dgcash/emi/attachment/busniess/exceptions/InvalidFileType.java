package com.dgcash.emi.attachment.busniess.exceptions;

import com.dgcash.common.core.data.dtos.translation.MessageParameter;
import com.dgcash.common.core.exception.DigitalCashException;

public class InvalidFileType extends DigitalCashException {
	private static final String ERROR_CODE = "INVALID_FILE_TYPE";
	public InvalidFileType() {
		super(ERROR_CODE);
	}

	public InvalidFileType(MessageParameter message) {
		super(ERROR_CODE, message);
	}
}
