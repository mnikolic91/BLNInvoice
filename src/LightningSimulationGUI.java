import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LightningSimulationGUI {

    private final Map<String, Long> nodeBalances;

    private final DefaultTableModel tableModel;
    private final JTable table;

    public LightningSimulationGUI() {
        nodeBalances = new HashMap<>();
        //update
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Node");
        tableModel.addColumn("Balance (satoshis)");

        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);

        JFrame frame = new JFrame("Lightning Network Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public void runSimulation(int numNodes, int numTransactions, long initialBalance) {
        // Initialize the node balances
        for (int i = 0; i < numNodes; i++) {
            String nodeName = "Node " + i;
            nodeBalances.put(nodeName, initialBalance);
            tableModel.addRow(new Object[] {nodeName, initialBalance});
        }

        // Execute the transactions
        Random random = new Random();
        for (int i = 0; i < numTransactions; i++) {
            String sourceNode = "Node " + random.nextInt(numNodes);
            String destNode = "Node " + random.nextInt(numNodes);
            long amount = random.nextInt((int) initialBalance);

            if (sourceNode.equals(destNode)) {
                continue; // Can't send to yourself
            }

            if (nodeBalances.get(sourceNode) < amount) {
                continue; // Not enough funds
            }

            nodeBalances.put(sourceNode, nodeBalances.get(sourceNode) - amount);
            nodeBalances.put(destNode, nodeBalances.get(destNode) + amount);
            tableModel.setValueAt(nodeBalances.get(sourceNode), getRowIndex(sourceNode), 1);
            tableModel.setValueAt(nodeBalances.get(destNode), getRowIndex(destNode), 1);
        }
    }

    private int getRowIndex(String nodeName) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(nodeName)) {
                return i;
            }
        }
        return -1;
    }

}
