import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.net.*;
import java.time;

// login screen that is seen on running the application
class LoginScreen extends JFrame implements ActionListener {

    // components that are added to the panels
    private JFrame loginFrame = new JFrame();
    private JLabel user = new JLabel("Username:");
    private JTextField userText = new JTextField(20);
    private JLabel pass = new JLabel("Password: ");
    private JPasswordField passText = new JPasswordField(20);
    private JButton loginButton = new JButton("Login");
    private JButton signUpButton = new JButton("Sign Up");
    private ImageIcon logo = new ImageIcon(
            "C:/Users/Admin/Desktop/TSA/Code/Desktop Application/v5/SoundMemoryLogoFull.png");
    private ImageIcon icon = new ImageIcon(
            "C:/Users/Admin/Desktop/TSA/Code/Desktop Application/v5/SoundMemoryLogoIcon.png");
    private JLabel logoLabel = new JLabel(logo);

    // panels that are added to the frame
    private JPanel userPanel = new JPanel(new FlowLayout());
    private JPanel passwordPanel = new JPanel(new FlowLayout());
    private JPanel buttonPanel = new JPanel(new FlowLayout());
    private JPanel imagePanel = new JPanel(new FlowLayout());

    // creates the gui window with its necessary information
    public LoginScreen() {
        setTitle("Sound Memory");
        setSize(300, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setIconImage(icon.getImage());

        // adds each component to its panel and adds each panel to the frame
        userPanel.add(user);
        userPanel.add(userText);
        passwordPanel.add(pass);
        passwordPanel.add(passText);
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        imagePanel.add(logoLabel);

        add(userPanel);
        add(passwordPanel);
        add(buttonPanel);
        add(imagePanel);

        setVisible(true);

        // creates a function that waits for the button to be clicked
        loginButton.addActionListener(new ActionListener() {
            /*
             * function takes the text from the username and password fields and checks
             * whether the credentials match that of an existing profile or not,
             * with the intention of moving on to the main menu if the profile exists
             * and displaying and error message if it does not
             */
            public void actionPerformed(ActionEvent event) {

                try {
                    Socket client = new Socket("192.168.0.59", 1235);
                    byte[] buf = new byte[1024];
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(client.getOutputStream()));

                    String username = userText.getText();
                    char[] passArr = passText.getPassword();
                    String password = "";
                    for (int i = 0; i < passArr.length; i++) {
                        password += passArr[i];
                    }

                    bufferedWriter.write("*9#" + username);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    bufferedWriter.write("#8*" + password);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    String correctLogin = "";
                    while (!correctLogin.equals("true") && !correctLogin.equals("false")) {
                        correctLogin = bufferedReader.readLine();
                    }

                    if (correctLogin.equals("true")) {
                        bufferedWriter.write("bye");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        String patientInfo = bufferedReader.readLine();
                        while (patientInfo.equals("")) {
                            patientInfo = bufferedReader.readLine();
                        }
                        dispose();
                        userText.setText("");
                        passText.setText("");
                        InteractiveMenu mainMenu = new InteractiveMenu(patientInfo);
                        setVisible(false);
                    } else {
                        System.out.println("incorrect login");
                        bufferedWriter.write("bye");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                SignUpScreen signUp = new SignUpScreen();
                setVisible(false);
            }
        });
    }
}

// info pages about the patient
class InteractiveMenu extends JFrame implements ActionListener {
    private JFrame menuFrame = new JFrame();
    private JTabbedPane pane = new JTabbedPane();

    // all information is put into its respective panel, which is then added to the
    // card layout
    private JPanel homePanel = new JPanel();
    private JPanel allergenPanel = new JPanel();
    private JPanel illnessPanel = new JPanel();
    private JPanel medsPanel = new JPanel();
    private JPanel notesPanel = new JPanel();

    // labels of the information stored in the first card
    private ImageIcon logo = new ImageIcon(
            "C:/Users/Admin/Desktop/TSA/Code/Desktop Application/v5/SoundMemoryLogoFull.png");
    private ImageIcon icon = new ImageIcon(
            "C:/Users/Admin/Desktop/TSA/Code/Desktop Application/v5/SoundMemoryLogoIcon.png");

    private JLabel imageLabel = new JLabel(logo);
    private JLabel nameLabel = new JLabel("name: ");
    private JLabel birthLabel = new JLabel("D.O.B.: ");
    private JLabel heightLabel = new JLabel("Height: ");
    private JLabel weightLabel = new JLabel("Weight: ");
    private JLabel roomNumLabel = new JLabel("Room Number: ");
    private JLabel nurseLabel = new JLabel("Caretaker: ");
    private JLabel contactLabel = new JLabel("Emergency Contact: ");

    // shows the information corresponding to each label above
    private JLabel patientNameLabel;
    private JLabel patientBirthLabel;
    private JLabel patientWeightLabel;
    private JLabel patientHeightLabel;
    private JLabel patientRoomNumLabel;
    private JLabel patientNurseLabel;
    private JLabel patientContactLabel;
    private JLabel patientAllergenLabel;
    private JLabel patientIllnessLabel;
    private JLabel patientMedicationLabel;
    private JTextArea patientExtraNotesLabel;

    // the panels that each label goes onto
    private JPanel imagePanel = new JPanel();
    private JPanel namePanel = new JPanel();
    private JPanel birthPanel = new JPanel();
    private JPanel heightPanel = new JPanel();
    private JPanel weightPanel = new JPanel();
    private JPanel hwPanel = new JPanel();
    private JPanel roomNumPanel = new JPanel();
    private JPanel nursePanel = new JPanel();
    private JPanel contactPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    // scroll panes in order to allow the information to be formatted in a more
    // readable way
    private JScrollPane notesPane = new JScrollPane(notesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    private JButton logoutButton = new JButton("Logout");
    private JButton editButton = new JButton("edit");

    private String patientInfo;

    // creates the menu screen that displays all patient information
    public InteractiveMenu(String info) {
        patientInfo = info;
        setSize(600, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());
        setIconImage(icon.getImage());
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        notesPane.setSize(600, 1800);

        String patientName = patientInfo.substring(4, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientHeight = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientWeight = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientDOB = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientCaretaker = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientIllnesses = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientAllergies = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientExtraNotes = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientEmergencyContact = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientRoomNumber = patientInfo.substring(0, patientInfo.indexOf("`"));
        patientInfo = patientInfo.substring(patientInfo.indexOf("`") + 1);
        String patientCurrMeds = patientInfo;

        patientHeight = ((Integer.valueOf(patientHeight)) / 12) + "'" + ((Integer.valueOf(patientHeight)) % 12) + '"';
        patientWeight += " lbs";

        patientNameLabel = new JLabel(patientName);
        patientBirthLabel = new JLabel(patientDOB);
        patientWeightLabel = new JLabel(patientWeight);
        patientHeightLabel = new JLabel(patientHeight);
        patientRoomNumLabel = new JLabel(patientRoomNumber);
        patientNurseLabel = new JLabel(patientCaretaker);
        patientContactLabel = new JLabel(patientEmergencyContact);
        patientAllergenLabel = new JLabel(patientAllergies);
        patientIllnessLabel = new JLabel(patientIllnesses);
        patientMedicationLabel = new JLabel(patientCurrMeds);

        patientExtraNotesLabel = new JTextArea(patientExtraNotes, 20, 56);
        patientExtraNotesLabel.setLineWrap(true);
        patientExtraNotesLabel.setWrapStyleWord(true);

        imagePanel.add(imageLabel);
        namePanel.add(nameLabel);
        birthPanel.add(birthLabel);
        heightPanel.add(heightLabel);
        weightPanel.add(weightLabel);
        roomNumPanel.add(roomNumLabel);
        nursePanel.add(nurseLabel);
        contactPanel.add(contactLabel);

        namePanel.add(patientNameLabel);
        birthPanel.add(patientBirthLabel);
        heightPanel.add(patientHeightLabel);
        weightPanel.add(patientWeightLabel);
        roomNumPanel.add(patientRoomNumLabel);
        nursePanel.add(patientNurseLabel);
        contactPanel.add(patientContactLabel);
        hwPanel.add(heightPanel);
        hwPanel.add(weightPanel);
        buttonPanel.add(logoutButton);
        buttonPanel.add(editButton);

        homePanel.add(imagePanel);
        homePanel.add(namePanel);
        homePanel.add(birthPanel);
        homePanel.add(hwPanel);
        homePanel.add(roomNumPanel);
        homePanel.add(nursePanel);
        homePanel.add(contactPanel);
        homePanel.add(buttonPanel);

        allergenPanel.add(patientAllergenLabel);
        illnessPanel.add(patientIllnessLabel);
        medsPanel.add(patientMedicationLabel);
        notesPanel.add(patientExtraNotesLabel);

        pane.addTab("Home", homePanel);
        pane.addTab("Allergies", allergenPanel);
        pane.addTab("Illnesses", illnessPanel);
        pane.addTab("Medications", medsPanel);
        pane.addTab("Add. Notes", notesPane);

        add(pane);
        setVisible(true);

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
                LoginScreen login = new LoginScreen();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
                EditScreen editScreen = new EditScreen(patientInfo);
            }
        });
    }
}

// allows the ability to create a new profile
class SignUpScreen extends JFrame implements ActionListener {
    // components that are added to the panels
    private JFrame loginFrame = new JFrame();
    private JLabel user = new JLabel("Create username:");
    private JTextField userText = new JTextField(15);
    private JLabel pass = new JLabel(" Create password:");
    private JPasswordField passText = new JPasswordField(15);
    private JButton button = new JButton("Sign Up");
    private ImageIcon icon = new ImageIcon("C:/Users/Admin/Desktop/v3/SoundMemoryLogoIcon.png");

