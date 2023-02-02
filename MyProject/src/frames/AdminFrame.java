/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import dao.dto.Admin;
import facade.AdminFacade;
import javax.swing.UIManager;

/**
 *
 * @author Milos
 */
public class AdminFrame extends javax.swing.JFrame {

    private static Admin admin;
    private AdminFacade aFacade;

    /**
     * Creates new form admin
     */
    public AdminFrame(Admin admin) {
        initComponents();
        setLocationRelativeTo(null);
        this.admin = admin;
        aFacade = new AdminFacade(admin);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        instructor_Gender_ButtonGroup_ = new javax.swing.ButtonGroup();
        header_Panel_ = new javax.swing.JPanel();
        main_Panel_ = new javax.swing.JPanel();
        control_Panel_ = new javax.swing.JPanel();
        register_Tabbed_ = new javax.swing.JTabbedPane();
        instructor_Panel_ = new javax.swing.JPanel();
        add_Instructor_ScrollPane_ = new javax.swing.JScrollPane();
        add_Instructor_Panel_ = new javax.swing.JPanel();
        instructor_First_Name_Label_ = new javax.swing.JLabel();
        instructor_First_Name_Field_ = new javax.swing.JTextField();
        instructor_Last_Name_Label_ = new javax.swing.JLabel();
        instructor_Last_Name_Field_ = new javax.swing.JTextField();
        instructor_Initials_Label_ = new javax.swing.JLabel();
        instructor_Initials_Field_ = new javax.swing.JTextField();
        instructor_Email_Label_ = new javax.swing.JLabel();
        instructor_Email_Field_ = new javax.swing.JTextField();
        instructor_Phone_Number_Label_ = new javax.swing.JLabel();
        instructor_Phone_Number_Field_ = new javax.swing.JTextField();
        instructor_Password_Label_ = new javax.swing.JLabel();
        instructor_Password_Field_ = new javax.swing.JTextField();
        instructor_Gender_Label_ = new javax.swing.JLabel();
        instructor_Gender_Male_ = new javax.swing.JRadioButton();
        instructor_Gender_Female_ = new javax.swing.JRadioButton();
        instructor_Birthdate_DateChooser_ = new com.toedter.calendar.JDateChooser();
        add_Instructor_Button_ = new javax.swing.JButton();
        student_Panel_ = new javax.swing.JPanel();
        admin_Panel_ = new javax.swing.JPanel();
        add_Admin_ScrollPane_ = new javax.swing.JScrollPane();
        add_Admin_Panel_ = new javax.swing.JPanel();
        admin_First_Name_Label_ = new javax.swing.JLabel();
        admin_First_Name_Field_ = new javax.swing.JTextField();
        admin_Last_Name_Label_ = new javax.swing.JLabel();
        admin_Last_Name_Field_ = new javax.swing.JTextField();
        admin_Username_Label_ = new javax.swing.JLabel();
        admin_Username_Field_ = new javax.swing.JTextField();
        admin_Password_Label_ = new javax.swing.JLabel();
        admin_Password_Field_ = new javax.swing.JTextField();
        add_Admin_Button_ = new javax.swing.JButton();
        server_Panel_ = new javax.swing.JPanel();
        chat_Panel_ = new javax.swing.JPanel();
        footer_Panel_ = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(950, 650));

