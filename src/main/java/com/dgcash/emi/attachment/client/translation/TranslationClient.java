package com.dgcash.emi.attachment.client.translation;

import com.dgcash.common.core.data.dtos.translation.ErrorMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "translation-service")
public interface TranslationClient {

    @GetMapping("/errors/{language}/{errorCode}")
    ErrorMessageDto errorMessage(@PathVariable("language") String language,
                                 @PathVariable("errorCode") String errorCode,
                                 @RequestParam Map<String, String> requestParams);
}
