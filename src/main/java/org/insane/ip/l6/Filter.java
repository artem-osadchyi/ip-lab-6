package org.insane.ip.l6;

public interface Filter {

    int apply(int[][] region);

    int getSize();

}
