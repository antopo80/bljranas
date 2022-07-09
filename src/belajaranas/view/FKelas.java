/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package belajaranas.view;
import belajaranas.koneksi.konkesi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.Toolkit;
/**
 *
 * @author PC 02
 */
public class FKelas extends javax.swing.JFrame {
    Connection connection;
    DefaultTableModel model;
    /**
     * Creates new form FKelas
     */
    public FKelas() {
        initComponents();
        connection = belajaranas.koneksi.konkesi.getConnection();
        getDataTable();
        getCmbProdi();
        getCmbKonsen();
        tKelas.requestFocus();
        
        //        mengambil ukuran layar
        Dimension layar = Toolkit.getDefaultToolkit().getScreenSize();
        
//        membuat titik x dan y
        int x = layar.width / 2 - this.getSize().width / 2;
        int y = layar.height / 2 - this.getSize().height / 2;
        
        this.setLocation(x,y);
    }
    
    private void getDataTable(){
        model = (DefaultTableModel) table.getModel();
        try{
            Statement stat = connection.createStatement();
            String sql = "SELECT * FROM kelas,prodi,konsentrasi "
                    + "WHERE kelas.id_prodi=prodi.id_prodi AND kelas.id_konsen=konsentrasi.id_konsen";
            ResultSet res = stat.executeQuery(sql);
            while(res.next()){
                Object[ ] obj = new Object[6];
                obj[0] = res.getString("id_kelas");
                obj[1] = res.getString("nama_kelas");
                obj[2] = res.getString("nama_prodi");
                obj[3] = res.getString("nama_konsen");
                model.addRow(obj);
            }
        }catch(SQLException err) {
                    err.printStackTrace();
                    }
    }
    
    private void getCmbProdi(){
        cmbProdi.removeAllItems();
        try{
            Statement stat =  connection.createStatement();
            String sql = "SELECT * FROM prodi";
            ResultSet res = stat.executeQuery(sql);
            while(res.next()) {
                cmbProdi.addItem(res.getString("nama_prodi"));
             
            }
            
        }catch(SQLException err) {
                    err.printStackTrace();
                    }
    }
    
    private void getCmbKonsen() {
        cmbKonsen.removeAllItems();
        try{
            Statement stat =  connection.createStatement();
            String sql = "SELECT * FROM konsentrasi";
            ResultSet res = stat.executeQuery(sql);
            while(res.next()) {
                cmbKonsen.addItem(res.getString("nama_konsen"));
             
            }
            
        }catch(SQLException err) {
                    err.printStackTrace();
                    }
    }
    
    private void refresh(){
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        getDataTable();
    }
    
     private void reset(){
        tKelas.setText("");
        tNama.setText("");
        tKelas.setEditable(true);
        bSimpan.setEnabled(true);
    }
     
     private int getIDProdi (String nama_prodi){
        int id = 0;
        try{
           PreparedStatement st = connection.prepareStatement("SELECT id_prodi FROM prodi WHERE nama_prodi=?", Statement.RETURN_GENERATED_KEYS);
           st.setString(1, nama_prodi);
           ResultSet rs = st.executeQuery();
           while (rs.next()){
               id = rs.getInt("id_prodi");
           }
           return id;
           
        } catch (SQLException ex){
            Logger.getLogger(FMahasiswa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
      private int getIDKonsen (String namakonsen){
        int id = 0;
        try{
           PreparedStatement st = connection.prepareStatement("SELECT id_konsen FROM konsentrasi WHERE nama_konsen=?", Statement.RETURN_GENERATED_KEYS);
           st.setString(1, namakonsen);
           ResultSet rs = st.executeQuery();
           while (rs.next()){
               id = rs.getInt("id_konsen");
           }
           return id;
           
        } catch (SQLException ex){
            Logger.getLogger(FMahasiswa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
     
     private void insert(){
         PreparedStatement statement =  null;
         String sql = "INSERT INTO kelas (id_kelas, nama_kelas, id_prodi, id_konsen) VALUES (?,?,?,?);";
         try{
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tKelas.getText());
            statement.setString(2, tNama.getText());
            statement.setInt(3, getIDProdi(cmbProdi.getSelectedItem().toString()));
            statement.setInt(4, getIDKonsen(cmbKonsen.getSelectedItem().toString()));
            statement.executeUpdate();
         }catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
         }
     }
     
     private void update (){
         PreparedStatement statement = null;
         String sql = "UPDATE kelas SET nama_kelas=?,id_prodi=?,id_konsen=? WHERE id_kelas=?"; 
         try{
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tNama.getText());
            statement.setString(2, tKelas.getText());
            statement.setInt(3, getIDProdi(cmbProdi.getSelectedItem().toString()));
            statement.setInt(4, getIDKonsen(cmbKonsen.getSelectedItem().toString()));
            statement.executeUpdate();
         }catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
         }
     }
     
     private void delete(){
        PreparedStatement statement = null;
        String sql = "DELETE FROM kelas WHERE id_kelas=?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, tKelas.getText());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
     }
     
     private void search(){
        model = (DefaultTableModel) table.getModel();
        PreparedStatement statement = null;
        String cari = tCari.getText();
        try{
            String sql = "SELECT * FROM kelas,prodi,konsentrasi "
                    + "WHERE kelas.id_prodi=prodi.id_prodi AND kelas.id_konsen=konsentrasi.id_konsen AND nama_kelas LIKE ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + tCari.getText() + "%");
            ResultSet res = statement.executeQuery();
            while(res.next ()) {
                Object[] obj = new Object[4];
                obj[0] = res.getString("id_kelas");
                obj[1] = res.getString("nama_kelas");
                obj[2] = res.getString("nama_prodi");
                obj[3] = res.getString("nama_konsen");
                model.addRow(obj);
            }
        } catch(SQLException err) {
            err.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        tKelas = new javax.swing.JTextField();
        bUbah = new javax.swing.JButton();
        bSimpan = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bReset = new javax.swing.JButton();
        tNama = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cmbProdi = new javax.swing.JComboBox();
        cmbKonsen = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tCari = new javax.swing.JTextField();
        bCari = new javax.swing.JButton();
        bRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Data Kelas");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("ID Kelas");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Nama Kelas");

        tKelas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        bUbah.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bUbah.setText("Ubah");
        bUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahActionPerformed(evt);
            }
        });

