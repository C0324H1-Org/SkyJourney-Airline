package com.group2.case_study.controllers;

import com.group2.case_study.models.Flight;
import com.group2.case_study.models.Seat;
import com.group2.case_study.services.IFlightService;
import com.group2.case_study.services.ISeatService;
import com.group2.case_study.services.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/flights")
public class SeatController {
    @Autowired
    private ISeatService seatService;

    @Autowired
    private IFlightService flightService;

    @Autowired
    private IUserService userService;

    @GetMapping("/{flightId}/seats")
    public String getSeatByFlightId(@PathVariable Integer flightId, Model model) {
        List<List<Seat>> seatRows = seatService.getSeatsGroupedByRows(flightId);
        Flight flight = flightService.getFlightById(flightId);
        if (flight != null) {
            model.addAttribute("seatRows", seatRows);
            model.addAttribute("flightId", flightId);
            model.addAttribute("seatPrice", flight.getPrice());
        }
        return "seat/seat-re";
    }

    @PostMapping("/{flightId}/confirm-booking")
    public String confirmBooking(@PathVariable Integer flightId,
                                 @RequestParam("seatIds") List<Integer> seatIds,
                                 HttpSession session) {
        LocalDateTime holdExpiration = LocalDateTime.now().plusMinutes(1);
        String userName = (String) session.getAttribute("username");
        int id = userService.findIdByUserName(userName);
        seatService.updateSeatStatus(seatIds, "BOOKED" + id, holdExpiration);
        flightService.saveUserId(flightId,id);
        session.setAttribute("seats", seatIds);
        return "redirect:/flights/" + flightId + "/seats";
    }

}
