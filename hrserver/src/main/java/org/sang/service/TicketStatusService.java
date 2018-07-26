package org.sang.service;

import org.sang.bean.TicketStatus;
import org.sang.mapper.TicketStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketStatusService {
    @Autowired
    TicketStatusMapper ticketStatusMapper;

    public List<TicketStatus> getStatusList() {
        return ticketStatusMapper.selectStatusList();
    }
}
