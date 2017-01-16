/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcodegeneration;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Parikh
 */
public class GeneratorPolynomial {
    
    int n;
    int counter = 2;
    int[] poly;
    
    //Constructor
    public GeneratorPolynomial(int n, int[] poly){
        this.n = n;
        this.poly = poly;
    }
    
    
    //Combines like terms of a polynomial
    public ArrayList<Integer> combineLikeTerms(int[] p){
        int DEGREE = p[1];
        ArrayList<Integer> NEW_POLY = new ArrayList<>();
        for(int i = 0; i <= DEGREE; i++){
            ArrayList<Integer> COMBINATION = new ArrayList<>();
            for(int j = 0; j < poly.length; j+=2){
                if(i == poly[j+1]){
                    COMBINATION.add(poly[j]);
                }
            }
            
            ErrorCorrection ec = new ErrorCorrection();
            int[] COMBINATIONGF = new int[COMBINATION.size()];
            for(int k = 0; k < COMBINATION.size(); k++){
                COMBINATIONGF[k] = (int)ec.GaloisField(COMBINATION.get(k));
            }
            int COEEF = COMBINATIONGF[0];
            for(int l = 1; l < COMBINATIONGF.length; l++){
                COEEF = COEEF ^ COMBINATIONGF[l];
            }
            NEW_POLY.add(i);
            NEW_POLY.add(ec.numberGaloisFieldToExponent(COEEF));
        }
        Collections.reverse(NEW_POLY);
        return NEW_POLY;
    }    
    
    
    //Evaluates the polynomial based on set guidelines to create the generator polynomial
    public int[] evaluate(){       
        
        if (n == counter){
            return poly;
        }
        
        int[] NEW_POLY = null;
        int x;

        for(int a = 2; a < n; a++){
            int ALPHA_EXP = a;
            int[] multiplier = {0, 1, ALPHA_EXP, 0};
            NEW_POLY  = new int[poly.length*2];
            
            for(int i = 0; i < poly.length; i++){
                if(i % 2 ==0){
                    x = poly[i] + multiplier[0];
                    if(x >= 256){
                        x = x % 255;
                    }
                    NEW_POLY[i] = x;
                }
                else{
                    NEW_POLY[i] = poly[i] + multiplier[1];
                }
            }

            for(int i = 0; i < poly.length; i++){
                if(i % 2 ==0){
                    x = poly[i] + multiplier[2];
                    if(x >= 256){
                        x = x % 255;
                    }
                    NEW_POLY[i + poly.length] = x;
                }
                else{
                    NEW_POLY[i + poly.length] = poly[i] + multiplier[3];
                }
            }

            poly = NEW_POLY;
        }               
        
        /*
        int index;
        index = poly.indexOf("(");
        while (index >= 0) {
            //System.out.println(index);
            //System.out.println(poly.charAt(index));
            index =  poly.indexOf("(", index + 1);
        }
        String ALPHA_EXP = n-1 + "";
        String multiplier = "0 1 " + ALPHA_EXP + " 0";
        String NEW_POLY = "";
        System.out.println(poly.charAt(6));
        for(int i = 0; i < poly.length(); i+=4){
            System.out.println(i);
            NEW_POLY += (Integer.parseInt(poly.charAt(i)+"") + Integer.parseInt(multiplier.charAt(0)+"")) + " ";
            NEW_POLY += (Integer.parseInt(poly.charAt(i+2)+"") + Integer.parseInt(multiplier.charAt(2)+"")) + " ";
            
        }
        
        System.out.println(NEW_POLY);*/
        return NEW_POLY;
    }
}
