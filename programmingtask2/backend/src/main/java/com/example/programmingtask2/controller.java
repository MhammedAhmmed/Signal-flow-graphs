package com.example.programmingtask2;
import com.example.programmingtask2.RouthHurwitzCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class controller {
    @GetMapping("/isStable")
    @CrossOrigin(origins = "http://localhost:8081")
    public int checkStability(@RequestParam double[] coefficients) {
        RouthHurwitzCriterion routhHurwitzCriterion = new RouthHurwitzCriterion(coefficients);
        if (routhHurwitzCriterion.isStable()) {
            System.out.println(1);
            return 1;
        } else {
            System.out.println(0);
            return 0;
        }
    }
    @GetMapping("/showRoots")
    @CrossOrigin(origins = "http://localhost:8081")
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
