package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Play match = new Play();
        State state;
        Scanner scanner = new Scanner(System.in);

	    int choice=0;
        System.out.println("1- User plays.");
        System.out.println("2- DFS.");
        System.out.println("3- BFS");
        System.out.println("4- UCS");
        System.out.println("5- A*");
        System.out.println("0- Exit");
        choice = scanner.nextByte();
        switch (choice){
            case 1:
                state = match.userPlays();
                break;
            case 2:
                match.init(2);
                break;
            case 3:
                match.init(3);
                break;
            case 4:
                match.init(4);
                break;
            case 5:
                match.init(5);
                break;
            case 0:
                return;
            default:
                System.out.print("\033[0;31m");
                System.out.println("Wrong input !");
                System.out.print("\033[0m");
        }
    }
}
