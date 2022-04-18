package Core.Exceptions;

import Core.Addition.Element.BasicInformation.ElementName;
import Core.Addition.Element.ElementImplement;
import Core.Addition.ModManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when element implement names clash.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ElementImplementNameClashException extends ElementImplementInformationException {
    @Serial
    private static final long serialVersionUID = -6343355127183664028L;

    private static final String DEFAULT_MESSAGE = "Element implements name clash!";
    private static @NotNull String getModNameClashMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ElementImplementNameClashException() {
        super(DEFAULT_MESSAGE);
    }

    public ElementImplementNameClashException(@Nullable ElementName ElementImplementName) {
        super(DEFAULT_MESSAGE + "Element implement names are '" + ElementImplementName + "'.");
    }

    public ElementImplementNameClashException(@Nullable ElementName ElementImplementName, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + "Element implement names are '" + ElementImplementName + "'." + moreMessage);
    }

    public ElementImplementNameClashException(@Nullable Class<? extends ElementImplement> ElementImplementClass1, @Nullable Class<? extends ElementImplement> ElementImplementClass2) {
        super(DEFAULT_MESSAGE + "Element implement names are '" + ElementImplement.getElementNameFromClass(ElementImplementClass1) + "'." + ModManager.crashClassInformation(ElementImplementClass1) + ModManager.crashClassInformation(ElementImplementClass2));
    }

    public ElementImplementNameClashException(@Nullable Class<? extends ElementImplement> ElementImplementClass1, @Nullable Class<? extends ElementImplement> ElementImplementClass2, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + "Element implement names are '" + ElementImplement.getElementNameFromClass(ElementImplementClass1) + "'." + moreMessage + ModManager.crashClassInformation(ElementImplementClass1) + ModManager.crashClassInformation(ElementImplementClass2));
    }

    public ElementImplementNameClashException(@Nullable String message) {
        super(getModNameClashMessage(message));
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable ElementName ElementImplementName) {
        super(getModNameClashMessage(message) + "Element implement names are '" + ElementImplementName + "'.");
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable ElementName ElementImplementName, @Nullable String moreMessage) {
        super(getModNameClashMessage(message) + "Element implement names are '" + ElementImplementName + "'." + moreMessage);
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable Class<? extends ElementImplement> ElementImplementClass1, @Nullable Class<? extends ElementImplement> ElementImplementClass2) {
        super(getModNameClashMessage(message) + "Element implement names are '" + ElementImplement.getElementNameFromClass(ElementImplementClass1) + "'." + ModManager.crashClassInformation(ElementImplementClass1) + ModManager.crashClassInformation(ElementImplementClass2));
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable Class<? extends ElementImplement> ElementImplementClass1, @Nullable Class<? extends ElementImplement> ElementImplementClass2, @Nullable String moreMessage) {
        super(getModNameClashMessage(message) + "Element implement names are '" + ElementImplement.getElementNameFromClass(ElementImplementClass1) + "'." + moreMessage + ModManager.crashClassInformation(ElementImplementClass1) + ModManager.crashClassInformation(ElementImplementClass2));
    }

    public ElementImplementNameClashException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ElementImplementNameClashException(@Nullable ElementName ElementImplementName, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Element implement names are '" + ElementImplementName + "'.", throwable);
    }

    public ElementImplementNameClashException(@Nullable ElementName ElementImplementName, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Element implement names are '" + ElementImplementName + "'." + moreMessage, throwable);
    }

    public ElementImplementNameClashException(@Nullable Class<? extends ElementImplement> ElementImplementClass1, @Nullable Class<? extends ElementImplement> ElementImplementClass2, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Element implement names are '" + ElementImplement.getElementNameFromClass(ElementImplementClass1) + "'." + ModManager.crashClassInformation(ElementImplementClass1) + ModManager.crashClassInformation(ElementImplementClass2), throwable);
    }

    public ElementImplementNameClashException(@Nullable Class<? extends ElementImplement> ElementImplementClass1, @Nullable Class<? extends ElementImplement> ElementImplementClass2, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Element implement names are '" + ElementImplement.getElementNameFromClass(ElementImplementClass1) + "'." + moreMessage + ModManager.crashClassInformation(ElementImplementClass1) + ModManager.crashClassInformation(ElementImplementClass2), throwable);
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message), throwable);
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable ElementName ElementImplementName, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message) + "Element implement names are '" + ElementImplementName + "'.", throwable);
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable ElementName ElementImplementName, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message) + "Element implement names are '" + ElementImplementName + "'." + moreMessage, throwable);
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable Class<? extends ElementImplement> ElementImplementClass1, @Nullable Class<? extends ElementImplement> ElementImplementClass2, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message) + "Element implement names are '" + ElementImplement.getElementNameFromClass(ElementImplementClass1) + "'." + ModManager.crashClassInformation(ElementImplementClass1) + ModManager.crashClassInformation(ElementImplementClass2), throwable);
    }

    public ElementImplementNameClashException(@Nullable String message, @Nullable Class<? extends ElementImplement> ElementImplementClass1, @Nullable Class<? extends ElementImplement> ElementImplementClass2, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message) + "Element implement names are '" + ElementImplement.getElementNameFromClass(ElementImplementClass1) + "'." + moreMessage + ModManager.crashClassInformation(ElementImplementClass1) + ModManager.crashClassInformation(ElementImplementClass2), throwable);
    }
}
