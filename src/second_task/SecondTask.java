package second_task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;



public class SecondTask extends Thread{
	
	List<List<Integer>> lists ;
    
	public List<List<Integer>> getLists() {
		return lists;
	}

	public void setLists(List<List<Integer>> lists) {
		this.lists = lists;
	}

	public void run() {
		System.out.println("thread staterd");
		for(List<Integer> list : lists) {
			test(list);
		}
	}
	
	
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		//I tested up till 100 million data. Smaller sublists like 10.000 data for 1.000.000 data is much faster than 100.000 data sublists.
		
		System.out.println("Input the number of elements : ");
		int n = sc.nextInt();
		
		int[] list = new int[n+1];
		
		fillArray(list);
		System.out.println("Input the partition size: ");
		int p = sc.nextInt();
		
		List<List<Integer>> partitions = getSublists(list, p);
		 
		
		System.out.println("Would you like to see the numbers and the partitions?");
		System.out.println("For better performance when testing huge numbers, displaying everything is advised against");
		System.out.println(" Select : 1 to see,  0: to skip");
		int question = sc.nextInt();
		
		switch(question) {
			case 1 : display(list);
				 System.out.println(" \n ");
				 displayPartitions(partitions);
				 break;
			case 0 : break;
		}
		
		
		if(partitions.size() == 1) {
			duplicates_1(list);		//If the partition list is only 1 list, then single threaded execution.
		}else {
			System.out.println("");
			execute(partitions);	//Best performing yet, without HashSet, multithreaded.
		}
		sc.close();
		
    }
		//For single threaded execution:
			//duplicates_1(list);  	// with sorting. , better than duplicates_3
			//duplicates_2(list);	//With HashSet().
			//duplicates_3(list);	//worst performing. inner loop. O(n^2)
	
	
	private static void fillArray(int[] list) {
		for( int i= 0; i< list.length; i++) {
			Random rnd = new Random();
			list[i] = rnd.nextInt(list.length) + 1;
		}
	}
	
	private static void display(int[] list) {
		for( int i= 0; i< list.length; i++) {
			System.out.print(list[i]+ " ");
		}
	}
	
	private static void displayPartitions(List<List<Integer>> partitions ) {
		for(List<Integer> lists : partitions) {
			for(int f : lists) {
				System.out.print(f + " ");
			}
			System.out.println(" ");
		}
		
	}

	private static List<List<Integer>> getSublists(int[] data, int size) {
		//This method will make sublists from the array for parallel processing.
		//First sort the whole array, then make sublist, which will be divided between the threads.
		//And because of the array sorting, there won't be lower numbers in later sublists. 
		//If a sublist contained for example 1, and another contained 1, Since it was in a separate list , it was not detected as duplicate.
		//After sorting the same problem appeared [1,2,3] [3,4,5]. 
		//The answer was making the partition bigger until the current and the next element is the same. [1,2,3,3] [4,5]. With this approach
		
		
		Arrays.sort(data);
		List<List<Integer>> partitions = new ArrayList<>();
		int i = 0;
		
	
		while (i < data.length) {
		    List<Integer> sub_list = new ArrayList<>();
		 
		    // Add elements to the sublist until blockSize or end of list
		    for (int j = 0; j < size && i + j < data.length; j++) {
		    	
		        int currentValue = data[i+j];
		        sub_list.add(currentValue);
		       
		        
		        // If there are duplicates ahead, add them to the sublist.
		        while (i + j + 1 < data.length && currentValue == data[i + j + 1] ) {
		        	sub_list.add(currentValue);
		      
		            j++;
		        }
		    
		    }
		    
		    partitions.add(sub_list);
		    //relocate the index with the changed or unchanged size of the sublist.
		    //this will place the index in the correct position.
		    i +=  sub_list.size() ;
		}
		return partitions;
    }
	
	private static void execute(List<List<Integer>> partitions) {
			//This method will create subpartitions from the partitions list. Each sub partition is a List of partitions.
		    //These sub partitions will be assigned to each thread. So every thread will have its own list of partitions to work on.
		
			System.out.println("Input the number of threads :");
			int t = sc.nextInt();
			int partitionSize = partitions.size() / t;
		    List<SecondTask> threads = new ArrayList<>();   
			for( int i = 0; i< t; i++) {
				SecondTask thread = new SecondTask();
				threads.add(thread);
			}
			//In this for loop the subpartition lists  will be made and assigned to each thread.
			for (int i = 0; i < t; i++) {
				    int startIndex = i * partitionSize;
				    int endIndex = (i == t - 1) ? partitions.size() : (startIndex + partitionSize);
				    List<List<Integer>> threadPartitions = partitions.subList(startIndex, endIndex);
				    
				    threads.get(i).setLists(threadPartitions);
				}
			
			for (SecondTask thread : threads) {
			    try {
	                thread.start();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
			}
			
			for (SecondTask thread : threads) {
	            try {
	                thread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }

	}

	private static void duplicates_1(int[] list) {
		//A separate dinamic array for the duplicate elements.
		//With sorting a nested for loop isn't required. Much faster solution then duplicates_3()
		List<Integer> duplicates = new ArrayList<>();
	
		
		
		Arrays.sort(list);
		for (int i = 0; i < list.length-1; i++) {

		    if (list[i] == list[i+1]) {
		    	if(!duplicates.contains(list[i])) {
					duplicates.add(list[i]);
				}
		        
		    }
		}
		System.out.println("");
		
		for(Integer d : duplicates) {
			System.out.println("duplicate " + d);
		}
		
	}
	
	private static void test(List<Integer> list) {
		//A separate dinamic array for the duplicate elements.
		List<Integer> duplicates = new ArrayList<>();
		
		/*Without the HashSet a custom logic for duplicates in the list is required.
		The duplicates with the nested for loop can be found, however if there is more than 2 duplicates,
		from the same element then the duplicates will be printed more than once.
		For this problem the contains() method can be used. 
		.*/
		
		
		for (int i = 0; i < list.size()-1; i++) {

		    if (list.get(i).equals(list.get(i+1)) ) {
		    	if(!duplicates.contains(list.get(i))) {
					duplicates.add(list.get(i));
				}
		        
		    }
		}
		System.out.println("");
		
		for(Integer d : duplicates) {
			System.out.println("duplicate " + d);
		}
		
	}
	
	private static Set<Integer> duplicates_2(int[] list) {
		
		//A HashSet to not list duplicates more than once.
		
		Set<Integer> duplicates = new HashSet<>();
        Set<Integer> unique = new HashSet<>();

        for (int i = 0;i < list.length; i++) {
        	// If element is already in shown, it wont be added.
            if (!unique.add(list[i])) { 
                duplicates.add(list[i]);
            }
        }
        
        System.out.println("\n");
		for(Integer d : duplicates) {
			System.out.println("duplicates : " + d);
		}
		return duplicates;
	}

	
	private static void duplicates_3(int[] list) {
		//A separate dinamic array for the duplicate elements.
		List<Integer> duplicates = new ArrayList<>();
		
		/*Without the HashSet a custom logic for duplicates in the list is required.
		The duplicates with the nested for loop can be found, however if there is more than 2 duplicates,
		from the same element then the duplicates will be printed more than once.
		For this problem the contains() method can be used. 
		.*/
		
		//Nested for loop to check if the element in the array is a duplicate. 
		for(int i =0; i< list.length; i++) {
			for(int j = i+1; j< list.length; j++) {
				if(list[i] == list[j]) {
					if(!duplicates.contains(list[i])) {
						duplicates.add(list[i]);
					}
				}
			}
		}
		System.out.println("");
		for(Integer d : duplicates) {
			System.out.println("duplicate " + d);
		}
		
	}
	
	
}
