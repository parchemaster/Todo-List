package cz.muni.fi.pv168.project.ui.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class I18N {

    private final ResourceBundle bundle;
    private final String prefix;


    public I18N(Class<?> clazz) {
        var packagePath = clazz.getPackageName().replace(".", "/") + "/";
        bundle = ResourceBundle.getBundle(packagePath + "i18n");
        prefix = clazz.getSimpleName() + ".";
    }

    public String getString(String key) {
        return bundle.getString(prefix + key);
    }

    public String getFormattedMessage(String key, Object... arguments) {
        return MessageFormat.format(getString(key), arguments);
    }

    public <E extends Enum<E>> String getString(E key) {
        return bundle.getString(key.getClass().getSimpleName() + "." + key.name());
    }
}
