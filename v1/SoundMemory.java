import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// login screen that is seen on running the application
class LoginScreen extends JFrame implements ActionListener {
    // components that are added to the panels
    private JFrame window = new JFrame();
    private JLabel user = new JLabel("Username:");
    private JTextField userText = new JTextField(20);
    private JLabel pass = new JLabel("Password: ");
    private JPasswordField passText = new JPasswordField(20);
    private JButton button = new JButton("Login");
    
    // panels that are added to the frame
    private JPanel userPanel = new JPanel(new FlowLayout());
    private JPanel passwordPanel = new JPanel(new FlowLayout());
    private JPanel buttonPanel = new JPanel(new FlowLayout());

    // temporary default login credentials for testing purposes while the profile database is being created
    // will be removed later when the database and client-server communications are set up
    private final String DEFAULT_USERNAME = "admin";
    private final String DEFAULT_PASSWORD = "root";

    // adds the actual window with its necessary information
    public LoginScreen() {
        setTitle("Sound Memory");
        setSize(300,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());

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

        // creates a function that waits for the button to be clicked
        button.addActionListener(new ActionListener() {
            /*
             * function takes the text from the username and password fields and checks 
             * whether the credentials match that of an existing profile or not,
             * with the intention of moving on to the main menu if the profile exists
             * and displaying and error message if it does not
             * (Once I know how to impoement the switching from one JFrame to another)
            */
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                char[] passArr = passText.getPassword();
                String password = "";
                for (int i = 0; i < passArr.length; i++) {
                    password += passArr[i];
                }

                if (username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
                    System.out.println("Success");
                } else {
                    System.out.println("failure");
                }
            }
        });
    }
}

class Main {
    public static void main(String[] args) {
        LoginScreen gui = new LoginScreen();
    }
}