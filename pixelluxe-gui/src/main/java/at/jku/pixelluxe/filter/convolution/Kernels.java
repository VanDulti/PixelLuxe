package at.jku.pixelluxe.filter.convolution;

public class Kernels {

	// --------------------------------------
	//				Sharpen/Blur
	// --------------------------------------
	public static Kernel sharpen = new Kernel(
			new int[][]{
					{0, -1, 0},
					{-1, 5, -1},
					{0, -1, 0}
			}, 1, "sharp"
	);

	public static Kernel gauss = new Kernel(
			new int[][]{
					{1, 2, 1},
					{2, 4, 2},
					{1, 2, 1}
			}, 16, "blur"
	);

	public static Kernel meanBlur = new Kernel(
			new int[][]{
					{1, 1, 1},
					{1, 1, 1},
					{1, 1, 1}
			}, 9, "blur"
	);

	// --------------------------------------
	//				Edge Detection
	// --------------------------------------
	public static Kernel horizontal = new Kernel(
			new int[][]{
					{-1, 0, 1},
					{-1, 0, 1},
					{-1, 0, 1}
			}, 1, "edge"
	);

	public static Kernel vertical = new Kernel(
			new int[][]{
					{-1, -1, -1},
					{0, 0, 0},
					{1, 1, 1}
			}, 1, "edge"
	);

	public static Kernel laplace = new Kernel(
			new int[][]{
					{0, 1, 0},
					{1, -4, 1},
					{0, 1, 0}
			}, 1, "edge"
	);

	public static Kernel emboss = new Kernel(
			new int[][]{
					{-2, -1, 0},
					{-1, 1, 1},
					{0, 1, 2}
			}, 1, "emboss"
	);

	public static Kernel outline = new Kernel(
			new int[][]{
					{-1, -1, -1},
					{-1, 8, -1},
					{-1, -1, -1}
			}, 1, "edge"
	);

	// --------------------------------------
	//					Sobel
	// --------------------------------------
	public static Kernel bottomSobel = new Kernel(
			new int[][]{
					{-1, -2, -1},
					{0, 0, 0},
					{1, 2, 1}
			}, 1, "sobelV"
	);

	public static Kernel topSobel = new Kernel(
			new int[][]{
					{1, 2, 1},
					{0, 0, 0},
					{-1, -2, -1}
			}, 1, "sobelV"
	);

	public static Kernel rightSobel = new Kernel(
			new int[][]{
					{-1, 0, 1},
					{-2, 0, 2},
					{-1, 0, 1}
			}, 1, "sobelH"
	);

	public static Kernel leftSobel = new Kernel(
			new int[][]{
					{1, 0, -1},
					{2, 0, -2},
					{1, 0, -1}
			}, 1, "sobelH"
	);
}