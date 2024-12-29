package com.example.weddingSitter.Controller;

import com.example.weddingSitter.entities.EventTable;
import com.example.weddingSitter.entities.Wedding;
import com.example.weddingSitter.services.EventTableRepository;
import com.example.weddingSitter.services.WeddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private EventTableRepository tableRepository;

    @Autowired
    private WeddingRepository weddingRepository;

    // GET all tables by wedding ID
    @GetMapping("/wedding-id/{id}")
    public List<EventTable> getAllTablesByWeddingId(@PathVariable Long id) {
        return tableRepository.findAllByWeddingId(id);
    }

    @GetMapping("/wedding-name/{weddingName}")
    public List<EventTable> getAllTablesByWedding(Principal principal, @PathVariable String weddingName) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();
        return tableRepository.findAllByWeddingId(wedding.getId());
    }

    @GetMapping("/{username}/{weddingName}/{tableName}")
    public EventTable getTableByKey(@PathVariable String username,@PathVariable String weddingName, @PathVariable String tableName) {
        Wedding tableWedding = weddingRepository.findByUser_UsernameAndName(username, weddingName).get();
        return tableRepository.findByWedding_IdAndName(tableWedding.getId(), tableName).get();
    }

    // GET a single table by ID
    @GetMapping("/{id}")
    public Optional<EventTable> getTableById(@PathVariable Long id) {
        return tableRepository.findById(id);
    }

    // CREATE a new table
//    @PostMapping("/create")
//    public EventTable createTable(@RequestParam Long weddingId, @RequestParam String name, @RequestParam Integer maxGuests) {
//        Wedding wedding = weddingRepository.findById(weddingId).get();
//
//        EventTable table = new EventTable();
//        table.setName(name);
//        table.setMaxGuests(maxGuests);
//        table.setWedding(wedding);
//        return tableRepository.save(table);
//    }

    @PostMapping("/create/{weddingName}/{tableName}")
    public EventTable createTable(Principal principal, @PathVariable String weddingName, @PathVariable String tableName) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName).get();

        EventTable table = new EventTable();
        table.setName(tableName);
        table.setMaxGuests(0);
        table.setWedding(wedding);
        return tableRepository.save(table);
    }

    @PutMapping("/addGuest/{weddingName}/{tableName}/{maxGuests}")
    public EventTable addGuest(Principal principal, @PathVariable String weddingName, @PathVariable String tableName, @PathVariable Integer maxGuests) {
        EventTable existingTable = tableRepository.findByUserUsernameAndWeddingNameAndTableName(principal.getName(), weddingName, tableName);

        existingTable.setMaxGuests(maxGuests);
        return tableRepository.save(existingTable);
    }

    // UPDATE a table by ID
    @PutMapping("/update/{username}/{weddingName}/{tableName}/{maxGuests}")
    public EventTable updateTableByName(@PathVariable String username,@PathVariable String weddingName, @PathVariable String tableName, @PathVariable Integer maxGuests) {
        EventTable existingTable = tableRepository.findByUserUsernameAndWeddingNameAndTableName(username, weddingName, tableName);

        existingTable.setMaxGuests(maxGuests);
        return tableRepository.save(existingTable);
    }

    // UPDATE a table by ID
    @PutMapping("/update/{id}")
    public EventTable updateTable(@PathVariable Long id, @RequestParam Integer maxGuests, @RequestParam String name) {
        EventTable existingTable = getTableById(id).orElseThrow(() -> new RuntimeException("Table not found"));
        existingTable.setName(name);
        existingTable.setMaxGuests(maxGuests);
        return tableRepository.save(existingTable);
    }

    // DELETE a table by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTable(@PathVariable Long id) {
        tableRepository.deleteById(id);
    }

    // DELETE a table by ID
    @DeleteMapping("/{username}/{weddingName}/{tableName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTable(@PathVariable String username,@PathVariable String weddingName, @PathVariable String tableName) {
        Wedding wedding = weddingRepository.findByUser_UsernameAndName(username, weddingName).get();
        EventTable table = tableRepository.findByWedding_IdAndName(wedding.getId(), tableName).get();
        tableRepository.deleteById(table.getId());
    }
}
