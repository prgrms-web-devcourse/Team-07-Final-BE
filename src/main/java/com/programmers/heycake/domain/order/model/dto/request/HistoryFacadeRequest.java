package com.programmers.heycake.domain.order.model.dto.request;

import com.programmers.heycake.domain.order.model.entity.Order;

public record HistoryFacadeRequest(
		Long memberId,
		Long marketId,
		Order order
) {
}
