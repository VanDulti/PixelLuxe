package at.jku.pixelluxe.filter.convolution;

public class Kernels {

	// --------------------------------------
	//				Sharpen/Blur
	// --------------------------------------
	public static Kernel sharpen = new Kernel(
			new double[][]{
					{0, -1, 0},
					{-1, 5, -1},
					{0, -1, 0}
			}, 1, "detail"
	);

	public static Kernel emboss = new Kernel(
			new double[][]{
					{-2, -1, 0},
					{-1, 1, 1},
					{0, 1, 2}
			}, 1, "detail"
	);

	public static Kernel gauss = new Kernel(
			new double[][]{
					{1, 2, 1},
					{2, 4, 2},
					{1, 2, 1}
			}, 16, "detail"
	);

	public static Kernel meanBlur = new Kernel(
			new double[][]{
					{1, 1, 1},
					{1, 1, 1},
					{1, 1, 1}
			}, 9, "detail"
	);

	// --------------------------------------
	//				Edge Detection
	// --------------------------------------
	public static Kernel horizontal = new Kernel(
			new double[][]{
					{-1, 0, 1},
					{-1, 0, 1},
					{-1, 0, 1}
			}, 1, "edge"
	);

	public static Kernel vertical = new Kernel(
			new double[][]{
					{-1, -1, -1},
					{0, 0, 0},
					{1, 1, 1}
			}, 1, "edge"
	);

	public static Kernel laplace = new Kernel(
			new double[][]{
					{0, 1, 0},
					{1, -4, 1},
					{0, 1, 0}
			}, 1, "edge"
	);

	public static Kernel outline = new Kernel(
			new double[][]{
					{-1, -1, -1},
					{-1, 8, -1},
					{-1, -1, -1}
			}, 1, "edge"
	);

	// --------------------------------------
	//					Sobel
	// --------------------------------------
	public static Kernel bottomSobel = new Kernel(
			new double[][]{
					{-1, -2, -1},
					{0, 0, 0},
					{1, 2, 1}
			}, 1, "sobelH"
	);

	public static Kernel topSobel = new Kernel(
			new double[][]{
					{1, 2, 1},
					{0, 0, 0},
					{-1, -2, -1}
			}, 1, "sobelH"
	);

	public static Kernel rightSobel = new Kernel(
			new double[][]{
					{-1, 0, 1},
					{-2, 0, 2},
					{-1, 0, 1}
			}, 1, "sobelV"
	);

	public static Kernel leftSobel = new Kernel(
			new double[][]{
					{1, 0, -1},
					{2, 0, -2},
					{1, 0, -1}
			}, 1, "sobelV"
	);
}