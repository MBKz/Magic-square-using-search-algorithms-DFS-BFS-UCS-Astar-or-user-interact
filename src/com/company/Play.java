package com.company;

import java.util.*;

public class Play {

    public State state;
    public int size;
    public int [] numList ;
    public ArrayList<Integer> visited = new ArrayList<>();
    public int initialGridHash;
    Scanner scanner = new Scanner(System.in);


    public void printPath(State state){
        while ( Arrays.deepHashCode(state.grid) != initialGridHash){
            state = state.father ;
//            System.out.println("cost: "+state.cost);
            state.print();
        }
    }

    public void init(int algo){
        int choice=0;
        System.out.print("\033[0;35m");
        System.out.println("1- Start from scratch.");
        System.out.println("2- start from initial grid. ");
        System.out.print("\033[0m");
        choice = scanner.nextByte();
        switch (choice) {
            case 1:
                initialGridHash = Arrays.deepHashCode(state.grid);
                if(algo == 2) dfs();
                if(algo == 3) bfs();
                if(algo == 4) ucs();
                if(algo == 5) a_star();
                break;
            case 2:
                userPlays();
                initialGridHash = Arrays.deepHashCode(state.grid);
                if(algo == 2) dfs();
                if(algo == 3) bfs();
                if(algo == 4) ucs();
                if(algo == 5) a_star();
                break;
            default:
                System.out.print("\033[0;31m");
                System.out.println("Wrong input !");
                System.out.print("\033[0m");
        }
    }

    public Play(){
        System.out.print("\033[0;36m");
        System.out.println("Enter the size of the grid :");
        size = scanner.nextByte();
        numList = new int[size*size];
        System.out.println("Enter the numbers to fill the grid :");
        for(int i=0;i<size*size;i++){
            numList[i] = scanner.nextByte();
        }
        state = new State(size,numList);
        System.out.print("\033[0m");
    }

    public State userPlays(){

        int choice , num=0 , row=0 , colom=0 ;
        boolean numIn , coordinateIn ;

        initialGridHash = Arrays.deepHashCode(state.grid);
        while (!state.isFull()){
            System.out.print("\033[0;33m");
            System.out.println("1- Fill an index.");
            System.out.println("2- Print current grid.");
            System.out.println("3- Show all next states.");
            System.out.println("4- Show user path.");
            System.out.println("0- Exit user mode.");
            System.out.print("\033[0m");
            choice = scanner.nextByte();
            switch (choice){
                case 1:
                    System.out.print("\033[0;33m");
                    System.out.println("Enter coordinate to fill in (row then colom) :");
                    coordinateIn=false ;
                    while (!coordinateIn){
                        row = scanner.nextByte();
                        colom = scanner.nextByte();
                        if(!( (row<size) && (row>=0) && (colom<size) && (colom>=0) )){
                            System.out.print("\033[0;31m");
                            System.out.println("Wrong Input !");
                            System.out.print("\033[0m");
                            continue;
                        }
                        coordinateIn = true;
                    }
                    System.out.println("Enter number from list :");
                    numIn = false;
                    System.out.println(Arrays.toString(numList));
                    while (!numIn){
                        num = scanner.nextByte();
                        for(int i=0;i<size*size;i++)
                            if(num == numList[i] && num !=0)  {
                                numIn = true;
                                break;
                            }
                        if(!numIn) {
                            System.out.print("\033[0;31m");
                            System.out.println("Wrong Input !");
                            System.out.print("\033[0m");
                        }
                    }
                    if(!state.canSet(row,colom,num)){
                        System.out.print("\033[0;31m");
                        System.out.println("Wrong ,You can not fill this index with this value !");
                        System.out.print("\033[0m");
                        continue;
                    }
                    State nextState = new State(state.size, state.numList);
                    nextState.clone(state);
                    nextState.fill(row,colom,num);
                    state = nextState;
                    System.out.print("\033[0;33m");
                    System.out.println("Your current matrix is :");
                    nextState.print();
                    System.out.print("\033[0m");
                    break;
                case 2:
                    System.out.print("\033[0;33m");
                    System.out.println("Your current matrix is :");
                    state.print();
                    System.out.print("\033[0m");
                    break;
                case 3:
                    ArrayList<State>  allNext ;
                    allNext = state.getAllNextStates(numList);
                    for(State one: allNext) {
                        one.print();
                    }
                    break;
                case 4:
                    System.out.print("\033[0;33m");
                    System.out.println("Your path is :");
                    printPath(state);
                    System.out.print("\033[0m");
                    break;
                case 0:
                    return state;
                default:
                    System.out.print("\033[0;31m");
                    System.out.println("Wrong input !");
                    System.out.print("\033[0m");
            }
        }
        if(state.isMagicSquare()){
            System.out.println("Congrates it's magic square .");
            state.print();
            return state;
        }
        System.out.print("\033[0;31m");
        System.out.println("You failed it's not magic square !");
        System.out.print("\033[0m");
        return state;
    }

