package Core.Exceptions;

import Core.Addition.Element.BasicInformation.ElementName;
import Core.Addition.Element.ElementUtil;
import Core.Addition.ModManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when element util names clash.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
public class ElementUtilNameClashException extends ElementImplementInformationException {
    @Serial
    private static final long serialVersionUID = -6560058512796698990L;

    private static final String DEFAULT_MESSAGE = "Element utils name clash!";
    private static @NotNull String getElementUtilNameClashMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ElementUtilNameClashException() {
        super(DEFAULT_MESSAGE);
    }

    public ElementUtilNameClashException(@Nullable ElementName ElementUtilName) {
        super(DEFAULT_MESSAGE + "Element util names are '" + ElementUtilName + "'.");
    }

    public ElementUtilNameClashException(@Nullable ElementName ElementUtilName, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + "Element util names are '" + ElementUtilName + "'." + moreMessage);
    }

    public ElementUtilNameClashException(@Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass1, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass2) {
        super(DEFAULT_MESSAGE + "Element util names are '" + ElementUtil.getElementNameFromClass(ElementUtilClass1) + "'." + ModManager.crashClassInformation(ElementUtilClass1) + ModManager.crashClassInformation(ElementUtilClass2));
    }

    public ElementUtilNameClashException(@Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass1, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass2, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + "Element util names are '" + ElementUtil.getElementNameFromClass(ElementUtilClass1) + "'." + moreMessage + ModManager.crashClassInformation(ElementUtilClass1) + ModManager.crashClassInformation(ElementUtilClass2));
    }

    public ElementUtilNameClashException(@Nullable String message) {
        super(getElementUtilNameClashMessage(message));
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable ElementName ElementUtilName) {
        super(getElementUtilNameClashMessage(message) + "Element util names are '" + ElementUtilName + "'.");
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable ElementName ElementUtilName, @Nullable String moreMessage) {
        super(getElementUtilNameClashMessage(message) + "Element util names are '" + ElementUtilName + "'." + moreMessage);
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass1, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass2) {
        super(getElementUtilNameClashMessage(message) + "Element util names are '" + ElementUtil.getElementNameFromClass(ElementUtilClass1) + "'." + ModManager.crashClassInformation(ElementUtilClass1) + ModManager.crashClassInformation(ElementUtilClass2));
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass1, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass2, @Nullable String moreMessage) {
        super(getElementUtilNameClashMessage(message) + "Element util names are '" + ElementUtil.getElementNameFromClass(ElementUtilClass1) + "'." + moreMessage + ModManager.crashClassInformation(ElementUtilClass1) + ModManager.crashClassInformation(ElementUtilClass2));
    }

    public ElementUtilNameClashException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ElementUtilNameClashException(@Nullable ElementName ElementUtilName, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Element util names are '" + ElementUtilName + "'.", throwable);
    }

    public ElementUtilNameClashException(@Nullable ElementName ElementUtilName, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Element util names are '" + ElementUtilName + "'." + moreMessage, throwable);
    }

    public ElementUtilNameClashException(@Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass1, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass2, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Element util names are '" + ElementUtil.getElementNameFromClass(ElementUtilClass1) + "'." + ModManager.crashClassInformation(ElementUtilClass1) + ModManager.crashClassInformation(ElementUtilClass2), throwable);
    }

    public ElementUtilNameClashException(@Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass1, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass2, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Element util names are '" + ElementUtil.getElementNameFromClass(ElementUtilClass1) + "'." + moreMessage + ModManager.crashClassInformation(ElementUtilClass1) + ModManager.crashClassInformation(ElementUtilClass2), throwable);
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable Throwable throwable) {
        super(getElementUtilNameClashMessage(message), throwable);
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable ElementName ElementUtilName, @Nullable Throwable throwable) {
        super(getElementUtilNameClashMessage(message) + "Element util names are '" + ElementUtilName + "'.", throwable);
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable ElementName ElementUtilName, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getElementUtilNameClashMessage(message) + "Element util names are '" + ElementUtilName + "'." + moreMessage, throwable);
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass1, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass2, @Nullable Throwable throwable) {
        super(getElementUtilNameClashMessage(message) + "Element util names are '" + ElementUtil.getElementNameFromClass(ElementUtilClass1) + "'." + ModManager.crashClassInformation(ElementUtilClass1) + ModManager.crashClassInformation(ElementUtilClass2), throwable);
    }

    public ElementUtilNameClashException(@Nullable String message, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass1, @Nullable Class<? extends ElementUtil<?, ?>> ElementUtilClass2, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getElementUtilNameClashMessage(message) + "Element util names are '" + ElementUtil.getElementNameFromClass(ElementUtilClass1) + "'." + moreMessage + ModManager.crashClassInformation(ElementUtilClass1) + ModManager.crashClassInformation(ElementUtilClass2), throwable);
    }
}
