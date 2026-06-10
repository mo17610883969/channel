package com.channelmargin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.channelmargin.entity.MarginAccountOperationRecord;
import com.channelmargin.mapper.MarginAccountOperationRecordMapper;
import com.channelmargin.service.MarginAccountOperationRecordService;
import org.springframework.stereotype.Service;

@Service
public class MarginAccountOperationRecordServiceImpl extends ServiceImpl<MarginAccountOperationRecordMapper, MarginAccountOperationRecord> implements MarginAccountOperationRecordService {
}