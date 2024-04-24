

package com.dgcash.emi.attachment.client.translation;

import com.dgcash.common.core.data.dtos.translation.ErrorMessageDto;
import com.dgcash.common.core.data.dtos.translation.MessageParameter;

public interface TranslationClientHandler {

    ErrorMessageDto getErrorMessageByLanguageAndCodeWithParameters(String language, String messageCode, MessageParameter... messageParameters);
}
