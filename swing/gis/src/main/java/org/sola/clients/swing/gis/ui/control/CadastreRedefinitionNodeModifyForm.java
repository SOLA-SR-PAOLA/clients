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
 * CadastreRedefinitionNodeModifyForm.java
 *
 * Created on Jan 14, 2012, 11:57:32 AM
 */
package org.sola.clients.swing.gis.ui.control;

import java.text.DecimalFormat;
import org.sola.clients.swing.gis.Messaging;
import org.sola.common.messaging.GisMessage;

/**
 * This form is used during the manipulation of nodes in the cadastre
 * redefinition process. If a node is identified or new inserted, then this form
 * gives the possibility to change the coordinates or to remove the node.
 *
 * @author Elton Manoku
 */
public class CadastreRedefinitionNodeModifyForm extends javax.swing.JDialog {

    /**
     * Status types of the form
     */
    public enum Status {

        RemoveNode,
        ModifyNode,
        ModifyNodeInteractive,
        DoNothing
    }
    private Status status = Status.DoNothing;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    /**
     * Creates new form CadastreRedefinitionNodeModifyForm
     */
    public CadastreRedefinitionNodeModifyForm() {
        initComponents();
        this.setAlwaysOnTop(true);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
    }

    /**
     * Gets the status of the form
     *
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the form
     *
     * @param status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Sets the X coordinate
     *
     * @param x
     */
    public void setCoordinateX(Double x) {
        this.txtX.setText(decimalFormat.format(x));
    }

    /**
     * Sets the Y coordinate
     *
     * @param y
     */
    public void setCoordinateY(Double y) {
        this.txtY.setText(decimalFormat.format(y));
    }

    /**
     * Gets X coordinate
     *
     * @return
     */
    public Double getCoordinateX() {
        return Double.valueOf(getCoordinateAsString(this.txtX.getText()));
    }

    /**
     * Gets Y coordinate
     *
     * @return
     */
    public Double getCoordinateY() {
        return Double.valueOf(getCoordinateAsString(this.txtY.getText()));
    }

    /**
     * Changes the visibility of the remove button. Sometimes the remove button
     * must not be enabled.
     *
     * @param visible
     */
    public void setRemoveButtonVisibility(boolean visible) {
        this.cmdRemove.setVisible(visible);
    }

    private String getCoordinateAsString(String inputedCoordinate) {
        return inputedCoordinate.replace(',', '.');
    }
    
    private void changeNodeModificationMode(){
        txtX.setEnabled(optUseCoordinateInput.isSelected());
        txtY.setEnabled(optUseCoordinateInput.isSelected());        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        txtY = new javax.swing.JTextField();
        txtX = new javax.swing.JTextField();
        lblY = new javax.swing.JLabel();
        lblX = new javax.swing.JLabel();
        cmdModify = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        optUseInteractiveTool = new javax.swing.JRadioButton();
        optUseCoordinateInput = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        txtY.setEnabled(false);
        txtY.setName("txtY"); // NOI18N

        txtX.setEnabled(false);
        txtX.setName("txtX"); // NOI18N

        lblY.setText("Y (Northing)");
        lblY.setName("lblY"); // NOI18N

        lblX.setText("X (Easting)");
        lblX.setName("lblX"); // NOI18N

        cmdModify.setText("Modify");
        cmdModify.setName("cmdModify"); // NOI18N
        cmdModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdModifyActionPerformed(evt);
            }
        });

        cmdRemove.setText("Remove");
        cmdRemove.setName("cmdRemove"); // NOI18N
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        buttonGroup1.add(optUseInteractiveTool);
        optUseInteractiveTool.setSelected(true);
        optUseInteractiveTool.setText("Use tool");
        optUseInteractiveTool.setName("optUseInteractiveTool"); // NOI18N
        optUseInteractiveTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optUseInteractiveToolActionPerformed(evt);
            }
        });

        buttonGroup1.add(optUseCoordinateInput);
        optUseCoordinateInput.setText("Use coordinate input");
        optUseCoordinateInput.setName("optUseCoordinateInput"); // NOI18N
        optUseCoordinateInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optUseCoordinateInputActionPerformed(evt);
            }
        });

        jLabel1.setText("To change the coordinates of the targeted node:");
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtX)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblX)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblY)
                            .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cmdRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdModify))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(optUseInteractiveTool, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(optUseCoordinateInput))
                            .addComponent(jLabel1))
                        .addGap(0, 55, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optUseInteractiveTool)
                    .addComponent(optUseCoordinateInput))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblX)
                            .addComponent(lblY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdModify)
                    .addComponent(cmdRemove))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void cmdModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdModifyActionPerformed
    if (optUseInteractiveTool.isSelected()){
        this.status = Status.ModifyNodeInteractive;
        this.setVisible(false);
        return;
    }
    try {
        Double.parseDouble(getCoordinateAsString(this.txtX.getText()));
        Double.parseDouble(getCoordinateAsString(this.txtY.getText()));
        this.status = Status.ModifyNode;
        this.setVisible(false);
    } catch (NumberFormatException ex) {
        Messaging.getInstance().show(GisMessage.CADASTRE_REDEFINITION_COORDS_NOT_VALID);
    }

}//GEN-LAST:event_cmdModifyActionPerformed

private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
    this.status = Status.RemoveNode;
    this.setVisible(false);

}//GEN-LAST:event_cmdRemoveActionPerformed

    private void optUseCoordinateInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optUseCoordinateInputActionPerformed
        changeNodeModificationMode();
    }//GEN-LAST:event_optUseCoordinateInputActionPerformed

    private void optUseInteractiveToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optUseInteractiveToolActionPerformed
        changeNodeModificationMode();
    }//GEN-LAST:event_optUseInteractiveToolActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdModify;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblX;
    private javax.swing.JLabel lblY;
    private javax.swing.JRadioButton optUseCoordinateInput;
    private javax.swing.JRadioButton optUseInteractiveTool;
    private javax.swing.JTextField txtX;
    private javax.swing.JTextField txtY;
    // End of variables declaration//GEN-END:variables
}
