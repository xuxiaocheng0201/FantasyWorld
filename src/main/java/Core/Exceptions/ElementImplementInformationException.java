package Core.Exceptions;

import Core.Addition.Element.ElementImplement;
import Core.Addition.ModManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when element implement information was wrong.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ElementImplementInformationException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = -84232262039498182L;

    private static final String DEFAULT_MESSAGE = "Element implement basic information was wrong!";
    private static @NotNull String getElementInformationMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ElementImplementInformationException() {
        super(DEFAULT_MESSAGE);
    }

    public ElementImplementInformationException(@Nullable Class<? extends ElementImplement> elementImplementClass) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(elementImplementClass));
    }

    public ElementImplementInformationException(@Nullable Class<? extends ElementImplement> elementImplementClass, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(elementImplementClass));
    }

    public ElementImplementInformationException(@Nullable String message) {
        super(getElementInformationMessage(message));
    }

    public ElementImplementInformationException(@Nullable String message, @Nullable Class<? extends ElementImplement> elementImplementClass) {
        super(getElementInformationMessage(message) + ModManager.crashClassInformation(elementImplementClass));
    }

    public ElementImplementInformationException(@Nullable String message, @Nullable Class<? extends ElementImplement> elementImplementClass, @Nullable String moreMessage) {
        super(getElementInformationMessage(message) + moreMessage + ModManager.crashClassInformation(elementImplementClass));
    }

    public ElementImplementInformationException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ElementImplementInformationException(@Nullable Class<? extends ElementImplement> elementImplementClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(elementImplementClass), throwable);
    }

    public ElementImplementInformationException(@Nullable Class<? extends ElementImplement> elementImplementClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(elementImplementClass), throwable);
    }

    public ElementImplementInformationException(@Nullable String message, @Nullable Throwable throwable) {
        super(getElementInformationMessage(message), throwable);
    }

    public ElementImplementInformationException(@Nullable String message, @Nullable Class<? extends ElementImplement> elementImplementClass, @Nullable Throwable throwable) {
        super(getElementInformationMessage(message) + ModManager.crashClassInformation(elementImplementClass), throwable);
    }

    public ElementImplementInformationException(@Nullable String message, @Nullable Class<? extends ElementImplement> elementImplementClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getElementInformationMessage(message) + moreMessage + ModManager.crashClassInformation(elementImplementClass), throwable);
    }
}
