package org.glsid.cinema.web;

import lombok.Data;
import org.glsid.cinema.dao.FilmRepository;
import org.glsid.cinema.dao.TicketRepository;
import org.glsid.cinema.entitie.Film;
import org.glsid.cinema.entitie.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CinemaRestController {
    @Autowired
    private  FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @GetMapping(value = "/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable (name ="id")Long id)throws Exception{
        Film film=filmRepository.findById(id).get();
        String photoName=film.getPhoto();
        File file=new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
        Path path= Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }
    @PostMapping("/payerTickets")
    @Transactional
    public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm){
        List<Ticket> tickets=new ArrayList<>();
        ticketForm.getTickets().forEach(id->{
            Ticket ticket=ticketRepository.findById(id).get();
            ticket.setNomClient(ticketForm.getNomClient());
            ticket.setReserve(true);
            ticket.setCodePayement(ticketForm.getCodePayment());
            ticketRepository.save(ticket);
            tickets.add(ticket);
        });
        return tickets;
    }
}

@Data
class TicketForm{
    private String nomClient;
    private int codePayment;
    private List<Long> tickets=new ArrayList<>();
}
