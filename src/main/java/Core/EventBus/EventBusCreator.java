package Core.EventBus;

import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.Logger;

import java.util.logging.Level;

public class EventBusCreator {
    public static EventBusBuilder loggerEventBusBuilder(HLog logger) {
        return EventBus.builder().logger(new Logger() {
            @Override
            public void log(Level level, String msg) {
                logger.log(HLogLevel.mapFromLevel(level), msg);
            }
            @Override
            public void log(Level level, String msg, Throwable th) {
                logger.log(HLogLevel.mapFromLevel(level), msg, " Caused by: ", th);
            }
        });
    }

    public static EventBusBuilder defaultLoggerEventBusBuilder() {
        return EventBus.builder().logger(new Logger() {
            @Override
            public void log(Level level, String msg) {
                HLog.logger(HLogLevel.mapFromLevel(level), msg);
            }
            @Override
            public void log(Level level, String msg, Throwable th) {
                HLog.logger(HLogLevel.mapFromLevel(level), msg, " Caused by: ", th);
            }
        });
    }
}
