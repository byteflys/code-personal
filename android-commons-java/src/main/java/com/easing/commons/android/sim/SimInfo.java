package com.easing.commons.android.sim;

import android.content.Context;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.easing.commons.android.app.CommonApplication;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class SimInfo {

    public String operatorCode;
    public String operatorName;

    public String networkType;

    public int signalLevel;
    public String signalLevelName;

    public int rsrp;
    public int rsrq;

    @Override
    public String toString() {
        return "（" + operatorCode + "）" + operatorName + networkType + " : " + signalLevel + "格/4格";
    }

    public void setOperatorName() {
        if (operatorCode.equals("46000"))
            operatorName = "中国移动";
        else if (operatorCode.equals("46002"))
            operatorName = "中国移动";
        else if (operatorCode.equals("46004"))
            operatorName = "中国移动";
        else if (operatorCode.equals("46007"))
            operatorName = "中国移动";
        else if (operatorCode.equals("46008"))
            operatorName = "中国移动";
        else if (operatorCode.equals("46020"))
            operatorName = "中国移动";
        else if (operatorCode.equals("46003"))
            operatorName = "中国电信";
        else if (operatorCode.equals("46005"))
            operatorName = "中国电信";
        else if (operatorCode.equals("46009"))
            operatorName = "中国电信";
        else if (operatorCode.equals("46011"))
            operatorName = "中国电信";
        else if (operatorCode.equals("46001"))
            operatorName = "中国联通";
        else if (operatorCode.equals("46006"))
            operatorName = "中国联通";
        else if (operatorCode.equals("46010"))
            operatorName = "中国联通";
        else
            operatorName = "其它运营商";
    }

    public void setSignalLevelName() {
        if (signalLevel == 4)
            signalLevelName = "好";
        else if (signalLevel == 3)
            signalLevelName = "中";
        else if (signalLevel == 2)
            signalLevelName = "差";
        else if (signalLevel == 1)
            signalLevelName = "极差";
        else
            signalLevelName = "无信号";
    }

    //获取所有SIM卡的信号强度信息
    public static List<SimInfo> getAllSimInfo(Context context) {
        TelephonyManager telephonyManager = context.getSystemService(TelephonyManager.class);
        List<SimInfo> simInfoList = new ArrayList();
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        for (CellInfo cellInfo : cellInfoList) {
            if (!cellInfo.isRegistered())
                continue;
            //4G
            if (cellInfo instanceof CellInfoLte) {
                CellInfoLte lte = (CellInfoLte) cellInfo;
                CellIdentityLte identity = lte.getCellIdentity();
                CellSignalStrengthLte strength = lte.getCellSignalStrength();
                SimInfo simInfo = new SimInfo();
                simInfo.operatorCode = identity.getMobileNetworkOperator();
                simInfo.networkType = "4G";
                simInfo.rsrp = strength.getRsrp();
                simInfo.rsrq = strength.getRsrq();
                simInfo.signalLevel = new SignalLevelLte(simInfo.rsrp, simInfo.rsrq).getLevel();
                simInfo.setOperatorName();
                simInfo.setSignalLevelName();
                simInfoList.add(simInfo);
            }
            //5G
            if (cellInfo instanceof CellInfoNr) {
                CellInfoNr nr = (CellInfoNr) cellInfo;
                CellIdentityNr identity = (CellIdentityNr) nr.getCellIdentity();
                CellSignalStrengthNr strength = (CellSignalStrengthNr) nr.getCellSignalStrength();
                SimInfo simInfo = new SimInfo();
                simInfo.operatorCode = "" + identity.getMccString() + identity.getMncString();
                simInfo.networkType = "5G";
                simInfo.rsrp = strength.getSsRsrp();
                simInfo.rsrq = strength.getSsRsrq();
                simInfo.signalLevel = new SignalLevelNr(simInfo.rsrp, simInfo.rsrq).getLevel();
                simInfo.setOperatorName();
                simInfo.setSignalLevelName();
                simInfoList.add(simInfo);
            }
        }
        return simInfoList;
    }

    //获取默认上网卡信息
    public static SimInfo getDefaultDataCardInfo() {
        try {
            int subId = SubscriptionManager.getDefaultDataSubscriptionId();
            SubscriptionManager subscriptionManager = SubscriptionManager.from(CommonApplication.ctx);
            SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(subId);
            SimInfo simInfo = new SimInfo();
            simInfo.operatorCode = subscriptionInfo.getMccString() + subscriptionInfo.getMncString();
            simInfo.setOperatorName();
            return simInfo;
        } catch (Throwable e) {
            return null;
        }
    }
}

