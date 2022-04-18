package Core.Addition.Mod.BasicInformation;

import Core.Exceptions.ModRequirementFormatException;
import HeadLibs.Version.HVersionComplex;
import HeadLibs.Version.HVersionFormatException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mod information - requirements.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModRequirements {
    private final List<Requirement> requirements = new ArrayList<>();

    public ModRequirements() {
        super();
    }

    public ModRequirements(@Nullable String requirements) {
        super();
        this.setRequirements(requirements);
    }

    public @NotNull List<Requirement> getRequirements() {
        return this.requirements;
    }

    public void setRequirements(@Nullable String requirements) throws ModRequirementFormatException {
        this.requirements.clear();
        if (requirements == null || requirements.isBlank())
            return;
        String[] requirementStrings = requirements.split(";");
        for (String requirement: requirementStrings)
            this.requirements.add(new Requirement(requirement));
    }

    @Override
    public String toString() {
        if (this.requirements.isEmpty())
            return "";
        StringBuilder builder = new StringBuilder(10);
        for (Requirement requirement: this.requirements)
            builder.append(requirement).append(';');
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public static class Requirement {
        private @NotNull Modifier modifier = Modifier.AFTER;
        private @NotNull ModName modName = new ModName();
        private @NotNull HVersionComplex versionComplex = new HVersionComplex();

        public Requirement() {
            super();
            this.versionComplex.setAll();
        }

        public Requirement(@Nullable ModName modName) {
            super();
            this.setModName(modName);
            this.versionComplex.setAll();
        }

        public Requirement(@Nullable ModName modName, @Nullable HVersionComplex versionComplex) {
            super();
            this.setModName(modName);
            this.setVersionComplex(versionComplex);
        }

        public Requirement(@Nullable Modifier modifier, @Nullable ModName modName, @Nullable HVersionComplex versionComplex) {
            super();
            this.setModifier(modifier);
            this.setModName(modName);
            this.setVersionComplex(versionComplex);
        }

        public Requirement(@Nullable String modRequirement) throws ModRequirementFormatException {
            super();
            this.setRequirement(modRequirement);
        }

        public @NotNull Modifier getModifier() {
            return this.modifier;
        }

        public void setModifier(@Nullable String modifier) {
            this.modifier = Modifier.getByName(modifier);
        }

        public void setModifier(@Nullable Modifier modifier) {
            if (modifier == null) {
                this.modifier = Modifier.AFTER;
                return;
            }
            this.modifier = modifier;
        }

        public @NotNull ModName getModName() {
            return this.modName;
        }

        public void setModName(@Nullable String modName) {
            this.modName.setName(modName);
        }

        public void setModName(@Nullable ModName modName) {
            if (modName == null) {
                this.modName.setName("");
                return;
            }
            this.modName = modName;
        }

        public @NotNull HVersionComplex getVersionComplex() {
            return this.versionComplex;
        }

        public void setVersionComplex(@Nullable String versionComplex) throws ModRequirementFormatException {
            try {
                this.versionComplex.setVersions(versionComplex);
            } catch (HVersionFormatException exception) {
                throw new ModRequirementFormatException("Version format is wrong.", exception);
            }
        }

        public void setVersionComplex(@Nullable HVersionComplex versionComplex) {
            if (versionComplex == null) {
                this.versionComplex.setAll();
                return;
            }
            this.versionComplex = versionComplex;
        }

        public void setRequirement(String modRequirement) throws ModRequirementFormatException {
            if (modRequirement == null || modRequirement.isBlank()) {
                this.modifier = Modifier.AFTER;
                this.modName.setName("");
                this.versionComplex.setAll();
                return;
            }
            int locationColon = modRequirement.indexOf(':');
            if (locationColon == -1)
                throw new ModRequirementFormatException("No colon in requirement.");
            this.modifier = Modifier.getByName(modRequirement.substring(0, locationColon));
            String modComplexString = modRequirement.substring(locationColon + 1);
            int locationAt = modComplexString.lastIndexOf('@');
            if (locationAt == -1)
                throw new ModRequirementFormatException("No at in requirement.");
            this.modName.setName(modComplexString.substring(0, locationAt));
            try {
                this.versionComplex.setVersions(modComplexString.substring(locationAt + 1));
            } catch (HVersionFormatException exception) {
                throw new ModRequirementFormatException("Version format is wrong.", exception);
            }
        }

        @Override
        public @NotNull String toString() {
            return this.modifier + this.modName.toString() + '@' + this.versionComplex;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            Requirement that = (Requirement) o;
            return this.modifier == that.modifier && this.modName.equals(that.modName) && this.versionComplex.equals(that.versionComplex);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.modName, this.versionComplex);
        }

        public enum Modifier {
            BEFORE("before", BasicModifier.BEFORE, false),
            REQUIRE_BEFORE("require-before", BasicModifier.BEFORE, true),
            AFTER("after", BasicModifier.AFTER, false),
            REQUIRE_AFTER("require-after", BasicModifier.AFTER, true);

            private final String name;
            private final BasicModifier basic;
            private final boolean required;

            Modifier(String name, BasicModifier basic, boolean required) {
                this.name = name;
                this.basic = basic;
                this.required = required;
            }

            public String getName() {
                return this.name;
            }

            public BasicModifier getBasic() {
                return this.basic;
            }

            public boolean isRequired() {
                return this.required;
            }

            public static @NotNull Modifier getByName(@Nullable String name) {
                if (name == null)
                    return AFTER;
                return switch (name.toLowerCase()) {
                    case "before" -> BEFORE;
                    case "require-before" -> REQUIRE_BEFORE;
                    // case "after" -> AFTER;
                    case "require-after" -> REQUIRE_AFTER;
                    default -> AFTER;
                };
            }

            @Override
            public String toString() {
                //return (this.required ? "require-" : "") + this.basic.toString();
                return this.name + ":";
            }

            public enum BasicModifier {
                BEFORE("before"),
                AFTER("after");

                private final String name;

                BasicModifier(String name) {
                    this.name = name;
                }

                public String getName() {
                    return this.name;
                }

                @Override
                public String toString() {
                    return this.name + ':';
                }
            }
        }
    }
}
