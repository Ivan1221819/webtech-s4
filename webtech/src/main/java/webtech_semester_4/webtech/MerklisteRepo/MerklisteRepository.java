package webtech_semester_4.webtech.MerklisteRepo;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import webtech_semester_4.webtech.mealdbMerkliste.MerklisteItem;

import java.util.Optional;

public interface MerklisteRepository extends JpaRepository<MerklisteItem, Long> {

    Optional<MerklisteItem> findByMealId(String mealId);

    @Modifying
    @Transactional
    void deleteByMealId(String mealId);

    boolean existsByMealId(String mealId);
}