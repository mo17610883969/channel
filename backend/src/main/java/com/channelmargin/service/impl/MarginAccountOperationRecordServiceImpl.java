package com.channelmargin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.channelmargin.dto.ApproveMarginAccountReq;
import com.channelmargin.dto.SaveMarginAccountOperationReq;
import com.channelmargin.dto.SubmitReleaseReq;
import com.channelmargin.dto.UpdateMarginAccountOperationReq;
import com.channelmargin.entity.MarginAccount;
import com.channelmargin.entity.MarginAccountChange;
import com.channelmargin.entity.MarginAccountOperationRecord;
import com.channelmargin.mapper.MarginAccountChangeMapper;
import com.channelmargin.mapper.MarginAccountMapper;
import com.channelmargin.mapper.MarginAccountOperationRecordMapper;
import com.channelmargin.service.MarginAccountOperationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarginAccountOperationRecordServiceImpl extends ServiceImpl<MarginAccountOperationRecordMapper, MarginAccountOperationRecord>
        implements MarginAccountOperationRecordService {

    @Autowired
    private MarginAccountOperationRecordMapper operationRecordMapper;

    @Autowired
    private MarginAccountMapper marginAccountMapper;

    @Autowired
    private MarginAccountChangeMapper marginAccountChangeMapper;

    @Override
    public IPage<MarginAccountOperationRecord> selectUseListPage(int current, int size, Map<String, Object> params) {
        Page<MarginAccountOperationRecord> page = new Page<>(current, size);
        return operationRecordMapper.selectUseListPage(page, params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveMarginAccountOperation(SaveMarginAccountOperationReq req) {
        Map<String, Object> result = new HashMap<>();

        // 检查订单号是否已存在使用记录
        long count = operationRecordMapper.countByBussNo(req.getBussNo());
        if (count > 0) {
            result.put("resultCode", "0002");
            result.put("resultMessage", "该订单号已存在账户使用");
            return result;
        }

        // 查询保证金账户信息
        LambdaQueryWrapper<MarginAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MarginAccount::getId, req.getMarginAccountId());
        MarginAccount marginAccount = marginAccountMapper.selectOne(wrapper);

        if (marginAccount == null) {
            result.put("resultCode", "0002");
            result.put("resultMessage", "保证金账户不存在");
            return result;
        }

        // 计算可用金额
        BigDecimal usableBalance = marginAccount.getMarginFirstBalance()
                .add(marginAccount.getRechargeBalance())
                .subtract(marginAccount.getMarginTransitUseBalance())
                .subtract(marginAccount.getMarginRefundedBalance());

        if (req.getMarginUseBalance().compareTo(usableBalance) > 0) {
            result.put("resultCode", "0002");
            result.put("resultMessage", "该渠道保证金可用金额不足");
            return result;
        }

        // 创建使用记录
        MarginAccountOperationRecord record = new MarginAccountOperationRecord();
        record.setType("USE");
        record.setMarginAccountId(req.getMarginAccountId());
        record.setMasterChannelName(req.getMasterChannelName());
        record.setBussNo(req.getBussNo());
        record.setCustId(req.getCustId());
        record.setCustName(req.getCustName());
        record.setContractNo(req.getContractNo());
        record.setCode(req.getCode());
        record.setName(req.getName());
        record.setContAmt(req.getLoanAmt());
        record.setAllocationRatio(req.getAllocationRatio());
        record.setMarginUseBalance(req.getMarginUseBalance());
        record.setStatus("0"); // 待审核
        record.setCreater("admin"); // TODO: 获取当前用户
        record.setCreaterId("1");
        record.setEditor("admin");
        record.setRemark(req.getRemark());
        record.setCreateTime(LocalDateTime.now());
        record.setEditTime(LocalDateTime.now());
        operationRecordMapper.insert(record);

        result.put("resultCode", "0000");
        result.put("resultMessage", "提交成功");
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateMarginAccountOperation(UpdateMarginAccountOperationReq req) {
        Map<String, Object> result = new HashMap<>();

        MarginAccountOperationRecord record = operationRecordMapper.selectById(req.getMarginAccountOperationRecordId());
        if (record == null) {
            result.put("resultCode", "0002");
            result.put("resultMessage", "账户使用记录不存在");
            return result;
        }

        if (!"2".equals(record.getStatus())) {
            result.put("resultCode", "0002");
            result.put("resultMessage", "只有审核拒绝的记录可以重新提交");
            return result;
        }

        // 更新记录
        record.setBussNo(req.getBussNo());
        record.setCustId(req.getCustId());
        record.setCustName(req.getCustName());
        record.setContractNo(req.getContractNo());
        record.setContAmt(req.getLoanAmt());
        record.setAllocationRatio(req.getAllocationRatio());
        record.setMarginUseBalance(req.getMarginUseBalance());
        record.setStatus("0");
        record.setEditor("admin");
        record.setRemark(req.getRemark());
        record.setEditTime(LocalDateTime.now());
        operationRecordMapper.updateById(record);

        result.put("resultCode", "0000");
        result.put("resultMessage", "重新提交成功");
        return result;
    }

    @Override
    public Map<String, Object> getDetails(Long id) {
        Map<String, Object> result = new HashMap<>();
        MarginAccountOperationRecord record = operationRecordMapper.selectById(id);
        result.put("accountOperationRecord", record);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> approve(ApproveMarginAccountReq req) {
        Map<String, Object> result = new HashMap<>();

        MarginAccountOperationRecord record = operationRecordMapper.selectById(req.getMarginApproveId());
        if (record == null) {
            result.put("resultCode", "0002");
            result.put("resultMessage", "查询不到渠道保证金使用记录");
            return result;
        }

        String status;
        if ("1".equals(req.getFirstApprStatus())) {
            status = "1";
            // 审核通过，增加在途使用金额
            marginAccountMapper.updateMarginTransitUseBalanceById(record.getMarginAccountId(), record.getMarginUseBalance());

            // 记录变更
            MarginAccountChange change = new MarginAccountChange();
            change.setMarginAccountId(record.getMarginAccountId());
            change.setChangeType("USE");
            change.setChangeAmount(record.getMarginUseBalance());
            change.setBeforeBalance(BigDecimal.ZERO);
            change.setAfterBalance(record.getMarginUseBalance());
            change.setBusinessId(record.getId());
            change.setBusinessNo(record.getBussNo());
            change.setReason("账户使用审核通过");
            change.setOperater("admin");
            change.setCreateTime(LocalDateTime.now());
            marginAccountChangeMapper.insert(change);
        } else {
            status = "2";
        }

        record.setStatus(status);
        record.setEditor("admin");
        record.setEditTime(LocalDateTime.now());
        operationRecordMapper.updateById(record);

        result.put("resultCode", "0000");
        result.put("resultMessage", "审核完成");
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> approveRelease(ApproveMarginAccountReq req) {
        Map<String, Object> result = new HashMap<>();

        MarginAccountOperationRecord record = operationRecordMapper.selectById(req.getMarginApproveId());
        if (record == null) {
            result.put("resultCode", "0002");
            result.put("resultMessage", "查询不到渠道保证金释放记录");
            return result;
        }

        if ("1".equals(req.getFirstApprStatus())) {
            // 审核通过，减少在途使用金额
            marginAccountMapper.updateSubMarginTransitUseBalanceById(record.getMarginAccountId(), record.getMarginUseBalance());
            record.setStatus("4"); // 已释放
        } else {
            record.setStatus("1"); // 回归使用中
        }

        record.setEditor("admin");
        record.setEditTime(LocalDateTime.now());
        operationRecordMapper.updateById(record);

        result.put("resultCode", "0000");
        result.put("resultMessage", "审核完成");
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitRelease(SubmitReleaseReq req) {
        Map<String, Object> result = new HashMap<>();

        MarginAccountOperationRecord original = operationRecordMapper.selectById(req.getOperationId());
        if (original == null) {
            result.put("resultCode", "0002");
            result.put("resultMessage", "查询不到渠道保证金使用记录");
            return result;
        }

        // 创建释放记录
        MarginAccountOperationRecord release = new MarginAccountOperationRecord();
        release.setType("RELEASE");
        release.setOperationRecordId(original.getId());
        release.setMarginAccountId(original.getMarginAccountId());
        release.setMarginAccountChannelId(original.getMarginAccountChannelId());
        release.setMasterChannelName(original.getMasterChannelName());
        release.setBussNo(original.getBussNo());
        release.setCustId(original.getCustId());
        release.setCustName(original.getCustName());
        release.setContractNo(original.getContractNo());
        release.setCode(original.getCode());
        release.setName(original.getName());
        release.setContAmt(original.getContAmt());
        release.setAllocationRatio(original.getAllocationRatio());
        release.setMarginUseBalance(original.getMarginUseBalance());
        release.setStatus("0");
        release.setCreater("admin");
        release.setCreaterId("1");
        release.setCreateTime(LocalDateTime.now());
        release.setEditor("admin");
        release.setEditTime(LocalDateTime.now());
        release.setRemark(req.getRemark());
        operationRecordMapper.insert(release);

        // 更新原记录状态为释放审核中
        original.setStatus("3");
        original.setEditTime(LocalDateTime.now());
        operationRecordMapper.updateById(original);

        result.put("resultCode", "0000");
        result.put("resultMessage", "提交成功");
        return result;
    }

    @Override
    public BigDecimal getMarginTransitUseBalance(Long marginAccountId) {
        return operationRecordMapper.getMarginTransitUseBalance(marginAccountId);
    }

    @Override
    public long getMarginAccountByBussNo(String bussNo) {
        return operationRecordMapper.countByBussNo(bussNo);
    }

    @Override
    public List<MarginAccountOperationRecord> queryListByParam(Map<String, Object> param) {
        return operationRecordMapper.selectListByParams(param);
    }
}
