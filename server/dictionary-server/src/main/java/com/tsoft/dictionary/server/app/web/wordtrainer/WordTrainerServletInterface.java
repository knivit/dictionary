package com.tsoft.dictionary.server.app.web.wordtrainer;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
@RemoteServiceRelativePath("wordTrainer")
public interface WordTrainerServletInterface extends RemoteService {
    public ListResponseTO getDictionaryList(UserInfo userInfo);

    public LessonResponseTO generateLesson(UserInfo userName);

    public SettingsResponseTO loadSettings(UserInfo userInfo);

    public void saveSettings(UserInfo userInfo, SettingsRequestTO requestTO);
}
