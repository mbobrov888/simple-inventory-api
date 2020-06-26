package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inventory.domain.InventoryItem;
import com.inventory.dto.InventoryItemDto;
import com.inventory.exception.InventoryItemBadRequestException;
import com.inventory.exception.InventoryItemExistsException;
import com.inventory.exception.InventoryItemNotFoundException;
import com.inventory.security.InventoryAuthenticationEntryPoint;
import com.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static com.inventory.Constants.*;
import static com.inventory.utils.TestUtils.createInventoryItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService service;
    private static ObjectMapper mapper;

    @BeforeAll
    static void init() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testGetInventoryItem_success() throws Exception {
        InventoryItem item = createInventoryItem();
        String uuid = item.getId().toString();
        InventoryItemDto dto = new InventoryItemDto(item);
        when(service.getItem(eq(uuid))).thenReturn(dto);

        mockMvc.perform(get(API_PATH_INVENTORY_BY_ID, uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(uuid))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.manufacturer.name").value(dto.getManufacturer().getName()));
    }

    @Test
    void testGetInventoryItem_notFound() throws Exception {
        when(service.getItem(anyString())).thenThrow(new InventoryItemNotFoundException(""));

        mockMvc.perform(get(API_PATH_INVENTORY_BY_ID, UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetInventoryItems_success() throws Exception {
        int offset = 60, limit = 20;
        InventoryItemDto dto1 = new InventoryItemDto(createInventoryItem());
        InventoryItemDto dto2 = new InventoryItemDto(createInventoryItem());
        when(service.findItems(eq(offset), eq(limit))).thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get(API_PATH_INVENTORY)
                .param(API_QUERY_PARAM_OFFSET, String.valueOf(offset))
                .param(API_QUERY_PARAM_LIMIT, String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(containsInAnyOrder(dto1.getId().toString(), dto2.getId().toString())))
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder(dto1.getName(), dto2.getName())))
                .andExpect(jsonPath("$[*].manufacturer.name")
                        .value(containsInAnyOrder(dto1.getManufacturer().getName(), dto2.getManufacturer().getName())));
    }

    @Test
    void testGetInventoryItems_badRequest() throws Exception {
        mockMvc.perform(get(API_PATH_INVENTORY)
                .param(API_QUERY_PARAM_OFFSET, String.valueOf(20))
                .param(API_QUERY_PARAM_LIMIT, String.valueOf(100)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryItem_success() throws Exception {
        InventoryItemDto dto = new InventoryItemDto(createInventoryItem());
        InventoryItemDto result = new InventoryItemDto(createInventoryItem());
        when(service.createItem(any(InventoryItemDto.class))).thenReturn(result);

        mockMvc.perform(post(API_PATH_INVENTORY)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(result.getId().toString()))
                .andExpect(jsonPath("$.name").value(result.getName()))
                .andExpect(jsonPath("$.manufacturer.name").value(result.getManufacturer().getName()));
    }

    @Test
    void testCreateInventoryItem_badRequestJSR303ValidationEmptyName() throws Exception {
        InventoryItem item = createInventoryItem();
        item.setName("");
        InventoryItemDto dto = new InventoryItemDto(item);
        InventoryItemDto result = new InventoryItemDto(createInventoryItem());
        when(service.createItem(any(InventoryItemDto.class))).thenReturn(result);

        mockMvc.perform(post(API_PATH_INVENTORY)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryItem_badRequestJSR303ValidationReleaseDateInFuture() throws Exception {
        InventoryItem item = createInventoryItem();
        item.setReleaseDate(LocalDateTime.now().plusDays(10));
        InventoryItemDto dto = new InventoryItemDto(item);
        InventoryItemDto result = new InventoryItemDto(createInventoryItem());
        when(service.createItem(any(InventoryItemDto.class))).thenReturn(result);

        mockMvc.perform(post(API_PATH_INVENTORY)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryItem_badRequest() throws Exception {
        when(service.createItem(any(InventoryItemDto.class))).thenThrow(new InventoryItemBadRequestException(""));

        mockMvc.perform(post(API_PATH_INVENTORY)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new InventoryItemDto(createInventoryItem()))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInventoryItem_itemAlreadyExists() throws Exception {
        when(service.createItem(any(InventoryItemDto.class))).thenThrow(new InventoryItemExistsException(""));

        mockMvc.perform(post(API_PATH_INVENTORY)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new InventoryItemDto(createInventoryItem()))))
                .andExpect(status().isConflict());
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        public InventoryAuthenticationEntryPoint authenticationEntryPoint() {
            return new InventoryAuthenticationEntryPoint();
        }
    }
}
