package com.company;

import java.util.*;

public class FCFS implements Algorithm {
    
    
    private ArrayList<Process> processes;
    private ArrayList<String> displayResult = new ArrayList<>();
    
    @Override
    public void simulate () {
        int timeLine = 0;
        // Used for sorting in ascending order of
        // roll number
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        //this will be the results displayed later
        displayResult.add(0+"");

        //to keep track of current time after a process is completed
        timeLine = processes.get(0).getArrivalTime();

        for (int i = 0; i < processes.size(); i++) {
            //if finishTime == 0, process is already completed
            if(processes.get(i).getFinishTime() == 0) {
                //the method to complete the current process: execute(int index, int time)
                processes.get(i).setFinishTime(execute(i, timeLine));
                //the time when the current process is completed, and the time which the next process will begin
                timeLine = processes.get(i).getFinishTime();
            }
        }
    }

    //processes are completed in a recursion method if the current process is pre-empted
    private int execute(int index, int time)
    {
        displayResult.add("P"+index);
        int tempTime = time;

        //this for loop will loop through remaining incomplete processes for comparison with the current process
        for (int j = 1; (j < processes.size()-index); j++) {

            //this if statement is used to check if the compared process will pre-empt the current process
            //if statement is true if:
            // 1. the current process has a lower priority than the compared process
            // 2. the compared process is incomplete
            // 3. the current time this current process began + the current process burst time >= compared process arrival time
            if(hasHigherPriority(processes.get(index), processes.get(index + j))
                    && processes.get(index + j).getFinishTime() == 0
                    && ((tempTime + processes.get(index).getBurstTime()) >= processes.get(index + j).getArrivalTime())){

                //to check if the compared process will pre-empt the current process while it is partially completed
                if(processes.get(index).getArrivalTime() + processes.get(index).getBurstTime() > processes.get(index + j).getArrivalTime()){

                    //the remaining uncompleted burst time for the current process
                    processes.get(index).setTimeLeft(processes.get(index).getArrivalTime()
                            + processes.get(index).getBurstTime()
                            - processes.get(index + j).getArrivalTime());
                    //time when the compared process will begin pre-empting
                    tempTime += processes.get(index).getBurstTime() - processes.get(index).getTimeLeft();
                    displayResult.add(tempTime+"");
                    //recursion starts here, the compared process will begin running
                    processes.get(index + j).setFinishTime(execute(index + j, tempTime));
                    //compared process is completed, continue on with running the current process

                    //time when the current process returns to continue completing its process
                    tempTime = processes.get(index + j).getFinishTime();
                }else {
                    processes.get(index + j).setFinishTime(execute(index + j, tempTime));
                    tempTime = processes.get(index + j).getFinishTime();
                }
            }
        }
        //if the current process was pre-empted when it was partially completed
        if(processes.get(index).getTimeLeft() != 0) {
            displayResult.add("P"+index+" "+(processes.get(index).getTimeLeft() + tempTime+""));
            return processes.get(index).getTimeLeft() + tempTime;
        } else {
            displayResult.add(processes.get(index).getBurstTime() + tempTime+"");
            return processes.get(index).getBurstTime() + tempTime;
        }
    }

    //comparison of priority, if first number is larger than second, second has higher priority vice versa
    private boolean hasHigherPriority (Process first, Process second) {
        return first.getPriority() > second.getPriority();
    }

    //constructor
    FCFS (ArrayList<Process> processes) {
        this.processes = processes;
    }

    @Override
    public void display () {
        for (String s : displayResult) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
