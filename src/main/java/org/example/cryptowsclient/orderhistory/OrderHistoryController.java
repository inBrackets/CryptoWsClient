package org.example.cryptowsclient.orderhistory;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.orderhistory.db.OrderHistoryEntity;
import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
public class OrderHistoryController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderHistoryService orderHistoryService;

    @GetMapping("/private/get-order-history")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    public ResponseEntity<List<OrderItemDto>> forwardRequest() {

        List<OrderItemDto> response = orderHistoryService.getOrderHistoryFromLast24Hours();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/private/get-order-history-total-value")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    public ResponseEntity<BigDecimal> forwardRequest(
            @RequestParam(value = "days_count", required = false, defaultValue = "1") long daysCount
    ) {
        BigDecimal total = orderHistoryService.getTotalOrderHistoryValueForTheLastDays(daysCount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(total);
    }
}
