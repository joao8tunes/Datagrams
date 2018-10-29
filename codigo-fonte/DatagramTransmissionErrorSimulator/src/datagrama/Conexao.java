package datagrama;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Conexao 
{
	
	
	private String ip;
	private int porta;
	private boolean conectado;
	private Datagrama bufferEntrada;
	private boolean erro;
	private short tratamentoErro;
	
	public static short CRC = 0;
	public static short HAMMING = 1;
	
	
	public Conexao(String ip, int porta)
	{
		conectado = false;
		setIP(ip);
		setPorta(porta);
	}

	
	public Conexao()
	{	  
		ArrayList<String[]> infos = new ArrayList<>();
		Enumeration e;
		
		conectado = false;
		
		try {
			e = NetworkInterface.getNetworkInterfaces();
		
			while (e.hasMoreElements()) { 
		    	NetworkInterface i = (NetworkInterface) e.nextElement(); 
		        Enumeration ds = i.getInetAddresses(); 
		        
		        while (ds.hasMoreElements()) { 
		        	InetAddress minhasInfos = (InetAddress) ds.nextElement();
		            String novaInfo[] = new String[2];
		            
		            //Adicionando hostname e endereço de cada interface:
		            novaInfo[0] = minhasInfos.getHostName();
		            novaInfo[1] = minhasInfos.getHostAddress();
		        	
		        	infos.add(novaInfo);
		        } 
		    }
			
			setIP(infos.get(1)[1]);
		} 
		catch (SocketException e1) {
			setIP("127.0.0.1");
			e1.printStackTrace();
		}
	}
	
	
	public void setIP(String ip)
	{
		if (validarIP(ip)) this.ip = ip;
		else this.ip = null;
	}
	
	
	public String getIP()
	{
		return ip;
	}


	public void setPorta(int porta)
	{
		this.porta = porta;
	}
	
	
	public int getPorta()
	{
		return porta;
	}
	
	
	public boolean validarIP(String ip)
	{
		String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern = Pattern.compile(regex);  
	    Matcher matcher = pattern.matcher(ip);  
	    
	    if (matcher.matches()) return true;
	  
	    return false;
	}
	
	
	public void receberDatagrama(Datagrama datagrama)
	{
		bufferEntrada = datagrama;
	}
	
	
	public void conectarDesconectar()
	{
		conectado = !conectado;
	}
	
	
	public boolean getStatus()
	{
		return conectado;
	}
	
	
	public void setErro(boolean erro)
	{
		this.erro = erro;
	}
	
	
	public boolean getErro()
	{
		return erro;
	}

	
	public void setTratamentoErro(short tratamentoErro)
	{
		this.tratamentoErro = tratamentoErro;
	}
	
	
	public short getTratamentoErro()
	{
		return tratamentoErro;
	}

	
	public Datagrama getEntrada()
	{
		Datagrama entrada = this.bufferEntrada;
		
		this.bufferEntrada = null;    //Removendo o datagrama do buffer de entrada.
		
		return entrada;
	}
	
	
}