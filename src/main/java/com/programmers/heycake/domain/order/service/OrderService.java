package com.programmers.heycake.domain.order.service;

import static com.programmers.heycake.common.mapper.OrderMapper.*;
import static com.programmers.heycake.common.utils.JwtUtil.*;
import static com.programmers.heycake.domain.order.model.vo.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmers.heycake.common.exception.BusinessException;
import com.programmers.heycake.common.exception.ErrorCode;
import com.programmers.heycake.common.mapper.OrderMapper;
import com.programmers.heycake.domain.offer.service.OfferService;
import com.programmers.heycake.domain.order.model.dto.OrderCreateRequest;
import com.programmers.heycake.domain.order.model.dto.request.MyOrderRequest;
import com.programmers.heycake.domain.order.model.dto.response.MyOrderResponseList;
import com.programmers.heycake.domain.order.model.dto.response.OrderGetResponse;
import com.programmers.heycake.domain.order.model.dto.response.OrderGetSimpleServiceResponse;
import com.programmers.heycake.domain.order.model.entity.CakeInfo;
import com.programmers.heycake.domain.order.model.entity.Order;
import com.programmers.heycake.domain.order.model.vo.CakeCategory;
import com.programmers.heycake.domain.order.repository.OrderCustomRepository;
import com.programmers.heycake.domain.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderCustomRepository orderCustomRepository;

	private final OfferService offerService;

	@Transactional
	public Long create(OrderCreateRequest orderCreateRequest) {
		CakeInfo cakeInfo = CakeInfo.builder()
				.cakeCategory(orderCreateRequest.cakeCategory())
				.cakeSize(orderCreateRequest.cakeSize())
				.cakeHeight(orderCreateRequest.cakeHeight())
				.breadFlavor(orderCreateRequest.breadFlavor())
				.creamFlavor(orderCreateRequest.creamFlavor())
				.requirements(orderCreateRequest.requirements())
				.build();

		// try {
		Order savedOrder = orderRepository.save(
				Order.builder()
						.cakeInfo(cakeInfo)
						.hopePrice(orderCreateRequest.hopePrice())
						.memberId(1L) // TODO memberId 넣어주기
						.orderStatus(NEW)
						.visitDate(orderCreateRequest.visitTime())
						.title(orderCreateRequest.title())
						.region(orderCreateRequest.region())
						.build()
		);
		// } catch (Exception e) {
		// 	System.out.println("##### #######");
		// 	e.printStackTrace();
		// }
		return savedOrder.getId();
	}

	@Transactional(readOnly = true)
	public MyOrderResponseList getMyOrderList(MyOrderRequest getOrderRequest, Long memberId) {
		List<Order> orderList = orderCustomRepository.findAllByMemberIdOrderByVisitDateAsc(
				memberId,
				getOrderRequest.orderStatus(),
				getOrderRequest.cursorTime(),
				getOrderRequest.pageSize()
		);

		LocalDateTime lastTime =
				orderList.size() == 0 ? LocalDateTime.MAX : orderList.get(orderList.size() - 1).getVisitDate();

		return toGetOrderResponseListForMember(orderList, lastTime);
	}

	@Transactional
	public OrderGetResponse getOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(EntityNotFoundException::new);
		return OrderMapper.toOrderGetResponse(order);
	}

	public List<OrderGetSimpleServiceResponse> getOrders(
			Long cursorId, int pageSize, CakeCategory cakeCategory, String region
	) {
		return orderCustomRepository
				.findAllByRegionAndCategoryOrderByCreatedAtAsc(cursorId, pageSize, cakeCategory, region)
				.stream()
				.map(OrderMapper::toOrderSimpleGetServiceResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public void deleteOrder(Long orderId) {
		if (!Objects.equals(getEntityById(orderId).getMemberId(), getMemberId())) {
			throw new BusinessException(ErrorCode.FORBIDDEN);
		}
		if (!isNewOrder(orderId)) {
			// throw new BusinessException(ErrorCode.DELETE_ERROR);
			throw new RuntimeException("");
		}
		orderRepository.deleteById(orderId);
	}

	private Order getEntityById(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}

	private boolean isNewOrder(Long orderId) {
		return getEntityById(orderId)
				.getOrderStatus()
				.equals(NEW);
	}

}
