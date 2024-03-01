import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class ProductForm extends JFrame {

    private JPanel p1, p2, p3, p4, p5;
    private JLabel idLabel, nameLabel, qtyLabel, priceLabel;
    private JButton btADD, btEDIT, btDEL, btref; // Fixed typo here
    private JTable productTable;
    private JTextField tid, tna, tq, tp;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private Product product;
    private Connection con = null;
    private String url = "jdbc:mysql://localhost/comstore";

    public ProductForm() {
        product = new Product();
        setTitle("Product Manager");
        setSize(1200, 600);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setLocationRelativeTo(null);

        idLabel = new JLabel("Product ID :");
        nameLabel = new JLabel("Product Name :");
        priceLabel = new JLabel("Product Price :");
        qtyLabel = new JLabel("Product Qty :");

        tid = new JTextField(10);
        tna = new JTextField(10);
        tq = new JTextField(10);
        tp = new JTextField(10);

        btADD = new JButton("Add");
        btEDIT = new JButton("Edit");
        btDEL = new JButton("DELETE");
        btref = new JButton("REFRESH"); // Fixed typo here

        productTable = new JTable();

        scrollPane = new JScrollPane(productTable);

        tableModel = new DefaultTableModel();
        productTable.setModel(tableModel);

        tableModel.addColumn("ID");
        tableModel.addColumn("NAME");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price");

        add(idLabel);
        add(tid);
        add(nameLabel);
        add(tna);
        add(qtyLabel);
        add(tq);
        add(priceLabel);
        add(tp);
        add(btADD);
        add(btEDIT);
        add(btDEL);
        add(btref);
        add(scrollPane);

        btADD.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String id = tid.getText();
                        String name = tna.getText();
                        int qty = Integer.parseInt(tq.getText());
                        int price = Integer.parseInt(tp.getText());

                        product.setDetails(id, name, qty, price);
                        product.insertP();
                        refreshTable();
                    }
                }
        );
        productTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        if (!event.getValueIsAdjusting()) {
                            int selectedRow = productTable.getSelectedRow();
                            int selectedColumn = productTable.getSelectedColumn();

                            if (selectedRow != -1 && selectedColumn != -1) {
                                String id = tableModel.getValueAt(selectedRow, 0).toString();
                                String name = tableModel.getValueAt(selectedRow, 1).toString();
                                int qty = Integer.parseInt(
                                        tableModel.getValueAt(selectedRow, 2).toString()
                                );
                                int price = Integer.parseInt(
                                        tableModel.getValueAt(selectedRow, 3).toString()
                                );

                                tid.setText(id);
                                tna.setText(name);
                                tq.setText(String.valueOf(qty));
                                tp.setText(String.valueOf(price));
                            }
                        }
                    }
                }
        );

        btEDIT.addMouseListener(
                new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        int rowIndex = productTable.getSelectedRow();
                        if (rowIndex != -1) {
                            String id = tid.getText();
                            String name = tna.getText();
                            int price = Integer.parseInt(tp.getText());
                            int qty = Integer.parseInt(tq.getText());

                            tableModel.setValueAt(id, rowIndex, 0);
                            tableModel.setValueAt(name, rowIndex, 1);
                            tableModel.setValueAt(qty, rowIndex, 2);
                            tableModel.setValueAt(price, rowIndex, 3);

                            product.setDetails(id, name, qty, price);
                            product.updatep();
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Please select a row to edit.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
        );

        btDEL.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int rowIndex = productTable.getSelectedRow();
                        if (rowIndex != -1) {
                            String id = tid.getText();
                            product.setDetails(id, "", 0, 0);
                            product.deletep();
                            refreshTable();
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Please select a row to delete.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
        );

        btref.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ResultSet rs = product.selectAllp();
        try {
            while (rs.next()) {
                String id = rs.getString("P_id");
                String name = rs.getString("P_name");
                int price = rs.getInt("P_price");
                int qty = rs.getInt("P_qty");
                Vector<Object> row = new Vector<>();
                row.add(id);
                row.add(name);
                row.add(qty);
                row.add(price);
                tableModel.addRow(row);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ProductForm();
    }
}
