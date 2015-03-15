package com.tsoft.dictionary.client.app.tutorial.callback;

import com.tsoft.dictionary.server.app.web.tutorial.TutorialResponseTO;

public interface GlossaryIndexRPC {
    public void beforeGlossaryIndexRPC(Void requestTO);

    public void afterGlossaryIndexRPC(TutorialResponseTO responseTO, boolean success);
}
