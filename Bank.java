/*
 * Bailey Thompson
 * Bank (1.1.1)
 * 20 February 2017
 */

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import static java.lang.Integer.parseInt;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Simulates an automated banking system.
 */
class Bank {

    private final Path FILE = Paths.get("JBBank.txt");

    private final JFrame frame = new JFrame("JB's Bank");
    private final JLabel text = new JLabel("Welcome to JB's Bank! What action would you like to make today?");
    private final JButton deposit = new JButton("Deposit");
    private final JButton withdraw = new JButton("Withdraw");
    private final JButton next = new JButton("Next");
    private final JButton exit = new JButton("Exit");
    private final JButton back = new JButton("Back");
    private final JButton login = new JButton("Login");
    private final JButton register = new JButton("Register");
    private final JButton money20 = new JButton("$20");
    private final JButton money50 = new JButton("$50");
    private final JButton money100 = new JButton("$100");
    private final JButton money200 = new JButton("$200");
    private final JButton money500 = new JButton("$500");
    private final JButton money1000 = new JButton("$1000");
    private final JPanel numberPanel = new JPanel();

    private NumberFormatter formatter;
    private JFormattedTextField textField;

    private String saveFile;
    private String[] split;
    private int username, password, balance, usernameIndex;
    private boolean isDepositing, isRegister, used;

    public static void main(String[] args) {
        Bank Bank = new Bank();
        Bank.prepareGUI();
    }

    private void prepareGUI() {
        load();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        startScreen();

        numberPanel.add(money20);
        numberPanel.add(money50);
        numberPanel.add(money100);
        numberPanel.add(money200);
        numberPanel.add(money500);
        numberPanel.add(money1000);
        numberPanel.setLayout(new GridLayout(3, 2));

        login.addActionListener((ActionEvent e) -> {
            isRegister = false;
            operations();
        });

        register.addActionListener((ActionEvent e) -> {
            isRegister = true;
            operations();
        });

        exit.addActionListener((ActionEvent e) -> {
            if ("Return To Main Screen".equals(exit.getText())) {
                startScreen();
            } else {
                System.exit(0);
            }
        });

        back.addActionListener((ActionEvent e) -> loginScreen());

        deposit.addActionListener((ActionEvent e) -> {
            text.setText("Select amount to deposit. Amount in account: $" + balance);
            frame.getContentPane().removeAll();
            frame.add(text);
            frame.add(numberPanel);
            frame.add(back);
            frame.setLayout(new GridLayout(3, 1));
            frame.repaint();
            isDepositing = true;
        });

        withdraw.addActionListener((ActionEvent e) -> {
            text.setText("Select amount to withdraw. Amount in account: $" + balance);
            frame.getContentPane().removeAll();
            frame.add(text);
            frame.add(numberPanel);
            frame.add(back);
            frame.setLayout(new GridLayout(3, 1));
            frame.repaint();
            isDepositing = false;
        });

        money20.addActionListener((ActionEvent e) -> {
            if (isDepositing) {
                if (balance <= 1000000000) {
                    balance += 20;
                    text.setText("Select amount to deposit. Amount in account: $" + balance);
                }
            } else {
                if (balance >= 20) {
                    balance -= 20;
                } else {
                    balance = 0;
                }
                text.setText("Select amount to withdraw. Amount in account: $" + balance);
            }

            split[usernameIndex + 2] = String.valueOf(balance);
            save();
        });

        money50.addActionListener((ActionEvent e) -> {
            if (isDepositing) {
                if (balance <= 1000000000) {
                    balance += 50;
                    text.setText("Select amount to deposit. Amount in account: $" + balance);
                }
            } else {
                if (balance >= 50) {
                    balance -= 50;
                } else {
                    balance = 0;
                }
                text.setText("Select amount to withdraw. Amount in account: $" + balance);
            }
            split[usernameIndex + 2] = String.valueOf(balance);
            save();
        });

        money100.addActionListener((ActionEvent e) -> {
            if (isDepositing) {
                if (balance <= 1000000000) {
                    balance += 100;
                    text.setText("Select amount to deposit. Amount in account: $" + balance);
                }
            } else {
                if (balance >= 100) {
                    balance -= 100;
                } else {
                    balance = 0;
                }
                text.setText("Select amount to withdraw. Amount in account: $" + balance);
            }

            split[usernameIndex + 2] = String.valueOf(balance);
            save();
        });

        money200.addActionListener((ActionEvent e) -> {
            if (isDepositing) {
                if (balance <= 1000000000) {
                    balance += 200;
                    text.setText("Select amount to deposit. Amount in account: $" + balance);
                }
            } else {
                if (balance >= 200) {
                    balance -= 200;
                } else {
                    balance = 0;
                }
                text.setText("Select amount to withdraw. Amount in account: $" + balance);
            }

            split[usernameIndex + 2] = String.valueOf(balance);
            save();
        });

        money500.addActionListener((ActionEvent e) -> {
            if (isDepositing) {
                if (balance <= 1000000000) {
                    balance += 500;
                    text.setText("Select amount to deposit. Amount in account: $" + balance);
                }
            } else {
                if (balance >= 500) {
                    balance -= 500;
                } else {
                    balance = 0;
                }
                text.setText("Select amount to withdraw. Amount in account: $" + balance);
            }

            split[usernameIndex + 2] = String.valueOf(balance);
            save();
        });

        money1000.addActionListener((ActionEvent e) -> {
            if (isDepositing) {
                if (balance <= 1000000000) {
                    balance += 1000;
                    text.setText("Select amount to deposit. Amount in account: $" + balance);
                }
            } else {
                if (balance >= 1000) {
                    balance -= 1000;
                } else {
                    balance = 0;
                }
                text.setText("Select amount to withdraw. Amount in account: $" + balance);
            }

            split[usernameIndex + 2] = String.valueOf(balance);
            save();
        });

        next.addActionListener((ActionEvent e) -> {
            used = false;
            if (username == 0) {
                if (textField.getValue() != null) {
                    if ((int) (textField.getValue()) >= 100000) {
                        int tempUsername = (int) (textField.getValue());
                        if (!isRegister) {
                            for (int counter = 0; counter < split.length; counter += 3) {
                                if (parseInt(split[counter]) == tempUsername) {
                                    used = true;
                                    usernameIndex = counter;
                                }
                            }
                            if (used) {
                                username = tempUsername;
                            } else {
                                text.setText("This bank code does not exist!");
                            }
                        } else {
                            for (int counter = 0; counter < split.length; counter += 3) {
                                if (parseInt(split[counter]) == tempUsername) {
                                    used = true;
                                }
                            }
                            if (!used) {
                                username = tempUsername;
                                usernameIndex = split.length;
                            } else {
                                text.setText("That bank code has already been used.");
                            }
                        }
                        if (username != 0) {
                            text.setText("Please insert your four digit passcode!");
                            formatter.setMaximum(9999);
                            textField.setValue(null);
                            save();
                        }
                    } else {
                        text.setText("Your bank code must not start with a zero and must be six digits!");
                    }
                }
            } else {
                if (textField.getValue() != null) {
                    if ((int) (textField.getValue()) >= 1000) {
                        int tempPassword = (int) (textField.getValue());
                        if (!isRegister) {
                            if (tempPassword == parseInt(split[usernameIndex + 1])) {
                                password = tempPassword;
                            } else {
                                text.setText("Incorrect password!");
                            }
                            balance = parseInt(split[usernameIndex + 2]);
                        } else {
                            password = (int) (textField.getValue());
                            saveFile = "";
                            for (String aSplit : split) {
                                saveFile += aSplit + " ";
                            }
                            saveFile += username + " " + password + " 0 ";
                            split = saveFile.split("\\s+");
                            save();
                        }
                        if (password != 0) {
                            loginScreen();
                        }
                    } else {
                        text.setText("Your passcode must not start with a zero and must be four digits!");
                    }
                }
            }
        });
    }

