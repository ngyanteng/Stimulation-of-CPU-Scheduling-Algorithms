package com.company;

public class Process {
    static int numberOfProcesses = 0;
    private String id;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private boolean active;
    private int finishTime;
    private int timeLeft;
    private int processNumber;
    private int totalRunTime;
    private int endTime;
    
    
    Process (int index, int priority, int burstTime, int arrivalTime) {
        this.processNumber = index;
        this.priority = priority;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        id = "P" + index;
        this.active = true;
        this.processNumber = numberOfProcesses;
        numberOfProcesses++;
        this.totalRunTime = 0;
        this.endTime = 0;
        finishTime = 0;
    }
    
    
    Process () {
        id = " ";
        arrivalTime = 0;
        burstTime = 0;
        priority = 0;
        totalRunTime = 0;
        endTime = 0;
        active = false;
    }
    
    
    String getPN () {
        return "P" + processNumber;
    }
    
    int getBurstTime () {
        return burstTime;
    }
    
    
    int getArrivalTime () {
        return arrivalTime;
    }
    
    int getPriority () {
        return this.priority;
    }
    
    public String toString () {
        return getPN() + "   ||   " + getTimeLeft() + "   ||   " + getFinishTime();
    }
    
    int getFinishTime () {
        return finishTime;
    }
    
    void setFinishTime (int finishTime) {
        this.finishTime = finishTime;
    }
    
    int getTimeLeft () {
        return timeLeft;
    }
    
    void setTimeLeft (int timeLeft) {
        this.timeLeft = timeLeft;
    }
    
    void runProcess (int runTime, int currentTime) {
        if (runTime > (burstTime - totalRunTime)) {
            System.out.println("run time > balance time");
        } else {
            totalRunTime = totalRunTime + runTime;
            //set end time if process complete
            if (totalRunTime == burstTime) {
                endTime = currentTime;
            }
        }
    }
    
    String getId () {
        return id;
    }
    
    int getTotalRunTime () {
        return totalRunTime;
    }
    
    int getBalanceTime () {
        return (burstTime - totalRunTime);
    }
    
    boolean getActive () {
        return active;
    }
    
    int getEndTime () {
        if (endTime == 0) {
            return -2000;
        } else {
            return endTime;
        }
        
    }
    
    int getWaitingTime () {
        if (endTime == 0) {
            return -2000;
        } else {
            return (endTime - burstTime - arrivalTime);
        }
    }
    
    int getTurnAroundTime () {
        if (endTime == 0) {
            return -2000;
        } else {
            return (endTime - arrivalTime);
        }
    }
}
