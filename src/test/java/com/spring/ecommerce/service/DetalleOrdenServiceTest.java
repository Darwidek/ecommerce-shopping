package com.spring.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.repository.IDetalleOrdenRepository;

/**
 * Verify ssaving orderdetails  correctly, repository works correctly.
 */
@ExtendWith(MockitoExtension.class)
class DetalleOrdenServiceTest {

    @InjectMocks
    private DetalleOrdenServiceImpl detalleOrdenService; // create real  service

    @Mock
    private IDetalleOrdenRepository detalleOrdenRepository; // mock simule fake bbdd

    @Test
    void shouldSaveDetalleOrden() {

        // create test detalle with id 1 and test detalleOrdenRepository.save
        DetalleOrden detalle = new DetalleOrden();
        detalle.setId(1);

        when(detalleOrdenRepository.save(detalle))
                .thenReturn(detalle);

        // executes service
        DetalleOrden result = detalleOrdenService.save(detalle);

        // verify if result is not null with id = 1
        assertNotNull(result);
        assertEquals(1, result.getId());

        // Verify times of interaction
        verify(detalleOrdenRepository, times(1)).save(detalle);
    }

}