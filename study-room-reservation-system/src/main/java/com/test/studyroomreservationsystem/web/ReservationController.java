package com.test.studyroomreservationsystem.web;

import com.test.studyroomreservationsystem.domain.Reservation;
import com.test.studyroomreservationsystem.repository.ReservationUpdateDto;
import com.test.studyroomreservationsystem.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // C
    @PostMapping("/enroll")
    public String enrollReservation(@ModelAttribute Reservation reservation, RedirectAttributes redirectAttributes) {
        Reservation savedReservation = reservationService.save(reservation);
        redirectAttributes.addAttribute("reservationId", savedReservation.getReservationId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/reservations/{reservationId}";
    }
    // R
    @GetMapping("/{reservationId}")
    public String reservation(@PathVariable long reservationId, Model model) {
        Reservation reservation = reservationService.findByReservationId(reservationId).get();
        model.addAttribute("reservation", reservation);
        return "reservation";
    }

    //   U
    @PostMapping("/{reservationId}/edit")
    public String edit(@PathVariable long reservationId, @ModelAttribute ReservationUpdateDto updateParam) {
        reservationService.update(reservationId,updateParam);
        return "redirect:/reservations/{reservationId}";
    }

}

