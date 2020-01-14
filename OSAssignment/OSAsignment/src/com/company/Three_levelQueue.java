package com.company;

import java.util.ArrayList;

public class Three_levelQueue implements Algorithm {
    
    private ArrayList<Process> processList;        //original process list
    private int totalRequiredBurstTime;
    private int quantum;
    private ArrayList<Process> recordSchedule = new ArrayList<>();//record schedule
    private ArrayList<Pair> displaySchedule = new ArrayList<>();   //display schedule
    

    Three_levelQueue (ArrayList<Process> pList, int quantum) {
        totalRequiredBurstTime = (0);
        processList = pList;
        this.quantum = quantum;
        for (int i = 0; i < pList.size(); i++) {
            totalRequiredBurstTime = totalRequiredBurstTime + processList.get(i).getBurstTime();
        }
    }
    
    private void addByArrival (ArrayList<Process> pList, Process p) {
        if (pList.size() == 0)        //List empty, insert directly
        {
            pList.add(p);
            return;
        } else {
            for (int i = 0; i < pList.size(); i++)       //List not empty, insert if arrival time earlier than the list
            {
                if (p.getArrivalTime() < pList.get(i).getArrivalTime()) {
                    pList.add(i, p);
                    return;
                }
            }
        }
        pList.add(p);//List not empty, arrival time is the largest, insert at last
    }
    
    private void  addToEnd (ArrayList<Process> pList, Process p) {
        pList.add(p);
    }
    
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
    public void simulate () {
        int timer = 0;
        int totalRunTime = 0;
        
        ArrayList<Process> newQueue = processList; //queue of new process
        ArrayList<Process> queue_one = new ArrayList<>();              //queue of 1 & 2 priority
        ArrayList<Process> queue_two = new ArrayList<>();              //queue of 3 & 4 priority
        ArrayList<Process> queue_three = new ArrayList<>();            //queue of 5 & 6 priority
        
        Process buffer = new Process();     //unfinished process
        
        if (recordSchedule != null)
            recordSchedule.clear();                 //record of schedule
        
        while (totalRunTime < totalRequiredBurstTime) {
            int counter = 0;
            while (counter < newQueue.size()) {
                if (newQueue.get(counter).getArrivalTime() == timer) {
                    //add to queue one for 1 & 2 priority (normal RR)
                    if (newQueue.get(counter).getPriority() == 1 || newQueue.get(counter).getPriority() == 2) {
                        addByArrival(queue_one, newQueue.get(counter));
                    }
                    //add to queue two for 3 & 4 priority (FCFS)
                    else if (newQueue.get(counter).getPriority() == 3 || newQueue.get(counter).getPriority() == 4) {
                        addByArrival(queue_two, newQueue.get(counter));
                    }
                    //add to queue three for 3 & 4 priority (FCFS)
                    else {
                        addByArrival(queue_three, newQueue.get(counter));
                    }
                    //take out process from new queue that already insert into running queue, counter adjusted
                    newQueue.remove(counter);
                    counter--;
                }
                counter++;
            }
            
            //if buffer(not finished process) of queue one is exist, add to queue one
                if (buffer.getActive()) {
                    addToEnd(queue_one, buffer);
                    buffer = new Process(); //reset
                }
            
            
            //if all queue are empty, insert empty process into schedule
            if (queue_one.size() == 0 && queue_two.size() == 0 && queue_three.size() == 0) {
                insertSchedule(displaySchedule, new Process());
            }
            //insert to schedule for not empty process
            else {
                //queue one not empty, run queue one
                if (queue_one.size() != 0) {
                    //running 1st process in queue one by 1 unit
                    queue_one.get(0).runProcess(1, timer + 1);
                    insertSchedule(displaySchedule, queue_one.get(0));
                    
                    //if 1st process in queue one is end, remove it from queue one
                    if (queue_one.get(0).getBalanceTime() == 0) {
                        addById(recordSchedule, queue_one.get(0));
                        queue_one.remove(0);
                        
                    }
                    //if 1st process in queue one reach the quantum, then it move out to buffer
                    else if (queue_one.get(0).getTotalRunTime() != 0 && queue_one.get(0).getTotalRunTime() % quantum == 0) {
                        buffer = queue_one.get(0);   //store unfinished process into buffer
                        queue_one.remove(0);
                    }
                } else if (queue_two.size() != 0) {
                    //running queue 1st process in queue two run 1 unit
                    queue_two.get(0).runProcess(1, timer + 1);
                    insertSchedule(displaySchedule, queue_two.get(0));
                    
                    //if 1st process in queue two is end, remove it from queue two
                    if (queue_two.get(0).getBalanceTime() == 0) {
                        addById(recordSchedule, queue_two.get(0));
                        queue_two.remove(0);
                        
                        //check if queue two empty, process in queue 3 will be elevated to queue 2
                        if (queue_two.size() == 0 && queue_three.size() != 0) {
                            while (queue_three.size() != 0) {
                                queue_two.add(queue_three.get(0));
                                queue_three.remove(0);
                            }
                        }
                    }
                }
                //queue 3 not empty
                else {
                    //move all process in queue three to queue two since queue two is empty
                    while (queue_three.size() != 0) {
                        queue_two.add(queue_three.get(0));
                        queue_three.remove(0);
                    }
                    
                    //run queue two
                    queue_two.get(0).runProcess(1, timer + 1);
                    insertSchedule(displaySchedule, queue_two.get(0));
                    
                    //if 1st process in queue two is end, remove it from queue two
                    if (queue_two.get(0).getBalanceTime() == 0) {
                        addById(recordSchedule, queue_two.get(0));
                        queue_two.remove(0);
                        //not need to check queue three again
                    }
                }
                totalRunTime++;
            }
            timer++;
        }
    }
    
    @Override
    public void display () {
        int timer = 0;
        System.out.print("0 ");
        for (Pair pair : displaySchedule) {
            System.out.print(pair.first.getId());
            timer = timer + pair.second;
            System.out.print(" " + timer + " ");
        }
        System.out.println();
        System.out.println();
        
        //new grant chart
        timer = 0;
    
        System.out.print("0 ");
    
        for (Pair pair : displaySchedule) {
            System.out.print(pair.first.getId());
            System.out.print(" | ");
        
            int counter = 0;
            while (counter < pair.second - 1) {
                System.out.print("");
                counter++;
            }
            timer = timer + pair.second;
        
            //timer
            System.out.print(" " + timer + " ");
        }
        System.out.println();
        System.out.println();
        
    }

}