    public void dfs(){

        int count=0;
        ArrayList<State> allNextState ;
        Stack<State> stack = new Stack<>();
        stack.push(state);

        while (!stack.empty()) {
            state = stack.pop();
            if (!visited.contains(Arrays.deepHashCode(state.grid))){
                visited.add(Arrays.deepHashCode(state.grid));
                if (state.isMagicSquare()) {
                    System.out.println("Congrates it's magic square after path with length = "+count);
                    state.print();
                    printPath(state);
                    return;
                }
                allNextState = state.getAllNextStates(numList);
                count++;
                if(allNextState.size() !=0){
                    for(int i=allNextState.size() ; i>0 ; i--){
                        stack.push(allNextState.get(i-1));
                    }
//                    for(State one:allNextState){
//                        stack.push(one);
//                    }
                }
            }
        }
        System.out.print("\033[0;31m");
        System.out.println("Can not be solved !");
        System.out.print("\033[0m");

    }

    public void bfs(){

        int count=0;
        ArrayList<State> allNextState ;
        Queue<State> queue = new LinkedList<>() ;
        queue.add(state);

        while (!queue.isEmpty()) {
            state = queue.remove();
            if (!visited.contains(Arrays.deepHashCode(state.grid))){
                visited.add(Arrays.deepHashCode(state.grid));
                if (state.isMagicSquare()) {
                    System.out.println("Congrates it's magic square after path with length = "+count);
                    state.print();
                    printPath(state);
                    return;
                }
                allNextState = state.getAllNextStates(numList);
                count++;
                if(allNextState.size() !=0){
//                    for(int i=allNextState.size() ; i>0 ; i--){
//                        queue.add(allNextState.get(i-1));
//                    }
                    for(State one:allNextState){
                        queue.add(one);
                    }
                }
            }
        }
        System.out.print("\033[0;31m");
        System.out.println("Can not be solved !");
        System.out.print("\033[0m");

    }

    public void ucs(){

        int count=0;
        ArrayList<State> allNextState ;
        PriorityQueue<State> pQueue = new PriorityQueue<>() ;
        pQueue.add(state);

        while (!pQueue.isEmpty()) {
            state = pQueue.remove();
            if (!visited.contains(Arrays.deepHashCode(state.grid))){
                visited.add(Arrays.deepHashCode(state.grid));
                if (state.isMagicSquare()) {
                    System.out.println("Congrates it's magic square after path with length = "+count);
                    state.print();
                    printPath(state);
                    return;
                }
                allNextState = state.getAllNextStates(numList);
                count++;
                if(allNextState.size() !=0){
                    for(State one:allNextState){
                        one.cost = -1 *(one.getCost() )+ state.cost;
                        pQueue.add(one);
                    }
                }
            }
        }
        System.out.print("\033[0;31m");
        System.out.println("Can not be solved !");
        System.out.print("\033[0m");

    }

    public void a_star(){

        int count=0;
        ArrayList<State> allNextState ;
        PriorityQueue<State> pQueue = new PriorityQueue<>() ;
        pQueue.add(state);

        while (!pQueue.isEmpty()) {
            state = pQueue.remove();
            if (!visited.contains(Arrays.deepHashCode(state.grid))){
                visited.add(Arrays.deepHashCode(state.grid));
                if (state.isMagicSquare()) {
                    System.out.println("Congrates it's magic square after path with length = "+count);
                    state.print();
                    printPath(state);
                    return;
                }
                allNextState = state.getAllNextStates(numList);
                count++;
                if(allNextState.size() !=0){
                    for(State one:allNextState){
                        one.cost = (-1*(one.getCost()) + state.cost + one.toRich() );
                        pQueue.add(one);
                    }
                }
            }
        }
        System.out.print("\033[0;31m");
        System.out.println("Can not be solved !");
        System.out.print("\033[0m");

    }
}
