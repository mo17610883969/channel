package com.channelmargin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.channelmargin.dto.Result;
import com.channelmargin.entity.MarginAccount;
import com.channelmargin.entity.MarginAccountChannel;
import com.channelmargin.service.MarginAccountChannelService;
import com.channelmargin.service.MarginAccountService;
import com.channelmargin.service.MarginAccountOperationRecordService;
import com.channelmargin.service.ChannelOrgService;
import com.channelmargin.entity.ChannelOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/margin-account")
public class MarginAccountController {

    @Autowired
    private MarginAccountService marginAccountService;

    @Autowired
    private MarginAccountChannelService marginAccountChannelService;

    @Autowired
    private MarginAccountOperationRecordService marginAccountOperationRecordService;

    @Autowired
    private ChannelOrgService channelOrgService;

    /**
     * 获取保证金账户列表
     */
    @GetMapping("/list")
    public Result<List<MarginAccount>> list(HttpServletRequest request) {
        LambdaQueryWrapper<MarginAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(MarginAccount::getStatus, "4");
        wrapper.orderByDesc(MarginAccount::getCreateTime);
        List<MarginAccount> list = marginAccountService.list(wrapper);

        for (MarginAccount account : list) {
            List<MarginAccountChannel> channels = marginAccountChannelService.list(
                new LambdaQueryWrapper<MarginAccountChannel>()
                    .eq(MarginAccountChannel::getMarginAccountId, account.getId())
            );

            if (channels != null && !channels.isEmpty()) {
                account.setChannelCodes(String.join(",", channels.stream().map(MarginAccountChannel::getCode).collect(Collectors.toList())));
                account.setChannelNames(String.join(",", channels.stream().map(MarginAccountChannel::getName).collect(Collectors.toList())));
                account.setCityNames(String.join(",", channels.stream().map(MarginAccountChannel::getCityArea).collect(Collectors.toList())));
            }
            calculateAccountFields(account);
        }
        return Result.success(list);
    }

    /**
     * 获取保证金账户详情
     */
    @GetMapping("/{id}")
    public Result<MarginAccount> getById(@PathVariable Long id) {
        MarginAccount account = marginAccountService.getById(id);
        if (account == null) {
            return Result.fail("账户不存在");
        }

        List<MarginAccountChannel> channels = marginAccountChannelService.list(
            new LambdaQueryWrapper<MarginAccountChannel>()
                .eq(MarginAccountChannel::getMarginAccountId, id)
        );
        account.setChannelList(channels);

        if (channels != null && !channels.isEmpty()) {
            account.setChannelCodes(String.join(",", channels.stream().map(MarginAccountChannel::getCode).collect(Collectors.toList())));
            account.setChannelNames(String.join(",", channels.stream().map(MarginAccountChannel::getName).collect(Collectors.toList())));
            account.setCityNames(String.join(",", channels.stream().map(MarginAccountChannel::getCityArea).collect(Collectors.toList())));
        }

        calculateAccountFields(account);
        return Result.success(account);
    }

    /**
     * 新增保证金账户
     */
    @PostMapping
    public Result<?> save(@RequestBody MarginAccount account, HttpServletRequest request) {
        account.setStatus("0");
        account.setCreaterId(getUserId(request));
        if (account.getMarginFirstBalance() != null) {
            account.setMarginFirstBalance(account.getMarginFirstBalance().multiply(new BigDecimal("10000")));
        }
        if (account.getMarginRemainBalance() != null) {
            account.setMarginRemainBalance(account.getMarginRemainBalance().multiply(new BigDecimal("10000")));
        }

        marginAccountService.save(account);

        if (account.getChannelList() != null && !account.getChannelList().isEmpty()) {
            for (MarginAccountChannel channel : account.getChannelList()) {
                channel.setMarginAccountId(account.getId());
                marginAccountChannelService.save(channel);
            }
        }
        return Result.success(account);
    }

