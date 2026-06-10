package com.channelmargin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.channelmargin.entity.MarginAccountChannel;
import com.channelmargin.mapper.MarginAccountChannelMapper;
import com.channelmargin.service.MarginAccountChannelService;
import org.springframework.stereotype.Service;

@Service
public class MarginAccountChannelServiceImpl extends ServiceImpl<MarginAccountChannelMapper, MarginAccountChannel> implements MarginAccountChannelService {
}