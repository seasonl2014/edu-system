package cn.xueden.utils;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author:梁志杰
 * @date:2023/3/20
 * @description:cn.xueden.system.ip2region
 * @version:1.0
 */
@Data
public class IpInfo {
    /**
     * 国家
     */
    private String country;

    /**
     * 区域
     */
    private String region;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 运营商
     */
    private String isp;

    /**
     * 拼接完整的地址
     * @return address
     */
    public String getAddress() {
        Set<String> regionSet = new LinkedHashSet<>();
        regionSet.add(country);
        regionSet.add(region);
        regionSet.add(province);
        regionSet.add(city);
        regionSet.removeIf(Objects::isNull);
        return StrUtil.join(StrUtil.EMPTY, regionSet);
    }

    /**
     * 拼接完整的地址
     * @return address
     */
    public String getAddressAndIsp() {
        Set<String> regionSet = new LinkedHashSet<>();
        regionSet.add(country);
        regionSet.add(region);
        regionSet.add(province);
        regionSet.add(city);
        regionSet.add(isp);
        regionSet.removeIf(Objects::isNull);
        return StrUtil.join(StrUtil.EMPTY, regionSet);
    }

}