    /**
     * 更新保证金账户
     */
    @PutMapping
    public Result<?> update(@RequestBody MarginAccount account) {
        MarginAccount existAccount = marginAccountService.getById(account.getId());
        if (existAccount == null) {
            return Result.fail("账户不存在");
        }

        if (account.getMarginFirstBalance() != null) {
            account.setMarginFirstBalance(account.getMarginFirstBalance().multiply(new BigDecimal("10000")));
        }
        if (account.getMarginRemainBalance() != null) {
            account.setMarginRemainBalance(account.getMarginRemainBalance().multiply(new BigDecimal("10000")));
        }

        marginAccountService.updateById(account);

        if (account.getChannelList() != null) {
            marginAccountChannelService.remove(
                new LambdaQueryWrapper<MarginAccountChannel>()
                    .eq(MarginAccountChannel::getMarginAccountId, account.getId())
            );
            for (MarginAccountChannel channel : account.getChannelList()) {
                channel.setMarginAccountId(account.getId());
                marginAccountChannelService.save(channel);
            }
        }
        return Result.success(account);
    }

    /**
     * 审核保证金账户
     */
    @PostMapping("/approve")
    public Result<?> approve(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String status = params.get("status").toString();

        MarginAccount account = marginAccountService.getById(id);
        if (account == null) {
            return Result.fail("账户不存在");
        }

        if ("1".equals(status)) {
            account.setStatus("1");
        } else if ("2".equals(status)) {
            account.setStatus("4");
        }
        marginAccountService.updateById(account);
        return Result.success(account);
    }

    /**
     * 保证金充值
     */
    @PostMapping("/recharge")
    public Result<?> recharge(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long marginAccountId = Long.valueOf(params.get("marginAccountId").toString());
        BigDecimal amount = new BigDecimal(params.get("amount").toString()).multiply(new BigDecimal("10000"));

        MarginAccount account = marginAccountService.getById(marginAccountId);
        if (account == null) {
            return Result.fail("账户不存在");
        }

        BigDecimal newRechargeBalance = account.getRechargeBalance().add(amount);
        account.setRechargeBalance(newRechargeBalance);
        marginAccountService.updateById(account);
        return Result.success(account);
    }

    /**
     * 保证金退款
     */
    @PostMapping("/refund")
    public Result<?> refund(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long marginAccountId = Long.valueOf(params.get("marginAccountId").toString());
        BigDecimal amount = new BigDecimal(params.get("amount").toString()).multiply(new BigDecimal("10000"));

        MarginAccount account = marginAccountService.getById(marginAccountId);
        if (account == null) {
            return Result.fail("账户不存在");
        }

        BigDecimal newRefundedBalance = account.getMarginRefundedBalance().add(amount);
        account.setMarginRefundedBalance(newRefundedBalance);
        marginAccountService.updateById(account);
        return Result.success(account);
    }

    /**
     * 关闭保证金账户
     */
    @PostMapping("/close")
    public Result<?> close(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());

        MarginAccount account = marginAccountService.getById(id);
        if (account == null) {
            return Result.fail("账户不存在");
        }

        account.setStatus("4");
        marginAccountService.updateById(account);
        return Result.success(account);
    }

    /**
     * 获取渠道列表（用于选择）
     */
    @GetMapping("/channels")
    public Result<List<ChannelOrg>> getChannels() {
        return Result.success(channelOrgService.list());
    }

    private void calculateAccountFields(MarginAccount account) {
        BigDecimal totalBalance = account.getMarginFirstBalance().add(account.getRechargeBalance());

        BigDecimal kyBzjBalance = totalBalance
            .subtract(account.getMarginTransitUseBalance())
            .subtract(account.getMarginRefundedBalance());
        account.setKyBzjBalance(kyBzjBalance.divide(new BigDecimal("10000"), 4, RoundingMode.DOWN));

        BigDecimal zmbzjAmount = totalBalance.subtract(account.getMarginRefundedBalance());
        account.setZmbzjAmount(zmbzjAmount.divide(new BigDecimal("10000"), 4, RoundingMode.DOWN));

        BigDecimal dtBzjAmount = zmbzjAmount.subtract(account.getMarginTransitUseBalance());
        if (dtBzjAmount.compareTo(BigDecimal.ZERO) < 0) {
            dtBzjAmount = BigDecimal.ZERO;
        }
        account.setMarginPendingRefundBalance(dtBzjAmount.divide(new BigDecimal("10000"), 4, RoundingMode.DOWN));
        account.setSjktAmount(account.getMarginPendingRefundBalance());
        account.setMarginFinishAmount(BigDecimal.ZERO);
    }

    private String getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        return userId != null ? userId.toString() : "admin";
    }
}