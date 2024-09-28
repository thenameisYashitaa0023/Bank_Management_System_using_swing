import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Account {
    private double balance;
    private boolean isFrozen;
    private List<String> transactionHistory;

    public Account() {
        this.balance = 0.0;
        this.isFrozen = false;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (!isFrozen) {
            balance += amount;
            transactionHistory.add("Deposited: " + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (!isFrozen && balance >= amount) {
            balance -= amount;
            transactionHistory.add("Withdrew: " + amount);
            return true;
        }
        return false;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void freeze() {
        isFrozen = true;
        transactionHistory.add("Account frozen.");
    }

    public void unfreeze() {
        isFrozen = false;
        transactionHistory.add("Account unfrozen.");
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}

public class BankManagementSystem extends JFrame {
    private HashMap<String, Account> accounts;
    private JTextField accountField, amountField, accountNumberField;
    private JTextArea outputArea;

    public BankManagementSystem() {
        accounts = new HashMap<>();
        setTitle("Bank Management System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Logo
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\HP NOTEBOOK\\Desktop\\bank.jpg"); // Change this path to your logo
        Image logoImage = logoIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.anchor = GridBagConstraints.CENTER;
        add(logoLabel, gbc);

        // Heading
        JLabel heading = new JLabel("<html><span style='color: orange;'>Welcome </span>" +
                             "<span style='color: #ADD8E6;'>To Indian </span>" +
                             "<span style='color: green;'>Bank !</span></html>", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy++;
        add(heading, gbc);

        // Create a panel for columns
        JPanel mainPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        
        // Create Account Panel
        JPanel createAccountPanel = createAccountPanel();
        mainPanel.add(createAccountPanel);

        // Deposit/Withdraw Panel
        JPanel transactionPanel = transactionPanel();
        mainPanel.add(transactionPanel);

        // Freeze Account Panel
        JPanel freezeAccountPanel = freezeAccountPanel();
        mainPanel.add(freezeAccountPanel);

        // Unfreeze Account Panel
        JPanel unfreezeAccountPanel = unfreezeAccountPanel();
        mainPanel.add(unfreezeAccountPanel);

        // Check Balance Panel
        JPanel checkBalancePanel = checkBalancePanel();
        mainPanel.add(checkBalancePanel);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        add(mainPanel, gbc);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setPreferredSize(new Dimension(300, 100)); // Set the preferred size

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(300, 100)); // Set the preferred size for the scroll pane

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 5; // Adjusted for the new column
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        getContentPane().setBackground(Color.decode("#f0f0f0"));

        setVisible(true);
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Create Account"));

        JLabel accountNameLabel = new JLabel("Account Name:");
        panel.add(accountNameLabel);

        accountField = new JTextField();
        panel.add(accountField);

        JLabel accountNumberLabel = new JLabel("Account Number:");
        panel.add(accountNumberLabel);

        accountNumberField = new JTextField();
        panel.add(accountNumberField);

        JButton createButton = new JButton("Create Account");
        panel.add(createButton);
        createButton.addActionListener(e -> createAccount());

        JButton clearButton = new JButton("Clear");
        panel.add(clearButton);
        clearButton.addActionListener(e -> clearFields());

        return panel;
    }

    private JPanel transactionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Deposit/Withdraw"));

        JLabel amountLabel = new JLabel("Amount:");
        panel.add(amountLabel);

        amountField = new JTextField();
        panel.add(amountField);

        JButton depositButton = new JButton("Deposit");
        panel.add(depositButton);
        depositButton.addActionListener(e -> deposit());

        JButton withdrawButton = new JButton("Withdraw");
        panel.add(withdrawButton);
        withdrawButton.addActionListener(e -> withdraw());

        return panel;
    }

    private JPanel freezeAccountPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Freeze Account"));

        JLabel accountLabel = new JLabel("Account Name:");
        panel.add(accountLabel);

        JTextField freezeAccountField = new JTextField();
        panel.add(freezeAccountField);

        JButton freezeButton = new JButton("Freeze Account");
        panel.add(freezeButton);
        freezeButton.addActionListener(e -> {
            String accountName = freezeAccountField.getText().trim();
            freezeAccount(accountName);
            freezeAccountField.setText("");
        });

        return panel;
    }

    private JPanel unfreezeAccountPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Unfreeze Account"));

        JLabel accountLabel = new JLabel("Account Name:");
        panel.add(accountLabel);

        JTextField unfreezeAccountField = new JTextField();
        panel.add(unfreezeAccountField);

        JButton unfreezeButton = new JButton("Unfreeze Account");
        panel.add(unfreezeButton);
        unfreezeButton.addActionListener(e -> {
            String accountName = unfreezeAccountField.getText().trim();
            unfreezeAccount(accountName);
            unfreezeAccountField.setText("");
        });

        return panel;
    }

    private JPanel checkBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Check Balance"));

        JLabel accountLabel = new JLabel("Account Name:");
        panel.add(accountLabel);

        JTextField checkBalanceField = new JTextField();
        panel.add(checkBalanceField);

        JButton checkButton = new JButton("Check Balance");
        panel.add(checkButton);
        checkButton.addActionListener(e -> {
            String accountName = checkBalanceField.getText().trim();
            checkBalance(accountName);
            checkBalanceField.setText("");
        });

        return panel;
    }

    private void checkBalance(String accountName) {
        if (accounts.containsKey(accountName)) {
            double balance = accounts.get(accountName).getBalance();
            showMessage("The balance for " + accountName + " is: " + balance);
        } else {
            showMessage("Account not found.");
        }
    }

    private void createAccount() {
        String accountName = accountField.getText().trim();
        String accountNumber = accountNumberField.getText().trim();
        if (!accountName.isEmpty() && !accountNumber.isEmpty() && !accounts.containsKey(accountName)) {
            accounts.put(accountName, new Account());
            showMessage("Account created for " + accountName + " (Account No: " + accountNumber + ").");
        } else {
            showMessage("Account already exists or name/number is empty.");
        }
        clearFields();
    }

    private void deposit() {
        String accountName = accountField.getText().trim();
        String amountStr = amountField.getText().trim();
        if (accounts.containsKey(accountName) && !amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);
            accounts.get(accountName).deposit(amount);
            showMessage("Deposited " + amount + " to " + accountName + ".\n" +
                        "Transaction History: " + accounts.get(accountName).getTransactionHistory());
        } else {
            showMessage("Account not found or invalid amount.");
        }
        clearFields();
    }

    private void withdraw() {
        String accountName = accountField.getText().trim();
        String amountStr = amountField.getText().trim();
        if (accounts.containsKey(accountName) && !amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);
            if (accounts.get(accountName).withdraw(amount)) {
                showMessage("Withdrew " + amount + " from " + accountName + ".\n" +
                            "Transaction History: " + accounts.get(accountName).getTransactionHistory());
            } else {
                showMessage("Insufficient funds or account is frozen.");
            }
        } else {
            showMessage("Account not found or invalid amount.");
        }
        clearFields();
    }

    private void freezeAccount(String accountName) {
        if (accounts.containsKey(accountName)) {
            accounts.get(accountName).freeze();
            showMessage("Account " + accountName + " has been frozen.");
        } else {
            showMessage("Account not found.");
        }
    }

    private void unfreezeAccount(String accountName) {
        if (accounts.containsKey(accountName)) {
            accounts.get(accountName).unfreeze();
            showMessage("Account " + accountName + " has been unfrozen.");
        } else {
            showMessage("Account not found.");
        }
    }

    private void showMessage(String message) { 
        JOptionPane.showMessageDialog(this, message, "Account Activity", JOptionPane.INFORMATION_MESSAGE);
        outputArea.append(message + "\n");
        outputArea.append("Thank You! dear Customer. Please Visit Again. We are Here to Help you Always.\n\n");
    }

    private void clearFields() {
        accountField.setText("");
        accountNumberField.setText("");
        amountField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankManagementSystem::new);
    }
}
