package com.kasperovich.laelectronics.api.controller;

import com.kasperovich.laelectronics.api.dto.order.ApproveOrderResponse;
import com.kasperovich.laelectronics.api.dto.order.OrderCreateDto;
import com.kasperovich.laelectronics.api.dto.order.OrderGetDto;
import com.kasperovich.laelectronics.api.dto.order.OrderUpdateDto;
import com.kasperovich.laelectronics.api.mapping.converters.order.OrderCreateConverter;
import com.kasperovich.laelectronics.api.mapping.converters.order.OrderGetConverter;
import com.kasperovich.laelectronics.api.mapping.converters.order.OrderUpdateConverter;
import com.kasperovich.laelectronics.enums.OrderStatus;
import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.exception.PreconditionException;
import com.kasperovich.laelectronics.models.Order;
import com.kasperovich.laelectronics.repository.OrderRepository;
import com.kasperovich.laelectronics.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
@Slf4j
@RequestMapping("data/orders")
@RequiredArgsConstructor
@Tag(name = "Orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class OrderController {

    OrderService orderService;

    OrderCreateConverter orderCreateConverter;

    OrderGetConverter orderGetConverter;

    OrderUpdateConverter orderUpdateConverter;

    OrderRepository orderRepository;


    @Operation(
            summary = "Create order",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
    )
    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, OrderGetDto>> create(@RequestBody OrderCreateDto orderCreateDto) throws PreconditionException {
        Order order = orderCreateConverter.convert(orderCreateDto);
        orderService.createOrder(order);
        return new ResponseEntity<>(Collections.singletonMap("New order:", orderGetConverter.convert(order)), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Find all orders(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderGetDto>> findAll() {
        List<OrderGetDto> list = orderService.findAll().stream().map(orderGetConverter::convert).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Find all orders by date(Admin&Moderator only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/dated")
    public ResponseEntity<List<OrderGetDto>> findAllDated(@RequestParam Timestamp from, @RequestParam Timestamp to) {
        List<OrderGetDto> list = orderRepository
                .findAll()
                .stream()
                .filter(order ->
                        order.getEditData().getCreationDate().after(from)&&order.getEditData().getCreationDate().before(to))
                .map(orderGetConverter::convert).toList();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Find all pending orders(Admin&Moderator only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/pending")
    public ResponseEntity<List<OrderGetDto>> findAllPending() {
        List<OrderGetDto> list = orderRepository.findAllByOrderStatus(OrderStatus.IN_PROGRESS).stream().map(orderGetConverter::convert).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Find order by id(Admin&Moderator only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderGetDto> findById(@PathVariable String id) {
        Order order = orderService.findById(id);
        OrderGetDto orderGetDto = orderGetConverter.convert(order);
        return ResponseEntity.ok(orderGetDto);
    }

    @Operation(
            summary = "Update order(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderGetDto.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PatchMapping("/update")
    public ResponseEntity<Map<String, OrderGetDto>> updateOrder(@RequestBody OrderUpdateDto orderUpdateDto,
                                                                @RequestParam String id) throws Exception {
        Order order = orderUpdateConverter.doConvert(orderUpdateDto, Long.parseLong(id));
        orderService.updateOrder(order);
        return ResponseEntity.ok(Collections.singletonMap("Updated order:", orderGetConverter.convert(order)));
    }

    @Operation(
            summary = "Delete order(Admin only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = String.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/delete")
    public ResponseEntity<String> deleteOrder(@RequestParam String Id) throws NotDeletableStatusException {
        orderService.deleteOrder(Long.parseLong(Id));
        return ResponseEntity.ok("Order with ID " + Id + " deleted");
    }


    @Operation(
            summary = "Approve order(Admin&Moderator only)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Approved",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = String.class)))
                            })
            }
            , parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "X-Auth-Token",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PatchMapping("/approve/{id}")
    public ResponseEntity<ApproveOrderResponse> approveOrder(@PathVariable String id, @RequestParam(required = false, defaultValue = "")String discount) throws PreconditionException {
        orderService.approveOrder(id, discount);
        return ResponseEntity.ok(ApproveOrderResponse.builder().status("SUCCESS").id(id).build());
    }


}
