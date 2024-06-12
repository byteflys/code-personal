package com.easing.commons.android.sim;

@SuppressWarnings("all")
public class SignalLevelLte {

    int rsrpLevel = 0;
    int rsrqLevel = 0;

    //rsrp正常范围：[-140,-43]
    //rsrq正常范围：[-34, 3]
    public SignalLevelLte(int rsrp, int rsrq) {
        //计算信号强度等级
        if (rsrp < -140 || rsrp > -43)
            rsrpLevel = 0;
        else if (rsrp >= -95)
            rsrpLevel = 4;
        else if (rsrp >= -105)
            rsrpLevel = 3;
        else if (rsrp >= -115)
            rsrpLevel = 2;
        else
            rsrpLevel = 1;
        //计算信号质量等级
        if (rsrq < -34 || rsrq > 3)
            rsrqLevel = 0;
        else if (rsrq >= -14)
            rsrqLevel = 4;
        else if (rsrq >= -17)
            rsrqLevel = 3;
        else if (rsrq >= -19)
            rsrqLevel = 2;
        else
            rsrqLevel = 1;
    }

    //计算综合信号等级
    public int getLevel() {
        if (rsrpLevel == 0 || rsrqLevel == 0)
            return 0;
        else if (rsrpLevel == 4 && rsrqLevel == 4)
            return 4;
        else if (rsrpLevel >= 3 && rsrqLevel >= 3)
            return 3;
        else if (rsrpLevel >= 2 && rsrqLevel >= 2)
            return 2;
        return 1;
    }
}

