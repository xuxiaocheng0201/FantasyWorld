package Core.EventBus;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.Logger;

import java.util.logging.Level;

public class EventBusCreator {
    public static EventBusBuilder loggerEventBusBuilder(HLog logger) {
        return EventBus.builder().logger(new Logger() {
            @Override
            public void log(Level level, String msg) {
                logger.log(HELogLevel.getFromLevel(level), msg);
            }
            @Override
            public void log(Level level, String msg, Throwable th) {
                logger.log(HELogLevel.getFromLevel(level), msg, " Caused by: ", th);
            }
        });
    }

    public static EventBusBuilder defaultLoggerEventBusBuilder() {
        return EventBus.builder().logger(new Logger() {
            @Override
            public void log(Level level, String msg) {
                HLog.logger(HELogLevel.getFromLevel(level), msg);
            }
            @Override
            public void log(Level level, String msg, Throwable th) {
                HLog.logger(HELogLevel.getFromLevel(level), msg, " Caused by: ", th);
            }
        });
    }
}
