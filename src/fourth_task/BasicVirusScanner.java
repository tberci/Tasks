package fourth_task;


public class BasicVirusScanner {
    private static BasicVirusScanner instance;
    private String systemMessage = "Scanning";
    // Private constructor so instancing will be impossible from outside.
    private BasicVirusScanner() {}

	// Check if class already has an instance. If it doesn't have one it will create one, else return.
    //This static method will call the private constructor.
    public static synchronized  BasicVirusScanner getInstance() {
        if (instance == null) {
            instance = new BasicVirusScanner();
            
        }
        return instance;
    }
    
    
    public String getSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	public void startScan() {
    	
    	System.out.println(this.systemMessage);
    }
    
    
}