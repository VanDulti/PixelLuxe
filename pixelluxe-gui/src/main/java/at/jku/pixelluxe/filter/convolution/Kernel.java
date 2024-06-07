package at.jku.pixelluxe.filter.convolution;

import java.util.Arrays;

/**
 * Represents a kernel for image convolution. A kernel consists of a matrix, a factor and a type. The matrix is the
 * actual convolution matrix, the factor is the divisor for the matrix and the type
 */
public enum Kernel {
	SHARPEN(
			new int[][]{
					{0, -1, 0},
					{-1, 5, -1},
					{0, -1, 0}
			},
			1,
			KernelType.DETAIL
	),
	EMBOSS(
			new int[][]{
					{-2, -1, 0},
					{-1, 1, 1},
					{0, 1, 2}
			},
			1,
			KernelType.DETAIL
	),
	GAUSS(
			new int[][]{
					{1, 2, 1},
					{2, 4, 2},
					{1, 2, 1}
			},
			16,
			KernelType.DETAIL
	),
	MEAN_BLUR(
			new int[][]{
					{1, 1, 1},
					{1, 1, 1},
					{1, 1, 1}
			},
			9,
			KernelType.DETAIL
	),
	EDGE_DETECTION_HORIZONTAL(
			new int[][]{
					{-1, -1, -1},
					{0, 0, 0},
					{1, 1, 1}
			},
			1,
			KernelType.EDGE
	),
	EDGE_DETECTION_VERTICAL(
			new int[][]{
					{-1, 0, 1},
					{-1, 0, 1},
					{-1, 0, 1}
			},
			1,
			KernelType.EDGE
	),
	EDGE_DETECTION_LAPLACIAN(
			new int[][]{
					{0, 1, 0},
					{1, -4, 1},
					{0, 1, 0}
			},
			1,
			KernelType.EDGE
	),
	EDGE_DETECTION_OUTLINE(
			new int[][]{
					{-1, -1, -1},
					{-1, 8, -1},
					{-1, -1, -1}
			},
			1,
			KernelType.EDGE
	),
	SOBEL_BOTTOM(
			new int[][]{
					{-1, -2, -1},
					{0, 0, 0},
					{1, 2, 1}
			},
			1,
			KernelType.SOBEL_H
	),
	SOBEL_TOP(
			new int[][]{
					{1, 2, 1},
					{0, 0, 0},
					{-1, -2, -1}
			},
			1,
			KernelType.SOBEL_H
	),
	SOBEL_RIGHT(
			new int[][]{
					{-1, 0, 1},
					{-2, 0, 2},
					{-1, 0, 1}
			},
			1,
			KernelType.SOBEL_V
	),
	SOBEL_LEFT(
			new int[][]{
					{1, 0, -1},
					{2, 0, -2},
					{1, 0, -1}
			},
			1,
			KernelType.SOBEL_V
	);

	private final int[][] matrix;
	private final int factor;
	private final KernelType type;

	/**
	 * Creates a new kernel with the given matrix, factor and type.
	 *
	 * @param matrix the convolution matrix
	 * @param factor the divisor for the matrix
	 * @param type   the type of the kernel
	 */
	Kernel(int[][] matrix, int factor, KernelType type) {
		this.matrix = matrix;
		this.factor = factor;
		this.type = type;
	}

	public int[][] matrix() {
		return matrix;
	}

	public int factor() {
		return factor;
	}

	public KernelType type() {
		return type;
	}

	@Override
	public String toString() {
		return "Kernel[matrix=%s, factor=%d, type=%s]".formatted(Arrays.deepToString(matrix), factor, type);
	}

}