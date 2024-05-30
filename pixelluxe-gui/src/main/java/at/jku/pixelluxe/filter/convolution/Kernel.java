package at.jku.pixelluxe.filter.convolution;

public class Kernel {
	private final String type;
	private final int[][] matrix;
	private final int factor;
	private boolean isTransformed;

	public Kernel(int[][] matrix, int factor, String type) {
		this.type = type;
		this.matrix = matrix;
		this.factor = factor;
		this.isTransformed = false;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public int getFactor() {
		return factor;
	}

	public String getType() {
		return type;
	}

	public boolean getIsTransformed(){
		return this.isTransformed;
	}

	public void setIsTransformed(boolean transformed) {
		this.isTransformed = transformed;
	}
}