        header_Panel_.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout header_Panel_Layout = new javax.swing.GroupLayout(header_Panel_);
        header_Panel_.setLayout(header_Panel_Layout);
        header_Panel_Layout.setHorizontalGroup(
            header_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 950, Short.MAX_VALUE)
        );
        header_Panel_Layout.setVerticalGroup(
            header_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(header_Panel_, java.awt.BorderLayout.NORTH);

        main_Panel_.setLayout(new java.awt.GridLayout(2, 2));

        javax.swing.GroupLayout control_Panel_Layout = new javax.swing.GroupLayout(control_Panel_);
        control_Panel_.setLayout(control_Panel_Layout);
        control_Panel_Layout.setHorizontalGroup(
            control_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 475, Short.MAX_VALUE)
        );
        control_Panel_Layout.setVerticalGroup(
            control_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 325, Short.MAX_VALUE)
        );

        main_Panel_.add(control_Panel_);

        instructor_Panel_.setLayout(new java.awt.BorderLayout(0, 5));

        java.awt.GridBagLayout add_Instructor_Panel_Layout = new java.awt.GridBagLayout();
        add_Instructor_Panel_Layout.columnWidths = new int[] {100, 100, 100, 100};
        add_Instructor_Panel_.setLayout(add_Instructor_Panel_Layout);

        instructor_First_Name_Label_.setText("First Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_First_Name_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_First_Name_Field_, gridBagConstraints);

        instructor_Last_Name_Label_.setText("Last Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Last_Name_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Last_Name_Field_, gridBagConstraints);

        instructor_Initials_Label_.setText("Initials:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Initials_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Initials_Field_, gridBagConstraints);

        instructor_Email_Label_.setText("Email:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Email_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Email_Field_, gridBagConstraints);

        instructor_Phone_Number_Label_.setText("Phone Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Phone_Number_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Phone_Number_Field_, gridBagConstraints);

        instructor_Password_Label_.setText("Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Password_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Password_Field_, gridBagConstraints);

        instructor_Gender_Label_.setText("Gender:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Gender_Label_, gridBagConstraints);

        instructor_Gender_ButtonGroup_.add(instructor_Gender_Male_);
        instructor_Gender_Male_.setText("Male");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Gender_Male_, gridBagConstraints);

        instructor_Gender_ButtonGroup_.add(instructor_Gender_Female_);
        instructor_Gender_Female_.setText("Female");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Gender_Female_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Instructor_Panel_.add(instructor_Birthdate_DateChooser_, gridBagConstraints);

        add_Instructor_ScrollPane_.setViewportView(add_Instructor_Panel_);

        instructor_Panel_.add(add_Instructor_ScrollPane_, java.awt.BorderLayout.CENTER);

        add_Instructor_Button_.setText("Add");
        add_Instructor_Button_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_Instructor_Button_MouseClicked(evt);
            }
        });
        instructor_Panel_.add(add_Instructor_Button_, java.awt.BorderLayout.SOUTH);

        register_Tabbed_.addTab("Instructor", instructor_Panel_);

        javax.swing.GroupLayout student_Panel_Layout = new javax.swing.GroupLayout(student_Panel_);
        student_Panel_.setLayout(student_Panel_Layout);
        student_Panel_Layout.setHorizontalGroup(
            student_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 475, Short.MAX_VALUE)
        );
        student_Panel_Layout.setVerticalGroup(
            student_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 294, Short.MAX_VALUE)
        );

        register_Tabbed_.addTab("Student", student_Panel_);

        admin_Panel_.setLayout(new java.awt.BorderLayout(0, 5));

        java.awt.GridBagLayout add_Admin_Panel_Layout = new java.awt.GridBagLayout();
        add_Admin_Panel_Layout.columnWidths = new int[] {150, 150};
        add_Admin_Panel_.setLayout(add_Admin_Panel_Layout);

        admin_First_Name_Label_.setText("First Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Admin_Panel_.add(admin_First_Name_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Admin_Panel_.add(admin_First_Name_Field_, gridBagConstraints);

        admin_Last_Name_Label_.setText("Last Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Admin_Panel_.add(admin_Last_Name_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Admin_Panel_.add(admin_Last_Name_Field_, gridBagConstraints);

        admin_Username_Label_.setText("Username:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Admin_Panel_.add(admin_Username_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Admin_Panel_.add(admin_Username_Field_, gridBagConstraints);

        admin_Password_Label_.setText("Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Admin_Panel_.add(admin_Password_Label_, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add_Admin_Panel_.add(admin_Password_Field_, gridBagConstraints);

        add_Admin_ScrollPane_.setViewportView(add_Admin_Panel_);

        admin_Panel_.add(add_Admin_ScrollPane_, java.awt.BorderLayout.CENTER);

        add_Admin_Button_.setText("Add");
        admin_Panel_.add(add_Admin_Button_, java.awt.BorderLayout.SOUTH);

        register_Tabbed_.addTab("Admin", admin_Panel_);

        main_Panel_.add(register_Tabbed_);

        javax.swing.GroupLayout server_Panel_Layout = new javax.swing.GroupLayout(server_Panel_);
        server_Panel_.setLayout(server_Panel_Layout);
        server_Panel_Layout.setHorizontalGroup(
            server_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 475, Short.MAX_VALUE)
        );
        server_Panel_Layout.setVerticalGroup(
            server_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 325, Short.MAX_VALUE)
        );

        main_Panel_.add(server_Panel_);

        javax.swing.GroupLayout chat_Panel_Layout = new javax.swing.GroupLayout(chat_Panel_);
        chat_Panel_.setLayout(chat_Panel_Layout);
        chat_Panel_Layout.setHorizontalGroup(
            chat_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 475, Short.MAX_VALUE)
        );
        chat_Panel_Layout.setVerticalGroup(
            chat_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 325, Short.MAX_VALUE)
        );

        main_Panel_.add(chat_Panel_);

        getContentPane().add(main_Panel_, java.awt.BorderLayout.CENTER);

        footer_Panel_.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout footer_Panel_Layout = new javax.swing.GroupLayout(footer_Panel_);
        footer_Panel_.setLayout(footer_Panel_Layout);
        footer_Panel_Layout.setHorizontalGroup(
            footer_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 950, Short.MAX_VALUE)
        );
        footer_Panel_Layout.setVerticalGroup(
            footer_Panel_Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(footer_Panel_, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void add_Instructor_Button_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_add_Instructor_Button_MouseClicked

        aFacade.addInstructorCheck(
                instructor_First_Name_Field_,
                instructor_Last_Name_Field_,
                instructor_Initials_Field_,
                instructor_Phone_Number_Field_,
                instructor_Gender_ButtonGroup_,
                instructor_Gender_Male_,
                instructor_Gender_Female_,
                instructor_Birthdate_DateChooser_,
                instructor_Email_Field_,
                instructor_Password_Field_);
    }//GEN-LAST:event_add_Instructor_Button_MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminFrame(admin).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_Admin_Button_;
    private javax.swing.JPanel add_Admin_Panel_;
    private javax.swing.JScrollPane add_Admin_ScrollPane_;
    private javax.swing.JButton add_Instructor_Button_;
    private javax.swing.JPanel add_Instructor_Panel_;
    private javax.swing.JScrollPane add_Instructor_ScrollPane_;
    private javax.swing.JTextField admin_First_Name_Field_;
    private javax.swing.JLabel admin_First_Name_Label_;
    private javax.swing.JTextField admin_Last_Name_Field_;
    private javax.swing.JLabel admin_Last_Name_Label_;
    private javax.swing.JPanel admin_Panel_;
    private javax.swing.JTextField admin_Password_Field_;
    private javax.swing.JLabel admin_Password_Label_;
    private javax.swing.JTextField admin_Username_Field_;
    private javax.swing.JLabel admin_Username_Label_;
    private javax.swing.JPanel chat_Panel_;
    private javax.swing.JPanel control_Panel_;
    private javax.swing.JPanel footer_Panel_;
    private javax.swing.JPanel header_Panel_;
    private com.toedter.calendar.JDateChooser instructor_Birthdate_DateChooser_;
    private javax.swing.JTextField instructor_Email_Field_;
    private javax.swing.JLabel instructor_Email_Label_;
    private javax.swing.JTextField instructor_First_Name_Field_;
    private javax.swing.JLabel instructor_First_Name_Label_;
    private javax.swing.ButtonGroup instructor_Gender_ButtonGroup_;
    private javax.swing.JRadioButton instructor_Gender_Female_;
    private javax.swing.JLabel instructor_Gender_Label_;
    private javax.swing.JRadioButton instructor_Gender_Male_;
    private javax.swing.JTextField instructor_Initials_Field_;
    private javax.swing.JLabel instructor_Initials_Label_;
    private javax.swing.JTextField instructor_Last_Name_Field_;
    private javax.swing.JLabel instructor_Last_Name_Label_;
    private javax.swing.JPanel instructor_Panel_;
    private javax.swing.JTextField instructor_Password_Field_;
    private javax.swing.JLabel instructor_Password_Label_;
    private javax.swing.JTextField instructor_Phone_Number_Field_;
    private javax.swing.JLabel instructor_Phone_Number_Label_;
    private javax.swing.JPanel main_Panel_;
    private javax.swing.JTabbedPane register_Tabbed_;
    private javax.swing.JPanel server_Panel_;
    private javax.swing.JPanel student_Panel_;
    // End of variables declaration//GEN-END:variables
}
