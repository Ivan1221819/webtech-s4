package webtech_semester_4.webtech.mealdbMerkliste;


import org.springframework.stereotype.Service;
import webtech_semester_4.webtech.MerklisteRepo.MerklisteRepository;

import java.util.List;

@Service
public class MerklisteService {

    private final MerklisteRepository repo;

    public MerklisteService(MerklisteRepository repo) {
        this.repo = repo;
    }

    public List<MerklisteItem> getAll() {
        return repo.findAll();
    }

    public MerklisteItem add(MerklisteItem item) {
        return repo.findByMealId(item.getMealId())
                .orElseGet(() -> repo.save(item));
    }

    public void removeByMealId(String mealId) {
        repo.deleteByMealId(mealId);
    }
}