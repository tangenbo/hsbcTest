package server.com.hsbc.util;

import java.io.*;
import java.util.*;

public class OSCommand {
    private final static Log log = new Log(OSCommand.class);
    public static Process runOSCommand(String[] cmd, boolean logCmd) throws IOException {
        if (logCmd) {
            log.info("Running exec: " + Arrays.deepToString(cmd));
        }

        return Runtime.getRuntime().exec(cmd);
    }

    public static String gatherStdoutStderr(Process proc) throws IOException {
        return gatherStdoutStderr(proc, null);
    }

    public static String gatherStdoutStderr(Process proc, String prefix) throws IOException {
        StringBuilder sb = new StringBuilder(128);

        InputStreamReader stdIsr = new InputStreamReader(proc.getInputStream());
        BufferedReader std = new BufferedReader(stdIsr);
        InputStreamReader errIsr = new InputStreamReader(proc.getErrorStream());
        BufferedReader err = new BufferedReader(errIsr);

        if (prefix != null) {
            sb.append(prefix);
        }

        // Print the standard output of the command we ran
        String line;
        while ((line = std.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }

        // Print the standard error of the command we ran
        while ((line = err.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }

        return sb.toString();
    }
}
