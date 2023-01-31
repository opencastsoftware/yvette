package com.opencastsoftware.yvette.handlers.graphical;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.function.Predicate;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;
import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.internal.CLibrary;

class TerminalSupport {
    public static boolean isAtty(Appendable output) {
        try {
            if (output instanceof PrintStream) {
                PrintStream outStream = (PrintStream) output;
                if (System.out.equals(outStream)) {
                    return CLibrary.isatty(CLibrary.STDOUT_FILENO) != 0;
                } else if (System.err.equals(outStream)) {
                    return CLibrary.isatty(CLibrary.STDERR_FILENO) != 0;
                } else {
                    // Can't easily get the file descriptor for anything else
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            // Couldn't load the Jansi library on this platform?
            return false;
        }
    }

    public static int terminalWidth(Appendable output) {
        if (!isAtty(output)) {
            return 80;
        } else {
            return AnsiConsole.getTerminalWidth();
        }
    }

    /*
     * A port of the logic from
     * https://docs.rs/supports-hyperlinks/1.2.0/supports_hyperlinks/
     */
    public static boolean supportsHyperlinks(Appendable output) {
        if (!isAtty(output)) {
            return false;
        }

        if (envVarSet("DOMTERM")) {
            return true;
        }

        String vteVersionVar = System.getenv("VTE_VERSION");
        if (vteVersionVar != null) {
            int vteVersion;
            try {
                vteVersion = Integer.parseInt(vteVersionVar);
            } catch (NumberFormatException e) {
                vteVersion = 0;
            }

            if (vteVersion >= 5000) {
                return true;
            }
        }

        String termProgramVar = System.getenv("TERM_PROGRAM");
        if ("Hyper".equals(termProgramVar)
                || "iTerm.app".equals(termProgramVar)
                || "terminology".equals(termProgramVar)
                || "WezTerm".equals(termProgramVar)) {
            return true;
        }

        String termVar = System.getenv("TERM");
        if ("xterm-kitty".equals(termVar)) {
            return true;
        }

        return envVarSet("WT_SESSION") || envVarSet("KONSOLE_VERSION");
    }

    /*
     * A port of the logic from
     * https://docs.rs/supports-unicode/1.0.2/supports_unicode/
     */
    public static boolean supportsUnicode(Appendable output) {
        if (!isAtty(output)) {
            return true;
        } else if (SystemUtils.IS_OS_WINDOWS) {
            return isCI()
                    || envVarSet("WT_SESSION")
                    || envVarEquals("ConEmuTask", "{cmd:Cmder}")
                    || envVarEquals("TERM_PROGRAM", "vscode")
                    || envVarEquals("TERM", "xterm-256color")
                    || envVarEquals("TERM", "alacritty");
        } else if (envVarEquals("TERM", "linux")) {
            return false;
        } else {
            String cType = ObjectUtils.firstNonNull(
                    System.getenv("LC_ALL"),
                    System.getenv("LC_CTYPE"),
                    System.getenv("LANG"),
                    "").toUpperCase();

            return cType.endsWith("UTF8") || cType.endsWith("UTF-8");
        }
    }

    static boolean checkAnsiColourSupport(String varName) {
        return envVarIn(varName,
                "screen", "xterm", "vt100",
                "vt220", "rxvt", "color",
                "ansi", "cygwin", "linux");
    }

    static boolean check256ColourSupport(String varName) {
        return envVarMatches(varName, t -> t.endsWith("256") || t.endsWith("256color"));
    }

    public static ColourSupport colourSupport(Appendable output) {
        if (envVarNotEquals("NO_COLOR", "") || envVarEquals("TERM", "dumb") || !isAtty(output)) {
            return ColourSupport.NONE;
        } else if (envVarEquals("COLORTERM", "truecolor")
                || envVarEquals("TERM_PROGRAM", "iTerm.app")) {
            return ColourSupport.COLOUR_16M;
        } else if (envVarEquals("TERM_PROGRAM", "Apple_Terminal")
                || check256ColourSupport("TERM")) {
            return ColourSupport.COLOUR_256;
        } else if (envVarSet("COLORTERM")
                || checkAnsiColourSupport("TERM")
                || SystemUtils.IS_OS_WINDOWS
                || envVarNotEquals("CLICOLOR", "0")
                || isCI()) {
            return ColourSupport.COLOUR_16;
        } else {
            return ColourSupport.NONE;
        }
    }

    /*
     * A port of the logic from https://docs.rs/is_ci/1.1.1/is_ci/
     */
    static boolean isCI() {
        return envVarIn("CI", "true", "1", "woodpecker")
                || envVarSet("CI_NAME")
                || envVarSet("GITHUB_ACTION")
                || envVarSet("GITLAB_CI")
                || envVarSet("NETLIFY")
                || envVarSet("TRAVIS")
                || envVarEnds("NODE", "//heroku/node/bin/node")
                || envVarSet("CODEBUILD_SRC_DIR")
                || envVarSet("BUILDER_OUTPUT")
                || envVarSet("GITLAB_DEPLOYMENT")
                || envVarSet("NOW_GITHUB_DEPLOYMENT")
                || envVarSet("NOW_BUILDER")
                || envVarSet("BITBUCKET_DEPLOYMENT")
                || envVarSet("GERRIT_PROJECT")
                || envVarSet("SYSTEM_TEAMFOUNDATIONCOLLECTIONURI")
                || envVarSet("BITRISE_IO")
                || envVarSet("BUDDY_WORKSPACE_ID")
                || envVarSet("BUILDKITE")
                || envVarSet("CIRRUS_CI")
                || envVarSet("APPVEYOR")
                || envVarSet("CIRCLECI")
                || envVarSet("SEMAPHORE")
                || envVarSet("DRONE")
                || envVarSet("DSARI")
                || envVarSet("TDDIUM")
                || envVarSet("STRIDER")
                || envVarSet("TASKCLUSTER_ROOT_URL")
                || envVarSet("JENKINS_URL")
                || envVarSet("bamboo.buildKey")
                || envVarSet("GO_PIPELINE_NAME")
                || envVarSet("HUDSON_URL")
                || envVarSet("WERCKER")
                || envVarSet("MAGNUM")
                || envVarSet("NEVERCODE")
                || envVarSet("RENDER")
                || envVarSet("SAIL_CI")
                || envVarSet("SHIPPABLE");
    }

    static boolean envVarEnds(String varName, String endString) {
        String varValue = System.getenv(varName);
        return varValue != null && varValue.endsWith(endString);
    }

    static boolean envVarSet(String varName) {
        String varValue = System.getenv(varName);
        return varValue != null;
    }

    static boolean envVarIn(String varName, String... values) {
        String varValue = System.getenv(varName);
        return varValue != null && Arrays.stream(values).anyMatch(v -> v.equals(varValue));
    }

    static boolean envVarNotIn(String varName, String... values) {
        String varValue = System.getenv(varName);
        return varValue != null && Arrays.stream(values).noneMatch(v -> v.equals(varValue));
    }

    static boolean envVarMatches(String varName, Predicate<String> valueFn) {
        String varValue = System.getenv(varName);
        return varValue != null && valueFn.test(varValue);
    }

    static boolean envVarEquals(String varName, String onValue) {
        String varValue = System.getenv(varName);
        return varValue != null && onValue.equals(varValue);
    }

    static boolean envVarNotEquals(String varName, String offValue) {
        String varValue = System.getenv(varName);
        return varValue != null && !offValue.equals(varValue);
    }
}
