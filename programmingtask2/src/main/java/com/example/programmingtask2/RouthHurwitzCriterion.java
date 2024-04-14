package com.example.programmingtask2;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.NewtonSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class RouthHurwitzCriterion {
    private final double[] coefficients;
    public static int num_of_roots;
    public RouthHurwitzCriterion(double[] arr) {
        this.coefficients = arr;
    }

    public boolean isStable() {
        int n = this.coefficients.length;
        int m = (n + 1) / 2;
        double[][] routhArray = new double[n][m];
        int j = 0;

        int i;
        for(i = 0; i < m; ++i) {
            if (j < n) {
                if (j < this.coefficients.length) {
                    routhArray[0][i] = this.coefficients[j];
                } else {
                    routhArray[0][i] = 0.0;
                }
            }

            j += 2;
        }

        j = 1;

        for(i = 0; i < m; ++i) {
            if (j < n) {
                if (j < this.coefficients.length) {
                    routhArray[1][i] = this.coefficients[j];
                } else {
                    routhArray[1][i] = 0.0;
                }
            }

            j += 2;
        }
        int firsttime=1;
        for(i = 2; i < n; ++i) {
            for(j = 0; j < m - 1; ++j) {
                double a = routhArray[i - 2][0];
                double b = i - 1 < routhArray.length ? routhArray[i - 1][0] : 0.0;
                double c = j + 1 < routhArray[i - 1].length ? routhArray[i - 1][j + 1] : 0.0;
                double d = i - 1 < routhArray.length && j + 1 < routhArray[i - 1].length ? routhArray[i - 2][j + 1] : 0.0;
                routhArray[i][j] = (d * b - a * c) / b;
            }
            boolean t=isFirstRowZero(routhArray,i);
            if(t==true&&firsttime==1){
                   firsttime=0;
             //    System.out.println(i);
                   routhArray = zerorow(routhArray, i, n, m);
            }
            else if(routhArray[i][0]==0){
                    routhArray[i][0] = 0.1;
              }
        }
        for( i=0;i<n;i++) {
            System.out.println();
            for (j = 0; j < m; j++)
                System.out.print(routhArray[i][j]+" ");
        }
        List<Double> firstColumn = new ArrayList();
        double[][] var14 = routhArray;
        int var7 = routhArray.length;

        for(int var16 = 0; var16 < var7; ++var16) {
            double[] row = var14[var16];
            firstColumn.add(row[0]);
        }

        return this.allPositive(firstColumn);
    }

    private boolean allPositive(List<Double> arr) {
        Iterator var2 = arr.iterator();

        double num;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            num = (Double)var2.next();
        } while(!(num < 0.0));

        this.findNumberOfRightHalfPlanePoles(arr);
        return false;
    }

    public void findNumberOfRightHalfPlanePoles(List<Double> lst) {
        int count = 0;

        for(int i = 0; i < lst.size() - 1; ++i) {
            if ((Double)lst.get(i) * (Double)lst.get(i + 1) < 0.0) {
                ++count;
            }
        }

        num_of_roots = count;
    }
    public  double[][] zerorow( double[][] routhtable,int nrow,int n ,int m){
        int h=((n-1)-nrow+1);
  for(int i=0;i<m-1;i++){
      routhtable[nrow][i]= routhtable[nrow-1][i]*h;
      h=h-2;
    //  System.out.println(routhtable[nrow][i]);
  }
      //  System.out.println(".........................................................");

        return routhtable;
    }
    public boolean isFirstRowZero(double[][] routhArray ,int i) {
        for (double element : routhArray[i]) {
            if (element != 0.0) {
                return false;
            }
        }
        return true;
    }

//    public List<double[]> findRightSideRoots() {
//        List<double[]> rightSideRoots = new ArrayList<>();
//
//        // Create a polynomial from the coefficients
//        double[] coefficientsCopy = Arrays.copyOf(coefficients, coefficients.length);
//        PolynomialFunction polynomial = new PolynomialFunction(coefficientsCopy);
//
//        // Use LaguerreSolver to find roots
//        LaguerreSolver solver = new LaguerreSolver();
//        Complex[] roots = solver.solveAllComplex(polynomial.getCoefficients(), 0.0);
//        for (Complex root : roots) {
//            if (root.getImaginary() == 0.0 && root.getReal() > 0.0) {
//                double[] rootPair = {root.getReal(), root.getImaginary()};
//                rightSideRoots.add(rootPair);
//            }
//        }
//
//        return rightSideRoots;
//    }


    public static void main(String[] args) {
        double[] coefficients = {-50,48,24,2,1};
        RouthHurwitzCriterion obj = new RouthHurwitzCriterion(coefficients);
        if (obj.isStable()) {
            System.out.println("The system is stable.");
        } else {
            System.out.println("\nThe system is unstable.");
//            List<double[]> rightSideRoots = obj.findRightSideRoots();
            System.out.println("Roots in the right-half of the s-plane: "+ RouthHurwitzCriterion.num_of_roots+ " ");
//            for (double[] rootPair : rightSideRoots) {
//                System.out.println("Real part: " + rootPair[0] + ", Imaginary part: " + rootPair[1]);
//            }
        }
    }


}
