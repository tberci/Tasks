package first_task;

import com.jtechnology.test.TestDataI;

public class FirstTask  {
	
	public static void main(String[] args) {
		//Matrix from interface.
		String[][] smatrix = TestDataI.MATRIX; 
		//The initial matricx is String so I have to convert it to float.
		float[][] converted = convertMatrix(smatrix);
		//Display the matrix
		display(converted);
		System.out.println("");
		
		//I saved each row's average to a new array.
		float[] avgs = rowAvg(converted.length, converted);
	
		//The max of the average from each row. 
		System.out.println("Max average : " + maxSearch(avgs));
	}
	
	
	public static float[][] convertMatrix(String[][] smatrix){
		int lines = smatrix.length;
		int rows = smatrix[0].length;
		//new matrix with the same lines and rows but in float.
		float[][] converted = new float[lines][rows];
		//Parsing from String to Float required.
		for(int i= 0; i< lines; i++) {
			for(int j = 0; j< rows; j++) {
				converted[i][j] = Float.parseFloat(smatrix[i][j]);
			}
		}
		return converted;
	}
	
	
	public static void display(float[][] matrix) {
		//Simple display method.
		System.out.println("Converted matrix:");
		for(int i= 0; i< matrix.length; i++) {
			for(int j = 0; j< matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " , ");
			}
			System.out.println("");
		}
	}
	
	
	public static float[] rowAvg(int sor, float [][] matrix) {
		//new array for the average values for each row.
		float[] avgs = new float[sor];
		
		for(int i = 0; i < matrix.length; i++){  
		float avgRow = 0;
		    for(int j = 0; j < matrix[0].length; j++){   
		    	//calculating the average value for the current row.
		    	avgRow +=  matrix[i][j] / matrix.length  ;   
		        }
		     	 System.out.printf("The average of row " + (i+1) +" : %.3f \n" , avgRow);  
		     	 //adding the value to the array.
		     	avgs[i] = avgRow;
		        }
			 System.out.println("");

		return avgs;

	}
	
	
	public static float maxSearch(float[] avgs) {
		//Basic linear search. Looping throught the averages in the avgs array.
		float max = avgs[0];
		for(int i= 0; i< avgs.length; i++) {
			
			if(max < avgs[i]) {
				max = avgs[i];
			}
		}
		return max;
		
	}
	
}
