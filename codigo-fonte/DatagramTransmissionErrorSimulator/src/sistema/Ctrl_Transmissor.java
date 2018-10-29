package sistema;

import java.util.Random;

import datagrama.Conexao;
import datagrama.Datagrama;

public class Ctrl_Transmissor 
{
	
	
	private Conexao conexao;
	private Ctrl_Conexao ctrlConexao;
	
	
	public Ctrl_Transmissor()
	{
		conexao = new Conexao();

		//Valores padrão:
		conexao.setErro(false);
		conexao.setTratamentoErro(Conexao.CRC);
	}
	
	
	public Conexao getConexao()
	{
		return conexao;
	}
	
	
	public void setCtrlConexao(Ctrl_Conexao ctrlConexao)
	{
		this.ctrlConexao = ctrlConexao;
	}
	
	
	public Ctrl_Conexao getCtrlConexao()
	{
		return ctrlConexao;
	}
	
	
	public boolean transmitirDatagrama(String ipFonte, int portaFonte, String ipDestino, int portaDestino, String dados, int id)
	{
		conexao.setPorta(ctrlConexao.getPortaAleatoria());    //Setando portas aleatórias e não conflitantes.
		
		Datagrama novoDatagrama = new Datagrama();
		Random possibilidadeErros = new Random();
		String dadosBinario = Integer.toBinaryString(dados.toCharArray()[0]);
		
		novoDatagrama.setID(id);
		novoDatagrama.setIPFonte(ipFonte);
		novoDatagrama.setPortaFonte(portaFonte);
		novoDatagrama.setIPDestino(ipDestino);
		novoDatagrama.setPortaDestino(portaDestino);

		if (getErro()) {
			if (getTratamentoErro() == Conexao.CRC) {
				novoDatagrama.setDadosOriginais(dadosBinario + ctrlConexao.getCRC12(dadosBinario));
				
				if (possibilidadeErros.nextBoolean()) {    //Gera erro apenas se a conexão for setada pelo usuário e a probabilidade de 50% coincidir, pois não necessariamente terá erros.
					char[] dadosECRC12 = novoDatagrama.getDadosOriginais().toCharArray();
					int bitInvertido = Math.abs(possibilidadeErros.nextInt())%dadosECRC12.length;    //Gerando uma posição errada.
					String dadosComErro = "";
					
					if (dadosECRC12[bitInvertido] == '0') dadosECRC12[bitInvertido] = '1';
					else dadosECRC12[bitInvertido] = '0';
					
					for (int i = 0; i < dadosECRC12.length; i++) {
						dadosComErro += dadosECRC12[i];
					}
					
					novoDatagrama.setDadosRecebidos(dadosComErro);
				}
				else novoDatagrama.setDadosRecebidos(dadosBinario + ctrlConexao.getCRC12(dadosBinario));
			}
			else {
				String hamming = ctrlConexao.getHammingPar(dadosBinario);
				
				novoDatagrama.setDadosOriginais(hamming);
				
				if (possibilidadeErros.nextBoolean()) {    //Gera erro apenas se a conexão for setada pelo usuário e a probabilidade de 50% coincidir, pois não necessariamente terá erros.
					int bitErrado = Math.abs(possibilidadeErros.nextInt())%hamming.length();
					char[] bitsHamming = hamming.toCharArray();
					
					if (bitsHamming[bitErrado] == '0') bitsHamming[bitErrado] = '1';
					else bitsHamming[bitErrado] = '0';
					
					hamming = "";
					
					//Alterando 1 bit no código:
					for (int i = 0; i < bitsHamming.length; i++) {
						hamming += bitsHamming[i];
					}
					
					novoDatagrama.setDadosRecebidos(hamming);
				}
				else novoDatagrama.setDadosRecebidos(hamming);
			}
		}
		else {    //Nada é alterado.
			novoDatagrama.setDadosOriginais(dadosBinario);
			novoDatagrama.setDadosRecebidos(dadosBinario);
		}
		
		ctrlConexao.setUltimoPacote(novoDatagrama);
		
		return ctrlConexao.transmitirDatagrama(novoDatagrama);
	}
	
	
	public void setErro(boolean erro)
	{
		conexao.setErro(erro);
	}
	
	
	public boolean getErro()
	{
		return conexao.getErro();
	}

	
	public void setTratamentoErro(short tratamentoErro)
	{
		conexao.setTratamentoErro(tratamentoErro);
	}
	
	
	public short getTratamentoErro()
	{
		return conexao.getTratamentoErro();
	}
	
	
	public Datagrama getUltimoPacote()
	{
		return ctrlConexao.getUltimoPacote();
	}
	
	
}