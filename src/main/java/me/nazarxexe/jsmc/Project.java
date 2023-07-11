package me.nazarxexe.jsmc;

import java.io.File;

public interface Project {
    void eval();
    void stop();

    boolean isEnabled();
    File file();
}
