package com.example.programmingtask2;
import com.example.programmingtask2.RouthHurwitzCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class controller {
    @GetMapping("/isStable")
    public String checkStability(@RequestParam double[] coefficients) {
        RouthHurwitzCriterion routhHurwitzCriterion = new RouthHurwitzCriterion(coefficients);
        if (routhHurwitzCriterion.isStable()) {
            return "The system is stable.";
        } else {
            List<double[]> rightSideRoots = routhHurwitzCriterion.findRightSideRoots();
            return "The system is unstable. Number of poles in the right-half of the s-plane: " + rightSideRoots ;
        }
    }
}
