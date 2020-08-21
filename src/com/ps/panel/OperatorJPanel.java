package com.ps.panel;

import com.ps.constants.PropertiesDef;
import com.ps.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OperatorJPanel {

    private MainFrame mainFrame;
    //安装工具栏
    private JPanel installJPanel;
    private JLabel installLable;     //安装标签
    private JLabel installStatus;     //安装状态
    private JButton installJbutton;     //安装按钮
    private boolean isInstall;             //服务是否安装

    //运行工具栏
    private JPanel runJPanel;
    private JLabel runLable;     //运行标签
    private JLabel runStatus;     //运行状态
    private JButton runJbutton;     //安装按钮
    private JButton stopJbutton;     //安装按钮
    private JButton restartJbutton;     //安装按钮
    private boolean isRun;                 //服务是否正常启动

    public OperatorJPanel(boolean isInstall, boolean isRun, MainFrame mainFrame) {
        this.isRun = isRun;
        this.isInstall = isInstall;
        this.mainFrame = mainFrame;
        //安钮事件
        ActionMonitor monitor = new ActionMonitor();

        /**
         *安装panel
         */
        this.installLable = new JLabel();    //安装标题
        this.installLable.setText(PropertiesDef.InstallLabelName); //标题

        //判断是否已经安装，如果为true，则禁止重新安装
        this.installStatus = new JLabel();   //安装状态
        if (this.isInstall) {
            this.installStatus.setText(PropertiesDef.InstallStatus); //安装状态
            //this.installStatus.setCaretColor(Color.blue);
        } else {
            this.installStatus.setText(PropertiesDef.NotInstallStatus); //未安装
            //this.installStatus.setCaretColor(Color.red);
        }

        //安装按钮
        this.installJbutton = new JButton(PropertiesDef.InstallButtonName);
        this.installJbutton.addActionListener(monitor);

        this.installJPanel = new JPanel(); //安装panel
        this.installJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.installJPanel.add(installLable);    //标签
        this.installJPanel.add(installStatus);   //状态
        this.installJPanel.add(installJbutton);  //按钮

        /**
         * 运行panel
         */
        this.runLable = new JLabel();    //运行标签
        this.runLable.setText(PropertiesDef.RunLabelName);

        this.runStatus = new JLabel();   //运行状态label
        this.runJbutton = new JButton(PropertiesDef.StartButtonName);        //启动
        this.runJbutton.addActionListener(monitor);
        this.stopJbutton = new JButton(PropertiesDef.StopButtonName);        //停止
        this.stopJbutton.addActionListener(monitor);
        this.restartJbutton = new JButton(PropertiesDef.RestartButtonName);  //重启
        this.restartJbutton.addActionListener(monitor);

        if (this.isRun) { //运行状态
            this.runStatus.setText(PropertiesDef.RunStatus); //运行中
            //this.runStatus.setCaretColor(Color.blue);
            this.runJbutton.setEnabled(false);
            this.restartJbutton.setEnabled(true);
        } else if(this.isInstall){//已经安装，未运行状态
            this.runStatus.setText(PropertiesDef.NotRunStatus); //未运行
            //this.installStatus.setCaretColor(Color.red);
            this.runJbutton.setEnabled(true);
            this.restartJbutton.setEnabled(false);
        } else {//未安装，未运行
            this.runStatus.setText(PropertiesDef.NotRunStatus); //未运行
            //this.installStatus.setCaretColor(Color.red);
            this.runJbutton.setEnabled(false);
            this.stopJbutton.setEnabled(false);
            this.restartJbutton.setEnabled(false);
        }

        this.runJPanel = new JPanel();
        this.runJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.runJPanel.add(this.runLable);
        this.runJPanel.add(this.runStatus);
        this.runJPanel.add(runJbutton);
        this.runJPanel.add(stopJbutton);
        this.runJPanel.add(restartJbutton);

        Container container = this.mainFrame.getContentPane();
        container.add(this.installJPanel, BorderLayout.NORTH);
        container.add(this.runJPanel, BorderLayout.CENTER);
    }

    //tool监听类
    class ActionMonitor implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println("1111111111111111111111111");
            if (event.getSource() == PropertiesDef.InstallButtonName) {       //安装服务
                new TimeDialog().showDialog(mainFrame,"确认安装",10);
            } else if (event.getSource() == PropertiesDef.StartButtonName) { //启动服务
                new TimeDialog().showDialog(mainFrame,"确认启动服务",10);
            } else if (event.getSource() == PropertiesDef.StopButtonName) { //停止服务
                new TimeDialog().showDialog(mainFrame,"确认停止服务",10);
            } else if (event.getSource() == PropertiesDef.RestartButtonName) { //重启服务
                new TimeDialog().showDialog(mainFrame,"确认重启服务",10);
            }
        }

    }

}
