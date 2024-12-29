package com.example.weddingSitter.Controller;

import com.example.weddingSitter.entities.User;
import com.example.weddingSitter.entities.Wedding;
import com.example.weddingSitter.services.UserRepository;
import com.example.weddingSitter.services.WeddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weddings")
public class WeddingController {

    @Autowired
    private WeddingRepository weddingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user-id/{id}")
    public List<Wedding> getAllWeddingsByUserId(@PathVariable Integer id) {
        return weddingRepository.findAllByUserId(id);
    }

    @GetMapping("/username")
    public List<Wedding> getAllWeddingsByUserName(Principal principal) {
        return weddingRepository.findAllByUser_Username(principal.getName());
    }

    @GetMapping("/name/{weddingName}")
    public Optional<Wedding> getWeddingByUserNameAndName(Principal principal, @PathVariable String weddingName) {
        return weddingRepository.findByUser_UsernameAndName(principal.getName(), weddingName);
    }

    @GetMapping("/name/{id}/")
    public Optional<Wedding> getWeddingById(@PathVariable Integer id) {
        return weddingRepository.findById(id);
    }



    @GetMapping("")
    public List<Wedding> getAllWeddings() {
        return weddingRepository.findAll();
    }

    @PostMapping("/create/{weddingName}/{location}")
    public Wedding createWedding(Principal principal,
                                 @PathVariable String weddingName,
                                 @PathVariable String location) {
        Wedding newWedding = new Wedding();
        User user = userRepository.findByUsername(principal.getName()).get();
        newWedding.setUser(user);
        newWedding.setName(weddingName);
        newWedding.setLocation(location);
        return weddingRepository.save(newWedding);
    }

    // UPDATE a guest by ID
    @PutMapping("/{id}")
    public Wedding updateWedding(@PathVariable Integer id, @RequestBody Wedding updatedWedding) {
        Wedding existingWedding = getWeddingById(id).get();
        existingWedding.setName(updatedWedding.getName());
        existingWedding.setUser(updatedWedding.getUser());
        existingWedding.setLocation(updatedWedding.getLocation());
        existingWedding.setDate(updatedWedding.getDate());
        existingWedding.setTables(updatedWedding.getTables());
        return weddingRepository.save(existingWedding);
    }

    @DeleteMapping("/name/{weddingName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWedding(@PathVariable String weddingName) {
        Wedding wedding = weddingRepository.findByName(weddingName).get();
        deleteWedding(wedding.getId());
    }


    // DELETE a guest by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWedding(@PathVariable Long id) {
        weddingRepository.deleteById(id);
    }

}
