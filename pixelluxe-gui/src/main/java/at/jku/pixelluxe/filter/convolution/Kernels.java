package at.jku.pixelluxe.filter.convolution;

public class Kernels {
	public static final Kernel SHARPEN = new Kernel(
			new int[][] {
					{0, -1, 0},
					{-1, 5, -1},
					{0, -1, 0}
			}, 1
	);

	public static final Kernel YEDGE = new Kernel(
			new int[][] {
					{-1, 0, 1},
					{-1, 0, 1},
					{-1, 0, 1}
			}, 1
	);

	public static final Kernel XEDGE = new Kernel(
			new int[][] {
					{-1, -1, -1},
					{0, 0, 0},
					{1, 1, 1}
			}, 1
	);

	public static final Kernel LAPLACE = new Kernel(
			new int[][] {
					{0, 1, 0},
					{1, -4, 1},
					{0, 1, 0}
			}, 1
	);

	public static final Kernel GAUSS = new Kernel(
			new int[][] {
					{1, 2, 1},
					{2, 4, 2},
					{1, 2, 1}
			}, 16
	);

	public static final Kernel MEANBLUR = new Kernel(
			new int[][] {
					{1, 1, 1},
					{1, 1, 1},
					{1, 1, 1}
			}, 9
	);

	public static final Kernel XSOBEL = new Kernel(
			new int[][] {
					{-1, -2, -1},
					{0, 0, 0},
					{1, 2, 1}
			}, 1
	);

	public static final Kernel YSOBEL = new Kernel(
			new int[][] {
					{-1, 0, 1},
					{-2, 0, 2},
					{-1, 0, 1}
			}, 1
	);
}
