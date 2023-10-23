package pjoz.advert.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Integer> {
    Optional<Advert> findById(int id);
    Optional<List<Advert>> findAllByUserId(int id);
}
