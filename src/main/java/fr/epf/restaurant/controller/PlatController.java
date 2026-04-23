package fr.epf.restaurant.controller;

import fr.epf.restaurant.dao.PlatDao;
import fr.epf.restaurant.dto.PlatDetailDto;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.model.Plat;
import fr.epf.restaurant.service.ApiResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plats")
public class PlatController {

    private final PlatDao platDao;
    private final ApiResponseMapper apiResponseMapper;

    public PlatController(final PlatDao platDao, final ApiResponseMapper apiResponseMapper) {
        this.platDao = platDao;
        this.apiResponseMapper = apiResponseMapper;
    }

    /**
     * Retourne tous les plats.
     */
    @GetMapping
    public List<Plat> getAll() {
        return platDao.findAll();
    }

    /**
     * Retourne un plat avec ses ingrédients.
     */
    @GetMapping("/{id}")
    public PlatDetailDto getById(@PathVariable final Long id) {
        Plat plat = platDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Plat introuvable: " + id));
        return apiResponseMapper.toPlatDetail(plat);
    }

    /**
     * Crée un plat simple.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Plat create(@RequestBody final Plat plat) {
        return platDao.save(plat);
    }
}
