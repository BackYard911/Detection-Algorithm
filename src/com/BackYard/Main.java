package com.BackYard;

import java.util.Scanner;

public class Main {
    Scanner scanner = new Scanner(System.in);
    int n;//= 5; //number of processes
    int m; //number of resource types
    int[] available;
    int[][] max;
    int[][] allocation;
    int[][] request; //= new int[n][m];
    int[] safeSeq; //=new int[n];
    boolean isSafe = false;
    boolean[] deadlocked;

    void initialization() {
        System.out.println("Please enter number of processes");
        n = scanner.nextInt();
        System.out.println("Please enter number of resource types");
        m = scanner.nextInt();
        allocation = new int[n][m];
        /*   {     {0,1,0}, //P0
                {2,0,0}, //P1
                {3,0,3}, //P2
                {2,1,1}, //P3
                {0,0,2} }; //P4
                */
        System.out.println("Please enter the allocation map respectively");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                allocation[i][j] = scanner.nextInt();
            }
        }

        max = new int[n][m];

        System.out.println("Please enter the request map respectively");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                max[i][j] = scanner.nextInt();
            }
        }
        available = new int[m];


        System.out.println("Please enter the number of instances of each resource respectively");
        for (int i = 0; i < m; i++) {
            available[i] = scanner.nextInt();
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                available[i] -= allocation[j][i];
            }
        }

        deadlocked = new boolean[n];
        for (int i = 0;i<n;i++){
            deadlocked[i]=false;
        }
    }

    void detect() {
        safeSeq = new int[n];
        int counter = 0;
        boolean finish[] = new boolean[n];
        for (int i = 0; i < n; i++) {
            finish[i] = false;
        }
        int work[] = new int[m];
        for (int i = 0; i < m; i++) {
            work[i] = available[i];
        }
        while (counter < n) {
            boolean flag = false;
            for (int i = 0; i < n; i++) {
                if (!finish[i]) {
                    int j;
                    for (j = 0; j < m; j++) {
                        if (request[i][j] > work[j]) {

                            break;
                        }
                    }

                    if (j == m) {
                        safeSeq[counter] = i;
                        counter++;
                        finish[i] = true;
                        flag = true;
                        for (j = 0; j < m; j++) {
                            work[j] = work[j] + allocation[i][j];

                        }

                    }
                }
            }
            if (!flag)
                break;
        }
        for (int i = 0; i < n; i++) {
            if (finish[i] == false) {
                deadlocked[i]=true;
                System.out.println("P" + i + " is deadlocked");
            }
        }


        if (counter < n) {
            System.out.println("System is not safe.");
            }
        else {
            isSafe=true;
            System.out.println("Safe sequence is:");
            for (int i = 0; i < n; i++) {
                if (!deadlocked[i]) {
                    System.out.print("P" + safeSeq[i]);
                    if (i != n - 1)
                        System.out.print(" -> ");
                }
            }
        }
        if (!isSafe){
            System.out.println("Aborting all deadlocked processes");
            for (int i = 0; i < n; i++) {
                if (finish[i] == false) {
                    for (int j = 0; j < m; j++) {
                        available[j] = available[j] + allocation[i][j];
                    }
                }
            }
            detect();
        }
    }




    void calcNeed(){
        request = new int[n][m];
        for (int i = 0; i < n;i++){
            for (int j = 0; j < m;j++){
                request[i][j] = max[i][j] ;
            }
        }
    }

    public static void main(String[] args) {
        Main detection = new Main();
        detection.initialization();
        detection.calcNeed();
        detection.detect();
    }
}
