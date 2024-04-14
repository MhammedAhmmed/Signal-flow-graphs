package com.example.programmingtask2;
import com.example.programmingtask2.RouthHurwitzCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class controller {
    @GetMapping("/isStable")
    public String checkStability(@RequestParam double[] coefficients) {
        RouthHurwitzCriterion routhHurwitzCriterion = new RouthHurwitzCriterion(coefficients);
        if (routhHurwitzCriterion.isStable()) {
            return "The system is stable.";
        } else {
            return "The system is unstable. Number of poles in the right-half of the s-plane: " +routhHurwitzCriterion.findRightSideRoots() ;
        }
    }
    @GetMapping("/showRoots")
    public List<List<Double>> showroots(@RequestParam double[] coefficients) {
        RouthHurwitzCriterion routhHurwitzCriterion = new RouthHurwitzCriterion(coefficients);
        List<double[]> roots = routhHurwitzCriterion.findRightSideRoots();
        List<List<Double>> formattedRoots = new ArrayList<>();
        for (double[] root : roots) {
            List<Double> formattedRoot = new ArrayList<>();
            formattedRoot.add(root[0]); // Real part
            formattedRoot.add(root[1]); // Imaginary part
            formattedRoots.add(formattedRoot);
        }
        return formattedRoots;
    }
}
