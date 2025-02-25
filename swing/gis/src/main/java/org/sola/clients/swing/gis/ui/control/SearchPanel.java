/**
 * ******************************************************************************************
 * Copyright (C) 2015 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SearchPanel.java
 *
 * Created on Jan 4, 2012, 5:50:28 PM
 */
package org.sola.clients.swing.gis.ui.control;

import org.geotools.swing.extended.Map;
import org.sola.clients.swing.common.laf.LafManager;
import org.sola.clients.swing.common.utils.LocalizationTools;

import org.sola.clients.swing.gis.beans.SpatialSearchOptionBean;

/**
 * This is the control used to search map objects.
 *
 * @author Elton Manoku
 */
public class SearchPanel extends javax.swing.JPanel {

    /**
     * Creates new form SearchPanel
     */
    public SearchPanel(Map map) {
        initComponents();
        this.mapObjectSearch.setMap(map);
        this.mapObjectSearch.setListScroll(this.listScrollPane);
        customizeComponents();
        initializeSearchOptions();
    }

    /**
     * It initializes the search by list. The list is defined in the database
     *
     */
    private void initializeSearchOptions() {
        cmdSearchBy.removeAllItems();
        for (SpatialSearchOptionBean choiceObject : SpatialSearchOptionBean.getInstanceList()) {
            cmdSearchBy.addItem(choiceObject);
        }
        this.cmdSearchBy.setSelectedIndex(0);
    }

    /**
     * Applies customization of component L&F.
     */
    private void customizeComponents() {

////    COMBOBOXES
//        LafManager.getInstance().setCmbProperties(cmdSearchBy);
//
////    LABELS    
//        LafManager.getInstance().setLabProperties(jLabel1);
//        
//        //  BUTTONS    
//        LafManager.getInstance().setBtnProperties(btnClearSelection);
        LocalizationTools.setOrientation(this);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        cmdSearchBy = new javax.swing.JComboBox();
        listScrollPane = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        mapObjectSearch = new org.sola.clients.swing.gis.ui.control.MapObjectSearch();
        btnClearSelection = new javax.swing.JButton();

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        setPreferredSize(new java.awt.Dimension(0, 0));

        cmdSearchBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Number", "Property", "Owner" }));
        cmdSearchBy.setName("cmdSearchBy"); // NOI18N
        cmdSearchBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSearchByActionPerformed(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/gis/ui/control/Bundle"); // NOI18N
        listScrollPane.setToolTipText(bundle.getString("SearchPanel.listScrollPane.toolTipText")); // NOI18N
        listScrollPane.setName("listScrollPane"); // NOI18N

        jLabel1.setText(bundle.getString("SearchPanel.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        mapObjectSearch.setText(bundle.getString("SearchPanel.mapObjectSearch.text")); // NOI18N
        mapObjectSearch.setName("mapObjectSearch"); // NOI18N

        btnClearSelection.setText(bundle.getString("SearchPanel.btnClearSelection.text")); // NOI18N
        btnClearSelection.setActionCommand(bundle.getString("SearchPanel.btnClearSelection.actionCommand")); // NOI18N
        btnClearSelection.setName(bundle.getString("SearchPanel.btnClearSelection.name")); // NOI18N
        btnClearSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSelectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listScrollPane)
                    .addComponent(mapObjectSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdSearchBy, 0, 151, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClearSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdSearchBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(14, 14, 14)
                .addComponent(mapObjectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(listScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClearSelection)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void cmdSearchByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSearchByActionPerformed
    this.mapObjectSearch.setSearchByObject(
            (SpatialSearchOptionBean) this.cmdSearchBy.getSelectedItem());
}//GEN-LAST:event_cmdSearchByActionPerformed

    private void btnClearSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSelectionActionPerformed
        this.mapObjectSearch.clearSelection();
    }//GEN-LAST:event_btnClearSelectionActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearSelection;
    private javax.swing.JComboBox cmdSearchBy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JScrollPane listScrollPane;
    private org.sola.clients.swing.gis.ui.control.MapObjectSearch mapObjectSearch;
    // End of variables declaration//GEN-END:variables
}
