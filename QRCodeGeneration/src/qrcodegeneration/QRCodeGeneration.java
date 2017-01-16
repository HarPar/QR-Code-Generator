/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcodegeneration;

import java.io.IOException;

/**
 *
 * @author parih9096
 */
public class QRCodeGeneration {

    /**
     * @param args the command line arguments
     */
    
    String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-.?:";
    
    //Main Class to get codewords and draw them
    public static void main(String[] args) throws IOException {
        String text = "HELLO WORLD";
        
        QRCodeGeneration qr = new QRCodeGeneration();
        
        String DATA = qr.getEncodedData(text);
       
        ErrorCorrection ec = new ErrorCorrection(DATA);
        int[] TERMS = ec.getErrorCorrectionCode();

        String[] binTERMS = new String[TERMS.length];
        for(int i =0; i < TERMS.length; i++){
            binTERMS[i] = padZeroLeft(intToBinary(TERMS[i]), 8);
        }
        DrawQRCode dqr = new DrawQRCode(DATA, binTERMS);
        int[][] map = dqr.getMap();
        dqr.drawMap(map);
    }
    
    
    //Gets the encoded data
    public String getEncodedData(String text){
        int Q1= 104;
        String ALPHA_MODE_IND = "0010";
        String CHAR_COUNT_IND = padZeroLeft(charCount(text), 9);
        String ENCODED_DATA = alphaModeEncoding(text);
        String TERMINATOR = getTerminator(Q1, ALPHA_MODE_IND.length()+CHAR_COUNT_IND.length()+ENCODED_DATA.length());
        
        String DATA = getDataMultuipleEight(ALPHA_MODE_IND + CHAR_COUNT_IND + ENCODED_DATA + TERMINATOR);
        String DATA_PADBYTES = addPadBytes(DATA, Q1);
        
        
        //System.out.println(ALPHA_MODE_IND);
        //System.out.println(CHAR_COUNT_IND);
        //System.out.println(ENCODED_DATA);
        //System.out.println(TERMINATOR);
        for(int i = 8; i < DATA_PADBYTES.length(); i+=9){
            DATA_PADBYTES = DATA_PADBYTES.substring(0, i) + " " + DATA_PADBYTES.substring(i, DATA_PADBYTES.length());
        }
        //System.out.println(DATA_PADBYTES);
        return DATA_PADBYTES;
    }
    
    
    //Adds pad bytes to encoded data
    public String addPadBytes(String DATA, int MAX_BIT){
        int CURR_BIT = DATA.length();
        int PAD_BYTES = (MAX_BIT - CURR_BIT) / 8;
        
        String[] PADDING = new String[]{"11101100", "00010001"};
        
        for(int i = 0; i < PAD_BYTES; i++){
            DATA += PADDING[i%2];
        }       
        
        return DATA;
    }
    
    //Makes the data a multiple of eight
    public String getDataMultuipleEight(String DATA){
        //Make data a multiple of 8 by adding 0
        int looping = 8-DATA.length()%8;
        for(int i = 0; i < looping; i++){
            DATA += "0";
        }
        return DATA;
    }
    
    
    //Gets terminator for encoded data
    public String getTerminator(int MAX_BIT, int CURR_BIT){
        String TERMINATOR = "";
        //ADd terminator bit
        int DIFF = MAX_BIT - CURR_BIT;
        if (DIFF >= 4){
            TERMINATOR = "0000";
        }
        else{
            for(int i = 0; i < DIFF; i++){
                TERMINATOR += "0";
            }
        }
        
        return TERMINATOR;
    }
    
    
    //Gets encoded data through alpha encoding
    public String alphaModeEncoding(String txt){
        int CHAR_NUM = txt.length();
        String data = "";
        int num;
        for(int i = 0; i < CHAR_NUM-1; i+=2){
            num = alphabet.indexOf(txt.charAt(i))*45  +  alphabet.indexOf(txt.charAt(i+1));
            data += padZeroLeft(intToBinary(num), 11);
        }
        
        //if txt is odd
        if (CHAR_NUM % 2 != 0){
            data += padZeroLeft((intToBinary(alphabet.indexOf(txt.charAt(CHAR_NUM-1)))), 6);
        }
        
        return data;
    }
    
    
    //Pads zero to the left of 8 bit of encoded data
    public static String padZeroLeft(String txt, int BIT_LENGTH){
        int CHAR_NUM = txt.length();
        for(int i = 0; i < BIT_LENGTH-CHAR_NUM; i++){
            txt = "0" + txt;
        }
        return txt;
    }
    
    
    //Converts integer to binary
    public static String intToBinary(int x){
        return Integer.toBinaryString(x);
    }
    
    //Counts the char of a text
    public String charCount(String txt){
        int CHAR_NUM = txt.length();
        return intToBinary(CHAR_NUM);
    }
}
