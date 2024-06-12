package com.easing.commons.android.lbs;

import com.easing.commons.android.data.JSON;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.manager.Resources;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@SuppressWarnings("all")
public class LBSArea {

    public List<Province> provinces = new ArrayList();

    //省
    @Data
    public static class Province {

        public int provinceId;
        public String provinceName;
        public List<City> cities = new ArrayList();

        @Override
        public String toString() {
            return provinceName;
        }
    }

    //市
    @Data
    public static class City {

        public int cityId;
        public String cityName;
        public List<District> districts = new ArrayList();

        @Override
        public String toString() {
            return cityName;
        }
    }

    //区县
    @Data
    public static class District {

        public int districtId;
        public String districtName;

        @Override
        public String toString() {
            return districtName;
        }
    }

    //全部城市
    public List<City> allCities() {
        List<City> cities = new ArrayList();
        for (Province province : provinces)
            for (City city : province.cities)
                cities.add(city);
        return cities;
    }

    //获取行政区域数据
    public static LBSArea getAdministrativeAreaData() {
        String data = Resources.readAssetText("area/area_code.json");
        LBSArea area = JSON.parse(data, LBSArea.class);
        return area;
    }

    //按照编号查找城市
    public City findCity(Integer cityId) {
        for (Province province : provinces)
            for (City city : province.cities)
                if (Maths.equal(cityId, city.cityId))
                    return city;
        return null;
    }

    //根据县区id查找城市
    public City findCityByDistrict(Integer districtId) {
        for (Province province : provinces)
            for (City city : province.cities)
                for (District district : city.districts)
                    if (Maths.equal(districtId, district.districtId))
                        return city;
        return null;
    }
}
