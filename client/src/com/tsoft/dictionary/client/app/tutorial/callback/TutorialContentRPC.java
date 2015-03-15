package com.tsoft.dictionary.client.app.tutorial.callback;

import com.tsoft.dictionary.server.app.web.content.ContentRequestTO;
import com.tsoft.dictionary.server.app.web.content.ContentResponseTO;

public interface TutorialContentRPC {
    public void beforeContentRPC(ContentRequestTO requestTO);

    public void afterContentRPC(ContentResponseTO responseTO, boolean isSuccess);
}
