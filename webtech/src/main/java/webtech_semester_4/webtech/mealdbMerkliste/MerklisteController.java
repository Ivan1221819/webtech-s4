package webtech_semester_4.webtech.mealdbMerkliste;


import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/merkliste")
@CrossOrigin(origins = {"http://localhost:5173", "https://webtech-s4-1.onrender.com"})
public class MerklisteController {

    private final MerklisteService service;

    public MerklisteController(MerklisteService service) {
        this.service = service;
    }

    @GetMapping
    public List<MerklisteItem> getAll() {
        return service.getAll();
    }

    @PostMapping
    public MerklisteItem add(@RequestBody MerklisteItem item) {
        return service.add(item);
    }

    @DeleteMapping("/{mealId}")
    public void remove(@PathVariable String mealId) {
        service.removeByMealId(mealId);
    }
}