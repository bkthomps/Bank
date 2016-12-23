/**
 ***********************************************************************************************************************
 * Bailey Thompson
 * Bank (1.0.2)
 * 22 December 2016
 * Info: This program simulates an automated banking system.
 ***********************************************************************************************************************
 */
package bank;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.Integer.parseInt;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

public class Bank {

    private static final Path FILE = Paths.get("JBBank.txt");
    private int username, password, balance, usernameIndex;
    private boolean isDepositing, isRegister;
    private String saveFile;
    private String[] split;
    private NumberFormat format;
    private NumberFormatter formatter;
    private JFormattedTextField textField;
    private final JFrame frame = new JFrame("JB's Bank");
    private final JLabel text = new JLabel("Welcome to JB's Bank! What action would you like to make today?");
    private final JButton deposit = new JButton("Deposit"), withdraw = new JButton("Withdraw"), next = new JButton("Next");
    private final JButton exit = new JButton("Exit"), back = new JButton("Back");
    private final JButton login = new JButton("Login"), register = new JButton("Register");
    private final JButton money20 = new JButton("$20"), money50 = new JButton("$50"), money100 = new JButton("$100");
    private final JButton money200 = new JButton("$200"), money500 = new JButton("$500"), money1000 = new JButton("$1000");
    private final JPanel numberPanel = new JPanel();

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

        back.addActionListener((ActionEvent e) -> {
            loginScreen();
        });

        deposit.addActionListener((ActionEvent e) -> {
            setInfo();
            isDepositing = true;
        });

        withdraw.addActionListener((ActionEvent e) -> {
            setInfo();
            isDepositing = false;
        });

        money20.addActionListener((ActionEvent e) -> {
            accountInteraction(20);
        });

        money50.addActionListener((ActionEvent e) -> {
            accountInteraction(50);
        });

        money100.addActionListener((ActionEvent e) -> {
            accountInteraction(100);
        });

        money200.addActionListener((ActionEvent e) -> {
            accountInteraction(200);
        });

        money500.addActionListener((ActionEvent e) -> {
            accountInteraction(500);
        });

        money1000.addActionListener((ActionEvent e) -> {
            accountInteraction(1000);
        });

        next.addActionListener((ActionEvent e) -> {
            boolean used = false;
            if (username == 0) {
                //if user has entered something
                if (textField.getValue() != null) {
                    //when what user has entered is long enough
                    if ((int) (textField.getValue()) >= 100000) {
                        int tempUsername = (int) (textField.getValue());
                        //if login is being proccessed
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
                            //if register is being proccessed
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
                        //if username entered passes all checks
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
                //when password is being entered
            } else if (username != 0) {
                //if user has entered something
                if (textField.getValue() != null) {
                    //when what user has entered is long enough
                    if ((int) (textField.getValue()) >= 1000) {
                        //setting temporary variable
                        int tempPassword = (int) (textField.getValue());
                        //if login is being proccessed
                        if (!isRegister) {
                            if (tempPassword == parseInt(split[usernameIndex + 1])) {
                                password = tempPassword;
                            } else {
                                text.setText("Incorrect password!");
                            }
                            balance = parseInt(split[usernameIndex + 2]);
                            //if register is being proccessed
                        } else {
                            password = (int) (textField.getValue());
                            saveFile = "";
                            for (int counter = 0; counter < split.length; counter += 1) {
                                saveFile += split[counter] + " ";
                            }
                            saveFile += username + " " + password + " 0 ";
                            split = saveFile.split("\\s+");
                            save();
                        }
                        //if password entered passes all checks
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

    private void setInfo() {
        text.setText("Select amount to withdraw. Amount in account: $" + balance);
        frame.getContentPane().removeAll();
        frame.add(text);
        frame.add(numberPanel);
        frame.add(back);
        frame.setLayout(new GridLayout(3, 1));
        frame.repaint();
    }

    private void accountInteraction(int amountOfMoney) {
        if (isDepositing) {
            if (balance <= 1000000000) {
                balance += amountOfMoney;
                text.setText("Select amount to deposit. Amount in account: $" + balance);
            }
        } else {
            if (balance >= amountOfMoney) {
                balance -= amountOfMoney;
            } else {
                balance = 0;
            }
            text.setText("Select amount to withdraw. Amount in account: $" + balance);
        }
        split[usernameIndex + 2] = String.valueOf(balance);
        save();
    }

    private void startScreen() {
        username = 0;
        password = 0;
        balance = 0;

        text.setText("Welcome to JB's Bank! What action would you like to make today?");
        exit.setText("Exit Application");
        setGUI();
    }

    private void loginScreen() {
        text.setText("Please select your operation!");
        setGUI();
    }

    private void setGUI() {
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

        format = NumberFormat.getInstance();
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
            //file is read from and saved to variable saveFile is file already exists
            try (InputStream in = Files.newInputStream(FILE);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line = reader.readLine();
                while (line != null) {
                    saveFile = line;
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        //if the file does not contain anything since it was just created, default variables are used for save file
        if (saveFile == null) {
            saveFile = "301942 1234 0 ";
        }
        //a String array is created and each part of the array is saved to from saveFile seperated by spaces
        split = saveFile.split("\\s+");
    }

    private void save() {
        saveFile = "";
        for (int counter = 0; counter < split.length; counter += 1) {
            saveFile += split[counter] + " ";
        }
        //saveFile is converted to byte data
        byte data[] = saveFile.getBytes();
        //byte data is saved to file using file io
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(FILE, WRITE, TRUNCATE_EXISTING))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}
