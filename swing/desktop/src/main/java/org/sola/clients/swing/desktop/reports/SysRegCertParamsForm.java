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
package org.sola.clients.swing.desktop.reports;

import com.vividsolutions.jts.io.ParseException;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.geotools.feature.SchemaException;
import org.geotools.swing.extended.exception.InitializeLayerException;
import org.geotools.swing.extended.exception.InitializeMapException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.beans.systematicregistration.SysRegCertificatesBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.ReportViewerForm;
import org.sola.clients.swing.gis.imagegenerator.MapImageGeneratorForSelectedParcel;
import org.sola.clients.swing.gis.imagegenerator.MapImageInformation;
import org.sola.common.FileUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.administrative.BaUnitTO;
import org.sola.webservices.transferobjects.cadastre.CadastreObjectTO;
import org.sola.webservices.transferobjects.casemanagement.ApplicationTO;



/**
 *
 * @author RizzoM
 */
public class SysRegCertParamsForm extends javax.swing.JDialog {
    
    private String location;
    private String title = "SR Document(s) for Work Unit ";
    private String nr;
    private String tmpLocation = "";
    private static String cachePath = System.getProperty("user.home") + "/sola/cache/documents/";
    private String reportdate;
    private String reportTogenerate;
    private Date currentDate;
    private SourceBean document;
    private String whichReport;
    private String whichFile;
    private Integer rowVersion=0;
    private ReportViewerForm form;
    private String prefix;
    private CadastreObjectBean cadastreObject;

    /**
     * Creates new form SysRegCertParamsForm
     */
    public SysRegCertParamsForm(java.awt.Frame parent, boolean modal, String nr, String location, String whichReport) {
        super(parent, modal);
        initComponents();
        this.location = location;
        this.whichReport = whichReport;
        this.nr = nr;
        if (nr != null) {
            this.title = " (Application: "+nr+") "+this.title ;
        }
        this.setTitle(this.title);
        this.document = new SourceBean();
        this.btnGenCertificate.doClick();
        
    }

