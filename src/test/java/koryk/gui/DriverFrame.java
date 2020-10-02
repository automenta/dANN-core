package koryk.gui;

import koryk.binpack.BinPackProblem;
import koryk.binpack.gwa.WaveletBinProblemPanel;
import koryk.binpack.sga.SimpleBinProblemPanel;
import koryk.ga.Problem;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DriverFrame extends JFrame {
    private final Dimension SIZE = new Dimension(800, 600);
    private ControlPanel cp;
    private DisplayPanel dp;
    private int hardness;

    public DriverFrame() throws Throwable {
        super();
        cp = new ControlPanel(this);
        dp = new DisplayPanel(this);
        dp.setLayout(new GridLayout());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(SIZE);
        Container cpanel = this.getContentPane();
        cpanel.setLayout(new BorderLayout());
        cpanel.add(cp, BorderLayout.SOUTH);
        cpanel.add(dp, BorderLayout.CENTER);

        int[] hards = {100, 500};
        for (int j = 0; j < 2; j++) {
            hardness = hards[j];
            for (int i = 0; i < 100; i++) {
                try {
                    BinPackProblem prob = new BinPackProblem(1 + (int) (Math.random() * 3), 30, BinPackProblem.SGA, hardness), probtwo = new BinPackProblem(prob.getBins(), prob.getItems(), prob.getWeights());
                    probtwo.setImplementation(BinPackProblem.GWA);
                    loadProblems(prob, probtwo);

                    layout();
//                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        new DriverFrame();
    }

    public void loadProblem(Problem problem) {
        dp.add(problem.getProblemPanel());
        problem.getProblemPanel().runGA();
    }

    public void loadProblems(Problem problemOne, Problem problemTwo) throws Throwable {
        dp.removeAll();
        if (problemOne.equals(problemTwo))
               return;

        SimpleBinProblemPanel sgaPanel = (SimpleBinProblemPanel) problemOne.getProblemPanel();
        WaveletBinProblemPanel gwaPanel = (WaveletBinProblemPanel) problemTwo.getProblemPanel();
        dp.add(sgaPanel);
        dp.add(gwaPanel);
        dp.doLayout();


        sgaPanel.runGA();
        gwaPanel.runGA();
        ArrayList<Integer> sgaOrder = sgaPanel.getItemOrder(),
                gwaOrder = gwaPanel.getItemOrder();
        int gwaGen = gwaPanel.getGeneration(), sgaGen = sgaPanel.getGeneration();
        BinPackProblem problem = (BinPackProblem) problemOne;
        double sgaff = 0, sgafull = 0, gwaff = 0, gwafull = 0;
        double[] items = ((BinPackProblem) problemOne).getItems(), weights = ((BinPackProblem) problemOne).getWeights(), bins = ((BinPackProblem) problemOne).getBins();

        FileOutputStream os = new FileOutputStream(new File("comp.txt"), true);
        os.write((problem.toString() + " at " + hardness + " hardness\nSGA:").getBytes());
        double sum = bins[0];
        int bin = 0;
        for (int i : sgaOrder) {
            sum += items[i];
            if (sum > bins[0]) {
                sum = items[i];
                bin++;
                os.write(("\nKnapsack #" + bin + ": ").getBytes());
            }
            sgafull += items[i];
            os.write((i + ":(" + items[i] + " * " + weights[i] + ":" + items[i] * weights[i] + "), ").getBytes());
            sgaff += items[i] * weights[i];
        }
        sum = bins[0];
        bin = 0;
        os.write(("\nGWA: ").getBytes());
        for (int i : gwaOrder) {
            sum += items[i];
            if (sum > bins[0]) {
                sum = items[i];
                bin++;
                os.write(("\nKnapsack #" + bin + ": ").getBytes());
            }

            gwafull += items[i];
            os.write((i + ":(" + items[i] + " * " + weights[i] + ":" + items[i] * weights[i] + "), ").getBytes());
            gwaff += items[i] * weights[i];
        }
        gwaff /= bins[0] * bins.length;
        gwafull /= bins[0] * bins.length;
        sgaff /= bins[0] * bins.length;
        sgafull /= bins[0] * bins.length;
        os.write(("\nSGA: " + sgaff + "% fitness and " + sgafull + "% full at generation #" + sgaGen + "\n").getBytes());
        os.write(("GWA: " + gwaff + "% fitness and " + gwafull + "% full at generation #" + gwaGen).getBytes());
        os.write("\n\n".getBytes());
        os.close();

    }
}
