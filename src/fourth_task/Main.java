package fourth_task;

public class Main {
	

	public static void main(String[] args) {
		//As an example a I selected virus scanner because there shouldn't be more than one instance of one running at the same time.
		BasicVirusScanner example = BasicVirusScanner.getInstance();
		
		BasicVirusScanner example2 = BasicVirusScanner.getInstance();
		
		
		example2.setSystemMessage("A problem has occured");
		
		example2.startScan();
		example.startScan();
		
		//So what happens is example and example2 will reference the same instance.
		//We can check if this is true by using the "==" operator.
		if(example == example2) {
			System.out.println("The Singleton pattern works");
		}else {
			System.out.println("This isn't singleton");
		}
	}

}
