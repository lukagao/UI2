package com.test.ui2.utils;

import java.io.IOException;

public class ShellUtil {
    public void execShell(String cmd) throws IOException {
        Runtime.getRuntime().exec(cmd);
    }
}
