/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcodegeneration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Parikh
 */
public final class ErrorCorrection {
    
    String data;
    
    public ErrorCorrection(){
        
    }
    
    
    //Constructor
    public ErrorCorrection(String d){
        this.data = d;
            //REMOVE THIS
    }
    
    
    //Gets the error correction code for encoded data
    public int[] getErrorCorrectionCode(){
                        //MINE WAS 13
        int n = 13;
        int steps = data.replace(" ", "").length()/8;
        GeneratorPolynomial gp = new GeneratorPolynomial(n, new int[]{0, 2, 25, 1, 1, 0});
        ArrayList<Integer> generatorPoly = gp.combineLikeTerms(gp.evaluate());
        ArrayList<Integer> result = new ArrayList<Integer>();
        MessagePolynomial mp = new MessagePolynomial(n, data);
        ArrayList<Integer> messagePoly = mp.getPoly();
        generatorPoly = matchExpo(messagePoly, generatorPoly);
        for(int i =0; i < steps; i++){
            result = ltMessagePoly(generatorPoly, numberGaloisFieldToExponent(messagePoly.get(0)));
            result = convertCoeffToIN(result);
            result = xorCoeff(result, messagePoly);
            result.remove(0);
            result.remove(0);
            generatorPoly = decreaseExpo(generatorPoly, 1);
            messagePoly = result;        

        }
        int[] terms = getTerms(result);
        return terms;
    }
    
    
    //Gets the terms of a poltnomial
    public int[] getTerms(ArrayList<Integer> r){
        int[] t = new int[r.size()/2];
        for(int i = 0; i < r.size(); i+=2){
            t[i/2] = r.get(i);
        }
        return t;
    }
    
    //Decrease coeff of every term of polynomial
    public ArrayList<Integer> decreaseTerm(ArrayList<Integer> gp, int n){
        for(int i = 0; i < gp.size(); i+=2){
            gp.set(i, gp.get(i)-n);
        }
        return gp;
    }
    
    //Decrease coeff of every exponent of polynomial
    public ArrayList<Integer> decreaseExpo(ArrayList<Integer> gp, int n){
        for(int i = 1; i < gp.size(); i+=2){
            gp.set(i, gp.get(i)-n);
        }
        return gp;
    }
    
    //Convert coeff to alpha notation
    public ArrayList<Integer> convertCoeffToExpo(ArrayList<Integer> a){
        ArrayList<Integer> r = new ArrayList<Integer>();
        for(int i = 0; i < a.size(); i++){
            if(i % 2 == 0){
                r.add(numberGaloisFieldToExponent(a.get(i)));
            }else{
                r.add(a.get(i));
            }
        }
        return a;
    }
    
    
    //Xoring coeff together
    public ArrayList<Integer> xorCoeff(ArrayList<Integer> r, ArrayList<Integer> mp){
        for(int i = 0; i < r.size(); i+=2){
            if(i < mp.size()){
                mp.set(i, r.get(i)^mp.get(i));
            }
            else{
                mp.add(r.get(i));
                mp.add(r.get(i+1));
            }
        }
        
        return mp;
    }
    
    
    //Convert alpha coeff to integer
    public ArrayList<Integer> convertCoeffToIN(ArrayList<Integer> a){
        for(int i = 0; i < a.size(); i+=2){
            a.set(i, GaloisField(a.get(i)));
        }
        return a;
    }
    
    
    //Adds lt to the lead term of g
    public ArrayList<Integer> ltMessagePoly(ArrayList<Integer> g, int lt){
        int x;
        ArrayList<Integer> r = new ArrayList<Integer>();
        for(int i = 0; i < g.size(); i++){
            if(i % 2 == 0){
                x = g.get(i)+lt;
                if(x >= 256){
                    x = x % 255;
                }
                r.add(x);
            }else{
                r.add(g.get(i));
            }
        }
        return r;
    }
    
    
    //Matches exponent of both polynomials
    public ArrayList<Integer> matchExpo(ArrayList<Integer> mp, ArrayList<Integer> gp){
        int INCR = mp.get(1) - gp.get(1);
        for(int i = 1; i < gp.size(); i+=2){
            gp.set(i, gp.get(i) + INCR);
        }
        return gp;
    }
    
    
    //Converting integer into GaloisField AKA alpha notation
    public int GaloisField(int exponent){
        int MAX = 256;
        int BYTE_WISE_MODULO = 285;
        
        if (exponent < 8){
            return (int) Math.pow(2, exponent);
        }
        
        int HOLDER = BYTE_WISE_MODULO - MAX;
        for(int i = 9; i <= exponent; i++){
            HOLDER = HOLDER * 2;
            if (HOLDER >= MAX){
                HOLDER = HOLDER^BYTE_WISE_MODULO;
            }
        }
        return HOLDER;
    }
    
    
    // Converting alpha notation back to an integer
    public int numberGaloisFieldToExponent(int num){
        int MAX = 256;
        int BYTE_WISE_MODULO = 285;
        
        for(int i = 0; i < 8; i++){
            if(num == Math.pow(2, i)){
                return i;
            }
        }
        
        int exponent = 8;
        int HOLDER = BYTE_WISE_MODULO - MAX;
        while (HOLDER != num){
            exponent++;
            HOLDER = HOLDER * 2;
            
            if (HOLDER >= MAX){
                HOLDER = HOLDER^BYTE_WISE_MODULO;
            }
            
            
        }
        return exponent;
    }
    

}
