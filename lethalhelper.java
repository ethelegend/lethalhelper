import javax.swing.*;
import javax.swing.BoxLayout;
import java.util.ArrayList;
import java.util.Collections;

public class lethalhelper extends JFrame {
    static JLabel instructions;
    static JTextField input;
    static byte mode;
    short quota; // Amount of credits (currency) required by the Company
    byte players; // Amount of players in the lobby. Unmodded max is 4 but modded max is ~40
    String[] scrapInput;
    ArrayList<Integer> scrapList = new ArrayList<>(); // List of all the objects' values.
    int scrapValue; // Total value of scrap
    byte bodiesNeeded; // Dead players' bodies can be sold to the Company for 5 scrap each, but you lose some credits that you could use to buy upgrades.
    public static void main(String[] args){
        new lethalhelper();
    }
    public lethalhelper() {
        setTitle("Lethal Helper");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        toFront();
        setVisible(true);
        setLayout (new BoxLayout (getContentPane(), BoxLayout.Y_AXIS));

        instructions = new JLabel("Enter quota");
        add(instructions);

        input = new JTextField();
        add(input);
        setSize(256,128);

        input.addActionListener(l -> {
            switch (mode) {
                case 0:
                    try {
                        quota = Short.parseShort(input.getText());
                        resetFrame("Enter player count, 0 to ignore");
                    } catch(Exception e) {}
                    break;
                case 1:
                    try {
                        players = Byte.parseByte(input.getText());
                        resetFrame("Enter scrap values, separated by commas");
                    } catch(Exception e) {}
                    break;
                case 2:
                    try {
                        scrapInput = input.getText().split(",");
                        for (String s:
                                scrapInput) {
                            scrapList.add(Integer.parseInt(s.replace(" ","")));
                        }

                        Collections.sort(scrapList, Collections.reverseOrder()); // Sorts the array in descending order
                        scrapList.addFirst(0); // A single instance of the recursive searcher considers all previous decisions, plus every possible future combination. Adding a 0 to the start allows it to be called once without having to write a case for it

                        // This code calculates the total amount of scrap you have
                        scrapValue = 0;
                        for (Integer i : scrapList) {
                            scrapValue += i;
                        }

                        bodiesNeeded = 0;
                        if (scrapValue < quota) {
                            bodiesNeeded = (byte) ((quota - scrapValue + 4) / 5); // The + 4 bypasses a need for a Math.ceiling since it's being written to an int
                            quota -= bodiesNeeded * 5;

                        }

                        getContentPane().removeAll();
                        // This is what prints the solution to the terminal
                        if (players > 0 && players <= bodiesNeeded) { // You need at least 1 person alive to sell the bodies, so an amount of bodies above maximum means you are unable to meet quota
                            add(new JLabel("Unable to reach quota"));
                        } else {
                            ArrayList<Integer> solution = recursiveSearcher(scrapList, 0, quota);
                            add(new JLabel(bodiesNeeded + " must be sacrificed, " + -solution.getLast() + " overshoot"));
                            System.out.println(bodiesNeeded + " must be sacrificed, " + -solution.getLast() + " overshoot"); // Overshoot is strictly <= 0, so the minus sign is important
                            solution.removeFirst();
                            solution.removeLast(); // The first value in the list is 0, and the last value in the list is the overshoot of the solution, and it might confuse people if I left it in
                            for (int i : solution) {
                                add(new JLabel((Integer.toString(i))));
                            }
                        }
                        revalidate();
                        repaint();
                    } catch(Exception e) {}
                    break;
            }
        });
    }

    static void resetFrame(String text) {
        mode++;
        instructions.setText(text);
        input.setText("");
    }

    // I had no plan when starting to make this, but I wandered my way into it, and I can't see a better solution
    static ArrayList<Integer> recursiveSearcher(ArrayList<Integer> scrapList, int pointer, int scrapRequired) {
        ArrayList<Integer> outputList = new ArrayList<Integer>(); // This list will be returned as inputList. It is initialised with the current scrap and required scrap
        outputList.add(scrapList.get(pointer));
        scrapRequired -= outputList.getFirst(); // The function inherits scrapRequired but not the previous pointer(s)
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
