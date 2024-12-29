package com.example.weddingSitter.Controller;

import com.example.weddingSitter.entities.EventTable;
import com.example.weddingSitter.entities.Seat;
import com.example.weddingSitter.entities.Wedding;
import com.example.weddingSitter.services.EventTableRepository;
import com.example.weddingSitter.services.GuestRepository;
import com.example.weddingSitter.services.SeatRepository;
import com.example.weddingSitter.services.WeddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private EventTableRepository tableRepository;

    @Autowired
    private WeddingRepository weddingRepository;

    @Autowired
    private TableController tableController;

    // GET all guests by table ID
    @GetMapping("/table/{table-id}")
    public List<Seat> getAllSeatsByTableId(@PathVariable Long tableId) {
        return seatRepository.findAllByTableId(tableId);
    }

    // GET a single guest by ID
    @GetMapping("/{id}")
    public Optional<Seat> getSeatsById(@PathVariable Integer id) {
        return seatRepository.findById(id);
    }

    @GetMapping("/{weddingName}/{tableName}/{seatNum}")
    public Optional<Seat> getSeatByWeddingTableName(Principal principal, @PathVariable String weddingName, @PathVariable String tableName, @PathVariable Long seatNum) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        EventTable table = tableRepository.findByWedding_IdAndName(wedding.getId(), tableName).get();
        return seatRepository.findByTable_IdAndNum(table.getId(), seatNum);
    }

    @GetMapping("all/{username}/{weddingName}")
    public Collection<Seat> getSeatsByWedding(Principal principal, @PathVariable String username, @PathVariable String weddingName) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(username, weddingName).get();
        List<EventTable> tables = tableRepository.findAllByWeddingId(wedding.getId());
        Collection<Seat> seats = tables.stream().flatMap(t -> seatRepository.findAllByTableId(t.getId()).stream()).collect(Collectors.toList());
        for (Seat seat: seats){
            seat.setTableRef(seat.getTable().getName());
        }
        return seats;
    }

    // CREATE a new seat
    @PostMapping("/{weddingName}/{tableName}/{seatNum}/{guestName}")
    public Seat createSeat(Principal principal, @PathVariable String weddingName, @PathVariable String tableName, @PathVariable Long seatNum, @PathVariable String guestName) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        EventTable table = tableRepository.findByWedding_IdAndName(wedding.getId(), tableName).get();

        Seat seat = new Seat();
        seat.setGuestName(guestName);
        seat.setSeatNum(seatNum);
        seat.setTable(table);
        return seatRepository.save(seat);
    }

    @PostMapping("/{weddingName}/{tableName}/{seatNum}")
    public Seat createSeat(Principal principal, @PathVariable String weddingName, @PathVariable String tableName, @PathVariable Long seatNum) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        EventTable table = tableRepository.findByWedding_IdAndName(wedding.getId(), tableName).get();

        Seat seat = new Seat();
        seat.setSeatNum(seatNum);
        seat.setTable(table);
        return seatRepository.save(seat);
    }

    // UPDATE a guest by ID
    @PutMapping("/{weddingName}/{tableName}/{seatNum}/{guestName}")
    public Seat updateSeat(Principal principal, @PathVariable String weddingName, @PathVariable String tableName, @PathVariable Long seatNum, @PathVariable String guestName) {

        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        EventTable table = tableRepository.findByWedding_IdAndName(wedding.getId(), tableName).get();
        Seat existingSeat = seatRepository.findByTable_IdAndNum(table.getId(), seatNum).get();
        existingSeat.setGuestName(guestName);
        return seatRepository.save(existingSeat);
    }

    @DeleteMapping("/{weddingName}/{tableName}/{seatNum}/{guestName}")
    public void deleteSeat(Principal principal, @PathVariable String weddingName, @PathVariable String tableName, @PathVariable Long seatNum, @PathVariable String guestName) {

        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        EventTable table = tableRepository.findByWedding_IdAndName(wedding.getId(), tableName).get();
        Seat existingSeat = seatRepository.findByTable_IdAndNum(table.getId(), seatNum).get();
        deleteSeat(existingSeat.getId());
    }

    public void deleteSeat(@PathVariable Long id) {
        guestRepository.deleteById(id);
    }
}
