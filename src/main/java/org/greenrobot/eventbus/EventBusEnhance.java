package org.greenrobot.eventbus;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventBusEnhance extends EventBus {
    protected static Method handleSubscriberException;
    static {
        try {
            EventBusEnhance.handleSubscriberException = EventBus.class.getDeclaredMethod("handleSubscriberException", Subscription.class, Object.class, Throwable.class);
            //noinspection NonFinalStaticVariableUsedInClassInitialization
            EventBusEnhance.handleSubscriberException.setAccessible(true);
        } catch (final NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    public EventBusEnhance() {
        super();
    }

    public EventBusEnhance(final @NotNull EventBusBuilder builder) {
        super(builder);
    }

    @Override
    void invokeSubscriber(final Subscription subscription, final Object event) {
        try {
            subscription.subscriberMethod.method.setAccessible(true);
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (final InvocationTargetException exception) {
            try {
                EventBusEnhance.handleSubscriberException.invoke(this, subscription, event, exception.getCause());
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } catch (final IllegalAccessException exception) {
            throw new IllegalStateException("Unexpected exception", exception);
        }
    }
}
