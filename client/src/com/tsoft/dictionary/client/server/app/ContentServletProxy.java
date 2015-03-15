package com.tsoft.dictionary.client.server.app;

import com.tsoft.dictionary.server.app.web.content.ContentResponseTO;
import com.tsoft.dictionary.server.app.web.content.ContentRequestTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tsoft.dictionary.client.server.IsManaged;
import com.tsoft.dictionary.client.server.ServerServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.client.server.cache.ContentCache;
import com.tsoft.dictionary.client.server.cache.HasId;
import com.tsoft.dictionary.client.server.cache.StringId;
import com.tsoft.dictionary.client.util.Logger;
import com.tsoft.dictionary.server.app.web.content.ContentServletInterfaceAsync;

public class ContentServletProxy extends ServerServletProxy implements IsManaged<ContentResponseTO> {
    private static final Logger logger = new Logger("com.tsoft.dictionary.client.server.app.content.ContentServletProxy");

    private ContentServletInterfaceAsync contentServletAsync;

    private ContentCache<ContentResponseTO> cache = new ContentCache<ContentResponseTO>(1 * 1024 * 1024);

    public ContentServletProxy(ServerServlets serverServlets, ContentServletInterfaceAsync contentServletAsync) {
        super(serverServlets);
        this.contentServletAsync = contentServletAsync;
    }

    public void getContent(ContentRequestTO requestTO, AsyncCallback<ContentResponseTO> callback) {
        ContentResponseTO responseTO = getFromCache(requestTO.getPageName());
        if (responseTO != null) {
            callback.onSuccess(responseTO);
            return;
        }

        contentServletAsync.getContent(getServerServlets().getUserInfo(), requestTO, callback);
    }

    @Override
    public void afterRemoteCall(ContentResponseTO responseTO, boolean isSuccess) {
        if (isSuccess) {
            addToCache(responseTO.getPageName(), responseTO);
        }
    }

    private ContentResponseTO getFromCache(String ref) {
        HasId id = new StringId(ref);
        ContentResponseTO responseTO = cache.get(id);
        return responseTO;
    }

    private void addToCache(String ref, ContentResponseTO content) {
        HasId id = new StringId(ref);
        if (!cache.inCache(id)) {
            cache.put(id, content);
        }
    }
}
