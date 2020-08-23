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

            if (event.getActionCommand() == PropertiesDef.InstallButtonName) {       //安装服务
                installJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame,"确认安装",10);
                if (result==PropertiesDef.TimeDialogNo){
                    installJbutton.setEnabled(true);
                } else { //安装应用操作
                    //TODO 安装服务

                    //安装成功后，可执行安钮允许操作
                    runJbutton.setEnabled(true);
                }
            } else if (event.getActionCommand() == PropertiesDef.StartButtonName) { //启动服务
                runJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame,"确认启动服务",10);
                if (result==PropertiesDef.TimeDialogNo){
                    runJbutton.setEnabled(true);
                } else { //启动服务
                    // TODO 启动服务

                    //服务启动成功后，设置允许进行服务停止和重启操作
                    stopJbutton.setEnabled(true);
                    restartJbutton.setEnabled(true);
                }
            } else if (event.getActionCommand() == PropertiesDef.StopButtonName) { //停止服务
                stopJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame,"确认停止服务",10);
                if (result==PropertiesDef.TimeDialogNo){
                    stopJbutton.setEnabled(true);
                } else { //停止服务
                    //TODO 停止服务

                    runJbutton.setEnabled(true);   //服务停止后，允许启动
                    restartJbutton.setEnabled(false); //服务停止后，不允许重启
                }
            } else if (event.getActionCommand() == PropertiesDef.RestartButtonName) { //重启服务
                restartJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame,"确认重启服务",10);
                if (result==PropertiesDef.TimeDialogNo){
                    restartJbutton.setEnabled(true);
                    stopJbutton.setEnabled(true);
                } else {
                    //TODO 重启服务

                    //设置允许重启操作
                    stopJbutton.setEnabled(true);
                    restartJbutton.setEnabled(true);
                }
            }
        }

    }

}
