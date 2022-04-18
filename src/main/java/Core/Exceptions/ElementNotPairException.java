package Core.Exceptions;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.ElementUtil;
import Core.Addition.ModManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when elements not paired.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ElementNotPairException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 5443512506937051931L;

    private static final String DEFAULT_MESSAGE = "Element has no pair of implement and util!";
    private static @NotNull String getElementNotPairMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ElementNotPairException() {
        super(DEFAULT_MESSAGE);
    }

    public ElementNotPairException(@Nullable Class<? extends ElementImplement> implementClass, @Nullable Object NULL) {
        super(DEFAULT_MESSAGE + " Element name: " + ElementImplement.getElementNameFromClass(implementClass) + ". No util for implement '" + ModManager.crashClassInformation(implementClass) + "'.");
    }

    public ElementNotPairException(@Nullable Object NULL, @Nullable Class<? extends ElementUtil<?>> utilClass) {
        super(DEFAULT_MESSAGE + " Element name: " + ElementUtil.getElementNameFromClass(utilClass) + ". No implement for util '" + ModManager.crashClassInformation(utilClass) + "'.");
    }

    public ElementNotPairException(@Nullable String message) {
        super(getElementNotPairMessage(message));
    }

    public ElementNotPairException(@Nullable String message, @Nullable Class<? extends ElementImplement> implementClass, @Nullable Object NULL) {
        super(getElementNotPairMessage(message) + " Element name: " + ElementImplement.getElementNameFromClass(implementClass) + ". No util for implement '" + ModManager.crashClassInformation(implementClass) + "'.");
    }

    public ElementNotPairException(@Nullable String message, @Nullable Object NULL, @Nullable Class<? extends ElementUtil<?>> utilClass) {
        super(getElementNotPairMessage(message) + " Element name: " + ElementUtil.getElementNameFromClass(utilClass) + ". No implement for util '" + ModManager.crashClassInformation(utilClass) + "'.");
    }

    public ElementNotPairException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ElementNotPairException(@Nullable Class<? extends ElementImplement> implementClass, @Nullable Object NULL, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + " Element name: " + ElementImplement.getElementNameFromClass(implementClass) + ". No util for implement '" + ModManager.crashClassInformation(implementClass) + "'.", throwable);
    }

    public ElementNotPairException(@Nullable Object NULL, @Nullable Class<? extends ElementUtil<?>> utilClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + " Element name: " + ElementUtil.getElementNameFromClass(utilClass) + ". No implement for util '" + ModManager.crashClassInformation(utilClass) + "'.", throwable);
    }

    public ElementNotPairException(@Nullable String message, @Nullable Throwable throwable) {
        super(getElementNotPairMessage(message), throwable);
    }

    public ElementNotPairException(@Nullable String message, @Nullable Class<? extends ElementImplement> implementClass, @Nullable Object NULL, @Nullable Throwable throwable) {
        super(getElementNotPairMessage(message) + " Element name: " + ElementImplement.getElementNameFromClass(implementClass) + ". No util for implement '" + ModManager.crashClassInformation(implementClass) + "'.", throwable);
    }

    public ElementNotPairException(@Nullable String message, @Nullable Object NULL, @Nullable Class<? extends ElementUtil<?>> utilClass, @Nullable Throwable throwable) {
        super(getElementNotPairMessage(message) + " Element name: " + ElementUtil.getElementNameFromClass(utilClass) + ". No implement for util '" + ModManager.crashClassInformation(utilClass) + "'.", throwable);
    }
}
