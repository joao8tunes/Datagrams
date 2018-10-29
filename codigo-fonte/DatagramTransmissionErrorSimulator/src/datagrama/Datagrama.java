package datagrama;


public class Datagrama
{
	
	
	private String ipFonte;
	private String ipDestino;
	private Integer portaFonte;
	private Integer portaDestino;
	private String dadosOriginais;
	private String dadosRecebidos;
	private short tratamentoErro;
	private int id;
	
	
	public void setIPFonte(String ipFonte)
	{
		this.ipFonte = ipFonte;
	}
	
	
	public String getIPFonte()
	{
		return ipFonte;
	}
	
	
	public void setIPDestino(String ipDestino)
	{
		this.ipDestino = ipDestino;
	}
	
	
	public String getIPDestino()
	{
		return ipDestino;
	}	
	
	
	public void setPortaFonte(Integer portaFonte)
	{
		this.portaFonte = portaFonte;
	}
	
	
	public Integer getPortaFonte()
	{
		return portaFonte;
	}
	
	
	public void setPortaDestino(Integer portaDestino)
	{
		this.portaDestino = portaDestino;
	}
	
	
	public Integer getPortaDestino()
	{
		return portaDestino;
	}
	
	
	public void setDadosOriginais(String dados)
	{
		this.dadosOriginais = dados;
	}
	
	
	public String getDadosOriginais()
	{
		return dadosOriginais;
	}

	
	public void setDadosRecebidos(String dados)
	{
		this.dadosRecebidos = dados;
	}
	
	
	public String getDadosRecebidos()
	{
		return dadosRecebidos;
	}
	
	
	public void setTratamentoErro(short tratamentoErro)
	{
		this.tratamentoErro = tratamentoErro;
	}
	
	
	public short getTratamentoErro()
	{
		return tratamentoErro;
	}

	
	public void setID(int id)
	{
		this.id = id;
	}


	public int getID()
	{
		return id;
	}
	
	
}