package com.example.weddingSitter.Controller;

import com.example.weddingSitter.entities.EventTable;
import com.example.weddingSitter.entities.Guest;
import com.example.weddingSitter.entities.Wedding;
import com.example.weddingSitter.services.EventTableRepository;
import com.example.weddingSitter.services.GuestRepository;
import com.example.weddingSitter.services.WeddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private EventTableRepository tableRepository;

    @Autowired
    private WeddingRepository weddingRepository;

    @Autowired
    private TableController tableController;

    // GET all guests by table ID
    @GetMapping("/table-id/{id}")
    public List<Guest> getAllGuestsByTableId(@PathVariable Integer id) {
        return guestRepository.findAllByTableId(id);
    }

    // GET a single guest by ID
    @GetMapping("/{id}")
    public Optional<Guest> getGuestById(@PathVariable Integer id) {
        return guestRepository.findById(id);
    }

    @GetMapping("exists/{weddingName}/{guestName}")
    public Boolean isGuestInWedding(Principal principal, @PathVariable String weddingName, @PathVariable String guestName){
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        List<EventTable> weddingsTables = wedding.getTables();
        for (EventTable table: weddingsTables){
            Collection<String> guestNames = table.getGuests().stream().map(g -> g.getName()).collect(Collectors.toList());
            if (guestNames.contains(guestName)) {
                return true;
            }
        }
        return false;
    }
    // CREATE a new guest
    @PostMapping("/create/{weddingName}/{tableName}/{guestName}")
    public Guest createGuest(Principal principal, @PathVariable String weddingName, @PathVariable String tableName, @PathVariable String guestName) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        EventTable table = tableRepository.findById(wedding.getId()).get();

        Guest guest = new Guest();
        guest.setName(guestName);
        guest.setTable(table);
        return guestRepository.save(guest);
    }

    // UPDATE a guest by ID
    @PutMapping("/switch-table/{guestId}/{tableId}")
    public Guest updateGuest(@PathVariable Integer guestId, @PathVariable Long tableId) {
        Guest existingGuest = getGuestById(guestId).orElseThrow(() -> new RuntimeException("Guest not found"));
        EventTable table = tableRepository.findById(tableId).get();
        existingGuest.setTable(table);
        return guestRepository.save(existingGuest);
    }

    @PutMapping("/change-name/{weddingName}/{tableName}/{guestName}/{newName}")
    public Guest updateGuestName(Principal principal, @PathVariable String weddingName, @PathVariable String tableName,@PathVariable String guestName, @PathVariable String newName) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        EventTable table = tableRepository.findById(wedding.getId()).get();

        Guest existingGuest = guestRepository.findByTable_IdAndName(table.getId(), guestName).orElseThrow(() -> new RuntimeException("Guest not found"));
        existingGuest.setName(newName);
        return guestRepository.save(existingGuest);
    }

    // DELETE a guest by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGuest(@PathVariable Long id) {
        guestRepository.deleteById(id);
    }
}
