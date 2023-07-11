package me.nazarxexe.jsmc.js;

import com.caoccao.javet.interception.logging.BaseJavetConsoleInterceptor;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.V8Value;
import org.slf4j.Logger;
import org.slf4j.MarkerFactory;

public class JSLogger extends BaseJavetConsoleInterceptor {

    final Logger logger;

    public JSLogger(Logger logger, V8Runtime runtime) {
        super(runtime);
        this.logger = logger;
    }

    @Override
    public void consoleDebug(V8Value... v8Values) {
        for (V8Value v8Value : v8Values) {
            logger.debug(concat(v8Values));
        }
    }

    @Override
    public void consoleError(V8Value... v8Values) {
        for (V8Value v8Value : v8Values) {
            logger.error(concat(v8Values));
        }
    }

    @Override
    public void consoleInfo(V8Value... v8Values) {
        for (V8Value v8Value : v8Values) {
            logger.info(concat(v8Values));
        }
    }

    @Override
    public void consoleLog(V8Value... v8Values) {
        for (V8Value v8Value : v8Values) {
            logger.info(MarkerFactory.getMarker("LOG"), concat(v8Values));
        }
    }

    @Override
    public void consoleTrace(V8Value... v8Values) {
        for (V8Value v8Value : v8Values) {
            logger.trace(concat(v8Values));
        }
    }

    @Override
    public void consoleWarn(V8Value... v8Values) {
        for (V8Value v8Value : v8Values) {
            logger.warn(concat(v8Values));
        }
    }
}
