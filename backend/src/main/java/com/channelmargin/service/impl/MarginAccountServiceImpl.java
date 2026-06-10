package com.channelmargin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.channelmargin.entity.MarginAccount;
import com.channelmargin.mapper.MarginAccountMapper;
import com.channelmargin.service.MarginAccountService;
import org.springframework.stereotype.Service;

@Service
public class MarginAccountServiceImpl extends ServiceImpl<MarginAccountMapper, MarginAccount> implements MarginAccountService {
}