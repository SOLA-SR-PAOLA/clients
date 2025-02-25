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
package org.sola.clients.reports;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.DisputeBean;
import org.sola.clients.beans.administrative.RrrBean;
import org.sola.clients.beans.administrative.RrrReportBean;
import org.sola.clients.beans.application.*;
import org.sola.clients.beans.system.BrReportBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.beans.system.BrListBean;
import org.sola.clients.beans.systematicregistration.*;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.services.boundary.wsclients.WSManager;

/**
 * Provides methods to generate and display various reports.
 */
public class ReportManager {

    private static String strconfFile = System.getProperty("user.home") + "/sola/configuration.properties";
    public static String prefix = "reports";
    private static String logoImage = "/images/sola/" + getPrefix() + "logoMinistry.png";

    public static String getPrefix() {

        prefix = WSManager.getInstance().getInstance().getAdminService().getSetting(
                "state", "");
        return prefix;
    }

    public static String getSvg(String svg) {
        String svgFile;

        svgFile = WSManager.getInstance().getInstance().getAdminService().getSetting(
                svg, "");

        return svgFile;

    }

    /**
     * Generates and displays <b>Lodgement notice</b> report for the new
     * application.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getLodgementNoticeReport(ApplicationBean appBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        ApplicationBean[] beans = new ApplicationBean[1];
        beans[0] = appBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream(logoImage));
        inputParameters.put("WHICH_CALLER", "N");

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ApplicationPrintingForm.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Application status report</b>.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getApplicationStatusReport(ApplicationBean appBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("MINISTRY_LOGO", ReportManager.class.getResourceAsStream(logoImage));

        ApplicationBean[] beans = new ApplicationBean[1];
        beans[0] = appBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ApplicationStatusReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getBaUnitReport(BaUnitBean baUnitBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        BaUnitBean[] beans = new BaUnitBean[1];
        beans[0] = baUnitBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/BaUnitReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Lease rejection</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getLeaseRejectionReport(RrrReportBean reportBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        RrrReportBean[] beans = new RrrReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/lease/LeaseRefuseLetter.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Lease offer</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getLeaseOfferReport(RrrReportBean reportBean, boolean isDraft) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("IS_DRAFT", isDraft);
        RrrReportBean[] beans = new RrrReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/lease/LeaseOfferReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Lease</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getLeaseReport(RrrReportBean reportBean, boolean isDraft) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("IS_DRAFT", isDraft);
        RrrReportBean[] beans = new RrrReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/lease/LeaseReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Application payment receipt</b>.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getApplicationFeeReport(ApplicationBean appBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        ApplicationBean[] beans = new ApplicationBean[1];
        beans[0] = appBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream(logoImage));
        inputParameters.put("WHICH_CALLER", "R");

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ApplicationPrintingForm.jasper"), inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>BR Report</b>.
     */
    public static JasperPrint getBrReport() {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        BrListBean brList = new BrListBean();
        brList.FillBrs();
        int sizeBrList = brList.getBrBeanList().size();

        BrReportBean[] beans = new BrReportBean[sizeBrList];
        for (int i = 0; i < sizeBrList; i++) {
            beans[i] = brList.getBrBeanList().get(i);
            if (beans[i].getFeedback() != null) {
                String feedback = beans[i].getFeedback();
                feedback = feedback.substring(0, feedback.indexOf("::::"));
                beans[i].setFeedback(feedback);
            }

            if (i > 0) {
                String idPrev = beans[i - 1].getId();
                String technicalTypeCodePrev = beans[i - 1].getTechnicalTypeCode();
                String id = beans[i].getId();
                String technicalTypeCode = beans[i].getTechnicalTypeCode();


                if (id.equals(idPrev)
                        && technicalTypeCode.equals(technicalTypeCodePrev)) {

                    beans[i].setId("");
                    beans[i].setBody("");
                    beans[i].setDescription("");
                    beans[i].setFeedback("");
                    beans[i].setTechnicalTypeCode("");
                }
            }
        }

        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/BrReport.jasper"), inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>BR VAlidaction Report</b>.
     */
    public static JasperPrint getBrValidaction() {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getUserName());
        BrListBean brList = new BrListBean();
        brList.FillBrs();
        int sizeBrList = brList.getBrBeanList().size();
        BrReportBean[] beans = new BrReportBean[sizeBrList];
        for (int i = 0; i < sizeBrList; i++) {
            beans[i] = brList.getBrBeanList().get(i);

        }
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/BrValidaction.jasper"), inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getLodgementReport(LodgementBean lodgementBean, Date dateFrom, Date dateTo) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        LodgementBean[] beans = new LodgementBean[1];
        beans[0] = lodgementBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/LodgementReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>SolaPrintReport</b> for the map.
     *
     * @param fileName String This is the id of the report. It is used to
     * identify the report file.
     * @param dataBean Object containing data for the report. it can be replaced
     * with appropriate bean if needed
     * @param mapImageLocation String this is the location of the map to be
     * passed as MAP_IMAGE PARAMETER to the report. It is necessary for
     * visualizing the map
     * @param scalebarImageLocation String this is the location of the scalebar
     * to be passed as SCALE_IMAGE PARAMETER to the report. It is necessary for
     * visualizing the scalebar
     */
    public static JasperPrint getSolaPrintReport(String fileName, Object dataBean,
            String mapImageLocation, String scalebarImageLocation) throws IOException {

        // Image Location of the north-arrow image
        String navigatorImage = "/images/sola/north-arrow.png";
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("MAP_IMAGE", mapImageLocation);
        inputParameters.put("SCALE_IMAGE", scalebarImageLocation);
        inputParameters.put("NAVIGATOR_IMAGE",
                ReportManager.class.getResourceAsStream(navigatorImage));
        inputParameters.put("LAYOUT", fileName);
        inputParameters.put("INPUT_DATE",
                DateFormat.getInstance().format(Calendar.getInstance().getTime()));


        //This will be the bean containing data for the report. 
        //it is the data source for the report
        //it must be replaced with appropriate bean if needed
        Object[] beans = new Object[1];
        beans[0] = dataBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        // this generates the report. 
        // NOTICE THAT THE NAMING CONVENTION IS TO PRECEED "SolaPrintReport.jasper"
        // WITH THE LAYOUT NAME. SO IT MUST BE PRESENT ONE REPORT FOR EACH LAYOUT FORMAT
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream(
                    "/reports/map/" + fileName + ".jasper"), inputParameters, jds);
            return jasperPrint;
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    public static JasperPrint getMapPublicDisplayReport(
            String layoutId, String areaDescription, String notificationPeriod,
            String mapImageLocation, String scalebarImageLocation) throws IOException {

        // Image Location of the north-arrow image
        String navigatorImage = "/images/sola/north-arrow.png";
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("MAP_IMAGE", mapImageLocation);
        inputParameters.put("SCALE_IMAGE", scalebarImageLocation);
        inputParameters.put("NAVIGATOR_IMAGE",
                ReportManager.class.getResourceAsStream(navigatorImage));
        inputParameters.put("LAYOUT", layoutId);
        inputParameters.put("INPUT_DATE",
                DateFormat.getInstance().format(Calendar.getInstance().getTime()));
        inputParameters.put("AREA_DESCRIPTION", areaDescription);
        inputParameters.put("PERIOD_DESCRIPTION", notificationPeriod);


        //This will be the bean containing data for the report. 
        //it is the data source for the report
        //it must be replaced with appropriate bean if needed
        Object[] beans = new Object[1];
        beans[0] = new Object();
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream(
                    "/reports/map/" + layoutId + ".jasper"), inputParameters, jds);
            return jasperPrint;
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Public display
     * report</b>.
     *
     * @param parcelnumberList List Parcel list bean containing data for the
     * report.
     *
     */
    public static JasperPrint getSysRegPubDisParcelNameReport(ParcelNumberListingListBean parcelnumberList,
            Date dateFrom, Date dateTo, String location, String subReport) {
        HashMap inputParameters = new HashMap();
        String upiCode = parcelnumberList.getParcelNumberListing().get(0).getNameLastpart();

        location = location.substring(location.indexOf("/") + 1);
        String tmpLocation = location.substring(location.indexOf("/") + 1);
        String lga = location.replace("/" + tmpLocation, " Lga");
        String section = tmpLocation.substring(tmpLocation.indexOf("/") + 1);
        String ward = tmpLocation.replace("/" + section, ", ");
        location = "PD Area " + section + ", Ward " + ward + lga + " ( " + upiCode + " )";
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROM_DATE", dateFrom);
        inputParameters.put("TO_DATE", dateTo);
        inputParameters.put("LOCATION", location);
        inputParameters.put("MINISTRY_LOGO", ReportManager.class.getResourceAsStream(logoImage));

        ParcelNumberListingListBean[] beans = new ParcelNumberListingListBean[1];
        beans[0] = parcelnumberList;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        String pdReport = null;
        pdReport = "/" + getPrefix() + "/SysRegPubDisParcelName.jasper";
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream(pdReport),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Public display
     * report</b>.
     *
     * @param ownernameList List Parcel list bean containing data for the
     * report.
     *
     */
    public static JasperPrint getSysRegPubDisOwnerNameReport(OwnerNameListingListBean ownernameList,
            Date dateFrom, Date dateTo, String location, String subReport) {
        HashMap inputParameters = new HashMap();
        String upiCode = ownernameList.getOwnerNameListing().get(0).getNameLastpart();
//        String logoImage = "/images/sola/logoMinistry.png";
        location = location.substring(location.indexOf("/") + 1);
        String tmpLocation = location.substring(location.indexOf("/") + 1);
        String lga = location.replace("/" + tmpLocation, " Lga");
        String section = tmpLocation.substring(tmpLocation.indexOf("/") + 1);
        String ward = tmpLocation.replace("/" + section, ", ");
        location = "PD Area " + section + ", Ward " + ward + lga + " ( " + upiCode + " )";

//	Date currentdate = new Date(System.currentTimeMillis());
//        inputParameters.put("CURRENT_DATE", currentdate);
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROM_DATE", dateFrom);
        inputParameters.put("TO_DATE", dateTo);
        inputParameters.put("LOCATION", location);
        inputParameters.put("MINISTRY_LOGO", ReportManager.class.getResourceAsStream(logoImage));

        OwnerNameListingListBean[] beans = new OwnerNameListingListBean[1];
        beans[0] = ownernameList;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        String pdReport = null;
        pdReport = "/" + getPrefix() + "/SysRegPubDisOwners.jasper";


        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream(pdReport),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Public display
     * report</b>.
     *
     * @param ownernameList List Parcel list bean containing data for the
     * report.
     *
     */
    public static JasperPrint getSysRegPubDisStateLandReport(StateLandListingListBean statelandList,
            Date dateFrom, Date dateTo, String location, String subReport) {
        HashMap inputParameters = new HashMap();
//	Date currentdate = new Date(System.currentTimeMillis());
//        inputParameters.put("CURRENT_DATE", currentdate);
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROM_DATE", dateFrom);
        inputParameters.put("TO_DATE", dateTo);
        inputParameters.put("LOCATION", location);
        inputParameters.put("SUB_REPORT", subReport);
        StateLandListingListBean[] beans = new StateLandListingListBean[1];
        beans[0] = statelandList;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegPubDisStateLand.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Public display
     * report</b>.
     *
     * @param signingList List Parcel list bean containing data for the report.
     *
     */
    public static JasperPrint getSysRegSigningListReport(SigningListListBean signingList, String location, String subReport) {
        HashMap inputParameters = new HashMap();
        String upiCode = signingList.getSigningList().get(0).getNameLastpart();
        Integer i = signingList.getSigningList().size();
        location = upiCode.substring(upiCode.indexOf("/") + 1);
        String tmpLocation = location.substring(location.indexOf("/") + 1);
        String lga = location.replace("/" + tmpLocation, " Lga");
        String section = tmpLocation.substring(tmpLocation.indexOf("/") + 1);
        String ward = tmpLocation.replace("/" + section, ", ");
        location = "Section " + section + ", Ward " + ward + lga + " ( " + upiCode + " )";

//	Date currentdate = new Date(System.currentTimeMillis());
//        inputParameters.put("CURRENT_DATE", currentdate);
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("LOCATION", location);
        inputParameters.put("MINISTRY_LOGO", ReportManager.class.getResourceAsStream(logoImage));
        inputParameters.put("STATE", getPrefix());
        inputParameters.put("LGA", lga.replace("Lga", ""));
        inputParameters.put("WARD", ward);
        inputParameters.put("SECTION", section);
        inputParameters.put("RECORDS", i);

        SigningListListBean[] beans = new SigningListListBean[1];
        beans[0] = signingList;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        String pdReport = null;
        pdReport = "/reports/SysRegSigningList.jasper";


        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream(pdReport),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Certificates
     * report</b>.
     *
     * @param certificatesList List Parcel list bean containing data for the
     * report.
     *
     */
//     public static JasperPrint getSysRegSlrtPlanReport(BaUnitBean baUnitBean, String location, ApplicationBean  appBean, SysRegCertificatesBean appBaunit,String featureImageFileName,
//   public static JasperPrint getSysRegCertificatesReport(BaUnitBean baUnitBean, String location, ApplicationBean  appBean, SysRegCertificatesBean appBaunit,String featureImageFileName,
//     String  featureFront,String featureBack) {
    public static JasperPrint getSysRegCertificatesReport(BaUnitBean baUnitBean, String location, ApplicationBean appBean, SysRegCertificatesBean appBaunit, String featureImageFileName,
            String featureScalebarFileName, Integer srid, Number scale, String featureFront, String featureBack, String featureImageFileNameSmall, String sourceReferenceNumber) {
        HashMap inputParameters = new HashMap();
        String featureFloatFront;
        String featureFloatBack;
        String featureNorthArrow = "images/sola/UN-north-arrow.png";
        String small = "";
        String map = "";
        String sourceRef = sourceReferenceNumber;

        String cofoReport = null;
        String appNr = null;
        String claimant = null;
        String imageryDate = null;
        String owners = null;
        String title = null;
        String address = null;
        Date lodgingDate = null;
        String timeToDevelop = null;
        String valueForImprov = null;
        String term = null;
        Date commencingDate = null;
        String landUse = null;
        String propAddress = null;
        String lga = null;
        String ward = null;
        String state = null;
        BigDecimal size = null;
        String groundRent = null;
        String imageryResolution = "50 cm";
        String sheetNr = "";
        String imagerySource = "";
        String surveyor = "";
        String rank = "";
        appNr = appBaunit.getNr();
        claimant = appBean.getContactPerson().getFullName();
        imageryDate = appBaunit.getImageryDate();


        address = appBean.getContactPerson().getAddress().getDescription();
        owners = appBaunit.getOwners();
        title = appBaunit.getTitle();
        lodgingDate = appBean.getLodgingDatetime();
        commencingDate = appBaunit.getCommencingDate();
        size = appBaunit.getSize();
        landUse = appBaunit.getLandUse();
        lga = appBaunit.getPropLocation();
        ward = appBaunit.getWard();
        state = appBaunit.getState();
        propAddress = baUnitBean.getLocation();
        //Special addition for generating image
        imageryResolution = appBaunit.getImageryResolution();
        imagerySource = appBaunit.getImagerySource();
        sheetNr = appBaunit.getSheetNr();
        surveyor = appBaunit.getSurveyor();
        rank = appBaunit.getRank();
        String mapImage = featureImageFileName;
        String mapImageSmall = featureImageFileNameSmall;
        String utmZone = srid.toString().substring(srid.toString().length() - 2);
        utmZone = imagerySource + utmZone ;
        String scaleLabel = "1: " + scale.intValue();
        String scalebarImageLocation = featureScalebarFileName;
        String prefix = getPrefix();
        cofoReport = prefix + "/CofO.jasper";


        if (!baUnitBean.isIsDeveloped()) {
            if (baUnitBean.getYearsForDev() != null) {
                timeToDevelop = baUnitBean.getYearsForDev().toString();
            }
            if (baUnitBean.getValueToImp() != null) {
                valueForImprov = baUnitBean.getValueToImp().toString();
            }
        }
        if (baUnitBean.getTerm() != null) {
            term = baUnitBean.getTerm().toString();
        }
        groundRent = appBaunit.getGroundRent().toString();


        featureFloatFront = getSvg("featureFloatFront");
        featureFloatBack = getSvg("featureFloatBack");
        featureFront = getSvg("featureFront");
        featureBack = getSvg("featureBack");

        featureFloatFront = featureFloatFront + prefix + ".svg";
        featureFloatBack = featureFloatBack + prefix + ".svg";
        featureFront = featureFront + prefix + ".svg";
        featureBack = featureBack + prefix + ".svg";


        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("LOCATION", location);
        inputParameters.put("AREA", location);
        inputParameters.put("APP_NR", appNr);
        inputParameters.put("CLIENT_NAME", owners);
        inputParameters.put("IMAGERY_DATE", imageryDate);
        inputParameters.put("ADDRESS", address);
        inputParameters.put("LODGING_DATE", lodgingDate);
        inputParameters.put("COMMENCING_DATE", commencingDate);
        inputParameters.put("TIME_DEVELOP", timeToDevelop);
        inputParameters.put("VALUE_IMPROV", valueForImprov);
        inputParameters.put("TERM", term);
        inputParameters.put("LAND_USE", landUse);
        inputParameters.put("PROP_LOCATION", propAddress);
        inputParameters.put("SIZE", size);
        inputParameters.put("REFNR", title);
        inputParameters.put("GROUND_RENT", groundRent);
        inputParameters.put("FRONT_IMAGE", featureFront);
        inputParameters.put("BACK_IMAGE", featureBack);
        inputParameters.put("FRONT_FLOAT_IMAGE", featureFloatFront);
        inputParameters.put("BACK_FLOAT_IMAGE", featureFloatBack);
        inputParameters.put("LGA", lga);
        inputParameters.put("WARD", ward);
        inputParameters.put("STATE", state);
        inputParameters.put("MAP_IMAGE", mapImage);
        inputParameters.put("SCALE", scaleLabel);
        inputParameters.put("UTM", utmZone);
        inputParameters.put("SCALEBAR", scalebarImageLocation);



        BaUnitBean[] beans = new BaUnitBean[1];
        beans[0] = baUnitBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        InputStream inputStream = ReportManager.class.getClassLoader().getResourceAsStream(cofoReport);

        try {
            JasperPrint report = JasperFillManager.fillReport(
                    inputStream,
                    inputParameters, jds);
            inputStream.close();


            return report;
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ReportManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Generates and displays <b>Systematic registration Certificates
     * report</b>.
     *
     * @param certificatesList List Parcel list bean containing data for the
     * report.
     *
     */
    public static JasperPrint getSysRegSlrtPlanReport(BaUnitBean baUnitBean, String location, ApplicationBean appBean, SysRegCertificatesBean appBaunit, String featureImageFileName,
            String featureScalebarFileName, Integer srid, Number scale, String featureFront, String featureBack, String featureImageFileNameSmall) {
        HashMap inputParameters = new HashMap();
        String featureFloatFront = "images/sola/front_float_"+ prefix + ".svg";
        String featureFloatBack = "images/sola/back_float_"+ prefix + ".svg";
        String SRPlanFront = "images/sola/slrtPlan.svg"+ prefix + ".svg";
        String featureSltr = "images/sola/sltr_"+ prefix + ".svg";
        
        
        String small = "";
        String map = "";


        String appNr = null;
        String claimant = null;
        String owners = null;
        String title = null;
        String address = null;
        String imageryDate = null;
        String timeToDevelop = null;
        String valueForImprov = null;
        String term = null;
        Date commencingDate = null;
        String landUse = null;
        String propAddress = null;
        String lga = null;
        String ward = null;
        String state = null;
        BigDecimal size = null;
        String groundRent = null;
        String imageryResolution = "50 cm";
        String sheetNr = "";
        String imagerySource = "";
        String surveyor = "";
        String rank = "";
        appNr = appBaunit.getNr();
        claimant = appBean.getContactPerson().getFullName();
        address = appBean.getContactPerson().getAddress().getDescription();
        owners = appBaunit.getOwners();
        title = appBaunit.getTitle();
        imageryDate = appBaunit.getImageryDate();
        commencingDate = appBaunit.getCommencingDate();
        size = appBaunit.getSize();
        landUse = appBaunit.getLandUse();
        lga = appBaunit.getPropLocation();
        ward = appBaunit.getWard();
        state = appBaunit.getState();
        propAddress = baUnitBean.getLocation();
        imageryResolution = appBaunit.getImageryResolution();
        imagerySource = appBaunit.getImagerySource();
        sheetNr = appBaunit.getSheetNr();
        surveyor = appBaunit.getSurveyor();
        rank = appBaunit.getRank();

        appBaunit.getId();

//        TODO CALL THE METHOD FOR GETTING THE MAP IMAGE
//        The method that will generate the map image will ask for these paramters/arguments:
//1 - cadastreObjectId: This is the id of the cadastre_object.id
//2 - width and height of the image in points: This is the width of the image which has to be the same as the image expected in the report. The same in the analogy with the existing reports where an map image can be embedded.
//
//The method will give back an object with:
//1 - Path to the image
//2 - Scale
//3 - Area in m2
//  4 - UTM zone
//  5 - Scalebar 

        String mapImage = featureImageFileName;
        String mapImageSmall = featureImageFileNameSmall;
        String utmZone = srid.toString().substring(srid.toString().length() - 2);
        utmZone = imagerySource + utmZone + "N";
        String scaleLabel = "1: " + scale.intValue();
        String scalebarImageLocation = featureScalebarFileName;

        if (!baUnitBean.isIsDeveloped()) {
            if (baUnitBean.getYearsForDev() != null) {
                timeToDevelop = baUnitBean.getYearsForDev().toString();
            }
            if (baUnitBean.getValueToImp() != null) {
                valueForImprov = baUnitBean.getValueToImp().toString();
            }
        }
        if (baUnitBean.getTerm() != null) {
            term = baUnitBean.getTerm().toString();
        }
        groundRent = appBaunit.getGroundRent().toString();

        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("LOCATION", location);
        inputParameters.put("AREA", location);
        inputParameters.put("APP_NR", appNr);
        inputParameters.put("CLIENT_NAME", owners);
        inputParameters.put("ADDRESS", address);
        inputParameters.put("IMAGERY_DATE", imageryDate);
        inputParameters.put("COMMENCING_DATE", commencingDate);
        inputParameters.put("TIME_DEVELOP", timeToDevelop);
        inputParameters.put("VALUE_IMPROV", valueForImprov);
        inputParameters.put("TERM", term);
        inputParameters.put("LAND_USE", landUse);
        inputParameters.put("PROP_LOCATION", propAddress);
        inputParameters.put("SIZE", size);
        inputParameters.put("REFNR", title);
        inputParameters.put("GROUND_RENT", groundRent);
        inputParameters.put("FRONT_IMAGE", featureFront);
        inputParameters.put("BACK_IMAGE", featureSltr);
        inputParameters.put("FRONT_FLOAT_IMAGE", featureFloatFront);
        inputParameters.put("BACK_FLOAT_IMAGE", featureFloatBack);
        inputParameters.put("LGA", lga);
        inputParameters.put("WARD", ward);
        inputParameters.put("STATE", state);
        inputParameters.put("MAP_IMAGE", mapImage);
        inputParameters.put("SCALE", scaleLabel);
        inputParameters.put("UTM", utmZone);
        inputParameters.put("SCALEBAR", scalebarImageLocation);
        inputParameters.put("MAP_IMAGE_SMALL", mapImageSmall);
        inputParameters.put("IMAGERY_RESOLUTION", imageryResolution);
        inputParameters.put("SHEET_NR", sheetNr);
        inputParameters.put("SURVEYOR", surveyor);
        inputParameters.put("RANK", rank);



        BaUnitBean[] beans = new BaUnitBean[1];
        beans[0] = baUnitBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        String slrtReport = null;
        slrtReport = getPrefix() + "/SltrPlan.jasper";

        InputStream inputStream = ReportManager.class.getClassLoader().getResourceAsStream(slrtReport);

        try {
            JasperPrint report = JasperFillManager.fillReport(
                    inputStream,
                    inputParameters, jds);
            inputStream.close();


            return report;


        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ReportManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }



    }

    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getSysRegManagementReport(SysRegManagementBean managementBean, Date dateFrom, Date dateTo, String nameLastpart) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("AREA", nameLastpart);
        SysRegManagementBean[] beans = new SysRegManagementBean[1];
        beans[0] = managementBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegMenagement.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

//      /**
//     * Generates and displays <b>Sys Reg Status</b> report.
//     *
//     * @param appBean Application bean containing data for the report.
//     */
    public static JasperPrint getSysRegGenderReport(SysRegGenderBean genderBean) {

        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);



        inputParameters.put("STATE", prefix);

        inputParameters.put("LGA", "");
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("MINISTRY_LOGO", ReportManager.class.getResourceAsStream(logoImage));

        SysRegGenderBean[] beans = new SysRegGenderBean[1];
        beans[0] = genderBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegGender.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

//      /**
//     * Generates and displays <b>Sys Reg Status</b> report.
//     *
//     * @param appBean Application bean containing data for the report.
//     */
    public static JasperPrint getSysRegStatusReport(SysRegStatusBean statusBean, Date dateFrom, Date dateTo, String nameLastpart) {

        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("STATE", getPrefix());
        inputParameters.put("LGA", "");
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("AREA", nameLastpart);
        inputParameters.put("MINISTRY_LOGO", ReportManager.class.getResourceAsStream(logoImage));

        SysRegStatusBean[] beans = new SysRegStatusBean[1];
        beans[0] = statusBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegStatus.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    //      /**
//     * Generates and displays <b>Sys Reg Progress</b> report.
//     *
//     * @param appBean Application bean containing data for the report.
//     */
    public static JasperPrint getSysRegProgressReport(SysRegProgressBean progressBean, Date dateFrom, Date dateTo, String nameLastpart) {

        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("AREA", nameLastpart);
        SysRegProgressBean[] beans = new SysRegProgressBean[1];
        beans[0] = progressBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegProgress.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Lodgement notice</b> report for the new
     * application.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getDisputeConfirmationReport(DisputeBean dispBean, String comments) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("LODGEMENTDATE", dispBean.getLodgementDate());
        inputParameters.put("COMMENTS", comments);
        inputParameters.put("PARTY", dispBean.getFilteredDisputePartyList());
        DisputeBean[] beans = new DisputeBean[1];
        beans[0] = dispBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream(logoImage));
        inputParameters.put("WHICH_CALLER", "N");

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/DisputeConfirmation.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    //      /**
//     * Generates and displays <b>Sys Reg Production</b> report.
//     *
//     * @param appBean Application bean containing data for the report.
//     */
    public static JasperPrint getSysRegProductionReport(SysRegProductionBean productionBean, Date dateFrom, Date dateTo) {

        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());

        inputParameters.put("STATE", prefix);
        inputParameters.put("LGA", "");
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("MINISTRY_LOGO", ReportManager.class.getResourceAsStream(logoImage));

        SysRegProductionBean[] beans = new SysRegProductionBean[1];
        beans[0] = productionBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegProduction.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
}
