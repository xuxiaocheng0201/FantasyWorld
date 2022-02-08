package HeadLibs.Logger;

import HeadLibs.Helper.HStringHelper;

public enum HELogLevel {
    FINEST("FINEST", 0),
    FINER("FINER", 10),
    FINE("FINE", 20),
    INFO("INFO", 100),
    WARN("WARN", 200, "1;33"),
    CONFIGURATION("CONFIGURATION", 230, "3;35"),
    MISTAKE("MISTAKE", 250, "1;3;34"),
    ERROR("ERROR", 300, "1;4;34;41"),
    BUG("BUG", 400, "1;4;41"),
    NORMAL("NORMAL", 0),
    DEBUG("DEBUG", 500, "1;3;4;30;44");

    private final String name;
    // Higher priority means higher chance to be logged.
    private final int PRIORITY;
    /**
     * From https://www.cnblogs.com/gzj03/p/14425860.html
     * 首先，说下输出格式："\033[数字;.....;数字;数字m"
     * 数字代码：
     * 0 恢复控制台本身的格式；
     * 1 加粗；2 正常；3 斜体；4 下划线；9 划线；
     * 前景色 30-37；背景色 40-47；
     * 可以在里面加上无数的分号写上相应的代码，但是不建议这么做，全是数字，理解起来太麻烦了，如下：
     * * System.out.println("\033[31;0;42;30;1;2;3;4;41m" + "显示");
     *
     * 前景色和后景色大致预览：
     * 0 -> Black
     * 1 -> Red
     * 2 -> Green
     * 3 -> Brown
     * 4 -> Blue
     * 5 -> Purple
     * 6 -> Cyan
     * 7 -> Gray
     */
    private final String PREFIX;

    HELogLevel(String name, int priority) {
        this.name = name;
        this.PRIORITY = priority;
        this.PREFIX = "";
    }

    HELogLevel(String name, int priority, String prefix) {
        this.name = name;
        this.PRIORITY = priority;
        this.PREFIX = HStringHelper.merge("\033[", prefix, "m");
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return PRIORITY;
    }

    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("HELogLevel{",
                "name='", name, '\'',
                '}');
    }
}
