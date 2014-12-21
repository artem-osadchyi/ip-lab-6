package org.insane.ip.l6;

public class RobinsonsFilter extends GradientFilter {

    public static enum Mask implements Filter {
        EAST(new int[][] { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } }),
        NORTH_EAST(new int[][] { { 0, 1, 2 }, { -1, 0, 1 }, { -2, -1, 0 } }),
        NORTH(new int[][] { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } }),
        NORTH_WEST(new int[][] { { 2, 1, 0 }, { 1, 0, -1 }, { 0, -1, -2 } }),
        WEST(new int[][] { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, -1 } }),
        SOUTH_WEST(new int[][] { { 0, -1, -2 }, { 1, 0, 1 }, { 2, 1, 0 } }),
        SOUTH(new int[][] { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } }),
        SOUTH_EAST(new int[][] { { -2, -1, 0 }, { -1, 0, 1 }, { 0, 1, 2 } });

        private final int[][] mask;

        private Mask(int[][] mask) {
            this.mask = mask;
        }

        @Override
        public int apply(int[][] region) {
            int result = 0;

            for (int x = 0; x < getSize(); x++)
                for (int y = 0; y < getSize(); y++)
                    result += mask[x][y] * region[x][y];

            return result;
        }

        @Override
        public int getSize() {
            return mask.length;
        }

    }

    public RobinsonsFilter(Mask dx, Mask dy) {
        super(dx, dy);
    }

}
