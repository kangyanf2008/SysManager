package com.ps.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeDialog {
	private String message = null;  
	private int secends = 0;  
	private JLabel label = new JLabel();  
	private JButton confirm;   
	private JButton cancel;   
	private JDialog dialog = null;  
	int result = -5;  
	public int  showDialog(JFrame father, String message, int sec) {
		this.message = message;  
		secends = sec;  
		label.setText(message);  
		label.setBounds(80,6,200,20);  
		ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();  
		confirm = new JButton("����");  
		confirm.setBounds(100,40,60,20);  
		confirm.addActionListener(new ActionListener() {              
			public void actionPerformed(ActionEvent e) {  
				result = 0;  
				TimeDialog.this.dialog.dispose();
			}  
		});  
		cancel = new JButton("�ܾ�");  
		cancel.setBounds(190,40,60,20);  
		cancel.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {  
				result = 1;  
				TimeDialog.this.dialog.dispose();
			}  
		});  
		dialog = new JDialog(father, true);  
		dialog.setTitle("��ʾ: �����ڽ���"+secends+"����Զ��ر�");  
		dialog.setLayout(null);  
		dialog.add(label);  
		dialog.add(confirm);  
		dialog.add(cancel);  
		s.scheduleAtFixedRate(new Runnable() {  
			public void run() {  
				TimeDialog.this.secends--;
				if(TimeDialog.this.secends == 0) {
					TimeDialog.this.dialog.dispose();
				}else {  
					dialog.setTitle("��ʾ: �����ڽ���"+secends+"����Զ��ر�");  
				}  
			}  
		}, 1, 1, TimeUnit.SECONDS);  
		dialog.pack();  
		dialog.setSize(new Dimension(350,130));
		dialog.setLocationRelativeTo(father);  
		dialog.setVisible(true);  
		return result;  
	}  
}  
