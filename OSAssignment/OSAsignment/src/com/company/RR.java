
package com.company;

import java.util.ArrayList;
    
public class RR implements Algorithm {

    private ArrayList<Process> processList;
    private int totalRequiredBurstTime;
    private ArrayList<Process> recordSchedule = new ArrayList<>();//record schedule

    private ArrayList<Pair> displaySchedule = new ArrayList<>();   //display schedule

    private int quantum;

    private Process next = new Process();
    private Process buffer = new Process();


    private void insertSchedule (ArrayList<Pair> schedule, Process p) {
        if (schedule.size() == 0) {
            schedule.add(new Pair(p, 1));
        } else {

            if (schedule.get(schedule.size() - 1).first.getId().equals(p.getId())) {
                schedule.get(schedule.size() - 1).second++;
            } else {
                schedule.add(new Pair(p, 1));
            }
        }
    }

    private void addByPriority (ArrayList<Process> pList, Process p) {

        if (pList.size() == 0)        //List empty, insert directly
        {
            pList.add(p);
            return;
        }
        else
        {
            for (int i = 0; i < pList.size(); i++)       //List not empty, insert if priority higher than the list (smaller is higher)
            {
                if (p.getPriority() < pList.get(i).getPriority())
                {
                    pList.add( i, p);
                    return;
                }
            }
        }
        pList.add(p);     //List not empty, priority is the lowest, insert at last

    }

    RR (ArrayList<Process> pList, int quantum) {
        totalRequiredBurstTime = (0);
        processList = pList;
        this.quantum = quantum;
        for (int i = 0; i < pList.size(); i++) {
            totalRequiredBurstTime = totalRequiredBurstTime + processList.get(i).getBurstTime();
        }
    }

    private void addById (ArrayList<Process> pList, Process p) {
        if (pList.size() == 0)        //List empty, insert directly
        {
            pList.add(p);
            return;
        } else {
            for (int i = 0; i < pList.size(); i++)       //List not empty, insert based on lowest id at 1st
            {
                while (p.getId().length() > pList.get(i).getId().length()) {
                    i++;
                    if (i == pList.size()) {
                        pList.add(p);
                        return;
                    }
                }
                if (p.getId().compareTo(pList.get(i).getId()) < 0) {
                    pList.add(i, p);
                    return;
                }
            }
        }
        pList.add(p);     //List not empty, having largest id, insert at last
    }


    @Override
    public void simulate() {
        int timer = 0;
        int totalRunTime = 0;
        ArrayList<Process> newQueue = processList;
        ArrayList<Process> runningQueue = new ArrayList<>();
        while (totalRunTime < totalRequiredBurstTime)
        {
            int counter = 0;
            while (counter < newQueue.size())
            {
                //move into ready queue if arrival = timer
                if (newQueue.get(counter).getArrivalTime() == timer)
                {
                    addByPriority(runningQueue, newQueue.get(counter));
                    //take out process from new queue that already insert into running queue, counter adjusted
                    newQueue.remove(counter);
                    counter--;
                }
                counter++;
            }

            //if buffer(not finished process) is exist, add to running queue
            if (buffer.getActive())
            {
                addByPriority(runningQueue, buffer);
                buffer = new Process(); //reset
            }

            //if running queue and next are empty, insert empty process into schedule
            if (runningQueue.size() == 0 && !next.getActive())
            {
                insertSchedule(displaySchedule, new Process());
            }
            //insert to schedule for not empty process
            else
            {
                //if next is empty, take 1st process from running queue
                if (!next.getActive())
                {
                    next = runningQueue.get(0);
                    //erase 1st process after the process move to next
                    runningQueue.remove(0);
                }

                //running next process by 1 unit
                next.runProcess(1, timer + 1);
                //insert next process into schedule
                insertSchedule(displaySchedule, next);
                totalRunTime++;

                //if next process is end,then reset it
                if (next.getBalanceTime() == 0)
                {
                    addById(recordSchedule, next);
                    next = new Process(); //reset
                }
                //if next process reach the quantum, then it move out to buffer, and take new process from running queue
                else if (next.getTotalRunTime() != 0 && next.getTotalRunTime() % quantum == 0)
                {
                    buffer = next;   //store unfinished process into buffer

                    if (runningQueue.size() != 0)
                    {
                        //store the next process to be run
                        next = runningQueue.get(0);
                        //erase 1st process after the process move to next
                        runningQueue.remove(0);
                    }
                    else
                    {
                        next = new Process();   //reset Process
                    }
                }
            }
            timer++;
        }
    }

    @Override
    public void display(){
        int timer = 0;
        System.out.print("0 ");
        for (Pair pair : displaySchedule) {
            System.out.print(pair.first.getId());
            timer = timer + pair.second;
            System.out.print(" " + timer + " ");
        }
        System.out.println();
        System.out.println();
    }

}
