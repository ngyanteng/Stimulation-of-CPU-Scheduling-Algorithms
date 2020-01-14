package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Process> processes = new ArrayList<>();
    
    private static int Q = 2;
    private static int BT, AT, P;
    
    public static void main (String[] args) throws Exception {
        //this is input from question
        autoInput();


        printTable();
        
        int algo;

        //Menu of choice to choose 1 of the scheduling process
        System.out.print("\n\n\n\nEnter the number of scheduling algorithm: ");
        System.out.print("\nEnter [1] for FCFS ");
        System.out.print("\nEnter [2] for RR ");
        System.out.print("\nEnter [3] for SRTN ");
        System.out.print("\nEnter [4] for Three-level queue ");
        algo = scanner.nextInt();
		
        while (algo < 0 || algo > 4) { // number must be between 1 and 4.
            System.out.println("\nError: Input is less than 0 or larger than 4.");
            System.out.print("Enter the number of scheduling algorithm");
            algo = scanner.nextInt();
        }
		
        //1. do fcfs
        if (algo == 1) {
            System.out.println("FCFS ");
            Algorithm fcfs = new FCFS(processes);
            fcfs.simulate();
            fcfs.display();
            //2. do RR
        } else if (algo == 2) {
            System.out.println("RR ");
            Algorithm rr = new RR(processes, Q);
            rr.simulate();
            rr.display();
            //3. do SRTN
        } else if (algo == 3) {
            System.out.println("SRTN ");
            Algorithm srtn = new SRTN(processes);
            srtn.simulate();
            srtn.display();
            //do 3 level queue
        } else if (algo == 4) {
            System.out.println("3 LVL QUEUE ");
            Algorithm TH = new Three_levelQueue(processes, Q);
            TH.simulate();
            TH.display();
        }
    }

    //method accepting user input
    private static void acceptInput () {
        processes.clear();
        int numOfProcesses;
        System.out.print("\n\n\n\nEnter the number of proccess: ");
        numOfProcesses = scanner.nextInt();
        while (numOfProcesses < 3 || numOfProcesses > 6) { // number of processes must be between 3 and 6.
            System.out.println("\nError: Input is less than 3 or larger than 6.");
            System.out.print("Enter the number of proccess: ");
            numOfProcesses = scanner.nextInt();
        }
        //input new time quantum
        System.out.print("Enter Quantum Time ([0] to ignore)" + " -> ");
        Q = scanner.nextInt();
        
        for (int i = 0; i < numOfProcesses; i++) {
            //inserting new process data
            setDataForProcess(i);
            System.out.println();
            processes.add(new Process(i, P, BT, AT));
            
        }
        
        
    }

    //data from asignment rubric
    private static void autoInput () {
        processes.add(new Process(0, 1, 2, 5));
        processes.add(new Process(1, 2, 2, 4));
        processes.add(new Process(2, 3, 2, 3));
        processes.add(new Process(3, 4, 3, 1));
        processes.add(new Process(4, 5, 4, 2));
        processes.add(new Process(5, 6, 10,9));
    }
    //print out the tabulated data to be used in the algorithms
    private static void printTable () {
        if (Q != 0)
            System.out.println("Quantum time = (" + Q + ")");
        
        System.out.println("+---------+------------+--------------+-----------+");
        System.out.println("|   PID   | Burst Time | Arrival Time | Priority  |");
        System.out.println("+---------+------------+--------------+-----------+");
        for (Process process : processes) {
            System.out.format("|   %s    |    %2d      |      %2d      |     %2d    |%n"
                    , process.getPN(), process.getBurstTime(), process.getArrivalTime(), process.getPriority());
            System.out.println("+---------+------------+--------------+-----------+");
        }
        System.out.println("Enter [1] if the table is correct");
        System.out.println("Enter [2] if the table is wrong");
        System.out.println("Enter [3] to change the whole table");
        int s = scanner.nextInt();

        //if 2. edit a specific process
        if (s == 2) {
            editProcess();
        }
        //if 3. clear the existing data and reinsert new
        if (s == 3) {
            processes.clear();
            acceptInput();
        }
        
        
    }

    //edit specific process data
    private static void editProcess () {
        System.out.print("Please enter the ID of process to correct. -> ");
        int s = scanner.nextInt();
        while (s < 0 || s >= processes.size()) {
            System.out.println("There is no such process with ID " + s);
            System.out.print("Please enter the correct ID -> ");
            s = scanner.nextInt();
        }
        setDataForProcess(s);
        processes.remove(s);
        processes.add(s, new Process(s, P, BT, AT));
    }

    //method to input process data
    private static void setDataForProcess (int s) {
        System.out.print("\nEnter Burst Time for process " + s + " -> ");
        BT = scanner.nextInt();
        System.out.print("Enter Arrival Time for process " + s + " -> ");
        AT = scanner.nextInt();
        System.out.print("Enter Priority for process " + s + "-> ");
        s = scanner.nextInt();
        while (1 > s || s > 6) {
            System.out.println("Priority is from 1 to 6 ");
            System.out.print("Please enter the correct priority -> ");
            s = scanner.nextInt();
        }
        P = s;
    }
    
}
