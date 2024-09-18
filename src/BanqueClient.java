import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BanqueClient {
    private BanqueService service;

    public BanqueClient() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service = (BanqueService) registry.lookup("BanqueService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BanqueClient client = new BanqueClient();
            client.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Banque Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(panel);

        // Header
        JLabel headerLabel = new JLabel("Gestion Bancaire", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(headerLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID Field
        JLabel idLabel = new JLabel("ID Compte:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);

        JTextField idField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(idField, gbc);

        // Somme Field
        JLabel sommeLabel = new JLabel("Somme:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(sommeLabel, gbc);

        JTextField sommeField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(sommeField, gbc);

        // ID Destination Field
        JLabel idDestLabel = new JLabel("ID Compte Destinataire:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(idDestLabel, gbc);

        JTextField idDestField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(idDestField, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton creerCompteButton = new JButton("Créer Compte");
        JButton ajouterButton = new JButton("Ajouter");
        JButton retirerButton = new JButton("Retirer");
        JButton consulterButton = new JButton("Consulter Solde");
        JButton transfererButton = new JButton("Transférer");

        buttonPanel.add(creerCompteButton);
        buttonPanel.add(ajouterButton);
        buttonPanel.add(retirerButton);
        buttonPanel.add(consulterButton);
        buttonPanel.add(transfererButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Result Area
        JTextArea resultArea = new JTextArea(5, 40);
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(new JScrollPane(resultArea), BorderLayout.EAST);

        frame.setVisible(true);

        // Ajouter des écouteurs d'événements pour les boutons
        creerCompteButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                double somme = Double.parseDouble(sommeField.getText());
                service.creerCompte(id, somme);
                resultArea.setText("Compte créé avec succès.");
            } catch (Exception ex) {
                resultArea.setText("Erreur: " + ex.getMessage());
            }
        });

        ajouterButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                double somme = Double.parseDouble(sommeField.getText());
                service.ajouter(id, somme);
                resultArea.setText("Somme ajoutée avec succès.");
            } catch (Exception ex) {
                resultArea.setText("Erreur: " + ex.getMessage());
            }
        });

        retirerButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                double somme = Double.parseDouble(sommeField.getText());
                service.retirer(id, somme);
                resultArea.setText("Somme retirée avec succès.");
            } catch (Exception ex) {
                resultArea.setText("Erreur: " + ex.getMessage());
            }
        });

        consulterButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                double solde = service.consulterSolde(id);
                resultArea.setText("Solde: " + solde);
            } catch (Exception ex) {
                resultArea.setText("Erreur: " + ex.getMessage());
            }
        });

        transfererButton.addActionListener(e -> {
            try {
                String id_C = idField.getText();
                String id_D = idDestField.getText();
                double somme = Double.parseDouble(sommeField.getText());
                service.transfererSolde(id_C, id_D, somme);
                resultArea.setText("Transfert effectué avec succès.");
            } catch (Exception ex) {
                resultArea.setText("Erreur: " + ex.getMessage());
            }
        });
    }
}
