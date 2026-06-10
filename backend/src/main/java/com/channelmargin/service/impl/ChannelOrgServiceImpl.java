package com.channelmargin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.channelmargin.entity.ChannelOrg;
import com.channelmargin.mapper.ChannelOrgMapper;
import com.channelmargin.service.ChannelOrgService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 渠道信息 Service 实现
 */
@Service
public class ChannelOrgServiceImpl extends ServiceImpl<ChannelOrgMapper, ChannelOrg> implements ChannelOrgService {

    @Override
    public IPage<ChannelOrg> listPage(Page<ChannelOrg> page, Map<String, Object> params) {
        LambdaQueryWrapper<ChannelOrg> wrapper = new LambdaQueryWrapper<>();

        // 只查询可用的渠道
        wrapper.eq(ChannelOrg::getAble, "0");

        // 渠道编码模糊查询
        if (params.containsKey("code") && StringUtils.hasText((String) params.get("code"))) {
            wrapper.like(ChannelOrg::getCode, params.get("code"));
        }

        // 渠道名称模糊查询
        if (params.containsKey("name") && StringUtils.hasText((String) params.get("name"))) {
            wrapper.like(ChannelOrg::getName, params.get("name"));
        }

        // 渠道全称模糊查询
        if (params.containsKey("fullName") && StringUtils.hasText((String) params.get("fullName"))) {
            wrapper.like(ChannelOrg::getFullName, params.get("fullName"));
        }

        // 区域查询
        if (params.containsKey("area") && StringUtils.hasText((String) params.get("area"))) {
            wrapper.eq(ChannelOrg::getArea, params.get("area"));
        }

        // 城市区域查询
        if (params.containsKey("cityArea") && StringUtils.hasText((String) params.get("cityArea"))) {
            wrapper.eq(ChannelOrg::getCityArea, params.get("cityArea"));
        }

        // 渠道属性查询
        if (params.containsKey("channelAttribute") && StringUtils.hasText((String) params.get("channelAttribute"))) {
            wrapper.eq(ChannelOrg::getChannelAttribute, params.get("channelAttribute"));
        }

        // 渠道经理查询
        if (params.containsKey("owner") && StringUtils.hasText((String) params.get("owner"))) {
            wrapper.eq(ChannelOrg::getOwner, params.get("owner"));
        }

        // 按创建时间降序
        wrapper.orderByDesc(ChannelOrg::getCreateTime);

        return page(page, wrapper);
    }

