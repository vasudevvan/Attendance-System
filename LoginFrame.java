import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame{
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn;

    public LoginFrame(){
        setTitle("Login"); setSize(300,150); setLayout(new GridLayout(3,2,10,10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null);
        add(new JLabel("Username:")); userField=new JTextField(); add(userField);
        add(new JLabel("Password:")); passField=new JPasswordField(); add(passField);
        loginBtn=new JButton("Login"); add(new JLabel()); add(loginBtn);
        loginBtn.addActionListener(e-> onLogin());
    }

    private void onLogin(){
        String u=userField.getText().trim();
        String p=new String(passField.getPassword());
        if(DatabaseHandler.getInstance().validateUser(u,p)){
            new MainFrame().setVisible(true);
            dispose();
        }else JOptionPane.showMessageDialog(this,"Invalid username/password");
    }
}
