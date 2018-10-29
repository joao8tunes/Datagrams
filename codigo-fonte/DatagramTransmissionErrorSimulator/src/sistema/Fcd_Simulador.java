package sistema;

import datagrama.Conexao;
import datagrama.Datagrama;


public class Fcd_Simulador 
{
	
	
	private Ctrl_Transmissor ctrlTransmissao;
	private Ctrl_Receptor ctrlRecepcao;
	private Ctrl_Conexao ctrlConexao;
	private Ctrl_Interface ctrlInterface;
	
	
	public Fcd_Simulador(Ctrl_Interface ctrlInterface)
	{
		this.ctrlInterface = ctrlInterface;
		ctrlTransmissao = new Ctrl_Transmissor();
		ctrlRecepcao = new Ctrl_Receptor();
		ctrlConexao = new Ctrl_Conexao(this, ctrlTransmissao.getConexao(), ctrlRecepcao.getConexao());
		ctrlTransmissao.setCtrlConexao(ctrlConexao);
	}
	
	
	public boolean validarIP(String ip)
	{
		return ctrlConexao.validarIP(ip);
	}

	
	public void conectarDesconectar(String ip, int porta)
	{
		ctrlRecepcao.conectarDesconectar(ip, porta);
	}
	
	
	public boolean getStatusConexao()
	{
		return ctrlRecepcao.getStatusConexao();
	}
	
	
	public Conexao getConexaoTransmissor()
	{
		return ctrlTransmissao.getConexao();
	}
	

	public Conexao getConexaoReceptor()
	{
		return ctrlRecepcao.getConexao();
	}
	
	
	
	public boolean transmitirDatagrama(String ipFonte, int portaFonte, String ipDestino, int portaDestino, String dados, int id)
	{
		return ctrlTransmissao.transmitirDatagrama(ipFonte, portaFonte, ipDestino, portaDestino, dados, id);
	}
	
	
	public void atualizarLogTransmissor(Datagrama datagrama)
	{
		ctrlInterface.atualizarLogTransmissor(datagrama);
	}
	
	
	public void atualizarLogReceptor(Datagrama datagrama)
	{
		ctrlInterface.atualizarLogReceptor(datagrama);
	}
	
	
	public Datagrama getUltimoPacote()
	{
		return ctrlConexao.getUltimoPacote();
	}
	
	
	public boolean verificarErroCRC12(String dadosECodigo)
	{
		return ctrlConexao.verificarErroCRC12(dadosECodigo);
	}
	
	
}