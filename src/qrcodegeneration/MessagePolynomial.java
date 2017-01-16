/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcodegeneration;

import java.util.ArrayList;

/**
 *
 * @author Parikh
 */
public class MessagePolynomial {
    
    String data;
    int n;
    
    
    //Constructor
    public MessagePolynomial(int n, String d){
        this.data = d;
        this.n = n;
    }
    
    //Gets coeff of encoded data
    public int[] getCoeff(){
        String NEW_DATA = data.replace(" ", "");
        int[] cf = new int[NEW_DATA.length()/8];
        for(int i = 0; i <= data.length(); i+=9){
            int binNum = Integer.parseInt(data.substring(i, i+8), 2);
            cf[i/9] = binNum;
        }

        return cf;
    }
    
    
    //Creates a polynomial based of encoded data called the message polynomial
    public ArrayList<Integer> getPoly(){
        ArrayList<Integer> arr = new ArrayList<Integer>();
        int[] coeff = getCoeff();
        for(int i = 0; i < coeff.length; i++){
            arr.add(coeff[i]);
            arr.add(coeff.length - i - 1 + n);
        }
        return arr;
    }
    
}
