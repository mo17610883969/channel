package com.channelmargin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.channelmargin.dto.Result;
import com.channelmargin.entity.ChannelOrg;
import com.channelmargin.service.ChannelOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 渠道信息 Controller
 */
@RestController
@RequestMapping("/api/channel")
public class ChannelOrgController {

    @Autowired
    private ChannelOrgService channelOrgService;

    /**
     * 渠道信息列表（分页）
     */
    @GetMapping("/list")
    public Result list(@RequestParam(required = false) Map<String, Object> params,
                      @RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<ChannelOrg> page = new Page<>(pageNum, pageSize);
        IPage<ChannelOrg> result = channelOrgService.listPage(page, params);
        return Result.success(result);
    }

    /**
     * 获取渠道详情
     */
    @GetMapping("/{code}")
    public Result getByCode(@PathVariable String code) {
        ChannelOrg channelOrg = channelOrgService.getByCode(code);
        if (channelOrg == null) {
            return Result.error("渠道不存在");
        }
        return Result.success(channelOrg);
    }

    /**
     * 新增渠道
     */
    @PostMapping
    public Result save(@RequestBody Map<String, Object> params) {
        // 检查渠道简称是否重复
        String name = (String) params.get("name");
        if (channelOrgService.getByName(name) != null) {
            return Result.error("渠道简称重复");
        }

        // 检查渠道全称是否重复
        String fullName = (String) params.get("fullName");
        if (fullName != null && channelOrgService.getByFullName(fullName) != null) {
            return Result.error("渠道全称重复");
        }

        ChannelOrg channelOrg = channelOrgService.saveChannelOrg(params);
        return Result.success(channelOrg);
    }

    /**
     * 更新渠道
     */
    @PutMapping
    public Result update(@RequestBody ChannelOrg channelOrg) {
        if (channelOrg.getCode() == null) {
            return Result.error("渠道编码不能为空");
        }

        // 检查渠道简称是否重复
        String name = channelOrg.getName();
        ChannelOrg existing = channelOrgService.getByName(name);
        if (existing != null && !existing.getId().equals(channelOrg.getId())) {
            return Result.error("渠道简称重复");
        }

        // 检查渠道全称是否重复
        String fullName = channelOrg.getFullName();
        if (fullName != null) {
            ChannelOrg existingFull = channelOrgService.getByFullName(fullName);
            if (existingFull != null && !existingFull.getId().equals(channelOrg.getId())) {
                return Result.error("渠道全称重复");
            }
        }

        boolean result = channelOrgService.updateChannelOrg(channelOrg);
        return result ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除渠道（移入回收站）
     */
    @DeleteMapping("/{code}")
    public Result delete(@PathVariable String code) {
        boolean result = channelOrgService.deleteChannel(code);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 获取渠道列表（不分页，用于下拉框等）
     */
    @GetMapping("/options")
    public Result getOptions() {
        Page<ChannelOrg> page = new Page<>(1, 1000);
        IPage<ChannelOrg> result = channelOrgService.listPage(page, Map.of());
        return Result.success(result.getRecords());
    }
}