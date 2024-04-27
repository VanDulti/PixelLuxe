package at.jku.pixelluxe.filter.convolution;

class Kernel {
	private int[][] matrix;
	private int factor;

	public Kernel(int[][] matrix, int factor){
		this.matrix = matrix;
		this.factor = factor;
	}

	public int[][] getMatrix(){
		return matrix;
	}

	public int getFactor(){
		return factor;
	}
}
