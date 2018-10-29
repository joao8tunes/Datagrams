package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import datagrama.Conexao;
import datagrama.Datagrama;
import sistema.Ctrl_Interface;


public class JanelaReceptor extends JFrame 
{

	
	private JPanel contentPane;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextArea textArea;
	private Ctrl_Interface ctrlInterface;
	private JLabel status;

	
	public JanelaReceptor(final Ctrl_Interface ctrlInterface, String ipPadrao, int portaPadrao)
	{
		this.ctrlInterface = ctrlInterface;
		setTitle("Simulador de Transmissão de Pacotes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		setMinimumSize(new Dimension(600, 400));
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblTransmissor = new JLabel("Receptor");
		lblTransmissor.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
		
		JLabel lblLog = new JLabel("Log");
		lblLog.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		JLabel lblIp = new JLabel("IPv4");
		lblIp.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		textField_1 = new JTextField();
		textField_1.setText(ipPadrao);
		textField_1.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
		textField_1.setColumns(10);
		
		JLabel lblPortaReceptor = new JLabel("Porta");
		lblPortaReceptor.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		textField_2 = new JTextField();
		textField_2.setText(String.valueOf(portaPadrao));
		textField_2.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
		textField_2.setColumns(10);
		
		JButton btnDesConectar = new JButton("(Con)Desconectar");
		
		//Evento de (des)conexão:
		btnDesConectar.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (!textField_1.getText().isEmpty() && !textField_2.getText().isEmpty()) {
					if (!ctrlInterface.validarIP(textField_1.getText().trim())) JOptionPane.showMessageDialog(null, "Dados inválidos.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					else {
						try {
							int porta = Integer.parseInt(textField_2.getText().trim());
							
							if (porta <= 0) JOptionPane.showMessageDialog(null, "Dados inválidos.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							else conectarDesconectar(textField_1.getText().trim(), porta);
						}
						catch (NumberFormatException ne) {
							JOptionPane.showMessageDialog(null, "Dados inválidos.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				else JOptionPane.showMessageDialog(null, "Omissão de dados de entrada.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		btnDesConectar.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		JSeparator separator = new JSeparator();
		
		JButton btnLimparLog = new JButton("Limpar Log");
		
		//Limpando área de log:
		btnLimparLog.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				textArea.setText("");
			}
		});
		
		JLabel lblStatusDaConexo = new JLabel("Desconectado");
		lblStatusDaConexo.setForeground(Color.RED);
		status = lblStatusDaConexo;
		lblStatusDaConexo.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblLog, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 370, Short.MAX_VALUE)
							.addComponent(btnLimparLog))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTransmissor, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblIp, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPortaReceptor))
							.addGap(368))
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnDesConectar)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblStatusDaConexo, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTransmissor)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIp)
						.addComponent(lblPortaReceptor))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDesConectar)
						.addComponent(lblStatusDaConexo))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLimparLog)
						.addComponent(lblLog))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JTextArea textArea = new JTextArea();
		this.textArea = textArea;
		
		textArea.setForeground(Color.GREEN);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textArea.setEditable(false);
		textArea.setBackground(Color.BLACK);
		scrollPane.setViewportView(textArea);
		contentPane.setLayout(gl_contentPane);
		setVisible(true);
	}
	
	
	public void conectarDesconectar(String ip, int porta)
	{
		ctrlInterface.conectarDesconectar(ip, porta);
		
		if (ctrlInterface.getStatusConexao()) {
			status.setText("Conectado");
			status.setForeground(new Color(0, 238, 0));
			textField_1.setEditable(false);
			textField_2.setEditable(false);
		}
		else {
			status.setText("Desconectado");
			status.setForeground(Color.RED);
			textField_1.setEditable(true);
			textField_2.setEditable(true);
		}
	}
	
	
	public void atualizarLog(Datagrama pacoteRecebido)
	{
		if (pacoteRecebido.getID() == -1) {
			if (!textArea.getText().isEmpty()) textArea.append("\n ----------------------------------------------------------------\n");
			textArea.append("\n ... NOVA RECEPÇÃO ...");
			return;
		}
		
		textArea.append("\n .............................\n");
		textArea.append(" ID: " + pacoteRecebido.getID() + "\t\t\t\t\t");
		
		//Conexão com PROBABILIDADE DE ERROS (não implica que necessariamente tenha erro):
		if (ctrlInterface.getConexaoTransmissor().getErro()) {    
			//CRC:
			if (ctrlInterface.getConexaoTransmissor().getTratamentoErro() == Conexao.CRC) {
				//Se não houve erro, manda ACK, senão, manda NACK [retransmissão]:
				if ( !ctrlInterface.verificarErroCRC12(pacoteRecebido.getDadosRecebidos()) ) textArea.append(" ACK\n");
				else textArea.append(" NACK [retransmissão]\n");
			}
			//HAMMING:
			else textArea.append(" ACK\n");
		}
		
		textArea.append(" Canal com Erro: ");
		if (ctrlInterface.getConexaoTransmissor().getErro()) {
			textArea.append("sim\t\t\t Algoritmo de Tratamento: ");
			if (ctrlInterface.getConexaoTransmissor().getTratamentoErro() == Conexao.CRC) textArea.append("CRC-12\n");
			else textArea.append("Hamming (par)\n");
		}
		else textArea.append("não\n");
		
		textArea.append(" IP Fonte: " + pacoteRecebido.getIPFonte() + "\t\t Porta Fonte: " + pacoteRecebido.getPortaFonte());
		textArea.append("\n IP Destino: " + pacoteRecebido.getIPDestino() + "\t\t Porta Destino: " + pacoteRecebido.getPortaDestino());
		
		if (ctrlInterface.getConexaoTransmissor().getErro()) {
			if (ctrlInterface.getConexaoTransmissor().getTratamentoErro() == Conexao.CRC) {
				String dadosOriginais = "";
				String dadosRecebidos = "";
				char[] dadosECRC12 = pacoteRecebido.getDadosOriginais().toCharArray();
				int limiteDados = dadosECRC12.length-12;
				
				//Removendo o código CRC-12:
				for (int i = 0; i < limiteDados; i++) {
					dadosOriginais += dadosECRC12[i];
				}
				
				dadosECRC12 = pacoteRecebido.getDadosRecebidos().toCharArray();
				
				//Removendo o código CRC-12:
				for (int i = 0; i < limiteDados; i++) {
					dadosRecebidos += dadosECRC12[i];
				}
				
				textArea.append("\n\n Frame Original: (dados+CRC-12) = " + pacoteRecebido.getDadosOriginais() + " - (dados) = " + dadosOriginais + " = " + converterBinarioString(dadosOriginais));
				textArea.append("\n Frame Recebido: (dados+CRC-12) = " + pacoteRecebido.getDadosRecebidos() + " - (dados) = " + dadosRecebidos + " = " + converterBinarioString(dadosRecebidos));
			}
			else {
				char[] bitsCodigo = pacoteRecebido.getDadosOriginais().toCharArray();
				String dadosA = "", dadosB = "";
				
				for (int i = 1; i < bitsCodigo.length; i *= 2) {
					bitsCodigo[bitsCodigo.length-i] = '9';
				}
				
				//Extraindo apenas os dados:
				for (int i = 0; i < bitsCodigo.length; i++) {
					if (bitsCodigo[i] != '9') dadosA += bitsCodigo[i];
				}
				
				textArea.append("\n\n Frame Original: (codigo Hamming) = " + pacoteRecebido.getDadosOriginais() + " - (dados) = " + dadosA + " = " + converterBinarioString(dadosA));
				
				bitsCodigo = pacoteRecebido.getDadosRecebidos().toCharArray();
				dadosB = "";
				
				for (int i = 1; i < bitsCodigo.length; i *= 2) {
					bitsCodigo[bitsCodigo.length-i] = '9';
				}
				
				//Extraindo apenas os dados:
				for (int i = 0; i < bitsCodigo.length; i++) {
					if (bitsCodigo[i] != '9') dadosB += bitsCodigo[i];
				}
				
				//Arrumar erro:
				if (!pacoteRecebido.getDadosOriginais().equals(pacoteRecebido.getDadosRecebidos())) {
					textArea.append("\n\n Frame Recebido: (codigo Hamming) = " + dadosA + " - (dados) = " + dadosA + " = " + converterBinarioString(dadosA) + " [com erro no bit (" + pacoteRecebido.getDadosRecebidos() + ")2 ]");
				}
				else {
					System.out.println("sem erro");
					textArea.append("\n Frame Recebido: (codigo Hamming) = " + pacoteRecebido.getDadosRecebidos() + " - (dados) = " + dadosB + " = " + converterBinarioString(dadosB) + " [sem erro]");
				}
			}
		}
		else {
			textArea.append("\n\n Frame Original: (dados) = " + pacoteRecebido.getDadosOriginais() + " = " + converterBinarioString(pacoteRecebido.getDadosOriginais()));
			textArea.append("\n Frame Recebido: (dados) = " + pacoteRecebido.getDadosRecebidos() + " = " + converterBinarioString(pacoteRecebido.getDadosRecebidos()));
		}
		
		textArea.setAutoscrolls(true);
	}
	
	
	
	private String converterBinarioString(String binario)
	{
		return String.valueOf((char) Integer.parseInt(binario, 2));
	}
	
	
}