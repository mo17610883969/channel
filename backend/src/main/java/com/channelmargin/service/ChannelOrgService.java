package com.channelmargin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.channelmargin.entity.ChannelOrg;

import java.util.Map;

/**
 * 渠道信息 Service
 */
public interface ChannelOrgService extends IService<ChannelOrg> {

    /**
     * 分页查询渠道列表
     */
    IPage<ChannelOrg> listPage(Page<ChannelOrg> page, Map<String, Object> params);

    /**
     * 根据编码查询
     */
    ChannelOrg getByCode(String code);

    /**
     * 根据编码或名称查询
     */
    ChannelOrg getByCodeOrName(String code, String name);

    /**
     * 根据渠道简称查询
     */
    ChannelOrg getByName(String name);

    /**
     * 根据渠道全称查询
     */
    ChannelOrg getByFullName(String fullName);

    /**
     * 保存渠道信息
     */
    ChannelOrg saveChannelOrg(Map<String, Object> params);

    /**
     * 更新渠道信息
     */
    boolean updateChannelOrg(ChannelOrg channelOrg);

    /**
     * 删除渠道（移入回收站）
     */
    boolean deleteChannel(String code);

    /**
     * 获取所有子渠道编码
     */
    java.util.List<String> getChildChannelsByCode(String code);
}