    private void startScreen() {
        username = 0;
        password = 0;
        balance = 0;
        text.setText("Welcome to JB's Bank! What action would you like to make today?");
        exit.setText("Exit Application");
        frame.getContentPane().removeAll();
        frame.add(text);
        frame.add(login);
        frame.add(register);
        frame.add(exit);
        frame.setLayout(new GridLayout(4, 1));
        frame.repaint();
    }

    private void loginScreen() {
        text.setText("Please select your operation!");
        frame.getContentPane().removeAll();
        frame.add(text);
        frame.add(deposit);
        frame.add(withdraw);
        frame.add(exit);
        frame.setLayout(new GridLayout(4, 1));
        frame.repaint();
    }

    private void operations() {
        text.setText("Please insert your six digit bank code!");
        exit.setText("Return To Main Screen");

        NumberFormat format = NumberFormat.getInstance();
        formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1);
        formatter.setMaximum(999999);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        textField = new JFormattedTextField(formatter);

        frame.getContentPane().removeAll();
        frame.add(text);
        frame.add(textField);
        frame.add(next);
        frame.add(exit);
        frame.setLayout(new GridLayout(4, 1));
        frame.repaint();
    }

    private void load() {
        try {
            Files.createFile(FILE);
        } catch (FileAlreadyExistsException x) {
            try (InputStream in = Files.newInputStream(FILE);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    saveFile = line;
                }
            } catch (IOException y) {
                System.err.println("Error 1 in load method");
            }
        } catch (IOException x) {
            System.err.println("Error 2 in load method");
        }
        if (saveFile == null) {
            saveFile = "301942 1234 0 ";
        }
        split = saveFile.split("\\s+");
    }

    private void save() {
        saveFile = "";
        for (String aSplit : split) {
            saveFile += aSplit + " ";
        }
        byte data[] = saveFile.getBytes();
        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(FILE, WRITE, TRUNCATE_EXISTING))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println("Error in save method");
        }
    }
}
