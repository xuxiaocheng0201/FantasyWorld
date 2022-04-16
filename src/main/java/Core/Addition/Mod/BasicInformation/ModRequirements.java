package Core.Addition.Mod.BasicInformation;

import HeadLibs.Version.HVersionComplex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModRequirements {
    private final List<Requirement> requirements = new ArrayList<>();

    public List<Requirement> getRequirements() {
        return this.requirements;
    }

    public static class Requirement {
        private @NotNull Modifier modifier;
        private @NotNull ModName modName;
        private @NotNull HVersionComplex versionComplex;

        public Requirement() {
            super();
            this.modifier = Modifier.AFTER;
            this.modName = new ModName();
            this.versionComplex = new HVersionComplex();
            this.versionComplex.setAll();
        }

        public @NotNull Modifier getModifier() {
            return this.modifier;
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

        public void setModName(@Nullable ModName modName) {
            if (modName == null) {
                this.modName = new ModName();
                return;
            }
            this.modName = modName;
        }

        public @NotNull HVersionComplex getVersionComplex() {
            return this.versionComplex;
        }

        public void setVersionComplex(@Nullable HVersionComplex versionComplex) {
            if (versionComplex == null) {
                this.versionComplex = new HVersionComplex();
                return;
            }
            this.versionComplex = versionComplex;
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
            BEFORE(BasicModifier.BEFORE, false),
            REQUIRE_BEFORE(BasicModifier.BEFORE, true),
            AFTER(BasicModifier.AFTER, false),
            REQUIRE_AFTER(BasicModifier.AFTER, true);

            private final BasicModifier basic;
            private final boolean required;

            Modifier(BasicModifier basic, boolean required) {
                this.basic = basic;
                this.required = required;
            }

            public BasicModifier getBasic() {
                return this.basic;
            }

            public boolean isRequired() {
                return this.required;
            }

            @Override
            public String toString() {
                return (this.required ? "require-" : "") + this.basic.toString();
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
