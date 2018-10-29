package sistema;

public class AlgoritmosErro 
{

	
	private String geradorCRC12 = "1100000001011";    //Gerador: x^12 + x^11 + x^3 + x^1 + x^0 = 1100000001011.
	private String zeroCRC12 =    "0000000000000";
	
	
	public String getCRC12(String dados)    //Recebe um valor binário e retorna seu respectivo CRC-12.
	{
		String zero = "000000000000";    //Quantidade de zeros à direita = grau (12) do polinômio gerador.
//		String quociente = "";
		String divisor;
		String dividendo = "";
		String resto = "";
		char[] bitDados = (dados + zero).toCharArray();    //Trabalha diretamento com os bits.
		char[] bitDivisor;
		char[] bitDividendo;
		int limiteDivisao = bitDados.length-1;
		
		//Inicializando o primeiro dividendo:
		for (int i = 0; i < geradorCRC12.length(); i++) {
			dividendo += bitDados[i];
		}
		
		bitDividendo = dividendo.toCharArray();
		
		for (int i = dividendo.length()-1; i < limiteDivisao; i++) {    //Dividindo de posição em posição, todos os bits de dados + zeros, a partir da posição do tamanho do gerador.
			if (bitDividendo[0] == '0') {
				divisor = zeroCRC12;
//				quociente += "0";
			}
			else {
				divisor = geradorCRC12;
//				quociente += "1";
			}
			
			bitDivisor = divisor.toCharArray();
			resto = "";
			
			//XOR:
			for (int j = 0; j < geradorCRC12.length(); j++) {    //Para cada posição de bit de dividendo, realiza XOR entre os bits desse intervalo e os bits do divisor atual (zero ou gerador).
				if (bitDividendo[j] != bitDivisor[j]) resto += "1";
				else resto += "0";
			}
			
			bitDivisor = resto.toCharArray();    //Reaproveitando variável.
			dividendo = "";
			
			//Gerando novo dividendo - excluindo bit mais significativo:
			for (int j = 1; j < geradorCRC12.length(); j++) {
				dividendo += bitDivisor[j];
			}
			
			dividendo += bitDados[i+1];    //Adicionando próximo bit de dado.
			bitDividendo = dividendo.toCharArray();
		}
		
		String crc = "";
		
		//Montando o CRC-12 (12 bits nesse caso):
		for (int i = bitDividendo.length-1; i > 0; --i) {
			crc = bitDividendo[i]+crc;
		}
		
		return crc;
	}
	
	
	public boolean verificarErroCRC12(String dadosECodigo)
	{
		String divisor;
		String dividendo = "";
		String resto = "";
		char[] bitDados = dadosECodigo.toCharArray();    //Trabalha diretamento com os bits.
		char[] bitDivisor;
		char[] bitDividendo;
		int limiteDivisao = bitDados.length-1;
		
		//Inicializando o primeiro dividendo:
		for (int i = 0; i < geradorCRC12.length(); i++) {
			dividendo += bitDados[i];
		}
		
		bitDividendo = dividendo.toCharArray();
		
		for (int i = dividendo.length()-1; i < limiteDivisao; i++) {    //Dividindo de posição em posição, todos os bits de dados + zeros, a partir da posição do tamanho do gerador.
			if (bitDividendo[0] == '0') {
				divisor = zeroCRC12;
			}
			else {
				divisor = geradorCRC12;
			}
			
			bitDivisor = divisor.toCharArray();
			resto = "";
			
			//XOR:
			for (int j = 0; j < geradorCRC12.length(); j++) {    //Para cada posição de bit de dividendo, realiza XOR entre os bits desse intervalo e os bits do divisor atual (zero ou gerador).
				if (bitDividendo[j] != bitDivisor[j]) resto += "1";
				else resto += "0";
			}
			
			bitDivisor = resto.toCharArray();    //Reaproveitando variável.
			dividendo = "";
			
			//Gerando novo dividendo - excluindo bit mais significativo:
			for (int j = 1; j < geradorCRC12.length(); j++) {
				dividendo += bitDivisor[j];
			}
			
			dividendo += bitDados[i+1];    //Adicionando próximo bit de dado.
			bitDividendo = dividendo.toCharArray();
		}
		
		//Verificando o resto da divisão:
		for (int i = 1; i < bitDividendo.length; ++i) {
			if (bitDividendo[i] != '0') return true;
		}
		
		return false;
	}
	
	
	public String getHammingPar(String dados)
	{
		int numBitsRedundancia = 1;
		char[] bitsDados = dados.toCharArray();
		
		//Obtendo a quantidade mínima de bits de redundância:
		while (Math.pow(2.0, numBitsRedundancia) < dados.length() + numBitsRedundancia + 1) ++numBitsRedundancia;
		
		char[] bitsCodigo = new char[numBitsRedundancia+dados.length()];
		
		//Inicializando vetor de bits com os dados passados e completando com bits zerados (não influeciam no cálculo do código):
		for (int i = 0; i < bitsCodigo.length; i++) bitsCodigo[i] = '0';
		
		int posicaoBitDados = bitsDados.length-1; 
		
		//Preenchendo os campos de dados (do menos significativo para o mais significativo = direita pra esquerda) baseado na multiplicidade binária:
		for (int i = 2; posicaoBitDados >= 0; i *= 2) {
			int proximaMultiplicidade = bitsCodigo.length - (i*2);
			
			for (int j = bitsCodigo.length-i-1; j > proximaMultiplicidade && posicaoBitDados >= 0; --j, --posicaoBitDados) {
				bitsCodigo[j] = bitsDados[posicaoBitDados];
			}
		}
		
		//Verificando paridade PAR das respectivas posições dos bits de redundância (r1, r2, r4, ...):
		for (int r = 1, nr = 0; nr < numBitsRedundancia; r *= 2, nr++) {
			int paridadePar = 0;    //Quantidade de bits 1 de suas respectivas posições.

			for (int j = r-1; j < bitsCodigo.length; j += 2*r) {
				for (int k = j; k < j+r && k < bitsCodigo.length; k++) {
					if (bitsCodigo[bitsCodigo.length-k-1] == '1') ++paridadePar;
				}
			}
			
			if (paridadePar%2 != 0) bitsCodigo[bitsCodigo.length-r] = '1';    //Se não for par, adiciona '1'; adiciona '0' caso contrário.
		}
		
		String hammingPar = "";
		
		for (int i = 0; i < bitsCodigo.length; i++) {
			hammingPar += bitsCodigo[i];
		}
	
		return hammingPar;
	}


}