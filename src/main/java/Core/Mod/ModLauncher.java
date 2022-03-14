package Core.Mod;

import Core.Exception.ModRequirementsException;
import Core.Exception.ModRequirementsFormatException;
import Core.Exception.ModVersionUnmatchedException;
import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;
import HeadLibs.HVersionComparator;
import HeadLibs.Helper.HStringHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModLauncher {
    private static List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModRequirementsException> exceptions = new ArrayList<>();
    private static final Set<Class<? extends ModImplement>> exceptions_flag = new HashSet<>();

    public static List<Class<? extends ModImplement>> getSortedMods() {
        return sortedMods;
    }

    public static List<ModRequirementsException> getExceptions() {
        return exceptions;
    }

    public static void sortMods() {
        sortedMods = ModClassesLoader.getModList();
        sortedMods.sort((o1, o2) -> {
            int result1 = 0;
            int result2 = 0;
            String name1 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o1.getAnnotation(Mod.class).name()));
            String name2 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o2.getAnnotation(Mod.class).name()));
            String version1 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o1.getAnnotation(Mod.class).version()));
            String version2 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o2.getAnnotation(Mod.class).version()));
            String[] require1 = HStringHelper.noNull(o1.getAnnotation(Mod.class).require()).split(";");
            String[] require2 = HStringHelper.noNull(o2.getAnnotation(Mod.class).require()).split(";");
            for (String require_source : require1) {
                String require = HStringHelper.delBlankHeadAndTail(require_source);
                if ((require.startsWith("after:") || require.startsWith("require-after:")) && !require.equals("after:*")) {
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
                        result1 = 1;
                        break;
                    }
                }
                if ((require.startsWith("before:") || require.startsWith("require-before:")) && !require.equals("before:*")) {
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
                        result1 = -1;
                        break;
                    }
                }
                if (require.equals("after:*")) {
                    result1 = 2;
                    break;
                }
                if (require.equals("before:*")) {
                    result1 = -2;
                    break;
                }
            }
            for (String require_source : require2) {
                String require = HStringHelper.delBlankHeadAndTail(require_source);
                if ((require.startsWith("after:") || require.startsWith("require-after:")) && !require.equals("after:*")) {
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
                        result2 = -1;
                        break;
                    }
                }
                if ((require.startsWith("before:") || require.startsWith("require-before:")) && !require.equals("before:*")) {
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
                        result2 = 1;
                        break;
                    }
                }
                if (require.equals("after:*")) {
                    result2 = -2;
                    break;
                }
                if (require.equals("before:*")) {
                    result2 = 2;
                    break;
                }
            }
            if (result1 == 1)
                return 1;
            if (result1 == -1)
                return -1;
            if (result2 == 1)
                return 1;
            if (result2 == -1)
                return -1;
            if (result1 == 2)
                return 2;
            if (result1 == -2)
                return -2;
            if (result2 == 2)
                return 2;
            if (result2 == -2)
                return -2;
            return 0;
        });
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
        if (interval.length > 2) {
            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("To many ',' in mod expression. ", require)));
            return true;
        }
        if (interval.length == 0)
            return false;
        boolean is_error = true;
        if (interval.length == 1) {
            if (require.charAt(1) == ',') {
                int compare = HVersionComparator.compareVersion(interval[0], version);
                if ((compare == 0 && can_equal_right == 1) || compare > 0)
                    is_error = false;
            }
            if (require.charAt(require.length() - 2) == ',') {
                int compare = HVersionComparator.compareVersion(interval[0], version);
                if ((compare == 0 && can_equal_left == 1) || compare < 0)
                    is_error = false;
            }
        }
        if (interval.length == 2) {
            boolean left_result = true;
            int left_compare = HVersionComparator.compareVersion(interval[0], version);
            boolean right_result = true;
            int right_compare = HVersionComparator.compareVersion(interval[1], version);
            if ((left_compare == 0 && can_equal_left == 1) || left_compare < 0)
                left_result = false;
            if ((right_compare == 0 && can_equal_right == 1) || right_compare > 0)
                right_result = false;
            is_error = left_result || right_result;
        }
        if (is_error) {
            exceptions.add(new ModVersionUnmatchedException(HStringHelper.merge("Version '", version, "' is out of range ", require)));
        }
        return false;
    }
}
