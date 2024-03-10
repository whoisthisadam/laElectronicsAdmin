package com.kasperovich.laelectronics.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasperovich.laelectronics.api.controller.exceptionHandler.DefaultExceptionHandler;
import com.kasperovich.laelectronics.api.dto.payment.PaymentCreateDto;
import com.kasperovich.laelectronics.api.dto.product.DeleteProductDto;
import com.kasperovich.laelectronics.api.dto.product.SubscriptionCreateDto;
import com.kasperovich.laelectronics.api.dto.product.SubscriptionGetDto;
import com.kasperovich.laelectronics.api.mapping.converters.product.SubscriptionUpdateConverter;
import com.kasperovich.laelectronics.api.mapping.mappers.SubscriptionListMapper;
import com.kasperovich.laelectronics.api.mapping.mappers.SubscriptionMapper;
import com.kasperovich.laelectronics.enums.PaymentProviders;
import com.kasperovich.laelectronics.enums.PaymentStatus;
import com.kasperovich.laelectronics.models.*;
import com.kasperovich.laelectronics.repository.*;
import com.kasperovich.laelectronics.service.subscription.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class SubscriptionsControllerTest {

    final String testUserName = "TestUserName";

    @InjectMocks
    SubscriptionsController subscriptionsController;
    @Mock
    SubscriptionService subscriptionService;
    @Spy
    SubscriptionMapper subscriptionMapper = new SubscriptionMapper() {
        @Override
        public Subscription toEntity(SubscriptionCreateDto subscriptionCreateDto) {
            if (subscriptionCreateDto == null) {
                return null;
            }

            Subscription subscription = new Subscription();

            subscription.setName(subscriptionCreateDto.getName());
            subscription.setPrice(subscriptionCreateDto.getPrice());
            subscription.setIsAvailable(subscriptionCreateDto.getIsAvailable());

            return subscription;
        }

        @Override
        public SubscriptionGetDto toDto(Subscription product) {
            if (product == null) {
                return null;
            }

            SubscriptionGetDto subscriptionGetDto = new SubscriptionGetDto();

            subscriptionGetDto.setId(product.getId());
            subscriptionGetDto.setName(product.getName());
            subscriptionGetDto.setPrice(product.getPrice());

            return subscriptionGetDto;
        }
    };
    @Spy
    SubscriptionListMapper subscriptionListMapper = new SubscriptionListMapper() {

        @Override
        public List<Subscription> toEntityList(List<SubscriptionCreateDto> dtos) {
            if (dtos == null) {
                return null;
            }

            List<Subscription> list = new ArrayList<Subscription>(dtos.size());
            for (SubscriptionCreateDto subscriptionCreateDto : dtos) {
                list.add(subscriptionMapper.toEntity(subscriptionCreateDto));
            }

            return list;
        }

        @Override
        public List<SubscriptionGetDto> toDto(List<Subscription> products) {
            if (products == null) {
                return null;
            }

            List<SubscriptionGetDto> list = new ArrayList<SubscriptionGetDto>(products.size());
            for (Subscription subscription : products) {
                list.add(subscriptionMapper.toDto(subscription));
            }

            return list;
        }
    };
    @Mock
    SubscriptionUpdateConverter subscriptionUpdateConverter;
    @Mock
    SubscriptionsRepository subscriptionsRepository;
    @Mock
    OrganizationRepository organizationRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    PaymentRepository paymentRepository;

    @Captor
    ArgumentCaptor<Organization> orgCaptor;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @Captor
    ArgumentCaptor<Payment> paymentCaptor;

    MockMvc mockMvc;


    JacksonTester<SubscriptionCreateDto> jsonCreateSubRequest;
    JacksonTester<SubscriptionGetDto> jsonGetSubResponse;
    JacksonTester<Map<String, SubscriptionGetDto>> jsonGetSubMapResponse;
    JacksonTester<List<SubscriptionGetDto>> jsonGetSubListResponse;
    JacksonTester<DeleteProductDto> jsonDeleteSubResponse;
    JacksonTester<PaymentCreateDto> jsonPaymentCreateRequest;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders
                .standaloneSetup(subscriptionsController)
                .setControllerAdvice(new DefaultExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnAllSubs() {

        when(subscriptionService.findAll()).thenReturn(List.of(Subscription
                .builder()
                .name("TestSub")
                .orders(Set.of(
                        Order
                                .builder()
                                .id(1L)
                                .user(User
                                        .builder()
                                        .credentials(Credentials
                                                .builder()
                                                .login(testUserName + "CHANGED")
                                                .build())
                                        .build())
                                .build()))
                .organization(Organization
                        .builder()
                        .name("testManufacturerName")
                        .build())
                .price(100L)
                .subDiscount(Discount.builder().discountPercent(55).build())
                .build()));
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(testUserName);

        ResponseEntity<List<SubscriptionGetDto>> actual = subscriptionsController.findAll(authentication);
        List<SubscriptionGetDto> expected = List.of(
                new SubscriptionGetDto(null, "TestSub", "testManufacturerName", 45L)
        );

        assertThat(actual.getBody()).isEqualTo(expected);

    }

    @Test
    void shouldCreateProduct() throws Exception {
        Subscription testSub = Subscription
                .builder()
                .id(1L)
                .name("TestSub")
                .price(100L)
                .organization(Organization
                        .builder()
                        .name("testManufacturerName")
                        .build())
                .subDiscount(Discount.builder().discountPercent(55).build())
                .build();
        when(organizationRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(subscriptionService.createProduct(any())).thenReturn(testSub);

        SubscriptionCreateDto request = new SubscriptionCreateDto("TestSub", 100L, true, "testManufacturerName");
        SubscriptionGetDto expectedResponse = new SubscriptionGetDto(1L, "TestSub", "testManufacturerName", 100L);
        String expected = jsonGetSubMapResponse.write(Collections.singletonMap("New product:", expectedResponse)).getJson();
        String actual = mockMvc.perform(
                MockMvcRequestBuilders.post("/data/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateSubRequest.write(request).getJson())
        ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expected);

        verify(organizationRepository, times(1)).save(orgCaptor.capture());

        Organization expectedOrgToSave = Organization.builder().name("testManufacturerName").build();

        assertThat(orgCaptor.getValue()).isEqualTo(expectedOrgToSave);
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Subscription subscription = Subscription
                .builder()
                .price(100L)
                .name("TestSub")
                .build();
        when(subscriptionUpdateConverter.doConvert(any(), anyLong())).thenReturn(subscription);
        when(subscriptionService.updateProduct(any())).thenReturn(subscription);
        SubscriptionGetDto subscriptionGetDto = new SubscriptionGetDto(1L, "TestSub", "testManufacturerName", 100L);
        when(subscriptionMapper.toDto(any())).thenReturn(subscriptionGetDto);

        String actual = mockMvc.perform(
                MockMvcRequestBuilders.patch("/data/products/update?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateSubRequest.write(new SubscriptionCreateDto()).getJson())
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String expected = jsonGetSubMapResponse.write(Collections.singletonMap("Updated product:", subscriptionGetDto)).getJson();

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldReturnDatedSubs() throws Exception {
        Timestamp from = new Timestamp(4);
        Timestamp to = new Timestamp(10);
        Subscription subscription = Subscription
                .builder()
                .name("TestSub")
                .price(100L)
                .organization(Organization
                        .builder()
                        .name("testManufacturerName")
                        .build())
                .editData(Edit.builder().creationDate(new Timestamp(5)).build())
                .build();
        List<Subscription> allSubs = List.of(Subscription.builder().editData(Edit.builder().creationDate(new Timestamp(3)).build()).build(), subscription);
        when(subscriptionService.findAll()).thenReturn(allSubs);
        String actual = mockMvc.perform(
                MockMvcRequestBuilders.get("/data/products/dated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", from.toString())
                        .param("to", to.toString())
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String expected = jsonGetSubListResponse.write(List.of(new SubscriptionGetDto(null, "TestSub", "testManufacturerName", 100L))).getJson();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldDeleteSub() throws Exception {
        when(subscriptionService.deleteProduct(any())).thenReturn(Subscription.builder().isDeleted(true).build());
        DeleteProductDto response = DeleteProductDto.builder().id(1L).status(DeleteProductDto.DeletedStatus.DELETED).build();
        String actual = mockMvc.perform(
                MockMvcRequestBuilders.patch("/data/products/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ID", "1")
        ).andExpect(status().isNoContent()).andReturn().getResponse().getContentAsString();
        String expected = jsonDeleteSubResponse.write(response).getJson();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void shouldReturnSubById() throws Exception {
        when(subscriptionsRepository.findProductByIdAndIsDeleted(anyLong(), eq(false))).
                thenReturn(Optional.of(
                        Subscription
                                .builder()
                                .id(1L)
                                .name("TestSub")
                                .organization(Organization.builder().name("TestManufacturer").build())
                                .price(100L)
                                .build()));
        String actual = mockMvc.perform(
                MockMvcRequestBuilders.get("/data/products/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ID", "1")
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String expected = jsonGetSubResponse
                .write(
                        new SubscriptionGetDto(1L, "TestSub", "TestManufacturer", 100L)
                )
                .getJson();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void shouldFailIfSubNotFound() throws Exception {
        when(subscriptionsRepository.findProductByIdAndIsDeleted(anyLong(), eq(false))).
                thenReturn(Optional.empty());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/data/products/product")
                                .param("ID", "1")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Product with this ID does not exist", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void shouldFailIfSubNotFoundAddToAUser() throws Exception {
        when(subscriptionsRepository.findProductByIdAndIsDeleted(anyLong(), eq(false))).
                thenReturn(Optional.empty());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/data/products/user")
                                .param("subID", "1")
                                .param("userLogin", "testLogin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPaymentCreateRequest.write(new PaymentCreateDto()).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Product with this ID does not exist", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void shouldFailIfUserNotFoundAddToAUser() throws Exception {
        when(subscriptionsRepository.findProductByIdAndIsDeleted(anyLong(), eq(false))).
                thenReturn(Optional.of(
                        Subscription
                                .builder()
                                .id(1L)
                                .name("TestSub")
                                .organization(Organization.builder().name("TestManufacturer").build())
                                .price(100L)
                                .build()));
        when(userRepository.findByCredentialsLoginAndIsDeleted(anyString(), eq(false))).
                thenReturn(Optional.empty());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/data/products/user")
                                .param("subID", "1")
                                .param("userLogin", "testLogin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPaymentCreateRequest.write(new PaymentCreateDto()).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("User with this login does not exist", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void shouldAddSubToAUser() throws Exception {
        Subscription subscription = Subscription
                .builder()
                .id(1L)
                .name("TestSub")
                .organization(Organization.builder().name("TestManufacturer").build())
                .price(100L)
                .build();
        User user = User
                .builder()
                .email("testuser@gmail.com")
                .build();
        when(subscriptionsRepository.findProductByIdAndIsDeleted(anyLong(), eq(false)))
                .thenReturn(Optional.of(subscription));
        when(userRepository.findByCredentialsLoginAndIsDeleted(anyString(), eq(false)))
                .thenReturn(Optional.of(user));
        String actual = mockMvc.perform(
                        MockMvcRequestBuilders.post("/data/products/user")
                                .param("subID", "1")
                                .param("userLogin", "testLogin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPaymentCreateRequest.write(new PaymentCreateDto(PaymentProviders.BANK_CARD, 150L)).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String expected = jsonGetSubResponse.write(new SubscriptionGetDto(1L, "TestSub", "TestManufacturer", 100L)).getJson();
        assertThat(expected).isEqualTo(actual);

        Order expectedOrderToSave = Order.builder().user(user).subscription(subscription).build();
        Payment payment = Payment
                .builder()
                .order(expectedOrderToSave)
                .amount(150L)
                .provider(PaymentProviders.BANK_CARD)
                .editData(Edit.builder().build())
                .status(PaymentStatus.PAID)
                .build();

        verify(orderRepository, times(1)).save(orderCaptor.capture());
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        assertThat(orderCaptor.getValue()).isEqualTo(expectedOrderToSave);
        assertThat(paymentCaptor.getValue()).usingRecursiveComparison().ignoringFieldsOfTypes(Timestamp.class).isEqualTo(payment);
    }

    @Test
    void shouldReturnSubsByUser() throws Exception {
        when(userRepository.findByCredentialsLoginAndIsDeleted(anyString(), eq(false)))
                .thenReturn(Optional.of(
                        User
                                .builder()
                                .orders(Set.of(
                                        Order
                                                .builder()
                                                .subscription(Subscription
                                                        .builder()
                                                        .id(1L)
                                                        .name("TestSub")
                                                        .organization(
                                                                Organization
                                                                        .builder()
                                                                        .name("TestOrgName")
                                                                        .build())
                                                        .price(100L)
                                                        .build())
                                                .build()))
                                .build()));

        String actual = mockMvc.perform(
                        MockMvcRequestBuilders.get("/data/products/subscriptions")
                                .param("userName", testUserName)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String expected = jsonGetSubListResponse.write(List.of(new SubscriptionGetDto(1L, "TestSub", "TestOrgName", 100L))).getJson();

        assertThat(expected).isEqualTo(actual);

    }

    @Test
    void shouldFailIfUserNotFound() throws Exception {
        when(userRepository.findByCredentialsLoginAndIsDeleted(anyString(), eq(false))).
                thenReturn(Optional.empty());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/data/products/subscriptions")
                                .param("userName", testUserName)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPaymentCreateRequest.write(new PaymentCreateDto()).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("User with this login does not exist", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @Test
    @WithMockUser(username = testUserName)
    void shouldRemoveUserSub() throws Exception {

        Payment expectedPaymentToSave = Payment
                .builder()
                .amount(100L)
                .status(PaymentStatus.PAID)
                .provider(PaymentProviders.BANK_CARD)
                .order(null)
                .build();

        UserDetails userDetails = mock(UserDetails.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(testUserName);

        when(userRepository.findByCredentialsLoginAndIsDeleted(anyString(), eq(false)))
                .thenReturn(Optional.of(
                        User
                                .builder()
                                .orders(Set.of(
                                        Order
                                                .builder()
                                                .id(2L)
                                                .subscription(Subscription
                                                        .builder()
                                                        .id(1L)
                                                        .name("TestSub")
                                                        .price(100L)
                                                        .build())
                                                .payment(Payment
                                                        .builder()
                                                        .amount(100L)
                                                        .status(PaymentStatus.PAID)
                                                        .provider(PaymentProviders.BANK_CARD)
                                                        .build())
                                                .build()))
                                .build()));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/data/products/subscriptions/remove")
                                .param("subId", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        verify(orderRepository, times(1)).deleteById(2L);

        assertThat(paymentCaptor.getValue()).usingRecursiveComparison().ignoringFieldsOfTypes(Timestamp.class).isEqualTo(expectedPaymentToSave);

    }

    @Test
    void shouldFailIfSubDoesNotExist() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(testUserName);

        when(userRepository.findByCredentialsLoginAndIsDeleted(anyString(), eq(false)))
                .thenReturn(Optional.of(
                        User
                                .builder()
                                .orders(Set.of(
                                        Order
                                                .builder()
                                                .id(2L)
                                                .subscription(Subscription
                                                        .builder()
                                                        .id(3L)
                                                        .name("TestSub")
                                                        .price(100L)
                                                        .build())
                                                .build()))
                                .build()));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/data/products/subscriptions/remove")
                                .param("subId", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Order with this ID does not exist", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void shouldFailIfUserNotFoundRemoveSub() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(testUserName);

        when(userRepository.findByCredentialsLoginAndIsDeleted(anyString(), eq(false)))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/data/products/subscriptions/remove")
                                .param("subId", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("User with this login does not exist", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


}