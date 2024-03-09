package com.dgcash.emi.attachment.busniess.exceptions;

import com.dgcash.common.core.exception.DigitalCashException;

public class InvalidFileType extends DigitalCashException {
	private static final String ERROR_CODE = "INVALID_FILE_TYPE";
	public InvalidFileType() {
		super(ERROR_CODE);
	}
}
