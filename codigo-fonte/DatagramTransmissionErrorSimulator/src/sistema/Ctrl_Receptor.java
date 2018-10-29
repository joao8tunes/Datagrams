package sistema;

import datagrama.Conexao;
import datagrama.Datagrama;

public class Ctrl_Receptor 
{
	
	
	private Conexao conexao;
	
	
	public Ctrl_Receptor()
	{
		conexao = new Conexao();
		
		conexao.setPorta(4321);
	}
	
	
	public void conectarDesconectar(String ip, int porta)
	{
		if (!conexao.getStatus()) {    //Se for estabelecer nova conexão, atualiza os valores de IP e porta.
			conexao.setIP(ip);
			conexao.setPorta(porta);
		}
		
		conexao.conectarDesconectar();
	}
	
	
	public boolean getStatusConexao()
	{
		return conexao.getStatus();
	}
	
	
	public Conexao getConexao()
	{
		return conexao;
	}
	
	
	public void receberDatagrama(Datagrama datagrama)
	{
		conexao.receberDatagrama(datagrama);
	}
	
	
	public void atualizaLog(Datagrama datagrama)
	{
		
	}
	
	
}