package com.tsoft.dictionary.client.server;

public abstract class ServerServletProxy {
    private ServerServlets serverServlets;

    public ServerServletProxy(ServerServlets serverServlets) {
        this.serverServlets = serverServlets;
    }

    protected ServerServlets getServerServlets() {
        return serverServlets;
    }
}
