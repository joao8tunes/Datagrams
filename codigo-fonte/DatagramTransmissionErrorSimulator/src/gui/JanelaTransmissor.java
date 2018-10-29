package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import datagrama.ConversorBit;
import datagrama.Datagrama;
import sistema.Ctrl_Interface;


public class JanelaTransmissor extends JFrame 
{

	
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextArea textArea;
	private Ctrl_Interface ctrlInterface;

	
	public JanelaTransmissor(final Ctrl_Interface ctrlInterface, String ipPadrao, int portaPadrao)
	{
		this.ctrlInterface = ctrlInterface;
		setTitle("Simulador de Transmissão de Pacotes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		setMinimumSize(new Dimension(600, 400));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblTransmissor = new JLabel("Transmissor");
		lblTransmissor.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
		
		JLabel lblMensagem = new JLabel("Entrada");
		lblMensagem.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		textField = new JTextField();
		textField.setToolTipText("ENTER para enviar a mensagem");
		textField.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
		textField.setColumns(10);
		
		textField.addKeyListener(new KeyListener() 
		{
			@Override
			public void keyTyped(KeyEvent e) 
			{
			}
			
			@Override
			public void keyReleased(KeyEvent e) 
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!textField.getText().isEmpty() && ctrlInterface.validarIP(textField_1.getText().trim())) {
						try {
							int porta = Integer.valueOf(textField_2.getText().trim());
							
							if (porta <= 0) JOptionPane.showMessageDialog(null, "Dados inválidos.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
							else {
								if (!textArea.getText().isEmpty()) textArea.append("\n\n\n ----------------------------------------------------------------\n\n");
								if (!ctrlInterface.getStatusConexao()) textArea.append(" ... IMPOSSÍVEL ACESSAR RECEPTOR ...");
								else {
									textArea.append(" ... NOVA TRANSMISSÃO ...");
									
									Datagrama pacoteEnviado;
									Random possibilidadeErros = new Random();
									char[] dadosEmCaracteres = textField.getText().trim().toCharArray();
									boolean ack;
									
									//Enviando requisição de transmissão (id = -1):
									ctrlInterface.transmitirDatagrama(ctrlInterface.getConexaoTransmissor().getIP(), ctrlInterface.getConexaoTransmissor().getPorta(), textField_1.getText().trim(), porta, "n", -1);
									
									//Enviando um frame por vez, cada qual contendo um único caractere da mensagem a ser enviada:
									for (int i = 0; i < dadosEmCaracteres.length; i++) {
										textArea.append("\n .............................\n");
										ack = ctrlInterface.transmitirDatagrama(ctrlInterface.getConexaoTransmissor().getIP(), ctrlInterface.getConexaoTransmissor().getPorta(), textField_1.getText().trim(), porta, String.valueOf(dadosEmCaracteres[i]), i);
										atualizarLog(ctrlInterface.getUltimoPacote(), ack);
										
										//Retransmitindo se receber NACK:
										while (!ack) {    //N tentativas de enviar o frame corretamente...
											ack = ctrlInterface.transmitirDatagrama(ctrlInterface.getConexaoTransmissor().getIP(), ctrlInterface.getConexaoTransmissor().getPorta(), textField_1.getText().trim(), porta, String.valueOf(dadosEmCaracteres[i]), i);
											textArea.append("\n ..............\n");
											atualizarLog(ctrlInterface.getUltimoPacote(), ack);
										}  
									}	
								}
							}
						}
						catch (NumberFormatException ne) {
							JOptionPane.showMessageDialog(null, "Dados incorretos.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						}
					} 
					else JOptionPane.showMessageDialog(null, "Dados incorretos.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) 
			{
			}
		});
		
		
		JLabel lblTipoDeCanal = new JLabel("Tipo de canal");
		lblTipoDeCanal.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Ideal", "Erros"}));
		
		comboBox.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (comboBox.getSelectedIndex() == 0) ctrlInterface.getConexaoTransmissor().setErro(false);
				else ctrlInterface.getConexaoTransmissor().setErro(true);
			}
		});
		
		JLabel lblLog = new JLabel("Log");
		lblLog.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"CRC-12", "Hamming (par)"}));
		
		comboBox_1.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (comboBox_1.getSelectedIndex() == 0) ctrlInterface.getConexaoTransmissor().setTratamentoErro(Conexao.CRC);
				else ctrlInterface.getConexaoTransmissor().setTratamentoErro(Conexao.HAMMING);
			}
		});
		
		JLabel lblManipulaoDeErro = new JLabel("Manipulação de Erros");
		lblManipulaoDeErro.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		JSeparator separator = new JSeparator();
		
		JButton btnLimparLog = new JButton("Limpar Log");
		
		btnLimparLog.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		
		JLabel lblIpvReceptor = new JLabel("IPv4 Receptor");
		lblIpvReceptor.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setText(ipPadrao);
		
		JLabel lblPortaReceptor = new JLabel("Porta Receptor");
		lblPortaReceptor.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setText(String.valueOf(portaPadrao));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
						.addComponent(lblTransmissor, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblMensagem)
							.addGap(10)
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblTipoDeCanal)
									.addGap(25))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)))
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblManipulaoDeErro)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblIpvReceptor, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
									.addGap(28)
									.addComponent(lblPortaReceptor, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblLog, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 370, Short.MAX_VALUE)
							.addComponent(btnLimparLog)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addComponent(lblTransmissor)
					.addGap(9)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMensagem))
					.addGap(25)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTipoDeCanal)
						.addComponent(lblManipulaoDeErro)
						.addComponent(lblIpvReceptor)
						.addComponent(lblPortaReceptor))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLog)
						.addComponent(btnLimparLog))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
					.addGap(10))
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
	
	
	public void atualizarLog(Datagrama pacoteRecebido, boolean ack)
	{
		if (ack) textArea.append(" ID: " + pacoteRecebido.getID() + "\t\t\t\t\t ACK\n");
		else textArea.append(" ID: " + pacoteRecebido.getID() + "\t\t\t\t\t NACK [retransmissão]\n");

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