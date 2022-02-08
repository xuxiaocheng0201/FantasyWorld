package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;

public class HConfig {
    private String name;
    private String note;
    private HEConfigType type;
    private String value;

    public HConfig(String name, String note, HEConfigType type, String value) {
        this.setName(name);
        this.note = note;
        this.type = type;
        this.setValue(value);
    }

    public HConfig(String name, String note, String value) {
        this(name, note, HEConfigType.STRING, value);
    }

    public HConfig(String name, HEConfigType type, String value) {
        this(name, null, type, value);
    }

    public HConfig(String name, String value) {
        this(name, null, HEConfigType.STRING, value);
    }
    
    public void setName(String name) {
        this.name = ((name == null) ? "null" : name);
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setType(HEConfigType type) {
        this.type = type;
    }

    public void setValue(String value) {
        if (this.type.checkString(value))
            this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getNote() {
        return this.note;
    }

    public HEConfigType getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("HConfig{",
                "name='", name, '\'',
                ", note='", note, '\'',
                ", type=", type,
                ", value='", value, '\'',
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfig))
            return false;
        return this.name.equals(((HConfig) a).name) && this.type == ((HConfig) a).type && this.value.equals(((HConfig) a).value);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
