package Core.Mod;

import Core.Exception.ModRequirementsException;
import Core.Exception.ModRequirementsFormatException;
import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;
import HeadLibs.HVersionComparator;
import HeadLibs.Helper.HStringHelper;

import java.util.ArrayList;
import java.util.List;

public class ModLauncher {
    private static List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModRequirementsException> exceptions = new ArrayList<>();

    public static List<Class<? extends ModImplement>> getSortedMods() {
        return sortedMods;
    }

    public static List<ModRequirementsException> getExceptions() {
        return exceptions;
    }

    public static void sortMods() {
        List<Class<? extends ModImplement>> mods = ModClassesLoader.getModList();
        mods.sort((o1, o2) -> {
            String name1 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o1.getAnnotation(Mod.class).name()));
            String name2 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o2.getAnnotation(Mod.class).name()));
            String version1 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o1.getAnnotation(Mod.class).version()));
            String version2 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o2.getAnnotation(Mod.class).version()));
            String[] require1 = HStringHelper.noNull(o1.getAnnotation(Mod.class).require()).split(";");
            String[] require2 = HStringHelper.noNull(o2.getAnnotation(Mod.class).require()).split(";");
            for (String require_source : require2) {
                String require = HStringHelper.delBlankHeadAndTail(require_source);
                if (require.equals("before:*"))
                    return 1;
                if (require.equals("after:*"))
                    return -1;
                if (require.startsWith("after:") || require.startsWith("require-after:")) {
                    String requireMod = require.startsWith("after:") ? require.substring(6) : require.substring(14);
                    String[] mod = requireMod.split("@");
                    if (mod.length != 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        continue;
                    }
                    mod[0] = HStringHelper.delBlankHeadAndTail(mod[0]);
                    mod[1] = HStringHelper.delBlankHeadAndTail(mod[1]);
                    if (mod[0].equals(name1)) {
                        if (checkVersionSort(mod[1], version1))
                            continue;
                        return -1;
                    }
                }
                if (require.startsWith("before:") || require.startsWith("require-before:")) {
                    String requireMod = require.startsWith("before:") ? require.substring(7) : require.substring(15);
                    String[] mod = requireMod.split("@");
                    if (mod.length != 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        continue;
                    }
                    mod[0] = HStringHelper.delBlankHeadAndTail(mod[0]);
                    mod[1] = HStringHelper.delBlankHeadAndTail(mod[1]);
                    if (mod[0].equals(name1)) {
                        if (checkVersionSort(mod[1], version1))
                            continue;
                        return 1;
                    }
                }
            }
            for (String require_source : require1) {
                String require = HStringHelper.delBlankHeadAndTail(require_source);
                if (require.equals("before:*"))
                    return -1;
                if (require.equals("after:*"))
                    return 1;
                if (require.startsWith("after:") || require.startsWith("require-after:")) {
                    String requireMod = require.startsWith("after:") ? require.substring(6) : require.substring(14);
                    String[] mod = requireMod.split("@");
                    if (mod.length != 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        continue;
                    }
                    mod[0] = HStringHelper.delBlankHeadAndTail(mod[0]);
                    mod[1] = HStringHelper.delBlankHeadAndTail(mod[1]);
                    if (mod[0].equals(name2)) {
                        if (checkVersionSort(mod[1], version2))
                            continue;
                        return 1;
                    }
                }
                if (require.startsWith("before:") || require.startsWith("require-before:")) {
                    String requireMod = require.startsWith("before:") ? require.substring(7) : require.substring(15);
                    String[] mod = requireMod.split("@");
                    if (mod.length != 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        continue;
                    }
                    mod[0] = HStringHelper.delBlankHeadAndTail(mod[0]);
                    mod[1] = HStringHelper.delBlankHeadAndTail(mod[1]);
                    if (mod[0].equals(name2)) {
                        if (checkVersionSort(mod[1], version2))
                            continue;
                        return -1;
                    }
                }
            }
            return 0;
        });
        sortedMods = mods;
    }

    private static boolean checkVersionSort(String require, String version) {
        byte can_equal_left = 0;
        if (require.charAt(0) == '(')
            can_equal_left = -1;
        if (require.charAt(0) == '[')
            can_equal_left = 1;
        if (can_equal_left == 0) {
            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Interval error. ", require)));
            return true;
        }
        byte can_equal_right = 0;
        if (require.charAt(require.length() - 1) == ')')
            can_equal_right = -1;
        if (require.charAt(require.length() - 1) == ']')
            can_equal_right = 1;
        if (can_equal_right == 0) {
            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Interval error. ", require)));
            return true;
        }
        String[] interval = require.substring(1, require.length() - 1).split(",");
        boolean is_error = true;
        if (interval.length == 1) {
            if (require.charAt(1) == ',') {
                int compare = HVersionComparator.compareVersion(interval[0], version);
                if ((compare == 0 && can_equal_right == 1) || compare < 0)
                    is_error = false;
            }
            if (require.charAt(require.length() - 2) == ',') {
                int compare = HVersionComparator.compareVersion(interval[0], version);
                if ((compare == 0 && can_equal_left == 1) || compare > 0)
                    is_error = false;
            }
        }
        if (interval.length == 2) {
            boolean left_result = true;
            int left_compare = HVersionComparator.compareVersion(interval[0], version);
            boolean right_result = true;
            int right_compare = HVersionComparator.compareVersion(interval[1], version);
            if ((left_compare == 0 && can_equal_left == 1) || left_compare > 0)
                left_result = false;
            if ((right_compare == 0 && can_equal_right == 1) || right_compare > 0)
                right_result = false;
            is_error = left_result || right_result;
        }
        if (is_error) {
            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("To many ',' in mod expression. ", require)));
            return true;
        }
        return false;
    }
}
