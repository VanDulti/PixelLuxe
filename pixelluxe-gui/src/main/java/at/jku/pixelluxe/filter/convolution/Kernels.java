package at.jku.pixelluxe.filter.convolution;

public class Kernels {
	public static final Kernel SHARPEN = new Kernel(
			new int[][] {
					{0, -1, 0},
					{-1, 5, -1},
					{0, -1, 0}
			}, 1
	);

	public static final Kernel EDGE_V = new Kernel(
			new int[][] {
					{-1, 0, 1},
					{-1, 0, 1},
					{-1, 0, 1}
			}, 1
	);

	public static final Kernel EDGE_H = new Kernel(
			new int[][] {
					{-1, -1, -1},
					{0, 0, 0},
					{1, 1, 1}
			}, 1
	);
}
