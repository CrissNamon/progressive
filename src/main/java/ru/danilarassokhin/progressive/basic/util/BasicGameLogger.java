package ru.danilarassokhin.progressive.basic.util;

public class BasicGameLogger {

    public static final String RESET = "\033[0m";  // Text Reset
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    public static void log(String prefix, String message) {
        System.out.println(prefix + " " + message);
    }

    public static void info(String message) {
        log("[PROGRESSIVE INFO]", message);
    }

    public static void error(String message) {
        log(RED + "[PROGRESSIVE ERROR]" + RESET, message);
    }

    public static void warning(String message) {
        log(YELLOW + "[PROGRESSIVE WARN]" + RESET, message);
    }

}
