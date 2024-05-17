package at.jku.pixelluxe.filter.convolution;

public class Kernels {
	public static final Kernel sharpen = new Kernel(
			new int[][]{
					{0, -1, 0},
					{-1, 5, -1},
					{0, -1, 0}
			}, 1, "sharp"
	);

	public static final Kernel xEdge = new Kernel(
			new int[][]{
					{-1, 0, 1},
					{-1, 0, 1},
					{-1, 0, 1}
			}, 1, "edge"
	);

	public static final Kernel yEdge = new Kernel(
			new int[][]{
					{-1, -1, -1},
					{0, 0, 0},
					{1, 1, 1}
			}, 1, "edge"
	);

	public static final Kernel laplace = new Kernel(
			new int[][]{
					{0, 1, 0},
					{1, -4, 1},
					{0, 1, 0}
			}, 1, "edge"
	);

	public static final Kernel gauss = new Kernel(
			new int[][]{
					{1, 2, 1},
					{2, 4, 2},
					{1, 2, 1}
			}, 16, "blur"
	);

	public static final Kernel meanBlur = new Kernel(
			new int[][]{
					{1, 1, 1},
					{1, 1, 1},
					{1, 1, 1}
			}, 9, "blur"
	);

	public static final Kernel xSobel = new Kernel(
			new int[][]{
					{-1, -2, -1},
					{0, 0, 0},
					{1, 2, 1}
			}, 1, "edge"
	);

	public static final Kernel ySobel = new Kernel(
			new int[][]{
					{-1, 0, 1},
					{-2, 0, 2},
					{-1, 0, 1}
			}, 1, "edge"
	);
}
