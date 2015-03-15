package com.tsoft.ui.binder;

import com.tsoft.dict.*;

interface Invoker {
    public Object invoke( Object... args ) throws InvokerException;
}
