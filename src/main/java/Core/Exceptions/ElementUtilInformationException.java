package Core.Exceptions;

import Core.Addition.Element.ElementUtil;
import Core.Addition.ModManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when element util information was wrong.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ElementUtilInformationException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 7451632741693899278L;

    private static final String DEFAULT_MESSAGE = "Element util basic information was wrong!";
    private static @NotNull String getElementInformationMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ElementUtilInformationException() {
        super(DEFAULT_MESSAGE);
    }

    public ElementUtilInformationException(@Nullable Class<? extends ElementUtil<?>> elementUtilClass) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(elementUtilClass));
    }

    public ElementUtilInformationException(@Nullable Class<? extends ElementUtil<?>> elementUtilClass, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(elementUtilClass));
    }

    public ElementUtilInformationException(@Nullable String message) {
        super(getElementInformationMessage(message));
    }

    public ElementUtilInformationException(@Nullable String message, @Nullable Class<? extends ElementUtil<?>> elementUtilClass) {
        super(getElementInformationMessage(message) + ModManager.crashClassInformation(elementUtilClass));
    }

    public ElementUtilInformationException(@Nullable String message, @Nullable Class<? extends ElementUtil<?>> elementUtilClass, @Nullable String moreMessage) {
        super(getElementInformationMessage(message) + moreMessage + ModManager.crashClassInformation(elementUtilClass));
    }

    public ElementUtilInformationException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ElementUtilInformationException(@Nullable Class<? extends ElementUtil<?>> elementUtilClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(elementUtilClass), throwable);
    }

    public ElementUtilInformationException(@Nullable Class<? extends ElementUtil<?>> elementUtilClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(elementUtilClass), throwable);
    }

    public ElementUtilInformationException(@Nullable String message, @Nullable Throwable throwable) {
        super(getElementInformationMessage(message), throwable);
    }

    public ElementUtilInformationException(@Nullable String message, @Nullable Class<? extends ElementUtil<?>> elementUtilClass, @Nullable Throwable throwable) {
        super(getElementInformationMessage(message) + ModManager.crashClassInformation(elementUtilClass), throwable);
    }

    public ElementUtilInformationException(@Nullable String message, @Nullable Class<? extends ElementUtil<?>> elementUtilClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getElementInformationMessage(message) + moreMessage + ModManager.crashClassInformation(elementUtilClass), throwable);
    }
}
