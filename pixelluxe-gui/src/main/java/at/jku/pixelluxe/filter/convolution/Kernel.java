package at.jku.pixelluxe.filter.convolution;

public class Kernel {
	private final String type;
	private final int[][] matrix;
	private final double factor;

	public Kernel(int[][] matrix, int factor, String type) {
		this.type = type;
		this.matrix = matrix;
		this.factor = factor;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public double getFactor() {
		return factor;
	}

	public String getType() {
		return type;
	}
}