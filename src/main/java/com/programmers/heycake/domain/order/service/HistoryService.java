package com.programmers.heycake.domain.order.service;

import static com.programmers.heycake.common.mapper.HistoryMapper.*;

import org.springframework.stereotype.Service;

import com.programmers.heycake.domain.order.model.entity.OrderHistory;
import com.programmers.heycake.domain.order.model.vo.request.HistoryFacadeRequest;
import com.programmers.heycake.domain.order.repository.HistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {
	private final HistoryRepository historyRepository;

	public Long createHistory(HistoryFacadeRequest historyRequest) {
		OrderHistory orderHistory = toOrderHistory(historyRequest);
		return historyRepository.save(orderHistory).getId();
	}
}