        bSimpan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bSimpan.setText("Simpan");
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });

        bHapus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bHapus.setText("Hapus");
        bHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusActionPerformed(evt);
            }
        });

        bReset.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bReset.setText("Reset");
        bReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bResetActionPerformed(evt);
            }
        });

        tNama.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Program Studi");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Konsentrasi");

        cmbProdi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Teknik Informatika", "Sistem Informasi", "Bisnis Digital", " " }));

        cmbKonsen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SE", "CN", "CDM", "Akuntasi", "Mangement", "Bisnis", " " }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbKonsen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel12)
                                .addComponent(jLabel13)
                                .addComponent(tNama, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                                .addComponent(tKelas))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(bSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(bHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(bReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(bUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel16)
                                .addComponent(jLabel17)
                                .addComponent(cmbProdi, 0, 243, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbProdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbKonsen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bUbah)
                    .addComponent(bSimpan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bHapus)
                    .addComponent(bReset))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Cari Data");

        tCari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        bCari.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bCari.setText("Cari");
        bCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCariActionPerformed(evt);
            }
        });

        bRefresh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bRefresh.setText("Refresh");
        bRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRefreshActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Kelas", "Nama Kelas", "Program Studi", "Konsentrasi"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(bCari)
                                    .addGap(18, 18, 18)
                                    .addComponent(bRefresh)))
                            .addGap(0, 4, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 354, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bCari)
                        .addComponent(bRefresh))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(10, 10, 10))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        if(!tKelas.getText().trim().isEmpty() && !tNama.getText().trim().isEmpty()) {
            insert();
            refresh();
            reset();
            JOptionPane.showMessageDialog(this, "Kelas ditambahkan",
                    "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this, "Lengakapi Dahulu",
                    "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        } 
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        if(!tKelas.getText().trim().isEmpty() && !tNama.getText().trim().isEmpty()) {
            int alert = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus Kelas ini?",
                    "Notifikasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(alert == JOptionPane.YES_OPTION){
                delete();
                refresh();
                reset();
                JOptionPane.showMessageDialog(this, "Kelas berhasil dihapus",
                        "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
            } else{
                JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu",
                    "Notifikasi", JOptionPane.WARNING_MESSAGE);
            }
         }
    }//GEN-LAST:event_bHapusActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        if(!tKelas.getText().trim().isEmpty()){
            if(!tKelas.getText().trim().isEmpty() && !tNama.getText().trim().isEmpty()){
                update();
                refresh();
                reset();
                JOptionPane.showMessageDialog(this, "KLS berhasil diubah",
                        "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
            } else{
                JOptionPane.showMessageDialog(this, "Lengkapi form terlebih dahulu!",
                        "Notifikasi", JOptionPane.WARNING_MESSAGE);
            }
        } else{
            JOptionPane.showMessageDialog(this, "Pilih data id kelas terlebih dahulu!",
                    "Notifikasi", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bUbahActionPerformed

    private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRefreshActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_bRefreshActionPerformed

    private void bResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bResetActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_bResetActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
        tKelas.setText(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
        tNama.setText(table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
        cmbProdi.setSelectedItem(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
        cmbKonsen.setSelectedItem(table.getModel().getValueAt(table.getSelectedRow(), 3).toString());
        tKelas.setEditable(false);
        bSimpan.setEnabled(false);
    }//GEN-LAST:event_tableMouseClicked

    private void bCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariActionPerformed
        // TODO add your handling code here:
        if(!tCari.getText().trim().isEmpty()){
            model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            search();
        } else {
            JOptionPane.showMessageDialog(this, "Masukan nama kelas yang ingin dicari!",
                    "Notifikasi", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bCariActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(FKelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FKelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FKelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FKelas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FKelas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCari;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bRefresh;
    private javax.swing.JButton bReset;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox cmbKonsen;
    private javax.swing.JComboBox cmbProdi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tKelas;
    private javax.swing.JTextField tNama;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
