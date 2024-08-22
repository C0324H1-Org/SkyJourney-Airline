package com.group2.case_study.controllers;

import com.group2.case_study.models.Flight;
import com.group2.case_study.models.Seat;
import com.group2.case_study.services.IFlightService;
import com.group2.case_study.services.ISeatService;
import com.group2.case_study.services.impl.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private IFlightService flightService;

    @Autowired
    private ISeatService seatService;

    @Autowired
    private UserService userService;

    @PostMapping
    public String showPay(@RequestParam("flightId") int flightId,
                          HttpSession session,
                          Model model) {
        Flight flight = flightService.getFlightById(flightId);
        String userName = (String) session.getAttribute("username");
        int id = userService.findIdByUserName(userName);
        List<Seat> seats = seatService.findAllSeat(flightId,id);
        model.addAttribute("flight", flight);
        model.addAttribute("seats", seats);
        return "pay/show-pay";
    }


}
