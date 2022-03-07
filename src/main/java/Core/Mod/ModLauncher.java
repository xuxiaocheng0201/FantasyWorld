package Core.Mod;

import Core.Exception.ModRequirementsException;
import Core.Exception.ModRequirementsFormatException;
import Core.Mod.New.NewElementImplement;
import HeadLibs.Helper.HStringHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModLauncher {
    public static void sortMods() {
        List<Class<?>> mods = ModClassesLoader.getModList();
        List<ModRequirementsException> exceptions = new ArrayList<>();
        mods.sort(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                int result = 0;
                String name1 = o1.getAnnotation(NewElementImplement.class).name();
                String name2 = o2.getAnnotation(NewElementImplement.class).name();
                String[] require1 = o1.getAnnotation(NewElementImplement.class).required().split(",");
                String[] require2 = o2.getAnnotation(NewElementImplement.class).required().split(",");
                for (String require : require1) {
                    if (require.startsWith("after:")) {
                        String requireMod = require.substring(6);
                        String[] mod = requireMod.split("@");
                        if (mod.length != 2)
                            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        if (mod[0].equals(name2)) {
                            int can_equal_left = 0;
                            if (mod[1].charAt(0) == '(')
                                can_equal_left = -1;
                            if (mod[1].charAt(0) == '[')
                                can_equal_left = 1;
                            if (can_equal_left == 0) {
                                exceptions.add(new ModRequirementsFormatException());
                            }
                        }
                    } else if (require.startsWith("before:")) {

                    }
                }
                return result;
            }
        });
    }
}
