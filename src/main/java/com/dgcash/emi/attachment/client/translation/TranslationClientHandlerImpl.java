
package com.dgcash.emi.attachment.client.translation;

import com.dgcash.common.core.data.dtos.translation.ErrorMessageDto;
import com.dgcash.common.core.data.dtos.translation.MessageParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TranslationClientHandlerImpl implements TranslationClientHandler {

    private final TranslationClient translationClient;

    @Override
    public ErrorMessageDto getErrorMessageByLanguageAndCodeWithParameters(String language, String messageCode,
                                                                          MessageParameter... messageParameters) {
        Map<String, String> parametersMap = Arrays.stream(messageParameters)
                .collect(Collectors.toMap(MessageParameter::getParameterName, MessageParameter::getParameterValue));
        return translationClient.errorMessage(language, messageCode, parametersMap);
    }
}
