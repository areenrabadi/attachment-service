package com.dgcash.emi.attachment.common;

import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
	IBAN_CUSTOMER("iban_customer"),
	IBAN_MERCHANT("iban_merchant"),
	VAT("vat"),
	CR("cr"),
	LOGO("logo"),
	SIGNATORY("signatory"),
	DOCUMENT("document"),
	NATIONAL_ADDRESS("national_address");

	private final String value;
}
