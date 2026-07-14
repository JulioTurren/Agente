package com.gimnasio.service;

import com.gimnasio.model.Membresia;
import com.gimnasio.model.User;
import com.gimnasio.repository.MembresiaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class MembresiaService {

    private final MembresiaRepository membresiaRepository;

    public MembresiaService(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    public Optional<Membresia> findByUserId(Long userId) {
        return membresiaRepository.findByUserIdAndActivaTrue(userId);
    }

    public Membresia getOrCreateFree(Long userId, User user) {
        return membresiaRepository.findByUserIdAndActivaTrue(userId)
                .orElseGet(() -> {
                    Membresia free = new Membresia(Membresia.MembresiaType.FREE, user);
                    return membresiaRepository.save(free);
                });
    }

    @Transactional
    public Membresia suscribir(Long userId, User user, Membresia.MembresiaType tipo) {
        Optional<Membresia> actual = membresiaRepository.findByUserIdAndActivaTrue(userId);
        actual.ifPresent(m -> {
            m.setActiva(false);
            membresiaRepository.save(m);
        });

        Membresia nueva = new Membresia(tipo, user);
        return membresiaRepository.save(nueva);
    }

    public boolean tieneAccesoA(Membresia membresia, Membresia.MembresiaType requerido) {
        if (membresia == null) return requerido == Membresia.MembresiaType.FREE;
        return membresia.tieneAcceso(requerido);
    }

    public boolean puedeAccederCategoria(Membresia membresia, String categoria) {
        if (membresia == null) return false;
        return membresia.puedeAccederCategoria(categoria);
    }
}
