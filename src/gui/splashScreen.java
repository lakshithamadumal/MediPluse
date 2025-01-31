/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author Laky
 */
public class splashScreen extends javax.swing.JFrame {

    private static splashScreen splashWindow;

    public splashScreen() {
        initComponents();
        loadingAnimation();
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

    }

    private void loadingAnimation() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    jProgressBar1.setValue(i);

                    if (i == 20) {
                        jLabel2.setText("Please Wait");
                    } else if (i == 30) {
                        jLabel2.setText("Libraries Loading");
                    } else if (i == 45) {
                        jLabel2.setText("Creating database connection");
                    } else if (i == 70) {
                        jLabel2.setText("UI generated successfully");
                    } else if (i == 88) {
                        jLabel2.setText("Database connection successfully created");
                    } else if (i == 98) {
                        jLabel2.setText("Done");
                    }

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                splashWindow.dispose();
                new SignIn().setVisible(true);
            }
        });
        t.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 334, 488, 10));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Loading...");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 290, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/slpashscreen.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                splashWindow = new splashScreen(); // Initialize the static splashWindow
                splashWindow.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
