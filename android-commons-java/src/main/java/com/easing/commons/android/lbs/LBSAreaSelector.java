package com.easing.commons.android.lbs;

import com.easing.commons.android.format.Texts;
import com.easing.commons.android.ui.control.selector.OptionSelector;

import java.util.List;

//工具类，自动将省市区控件关联起来
@SuppressWarnings("all")
public class LBSAreaSelector {

    LBSArea area;
    OptionSelector<LBSArea.Province> provinceSelector;
    OptionSelector<LBSArea.City> citySelector;
    OptionSelector<LBSArea.District> districtSelector;

    protected LBSAreaSelector() {}

    public static LBSAreaSelector from(OptionSelector<LBSArea.Province> provinceSelector, OptionSelector<LBSArea.City> citySelector, OptionSelector<LBSArea.District> districtSelector) {
        LBSAreaSelector selector = new LBSAreaSelector();
        selector.area = LBSArea.getAdministrativeAreaData();
        selector.provinceSelector = provinceSelector;
        selector.citySelector = citySelector;
        selector.districtSelector = districtSelector;
        selector.initOptions();
        return selector;
    }

    //初始化选项和事件
    protected void initOptions() {
        //设置省级选择框
        List<LBSArea.Province> provinces = area.getProvinces();
        provinces.add(0, null);
        provinceSelector.options(provinces, LBSArea.Province[]::new);
        provinceSelector.nameTranslator(province -> {
            if (province == null)
                return "无";
            return province.provinceName;
        });
        provinceSelector.onOptionSelect((itemView1, province, index1) -> {
            //清除旧选项
            citySelector.clearSelection();
            districtSelector.clearSelection();
            //空的不处理
            if (province == null) {
                provinceSelector.clearSelection();
                citySelector.options(null);
                districtSelector.options(null);
                return;
            }
            //设置市级选择框
            List<LBSArea.City> cities = province.getCities();
            cities.add(0, null);
            citySelector.options(cities, LBSArea.City[]::new);
            citySelector.nameTranslator(city -> {
                if (city == null)
                    return "无";
                return city.cityName;
            });
            citySelector.onOptionSelect((itemView2, city, index2) -> {
                //清除旧选项
                districtSelector.clearSelection();
                //空的不处理
                if (city == null) {
                    citySelector.clearSelection();
                    districtSelector.options(null);
                    return;
                }
                //设置县级选择框
                List<LBSArea.District> districts = city.getDistricts();
                districts.add(0, null);
                districtSelector.options(districts, LBSArea.District[]::new);
                districtSelector.onOptionSelect((itemView3, district, index3) -> {
                    //空的不处理
                    if (district == null)
                        districtSelector.clearSelection();
                });
                districtSelector.nameTranslator(district -> {
                    if (district == null)
                        return "无";
                    return district.districtName;
                });
            });
        });
        provinceSelector.clearSelection();
        citySelector.clearSelection();
        districtSelector.clearSelection();
    }

    //选择默认地点
    public void setInitialLocation(LBSLocation location) {
        if (location.province == null) return;
        for (LBSArea.Province province : provinceSelector.options()) {
            if (province == null) continue;
            if (Texts.equal(province.provinceName, location.province)) {
                provinceSelector.performSelection(province);
                for (LBSArea.City city : citySelector.options()) {
                    if (city == null) continue;
                    if (Texts.equal(city.cityName, location.city)) {
                        citySelector.performSelection(city);
                        for (LBSArea.District district : districtSelector.options()) {
                            if (district == null) continue;
                            if (Texts.equal(district.districtName, location.district)) {
                                districtSelector.performSelection(district);
                                return;
                            }
                        }
                        return;
                    }
                }
                return;
            }
        }
    }

    //获取Province
    public LBSArea.Province getProvince() {
        return provinceSelector.selectedOption();
    }

    //获取City
    public LBSArea.City getCity() {
        return citySelector.selectedOption();
    }

    //获取District
    public LBSArea.District getDistrict() {
        return districtSelector.selectedOption();
    }


}


