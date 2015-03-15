package com.tsoft.dictionary.client.app.dictionary.callback;

import com.tsoft.dictionary.server.app.web.dictionary.TranslationRequestTO;
import com.tsoft.dictionary.server.app.web.dictionary.TranslationResponseTO;

public interface TranslationRPC {
    public void beforeTranslationRPC(TranslationRequestTO requestTO);

    public void afterTranslationRPC(TranslationResponseTO responseTO, boolean isSuccess);
}
