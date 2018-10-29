package sistema;

import gui.JanelaReceptor;
import gui.JanelaTransmissor;
import datagrama.Conexao;
import datagrama.Datagrama;

public class Ctrl_Interface 
{

	
	private JanelaTransmissor janelaTransmissor;
	private JanelaReceptor janelaReceptor;
	private Fcd_Simulador fcdSimulador;    //Controlador de fachada.    
	
	
	public Ctrl_Interface()
	{
		fcdSimulador = new Fcd_Simulador(this);
		janelaTransmissor = new JanelaTransmissor(this, fcdSimulador.getConexaoReceptor().getIP(), fcdSimulador.getConexaoReceptor().getPorta());
		janelaReceptor = new JanelaReceptor(this, fcdSimulador.getConexaoReceptor().getIP(), fcdSimulador.getConexaoReceptor().getPorta());
	}
	
	
	public boolean validarIP(String ip)
	{
		return fcdSimulador.validarIP(ip);
	}

	
	public void conectarDesconectar(String ip, int porta)
	{
		fcdSimulador.conectarDesconectar(ip, porta);
	}
	
	
	public boolean getStatusConexao()
	{
		return fcdSimulador.getStatusConexao();
	}
	
	
	public boolean transmitirDatagrama(String ipFonte, int portaFonte, String ipDestino, int portaDestino, String dados, int id)
	{
		return fcdSimulador.transmitirDatagrama(ipFonte, portaFonte, ipDestino, portaDestino, dados, id);
	}
	
	
	public Conexao getConexaoTransmissor()
	{
		return fcdSimulador.getConexaoTransmissor();
	}

	
	public Conexao getConexaoReceptor()
	{
		return fcdSimulador.getConexaoReceptor();
	}
	
	
	public void atualizarLogTransmissor(Datagrama datagrama)
	{
	}

	
	public void atualizarLogReceptor(Datagrama datagrama)
	{
		janelaReceptor.atualizarLog(datagrama);
	}
	
	
	public Datagrama getUltimoPacote()
	{
		return fcdSimulador.getUltimoPacote();
	}
	
	
	public boolean verificarErroCRC12(String dadosECodigo)
	{
		return fcdSimulador.verificarErroCRC12(dadosECodigo);
	}
	
	
}