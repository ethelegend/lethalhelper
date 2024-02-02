import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class lethalhelper {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> scrapList = new ArrayList<Integer>(); // List of all the objects' values.
        int quota; // Amount of credits (currency) required by the Company
        int players; // Amount of players in the lobby
        int input; // Temporary variable used to copy terminal input to scrapList
        int scrapValue; // Total value of scrap
        int bodiesNeeded; // Dead players' bodies can be sold to the Company for 5 scrap each, but you lose some credits that you could use to buy upgrades.

        while (true) {
            System.out.println();
            System.out.println();
            System.out.println("Enter quota, 0 to quit"); // Having 0 as a finish code lets me use nextInt instead of nextLine.toString
            quota = Math.abs(scanner.nextInt());
            if (quota == 0) {
                break;
            }

            System.out.println();
            System.out.println("Enter player count, 0 to ignore");
            players = Math.abs(scanner.nextInt());

            System.out.println();
            System.out.println("Enter scrap values, 0 to finish, -X to delete previous X entries");
            scrapList.clear();
            do {
                input = scanner.nextInt();
                if (input > 0) {
                    scrapList.add(input); // Having a multidimensional array to label the scrap would be cool, but it would be a headache to implement
                } else { // By prefixing the input with a -, you can remove previous inputs
                    for (int i = input; i < 0; i++) {
                        scrapList.removeLast();
                    }
                }
            } while (input != 0); // I used a do-while loop because when run more than once, the starting value of input is 0 and not null, and the scanner writes to input within the loop
            

            Collections.sort(scrapList, Collections.reverseOrder()); // Sorts the array in descending order
            scrapList.addFirst(0); // A single instance of the recursive searcher considers all previous decisions, plus every possible future combination. Adding a 0 to the start allows it to be called once without having to write a case for it

            // This code calculates the total amount of scrap you have
            scrapValue = 0;
            for (Integer i : scrapList) {
                scrapValue += i;
            }
            
            bodiesNeeded = 0;
            if (scrapValue < quota) {
                bodiesNeeded = (quota - scrapValue + 4) / 5; // The + 4 bypasses a need for a Math.ceiling since it's being written to an int
                quota -= bodiesNeeded * 5;
                
            }
            
            // This is what prints the solution to the terminal
            System.out.println();
            if (players > 0 && players <= bodiesNeeded) { // You need at least 1 person alive to sell the bodies, so an amount of bodies above maximum means you are unable to meet quota
                System.out.println("Unable to reach quota");
            } else {
                ArrayList<Integer> solution = recursiveSearcher(scrapList, 0, quota);
                System.out.println(bodiesNeeded + " must be sacrificed, " + -solution.getLast() + " overshoot"); // Overshoot is strictly <= 0, so the minus sign is important
                solution.removeFirst();
                solution.removeLast(); // THe first value in the list is 0, and the last value in the list is the overshoot of the solution, and it might confuse people if I left it in
                System.out.println(solution);
            }
            
        }
        scanner.close();
    }

    // I had no plan when starting to make this, but I reasoned my way into it and I can't see a better solution
    static ArrayList<Integer> recursiveSearcher(ArrayList<Integer> scrapList, int pointer, int scrapRequired) {
        ArrayList<Integer> outputList = new ArrayList<Integer>(); // This list will be returned as inputList. It is initialised with the current scrap and required scrap
        outputList.add(scrapList.get(pointer));
        scrapRequired -= outputList.getFirst(); // The function inherites scrapRequired but not the previous pointer(s)
        outputList.add(scrapRequired); // This will always be at the end of the array.
        if (scrapRequired > 0) { // Don't look for more scrap if you've already met quota
            for (int i = pointer + 1; i < scrapList.size(); i++) { // Thankfully I don't have to make a case for when the instance is pointing to the last item in the array, as in that case pointer + 1 >= scrapList.size() and it returns as if scrapRequired > 0 
                ArrayList<Integer> inputList = new ArrayList<Integer>();
                inputList = recursiveSearcher(scrapList, i, scrapRequired); // Recursion
                if (inputList.getLast() <= 0 && inputList.getLast() > outputList.getLast() || outputList.getLast() > 0) { // I forgot to include this last condition and spent hours trying to figure out why this wasn't running
                    outputList.clear(); // Copies the inputList with the current scrap added to the start
                    outputList.add(scrapList.get(pointer));
                    outputList.addAll(inputList);
                }
                if (inputList.getLast() >= 0) { // If it equals 0 it triggers a return chain to main. If it is over 0, it means that the total value of scrap from i onwards is not enough to reach quota, and it is pointless to continue searching.
                    break;
                }
            }
        }
        return outputList; // Returns the list as an inputList for the higher layer. Since the current scrap was already subtracted from scrapRequired there's no need to update it
    }
}