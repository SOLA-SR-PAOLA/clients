/**
 * ******************************************************************************************
 * Copyright (C) 2015 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.swing.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import org.sola.clients.swing.common.laf.LafManager;

/**
 * Decorative groups separation panel
 */
public class GroupPanel extends javax.swing.JPanel {

    /**
     * Default constructor.
     */
    public GroupPanel() {
        initComponents();
        customizeComponents();
    }

    /**
     * Constructs panel and sets provided text as a group title.
     *
     * @param title Text to set as a group title.
     */
    public GroupPanel(String title) {
        initComponents();
        customizeComponents();
        lblGroupTitle.setText(title);
    }

    /**
     * Applies customization of component L&F.
     */
    private void customizeComponents() {
//    LABELS    
//        LafManager.getInstance().setLabProperties(lblGroupTitle);
        Object newForeground = "textHighlight";
        Color newForegroundColor = UIManager.getColor(newForeground);
//        lblGroupTitle.setForeground(newForegroundColor);
//        lblGroupTitle.setFont(lblGroupTitle.getFont().deriveFont(lblGroupTitle.getFont().getStyle() | java.awt.Font.BOLD, lblGroupTitle.getFont().getSize()));
        lblGroupTitle.setFont(LafManager.getUiFont().deriveFont(Font.BOLD));
        Object newSelectedRow = "SolaGroup";
        Color newSelColor = UIManager.getColor(newSelectedRow);
        this.setBackground(newSelColor);
    }

    /**
     * Returns group title text.
     */
    public String getTitleText() {
        return lblGroupTitle.getText();
    }

    /**
     * Sets group title text.
     */
    public void setTitleText(String title) {
        lblGroupTitle.setText(title);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblGroupTitle = new javax.swing.JLabel();

        lblGroupTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblGroupTitle.setForeground(new java.awt.Color(255, 255, 255));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/Bundle"); // NOI18N
        lblGroupTitle.setText(bundle.getString("GroupPanel.lblGroupTitle.text")); // NOI18N
        lblGroupTitle.setName("lblGroupTitle"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGroupTitle)
                .addContainerGap(305, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblGroupTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblGroupTitle;
    // End of variables declaration//GEN-END:variables
}
