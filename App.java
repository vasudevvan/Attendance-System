public class App {
    public static void main(String[] args) {
        DatabaseHandler.getInstance().initialize(); // setup DB & tables
        javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
