package sistema;

import java.util.Random;

import datagrama.Conexao;
import datagrama.Datagrama;

public class Ctrl_Conexao 
{
	
	
	private Conexao conexaoTransmissor;
	private Conexao conexaoReceptor;
	private Fcd_Simulador fcdSimulador;
	private Datagrama ultimoPacote;
	private AlgoritmosErro algoritmosErro;
	
	
	public Ctrl_Conexao(Fcd_Simulador fcdSimulador, Conexao conexaoTransmissor, Conexao conexaoReceptor)
	{
		this.fcdSimulador = fcdSimulador;
		this.conexaoTransmissor = conexaoTransmissor;
		this.conexaoReceptor = conexaoReceptor;
		algoritmosErro = new AlgoritmosErro();
	}
	
	
	public boolean validarIP(String ip)
	{
		Conexao conexao = new Conexao();
		
		return conexao.validarIP(ip);
	}
	
	
	public boolean transmitirDatagrama(Datagrama datagrama)
	{
		boolean sucesso = true;
		
	    //Transmissor para Receptor.
		if (datagrama.getIPDestino().equals(conexaoReceptor.getIP()) && datagrama.getPortaDestino().equals(conexaoReceptor.getPorta())) {
			conexaoReceptor.receberDatagrama(datagrama);
			fcdSimulador.atualizarLogReceptor(conexaoReceptor.getEntrada());
			fcdSimulador.atualizarLogTransmissor(datagrama);
			
			//Apenas o CRC envia NACK:
			if (conexaoTransmissor.getErro() && conexaoTransmissor.getTratamentoErro() == Conexao.CRC) {
				sucesso = !verificarErroCRC12(datagrama.getDadosRecebidos());
			}
		}
		else sucesso = false;    //Se IP ou porta não corresponderem à um host, retorna false.
		
		return sucesso;    
	}
	
	
	public int getPortaAleatoria()    //Retorna uma porta que não está sendo usada no momento, no caso do Transmissor e Receptor terem o mesmo IP.
	{
		int portaDisponivel = 0;
		
		if (conexaoTransmissor.getIP().equals(conexaoReceptor.getIP()) && conexaoReceptor.getStatus()) {
			do {
				portaDisponivel = Math.abs((new Random()).nextInt())%10000;    //Portas de até 4 dígitos.
			} while (portaDisponivel == conexaoReceptor.getPorta());
		}
		else portaDisponivel = Math.abs((new Random()).nextInt())%10000;
		
		++portaDisponivel;    //Evitando porta nula (0);
		
		return portaDisponivel;
	}
	
	
	public Datagrama getUltimoPacote()
	{
		return ultimoPacote;
	}
	
	
	public void setUltimoPacote(Datagrama ultimoPacote)
	{
		this.ultimoPacote = ultimoPacote;
	}
	
	
	public boolean verificarErroCRC12(String dadosECodigo)
	{
		return algoritmosErro.verificarErroCRC12(dadosECodigo);
	}
	
	
	public String getCRC12(String dados)
	{
		return algoritmosErro.getCRC12(dados);
	}
	
	
	public String getHammingPar(String dados)
	{
		return algoritmosErro.getHammingPar(dados);
	}
	
	
}