package com.channelmargin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.channelmargin.dto.ApproveMarginAccountReq;
import com.channelmargin.dto.SaveMarginAccountOperationReq;
import com.channelmargin.dto.SubmitReleaseReq;
import com.channelmargin.dto.UpdateMarginAccountOperationReq;
import com.channelmargin.dto.Result;
import com.channelmargin.entity.MarginAccountOperationRecord;
import com.channelmargin.service.MarginAccountOperationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 保证金账户使用控制器
 */
@RestController
@RequestMapping("/api/margin/account/use")
@RequiredArgsConstructor
public class MarginAccountOperationRecordController {

    private final MarginAccountOperationRecordService operationRecordService;

    /**
     * 账户使用列表
     */
    @GetMapping("/list")
    public Result<IPage<MarginAccountOperationRecord>> list(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String masterChannelName,
            @RequestParam(required = false) String bussNo,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "USE");
        if (masterChannelName != null) params.put("masterChannelName", masterChannelName);
        if (bussNo != null) params.put("bussNo", bussNo);
        if (custName != null) params.put("custName", custName);
        if (contractNo != null) params.put("contractNo", contractNo);
        if (status != null) params.put("status", status);
        return Result.success(operationRecordService.selectUseListPage(current, size, params));
    }

    /**
     * 新增账户使用
     */
    @PostMapping("/save")
    public Result<Map<String, Object>> save(@RequestBody SaveMarginAccountOperationReq req) {
        req.setType("USE");
        Map<String, Object> result = operationRecordService.saveMarginAccountOperation(req);
        return "0000".equals(result.get("resultCode"))
                ? Result.success(result)
                : Result.error(result.get("resultMessage").toString());
    }

    /**
     * 更新账户使用
     */
    @PutMapping("/update")
    public Result<Map<String, Object>> update(@RequestBody UpdateMarginAccountOperationReq req) {
        Map<String, Object> result = operationRecordService.updateMarginAccountOperation(req);
        return "0000".equals(result.get("resultCode"))
                ? Result.success(result)
                : Result.error(result.get("resultMessage").toString());
    }

    /**
     * 获取详情
     */
    @GetMapping("/details/{id}")
    public Result<Map<String, Object>> details(@PathVariable Long id) {
        return Result.success(operationRecordService.getDetails(id));
    }

    /**
     * 审核使用
     */
    @PostMapping("/approve")
    public Result<Map<String, Object>> approve(@RequestBody ApproveMarginAccountReq req) {
        Map<String, Object> result = operationRecordService.approve(req);
        return "0000".equals(result.get("resultCode"))
                ? Result.success(result)
                : Result.error(result.get("resultMessage").toString());
    }

    /**
     * 提交释放
     */
    @PostMapping("/submitRelease")
    public Result<Map<String, Object>> submitRelease(@RequestBody SubmitReleaseReq req) {
        req.setType("RELEASE");
        Map<String, Object> result = operationRecordService.submitRelease(req);
        return "0000".equals(result.get("resultCode"))
                ? Result.success(result)
                : Result.error(result.get("resultMessage").toString());
    }

    /**
     * 审核释放
     */
    @PostMapping("/approveRelease")
    public Result<Map<String, Object>> approveRelease(@RequestBody ApproveMarginAccountReq req) {
        Map<String, Object> result = operationRecordService.approveRelease(req);
        return "0000".equals(result.get("resultCode"))
                ? Result.success(result)
                : Result.error(result.get("resultMessage").toString());
    }

    /**
     * 检查订单号是否存在使用记录
     */
    @GetMapping("/checkBussNo")
    public Result<Long> checkBussNo(@RequestParam String bussNo) {
        return Result.success(operationRecordService.getMarginAccountByBussNo(bussNo));
    }
}