    /**
     * Creates new form SysRegCertParamsForm
     */
    public SysRegCertParamsForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle(this.title);
        this.document = new SourceBean();
    }
    
      /**
     * Creates new form SysRegCertParamsForm
     */
    public SysRegCertParamsForm(java.awt.Frame parent, boolean modal, String report) {
        super(parent, modal);
        initComponents();
        this.whichReport = report;
        this.setTitle(report);
        this.document = new SourceBean();
    }

    /**
     * Opens {@link ReportViewerForm} to display report.
     */
    private void showReport(JasperPrint report, String parcelLabel, String docType) {
        
        if ((this.nr != "" && this.nr != null)) {
             ReportViewerForm form = new ReportViewerForm(report);
             this.form = form;
//             this.form.setVisible(true);
                        
        }
        try {
            postProcessReport(report, parcelLabel, docType);
        } catch (Exception ex) {
            Logger.getLogger(SysRegListingParamsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void postProcessReport(JasperPrint populatedReport, String parcelLabel, String docType) throws Exception {
        
        
        Date recDate = this.currentDate;
        String location = this.tmpLocation.replace(" ", "_");
        this.reportTogenerate = docType + "-" + this.reportTogenerate;
        JRPdfExporter exporterPdf = new JRPdfExporter();
        exporterPdf.setParameter(JRXlsExporterParameter.JASPER_PRINT, populatedReport);
        exporterPdf.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporterPdf.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        exporterPdf.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, cachePath + this.reportTogenerate);
        exporterPdf.setParameter(JRPdfExporterParameter.FORCE_SVG_SHAPES, Boolean.TRUE);
        exporterPdf.exportReport();
        
        
        
        FileUtility.saveFileFromStream(null, this.reportTogenerate);
        saveDocument(this.reportTogenerate, recDate, this.reportdate, parcelLabel, docType);
        FileUtility.deleteFileFromCache(this.reportTogenerate);

    }
    
    private void saveDocument(String fileName, Date recDate, String subDate, String parcelLabel, String docType) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String reportdate = formatter.format(recDate);
        
        this.document.setTypeCode(docType);
        this.document.setRecordation(this.currentDate);
        this.document.setReferenceNr(this.location);
        this.document.setDescription(parcelLabel);
        DocumentBean document1 = new DocumentBean();
        File file = new File(cachePath + fileName);
        
        document1 = DocumentBean.createDocumentFromLocalFile(file);
        document.setArchiveDocument(document1);
        document.save();
        document.clean2();
    }
    
    private void showDocMessage(String fileName, String prevCofO) {
        
        String params = this.title + ":  " + fileName;
        MessageUtility.displayMessage(ClientMessage.SOURCE_SYS_REP_GENERATED, new Object[]{params, prevCofO});
        
    }
    
    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cadastreObjectBean = new org.sola.clients.beans.cadastre.CadastreObjectBean();
        sysRegCertificatesBean = new org.sola.clients.beans.systematicregistration.SysRegCertificatesBean();
        sysRegCertificatesListBean = new org.sola.clients.beans.systematicregistration.SysRegCertificatesListBean();
        btnGenCertificate = new javax.swing.JButton();
        cadastreObjectSearch = new org.sola.clients.swing.ui.cadastre.WorkUnitSearch();
        labHeader = new org.sola.clients.swing.ui.GroupPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/reports/Bundle"); // NOI18N
        btnGenCertificate.setText(bundle.getString("SysRegCertParamsForm.btnGenCertificate.text")); // NOI18N
        btnGenCertificate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenCertificateActionPerformed(evt);
            }
        });

        cadastreObjectSearch.setText(bundle.getString("SysRegCertParamsForm.cadastreObjectSearch.text")); // NOI18N

        labHeader.setTitleText(bundle.getString("SysRegCertParamsForm.labHeader.titleText")); // NOI18N
        labHeader.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGenCertificate))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 22, Short.MAX_VALUE)
                        .addComponent(labHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenCertificate)
                    .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(203, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Returns {@link BaUnitBean} by first and last name part.
     */
    private BaUnitBean getBaUnit(String id) {
        BaUnitTO baUnitTO = WSManager.getInstance().getAdministrative().getBaUnitById(id);
        return TypeConverters.TransferObjectToBean(baUnitTO, BaUnitBean.class, null);
    }
    
    private ApplicationBean getApplication(String id) {
        ApplicationTO applicationTO = WSManager.getInstance().getCaseManagementService().getApplication(id);
        return TypeConverters.TransferObjectToBean(applicationTO, ApplicationBean.class, null);
    }
    private CadastreObjectBean getCadastre(String id)
    {
        List<CadastreObjectTO> cadastreTo=WSManager.getInstance().getCadastreService().getCadastreObjectByParts(id);
        return TypeConverters.TransferObjectToBean(cadastreTo.get(0), CadastreObjectBean.class, null);
    }
    private void generateReport() throws InitializeLayerException {
        
      
       if (this.location==null) { 
        if (  cadastreObjectSearch.getSelectedElement() != null) {
            this.location = cadastreObjectSearch.getSelectedElement().toString();
            tmpLocation = (this.location);
        } else {
            MessageUtility.displayMessage(ClientMessage.CHECK_SELECT_LOCATION);
            return;
        }
       } else {
           tmpLocation = (this.location);
       } 
        Date currentdate = new Date(System.currentTimeMillis());
        this.currentDate = currentdate;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
        this.reportdate = formatter.format(currentdate);
        if (nr != null) {
            sysRegCertificatesListBean.passParameterApp(tmpLocation, nr);
        } else {
            sysRegCertificatesListBean.passParameter(tmpLocation);
        }
        
        String prefix = getPrefix();
        String baUnitId = null;
        String nrTmp = null;
        String appId = null;
        Integer prevCofO = 0;
        String sourceRef="";
        int i = 0;
        
        
                int imageWidth   = 520;
                int imageHeight  = 300;
                int sketchWidth  = 200;
                int sketchHeight = 200;
                
        try {
           
                           
            MapImageGeneratorForSelectedParcel mapImage = new MapImageGeneratorForSelectedParcel(imageWidth, imageHeight,sketchWidth,sketchHeight,false, 0, 0);
            
            List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
            JasperPrint CofO = null;
            JasperPrint ParcelPlan = null;
            for (Iterator<SysRegCertificatesBean> it = sysRegCertificatesListBean.getSysRegCertificates().iterator(); it.hasNext();) {
                final SysRegCertificatesBean appBaunit = it.next();
                baUnitId = appBaunit.getBaUnitId();
                appId = appBaunit.getAppId();
                prevCofO = appBaunit.getCofO();
                cadastreObject=this.getCadastre(appBaunit.getNameFirstpart());
                if(cadastreObject!=null)
                {
                    sourceRef=cadastreObject.getSourceReference();
                }
                else sourceRef="";
                this.reportTogenerate = baUnitId + "_" + tmpLocation + "_" + this.reportdate + ".pdf";
                this.reportTogenerate = this.reportTogenerate.replace(" ", "_");
                this.reportTogenerate = this.reportTogenerate.replace("/", "_");
                final BaUnitBean baUnit = getBaUnit(baUnitId);
                final ApplicationBean applicationBean = getApplication(appId);
                String parcelLabel = tmpLocation + '/' + appBaunit.getNameFirstpart();
                final String featureFront = null;
                final String featureBack = null;
                MapImageInformation mapImageInfo = mapImage.getInformation(appBaunit.getId());
                final String featureImageFileName = mapImageInfo.getMapImageLocation();
                final String featureScalebarFileName = mapImageInfo.getScalebarImageLocation();
                final Number scale = mapImageInfo.getScale();
                final Integer srid = mapImageInfo.getSrid();
                final String featureImageFileNameSmall = mapImageInfo.getSketchMapImageLocation();
                    
                if (this.whichReport.contains("parcelPlan")){  
                    ParcelPlan = ReportManager.getSysRegSlrtPlanReport(baUnit, tmpLocation, applicationBean, appBaunit, featureImageFileName, featureScalebarFileName, srid, scale, featureFront, featureBack, featureImageFileNameSmall);
                    showReport(ParcelPlan, parcelLabel, this.whichReport);
                    jprintlist.add(ParcelPlan);
                } else if (this.whichReport.contains("title")){  
                    CofO = ReportManager.getSysRegCertificatesReport(baUnit, tmpLocation, applicationBean, appBaunit, featureImageFileName, featureScalebarFileName, srid, scale, featureFront, featureBack, featureImageFileNameSmall,sourceRef);
                    showReport(CofO, parcelLabel, this.whichReport);
                    jprintlist.add(CofO);
                }
                else {  
                    CofO = ReportManager.getSysRegCertificatesReport(baUnit, tmpLocation, applicationBean, appBaunit, featureImageFileName, featureScalebarFileName, srid, scale, featureFront, featureBack, featureImageFileNameSmall,sourceRef);
                    showReport(CofO, parcelLabel, "title");
                    ParcelPlan = ReportManager.getSysRegSlrtPlanReport(baUnit, tmpLocation, applicationBean, appBaunit, featureImageFileName, featureScalebarFileName, srid, scale, featureFront, featureBack, featureImageFileNameSmall);
                    showReport(ParcelPlan, parcelLabel,"parcelPlan");
                    jprintlist.add(CofO);
                    jprintlist.add(ParcelPlan);
                }
                 
                 i = i + 1;
            }

         if (this.nr == "" || this.nr == null) {         
            whichFile= "TOTAL_"+this.whichReport+"-"+ this.location.replace('/', '-');
            for(int c=0; c<whichFile.length(); c++){
                if (!Character.isLetterOrDigit(whichFile.charAt(c)))
                {
                    whichFile = whichFile.replace(whichFile.charAt(c),'-');
                }
            }
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
            OutputStream output = new FileOutputStream(new File(cachePath +whichFile+ ".pdf"));
            exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, output);
            try {
                exporter.exportReport();
                output.close();
            } catch (JRException ex) {
                Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {    
                FileUtility.saveFileFromStream(null, cachePath + whichFile+ ".pdf");
                saveDocument(whichFile + ".pdf", this.currentDate, this.reportdate, whichFile, whichReport);
                FileUtility.deleteFileFromCache(cachePath + whichFile+ ".pdf");
            } catch (Exception ex) {
                Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
            }
           }  
        } catch (InitializeMapException ex) {
            Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchemaException ex) {
            Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FactoryException ex) {
            Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformException ex) {
            Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (i == 0) {
            MessageUtility.displayMessage(ClientMessage.NO_CERTIFICATE_GENERATION);
        } else {
            showDocMessage(this.tmpLocation, prevCofO.toString());
               
        }
        
        this.dispose();
        
        if (Desktop.isDesktopSupported()&&(this.nr == "" || this.nr == null)) {         
            try {
                File myFile = new File(cachePath + whichFile+ ".pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
        else {
            this.form.setVisible(true);
            this.form.setAlwaysOnTop(true);
        }
    }
    private void btnGenCertificateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenCertificateActionPerformed
        SolaTask t = new SolaTask<Void, Void>() {
            
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_CREATE_CERTIFICATE));
                try {
                    generateReport();
                    
                    
                } catch (InitializeLayerException ex) {
                    Logger.getLogger(SysRegCertParamsForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);        
        
    }//GEN-LAST:event_btnGenCertificateActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenCertificate;
    private org.sola.clients.beans.cadastre.CadastreObjectBean cadastreObjectBean;
    private org.sola.clients.swing.ui.cadastre.WorkUnitSearch cadastreObjectSearch;
    private org.sola.clients.swing.ui.GroupPanel labHeader;
    private org.sola.clients.beans.systematicregistration.SysRegCertificatesBean sysRegCertificatesBean;
    private org.sola.clients.beans.systematicregistration.SysRegCertificatesListBean sysRegCertificatesListBean;
    // End of variables declaration//GEN-END:variables

    private String getPrefix() {
                prefix = WSManager.getInstance().getInstance().getAdminService().getSetting(
                "state", "");
                return prefix;
    }
}
