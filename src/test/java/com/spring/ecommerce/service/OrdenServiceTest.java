package com.spring.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.repository.IOrdenRepository;

/**
 * Junit to test OrdenService.
 * Validate main order operations,
 */

@ExtendWith(MockitoExtension.class)
class OrdenServiceTest {

    @InjectMocks
    private OrdenServiceImpl ordenService; // create real  service

    @Mock
    private IOrdenRepository ordenRepository; // mock simule fake bbdd

    /**
     * Verify ssaving orders and details included correctly, repository works correctly.
     */
    @Test
    void shouldCreateOrdenWithDetails() {

        // create test 2 test detalles to test order and test ordenRepository.save
        Orden orden = new Orden();
        orden.setId(1);

        DetalleOrden detalle1 = new DetalleOrden();
        detalle1.setId(1);

        DetalleOrden detalle2 = new DetalleOrden();
        detalle2.setId(2);

        orden.setDetalle(List.of(detalle1, detalle2)); // adding list detalles to create an order

        // test ordenRepository.save method with any order
        when(ordenRepository.save(any(Orden.class)))
                .thenReturn(orden);

        // executes service
        Orden result = ordenService.save(orden);

        // Assert
        assertAll(
                () -> assertNotNull(result), // result is not null?
                () -> assertEquals(1, result.getId()), // result.id = 1?
                () -> assertEquals(2, result.getDetalle().size()) // result contains 2 detalles
        );

        // Verify times of interaction
        verify(ordenRepository, times(1)).save(any(Orden.class));
        verifyNoMoreInteractions(ordenRepository); // Has any other method been verified?
    }

    /**
     * Verify method to generate order code.
     * IMPORTANT darcodigoorden() not exist in repository, but it executes ordenRepository.findAll() so
     * you should configure this method in test
     */
    @Test
    void shouldDarCodigoOrden() {

        Orden orden = new Orden();
        orden.setNumero("00000000001");

        when(ordenRepository.findAll())
                .thenReturn(List.of(orden));

        String result = ordenService.darCodigoOrden();

        assertNotNull(result);
        assertEquals("0000000002", result);

        verify(ordenRepository, times(1)).findAll();
    }

}