    @Override
    public ChannelOrg getByCode(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }
        return getOne(new LambdaQueryWrapper<ChannelOrg>()
                .eq(ChannelOrg::getCode, code));
    }

    @Override
    public ChannelOrg getByCodeOrName(String code, String name) {
        if (!StringUtils.hasText(code) && !StringUtils.hasText(name)) {
            return null;
        }
        LambdaQueryWrapper<ChannelOrg> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(code)) {
            wrapper.eq(ChannelOrg::getCode, code);
        }
        if (StringUtils.hasText(name)) {
            wrapper.or().eq(ChannelOrg::getName, name);
        }
        return getOne(wrapper);
    }

    @Override
    public ChannelOrg getByName(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return getOne(new LambdaQueryWrapper<ChannelOrg>()
                .eq(ChannelOrg::getName, name));
    }

    @Override
    public ChannelOrg getByFullName(String fullName) {
        if (!StringUtils.hasText(fullName)) {
            return null;
        }
        return getOne(new LambdaQueryWrapper<ChannelOrg>()
                .eq(ChannelOrg::getFullName, fullName));
    }

    @Override
    public ChannelOrg saveChannelOrg(Map<String, Object> params) {
        ChannelOrg channelOrg = new ChannelOrg();

        // 设置基本信息
        channelOrg.setCode(generateChannelCode((String) params.get("name")));
        channelOrg.setName((String) params.get("name"));
        channelOrg.setFullName((String) params.get("fullName"));
        channelOrg.setArea((String) params.get("area"));
        channelOrg.setCityArea((String) params.get("cityArea"));
        channelOrg.setPhone((String) params.get("phone"));
        channelOrg.setIdcard((String) params.get("idCard"));
        channelOrg.setBankAccount((String) params.get("bankAccount"));
        channelOrg.setOpenBank((String) params.get("openBank"));
        channelOrg.setAccount((String) params.get("account"));
        channelOrg.setCommunicationAddress((String) params.get("communicationAddress"));
        channelOrg.setRegisteCapital((String) params.get("registeCapital"));
        channelOrg.setFirmScale((String) params.get("firmScale"));
        channelOrg.setExhibition((String) params.get("exhibition"));
        channelOrg.setExhibitionRemark((String) params.get("exhibitionRemark"));
        channelOrg.setMajorBusiness((String) params.get("majorBusiness"));
        channelOrg.setMajorBusinessRemark((String) params.get("majorBusinessRemark"));
        channelOrg.setMajorBusinessCity((String) params.get("majorBusinessCity"));
        channelOrg.setMajorBusinessCityCode((String) params.get("majorBusinessCityCode"));
        channelOrg.setDetailAddr((String) params.get("detailAddr"));
        channelOrg.setComment((String) params.get("comment"));
        channelOrg.setChannelAttribute((String) params.get("channelAttribute"));
        channelOrg.setCapitalAllocation((String) params.get("capitalAllocation"));

        // 付款信息
        channelOrg.setPayOpenBank((String) params.get("payOpenBank"));
        channelOrg.setPayAccount((String) params.get("payAccount"));
        channelOrg.setPayBankAccount((String) params.get("payBankAccount"));

        // 设置默认值
        channelOrg.setAble("0");
        channelOrg.setRecycle("0");
        channelOrg.setChannelLevel("1");
        channelOrg.setType("0");
        channelOrg.setCategory("0");

        // 设置时间
        LocalDateTime now = LocalDateTime.now();
        channelOrg.setCreateTime(now);
        channelOrg.setUpdateTime(now);

        // 设置渠道经理（从当前登录用户获取）
        channelOrg.setOwner((String) params.get("owner"));
        channelOrg.setOwnerName((String) params.get("ownerName"));
        channelOrg.setOperater((String) params.get("operater"));
        channelOrg.setOperaterName((String) params.get("operaterName"));

        // 解析时间字段
        if (params.containsKey("signTime") && StringUtils.hasText((String) params.get("signTime"))) {
            channelOrg.setSignTime(LocalDateTime.parse((String) params.get("signTime")));
        }
        if (params.containsKey("setUpTime") && StringUtils.hasText((String) params.get("setUpTime"))) {
            channelOrg.setSetUpTime(LocalDateTime.parse((String) params.get("setUpTime")));
        }

        save(channelOrg);
        return channelOrg;
    }

    @Override
    public boolean updateChannelOrg(ChannelOrg channelOrg) {
        channelOrg.setUpdateTime(LocalDateTime.now());
        return updateById(channelOrg);
    }

    @Override
    public boolean deleteChannel(String code) {
        ChannelOrg channelOrg = getByCode(code);
        if (channelOrg == null) {
            return false;
        }
        channelOrg.setAble("1");
        channelOrg.setRecycle("1");
        channelOrg.setUpdateTime(LocalDateTime.now());
        return updateById(channelOrg);
    }

    @Override
    public List<String> getChildChannelsByCode(String code) {
        List<String> codes = new ArrayList<>();
        codes.add(code);

        // 递归查询所有子渠道
        List<ChannelOrg> children = list(new LambdaQueryWrapper<ChannelOrg>()
                .eq(ChannelOrg::getParentChannelId, code)
                .eq(ChannelOrg::getAble, "0"));

        for (ChannelOrg child : children) {
            codes.addAll(getChildChannelsByCode(child.getCode()));
        }

        return codes;
    }

    /**
     * 生成渠道编码
     * 规则：取渠道简称首字母（前3位）+ 两位随机大写字母
     */
    private String generateChannelCode(String name) {
        if (!StringUtils.hasText(name)) {
            return "CHN" + String.format("%02d", (int) (Math.random() * 100));
        }

        StringBuilder code = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (Character.isLetter(c) && code.length() < 3) {
                code.append(Character.toUpperCase(c));
            }
            if (code.length() >= 3) break;
        }

        // 补齐3位
        while (code.length() < 3) {
            code.append("X");
        }

        // 添加随机后缀
        code.append((char) ('A' + (int) (Math.random() * 26)));
        code.append((char) ('A' + (int) (Math.random() * 26)));

        return code.toString();
    }
}