    // panels that are added to the frame
    private JPanel userPanel = new JPanel(new FlowLayout());
    private JPanel passwordPanel = new JPanel(new FlowLayout());
    private JPanel buttonPanel = new JPanel(new FlowLayout());

    // creates the gui window with its necessary information
    public SignUpScreen() {
        setTitle("Sound Memory");
        setSize(300, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setIconImage(icon.getImage());

        // adds each component to its panel and adds each panel to the frame
        userPanel.add(user);
        userPanel.add(userText);
        passwordPanel.add(pass);
        passwordPanel.add(passText);
        buttonPanel.add(button);

        add(userPanel);
        add(passwordPanel);
        add(buttonPanel);

        setVisible(true);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    Socket client = new Socket("192.168.219.217", 1235);
                    byte[] buf = new byte[1024];
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(client.getOutputStream()));

                    String username = userText.getText();
                    char[] passArr = passText.getPassword();
                    String password = "";
                    for (int i = 0; i < passArr.length; i++) {
                        password += passArr[i];
                    }

                    if (password.indexOf("'") >= 0 || password.indexOf('"') >= 0) {
                        System.out.println("Invalid password, cannot use characters ' or " + '"');
                        bufferedWriter.write("bye");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    } else {
                        bufferedWriter.write("!:)1" + username);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        bufferedWriter.write("!:)2" + password);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        String signUpAttempt = bufferedReader.readLine();
                        while (!signUpAttempt.equals("good") && !signUpAttempt.equals("username")) {
                            signUpAttempt = bufferedReader.readLine();
                        }

                        if (signUpAttempt.equals("username")) {
                            System.out.println("Username is already taken or invalid");
                        } else if (signUpAttempt.equals("password")) {
                            System.out.println("Password is invalid");
                        } else if (signUpAttempt.equals("good")) {
                            System.out.println("Account created successfully");
                            bufferedWriter.write("bye");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            dispose();
                            EditScreen profileCreator = new EditScreen("");
                        } else {
                            System.out.println("Unknown error");
                        }

                        bufferedWriter.write("bye");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

// reached after the profile is created, allows information about the patient to
// be entered into the database
class EditScreen extends JFrame implements ActionListener {
    private String residentInformation;
    private JFrame infoFrame = new JFrame();
    private ImageIcon icon = new ImageIcon(
            "C:/Users/Admin/Desktop/TSA/Code/Desktop Application/v5/SoundMemoryLogoIcon.png");

    private JLabel nameLabel = new JLabel("                                                           name: ");
    private JLabel birthLabel = new JLabel("                          Patient DOB mm/dd/yyyy: ");
    private JLabel heightLabel = new JLabel("                                                        Height: ");
    private JLabel weightLabel = new JLabel("                                                        Weight: ");
    private JLabel roomNumLabel = new JLabel("                                           Room Number: ");
    private JLabel nurseLabel = new JLabel("                                                   Caretaker: ");
    private JLabel contactLabel = new JLabel("                                  Emergency Contact: ");
    private JLabel allergenLabel = new JLabel("                                                     Allergies: ");
    private JLabel illnessLabel = new JLabel("Illnesses, including mental and chronic: ");
    private JLabel medsLabel = new JLabel("                                Current Medications: ");
    private JLabel extraNotesLabel = new JLabel("                              Additional Patient Info: ");
    private JButton createButton = new JButton("Create Profile");

    private JTextArea patientNameTextArea;
    private JTextArea patientBirthTextArea;
    private JTextArea patientWeightTextArea;
    private JTextArea patientHeightTextArea;
    private JTextArea patientRoomNumTextArea;
    private JTextArea patientNurseTextArea;
    private JTextArea patientContactTextArea;
    private JTextArea patientAllergenTextArea;
    private JTextArea patientIllnessTextArea;
    private JTextArea patientMedicationTextArea;
    private JTextArea patientNotesTextArea;

    private JPanel infoPanel = new JPanel();
    private JPanel namePanel = new JPanel();
    private JPanel birthPanel = new JPanel();
    private JPanel heightPanel = new JPanel();
    private JPanel weightPanel = new JPanel();
    private JPanel roomNumPanel = new JPanel();
    private JPanel nursePanel = new JPanel();
    private JPanel contactPanel = new JPanel();
    private JPanel allergenPanel = new JPanel();
    private JPanel illnessPanel = new JPanel();
    private JPanel medsPanel = new JPanel();
    private JPanel notesPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JScrollPane infoPane = new JScrollPane(infoPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public EditScreen(String patientInfo) {
        residentInformation = patientInfo;
        setTitle("Sound Memory");
        setSize(600, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setIconImage(icon.getImage());

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(587, 587));
        infoPane.setPreferredSize(new Dimension(587, 587));

        if (patientInfo.equals("")) {
            patientNameTextArea = new JTextArea(1, 15);
            patientBirthTextArea = new JTextArea(1, 15);
            patientWeightTextArea = new JTextArea(1, 15);
            patientHeightTextArea = new JTextArea(1, 15);
            patientRoomNumTextArea = new JTextArea(1, 15);
            patientNurseTextArea = new JTextArea(1, 15);
            patientContactTextArea = new JTextArea(1, 15);
            patientAllergenTextArea = new JTextArea(1, 15);
            patientIllnessTextArea = new JTextArea(1, 15);
            patientMedicationTextArea = new JTextArea(1, 15);
            patientNotesTextArea = new JTextArea(1, 15);
        } else {
            String patientName = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientHeight = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientWeight = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientDOB = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientCaretaker = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientIllnesses = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientAllergies = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientExtraNotes = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientEmergencyContact = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientRoomNumber = residentInformation.substring(0, residentInformation.indexOf("`"));
            residentInformation = residentInformation.substring(residentInformation.indexOf("`") + 1);
            String patientCurrMeds = residentInformation;

            patientNameTextArea = new JTextArea(patientName, 1, 15);
            patientBirthTextArea = new JTextArea(patientDOB, 1, 15);
            patientWeightTextArea = new JTextArea(patientWeight, 1, 15);
            patientHeightTextArea = new JTextArea(patientHeight, 1, 15);
            patientRoomNumTextArea = new JTextArea(patientRoomNumber, 1, 15);
            patientNurseTextArea = new JTextArea(patientCaretaker, 1, 15);
            patientContactTextArea = new JTextArea(patientEmergencyContact, 1, 15);
            patientAllergenTextArea = new JTextArea(patientAllergies, 1, 15);
            patientIllnessTextArea = new JTextArea(patientIllnesses, 1, 15);
            patientMedicationTextArea = new JTextArea(patientCurrMeds, 1, 15);
            patientNotesTextArea = new JTextArea(patientExtraNotes, 1, 15);
        }

        patientAllergenTextArea.setLineWrap(true);
        patientAllergenTextArea.setWrapStyleWord(true);
        patientIllnessTextArea.setLineWrap(true);
        patientIllnessTextArea.setWrapStyleWord(true);
        patientMedicationTextArea.setLineWrap(true);
        patientMedicationTextArea.setWrapStyleWord(true);
        patientNotesTextArea.setLineWrap(true);
        patientNotesTextArea.setWrapStyleWord(true);

        namePanel.add(nameLabel);
        birthPanel.add(birthLabel);
        heightPanel.add(heightLabel);
        weightPanel.add(weightLabel);
        roomNumPanel.add(roomNumLabel);
        nursePanel.add(nurseLabel);
        contactPanel.add(contactLabel);
        allergenPanel.add(allergenLabel);
        illnessPanel.add(illnessLabel);
        medsPanel.add(medsLabel);
        notesPanel.add(extraNotesLabel);

        namePanel.add(patientNameTextArea);
        birthPanel.add(patientBirthTextArea);
        heightPanel.add(patientHeightTextArea);
        weightPanel.add(patientWeightTextArea);
        roomNumPanel.add(patientRoomNumTextArea);
        nursePanel.add(patientNurseTextArea);
        contactPanel.add(patientContactTextArea);
        allergenPanel.add(patientAllergenTextArea);
        illnessPanel.add(patientIllnessTextArea);
        medsPanel.add(patientMedicationTextArea);
        notesPanel.add(patientNotesTextArea);
        buttonPanel.add(createButton);

        infoPanel.add(namePanel);
        infoPanel.add(birthPanel);
        infoPanel.add(heightPanel);
        infoPanel.add(weightPanel);
        infoPanel.add(roomNumPanel);
        infoPanel.add(nursePanel);
        infoPanel.add(contactPanel);
        infoPanel.add(allergenPanel);
        infoPanel.add(illnessPanel);
        infoPanel.add(medsPanel);
        infoPanel.add(notesPanel);
        infoPanel.add(buttonPanel);

        add(infoPane);
        setVisible(true);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    Socket client = new Socket("192.168.219.217", 1235);
                    byte[] buf = new byte[1024];
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(client.getOutputStream()));

                    if (patientNameTextAreaName.getText().equals("") ||
                            patientBirthTextArea.getText().equals("") ||
                            patientWeightTextArea.getText().equals("") ||
                            patientHeightTextArea.getText().equals("") ||
                            patientRoomNumTextArea.getText().equals("") ||
                            patientNurseTextArea.getText().equals("") ||
                            patientContactTextArea.getText().equals("") ||
                            patientAllergenTextArea.getText().equals("") ||
                            patientIllnessTextArea.getText().equals("") ||
                            patientMedicationTextArea.getText().equals("") ||
                            patientNotesTextArea.getText().equals("")) {
                        System.out.println("All boxes must be filled in order to proceed");
                    } else {
                        String newInfo = (patientName.getText() + "`" + patientHeight.getText() + "`"
                                + patientWeight.getText() + "`" + patientDOB.getText() + "`" +
                                patientCaretaker.getText() + "`" + patientIllnesses.getText() + "`"
                                + patientAllergies.getText() + "`" + patientExtraNotes.getText()
                                + "`" + patientEmergencyContact.getText() + "`" + patientRoomNumber.getText() + "`"
                                + patientCurrMeds.getText());
                        if (!newInfo.equals(residentInformation)) {
                            residentInformation = newInfo;
                            // '^_^' is a tag to tell the server that the string being sent is that of the
                            // patient information
                            // It looks like a face on purpose in order to add some small personalization to
                            // the code
                            bufferedWriter.write("^_^" + residentInformation);
                            dispose();
                            InteractiveMenu newMenu = new InteractiveMenu(residentInformation);
                        }
                    }
                } catch (IOException e) {
                }
            }
        });
    }
}

class Main {
    public static void main(String[] args) {
        LoginScreen login = new LoginScreen();
    }
}
