package HeadLibs.Configuration.SimpleMode;

import HeadLibs.Helper.HStringHelper;

public class HConfigSimple {
    private String name;
    private String value;

    public HConfigSimple(String name, String value) {
        this.setName(name);
        this.setValue(value);
    }

    public void setName(String name) {
        this.name = ((name == null) ? "null" : name);
    }

    public void setValue(String value) {
        this.value = ((value == null) ? "null" : value);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("HConfigSimple{",
                "name='", name, '\'',
                ", value='", value, '\'',
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfigSimple))
            return false;
        return this.name.equals(((HConfigSimple) a).name) && this.value.equals(((HConfigSimple) a).value);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
