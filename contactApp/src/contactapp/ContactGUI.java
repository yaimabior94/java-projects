package contactapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ContactGUI extends JFrame {

    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;

    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;

    private JTable table;
    private DefaultTableModel model;

    public ContactGUI() {

        setTitle("Contact Manager");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() {

        setLayout(new BorderLayout());

        // ===== Input Panel =====
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        inputPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        inputPanel.add(txtPhone);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);

        add(inputPanel, BorderLayout.NORTH);

        // ===== Table =====
        String[] columns = {"Name", "Phone", "Email"};

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel();

        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Add Contact =====
        btnAdd.addActionListener(e -> {

            String name = txtName.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all fields.");
                return;
            }

            model.addRow(new Object[]{name, phone, email});

            clearFields();
        });

        // ===== Edit Contact =====
        btnEdit.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {

                model.setValueAt(txtName.getText(), row, 0);
                model.setValueAt(txtPhone.getText(), row, 1);
                model.setValueAt(txtEmail.getText(), row, 2);

                JOptionPane.showMessageDialog(this,
                        "Contact updated successfully.");

            } else {
                JOptionPane.showMessageDialog(this,
                        "Select a contact first.");
            }
        });

        // ===== Delete Contact =====
        btnDelete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Delete selected contact?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    model.removeRow(row);
                    clearFields();
                }

            } else {
                JOptionPane.showMessageDialog(this,
                        "Select a contact first.");
            }
        });

        // ===== Clear Fields =====
        btnClear.addActionListener(e -> clearFields());

        // ===== Load Selected Row =====
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int row = table.getSelectedRow();

                txtName.setText(model.getValueAt(row, 0).toString());
                txtPhone.setText(model.getValueAt(row, 1).toString());
                txtEmail.setText(model.getValueAt(row, 2).toString());
            }
        });
    }

    private void clearFields() {

        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");

        txtName.requestFocus();
    }
}