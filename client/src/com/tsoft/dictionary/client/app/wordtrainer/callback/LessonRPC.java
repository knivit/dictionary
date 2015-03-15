package com.tsoft.dictionary.client.app.wordtrainer.callback;

import com.tsoft.dictionary.server.app.web.wordtrainer.LessonResponseTO;

public interface LessonRPC {
    public void beforeLessonRPC();

    public void afterLessonRPC(LessonResponseTO responseTO, boolean isSuccess);
}
