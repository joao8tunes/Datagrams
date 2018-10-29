package datagrama;

import java.util.BitSet;

public class ConversorBit 
{

	
	public static int bitParaInt(BitSet valorBit)
	{
		if (valorBit.length() > 4) return -1;
		
	    int valorInt = 0;
	    
	    for (int i = 0 ; i < valorBit.length(); i++) {
	        if(valorBit.get(i)) valorInt |= (1 << i);
	    }
	    
	    return valorInt;
	}
	
	
	public static BitSet intParaBit(int valorInt)
	{
	    char[] bits = Integer.toBinaryString(valorInt).toCharArray();  
	    BitSet valorBit = new BitSet(bits.length);  
	    
	    for(int i = 0; i < bits.length; i++) {
	        if(bits[i] == '1') valorBit.set(i, true);
	        else valorBit.set(i, false);            
	    }
	    
	    return valorBit;
	}  
	
	
	public static BitSet stringToBit(String string, int quantidadeBits)
	{
//		if (string.length() != quantidadeBits) return null;
		
	    BitSet t = new BitSet(string.length());
	    int ultimoIndiceBit = string.length()-1;
	    
	    for (int i = ultimoIndiceBit; i >= 0; i--) {
	        if (string.charAt(i) == '1') t.set(ultimoIndiceBit-i);
	    }
	    
	    return t;
	}
	
	
}