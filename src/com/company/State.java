package com.company;

import java.util.ArrayList;

public class State implements Comparable<State>{
    public int [][] grid ;
    public int size;
    public int [] numList;
    public State father;
    public int cost = 0 ;

    public State(){}

    public State(int size , int [] numList){
        this.size = size ;
        this.numList = numList ;
        grid = new int[size][size];
        for(int i=0 ; i<this.size;i++){
            for(int j=0 ; j<this.size;j++){
                grid[i][j] = 0 ;
            }
        }
        this.father = this ;
    }

    public void clone(State state){
        this.size = state.size;
        this.numList = state.numList ;
        for(int i=0 ; i<this.size;i++){
            for(int j=0 ; j<this.size;j++){
                this.grid[i][j] = state.grid[i][j] ;
            }
        }
        this.father = state;
    }

    public void print(){
        System.out.print("\033[0;34m");
        for(int i=0 ; i<this.size;i++){
            for(int j=0 ; j<this.size;j++){
                System.out.print("\t"+ this.grid[i][j]);
            }
            System.out.println();
        }
        System.out.println("-----------------------------");
        System.out.print("\033[0m");
    }

    public boolean isMagicSquare(){

        if(!this.isFull()) return false;
        // check diameters
        int directDiameters = 0, backwardDiameters=0;
        for (int i = 0; i < this.size; i++) {
            directDiameters += this.grid[i][i];
            backwardDiameters += this.grid[i][this.size-1-i];
        }
        if(directDiameters!=backwardDiameters)  return false;

        // check colom and row
        for (int i = 0; i < this.size; i++) {

            int sumRow = 0, sumCol = 0;
            for (int j = 0; j < this.size; j++) {
                sumRow += this.grid[i][j];
                sumCol += this.grid[j][i];
            }
            if (sumRow != sumCol || sumCol != directDiameters)
                return false;
        }

                return true;
    }

    public boolean canSet ( int row , int colom , int num){
        ArrayList<Integer> sums = new ArrayList<>();

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(this.grid[i][j]==num) return false;
            }
        }

        if (colom < size && row < size && this.grid[row][colom] == 0) {
            int res;
            this.grid[row][colom] = num ;

            // check rows
            for (int i=0;i<this.size;i++){
                res = checkRow(i);
                if(res != 0)    sums.add(res);
            }
            // check coloms
            for (int i=0;i<this.size;i++){
                res = checkColom(i);
                if(res != 0)    sums.add(res);
            }

            // check Diameters
            int res1=0 , res2=0;
            res1 = checkDirectDiameters();
            res2 = checkBackwardDiameters();
            if(res1!=0 && res2 !=0){
               if(res1 == res2) sums.add(res1);
               else return false ;
            }

            if(sums.size()!=0){
                int goal = sums.get(0);
                for (int i=0;i<sums.size();i++){
                    this.grid[row][colom] = 0 ;
                    if(sums.get(i) != goal) return false;
                }
            }
            this.grid[row][colom] = 0 ;
            return true;
        }
        return false ;
    }

    public int checkRow(int row){
        int sum=0;
        for(int i=0 ; i<this.size;i++){
            if(this.grid[row][i] == 0) return 0;
            sum+=this.grid[row][i];
        }
        return sum;
    }

    public int checkColom(int col){
        int sum=0;
        for(int i=0 ; i<this.size;i++){
            if(this.grid[i][col] == 0) return 0;
            sum+=this.grid[i][col];
        }
        return sum;
    }

    public  int checkDirectDiameters(){
        int sum=0;
        for(int i=0;i<this.size;i++){

            if(this.grid[i][i] == 0) return 0;
            sum+= this.grid[i][i];
        }
        return sum;
    }

    public  int checkBackwardDiameters(){
        int sum=0;
        for(int i=0;i<this.size;i++){

            if(this.grid[i][size-1-i] == 0) return 0;
            sum+= this.grid[i][size-i-1];
        }
        return sum;
    }

    public int getCost(){
        int cost,filled=0,res;

        // check rows
        for (int i=0;i<this.size;i++){
            res = checkRow(i);
            if(res != 0)   filled++;
        }
        // check coloms
        for (int i=0;i<this.size;i++){
            res = checkColom(i);
            if(res != 0)    filled++;
        }
        // check diameters
        res = checkDirectDiameters();
        if(res != 0) filled++;
        res = checkBackwardDiameters();
        if(res != 0) filled++;

        return ((2*this.size)+2) - filled;
    }

    public void fill(int row , int colom , int num){
        this.grid[row][colom] = num ;
    }

    public ArrayList getAllNextStates(int[] numList){
        ArrayList<State> allNextStates = new ArrayList<>();
        for(int k=0 ; k< numList.length ;k++){
            for(int i=0 ; i<this.size;i++){
                for(int j=0 ; j<this.size;j++){
                    if(this.grid[i][j]==0){
                        if(this.canSet(i,j,numList[k])){
                            State newState = new State(this.size , this.numList);
                            newState.clone(this);
                            newState.fill(i,j,numList[k]);
                            allNextStates.add(newState);
                        }
                    }
                }
            }
        }
        return allNextStates;
    }

    public boolean isFull(){
        for(int i=0 ; i<this.size;i++){
            for(int j=0 ; j<this.size;j++){
                if(this.grid[i][j] == 0) return false;
            }
        }
        return true;
    }

    public int toRich (){
        int res = 0 ;
        for(int i=0;i<this.numList.length;i++){
            res+=this.numList[i];
        }
        int solution = res / this.size;

        int counter = 0;

        // check rows
        for (int i=0;i<this.size;i++){
            res = checkRow(i);
            if(res == solution) counter++;
        }
        // check coloms
        for (int i=0;i<this.size;i++){
            res = checkColom(i);
            if(res == solution) counter++;
        }
        // check diameters
        res = checkDirectDiameters();
        if(res == solution) counter++;
        res = checkBackwardDiameters();
        if(res == solution) counter++;

        return ( ( (this.size * 2) + 2 ) - counter ) ;
    }

    @Override
    public int compareTo(State state) {
        if (this.cost == state.cost) return 0;
        return this.cost > state.cost ? 1 : -1 ;
    }
}
