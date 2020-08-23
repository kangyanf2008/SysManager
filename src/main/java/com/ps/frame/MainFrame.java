package com.ps.frame;

import com.ps.constants.PropertiesDef;
import com.ps.panel.OperatorJPanel;
import com.ps.panel.ViewOperatorLogJPanel;
import com.ps.utils.ServerUtils;

import javax.swing.*;
import java.awt.*;

public class MainFrame  extends JFrame {

    private Container container ;
    private OperatorJPanel operatorJpanel;
    private ViewOperatorLogJPanel viewOperatorLogJpanel;

    //创建窗体ViewOperatorLog
    public MainFrame() {
        this.container = this.getContentPane();
        this.createFrame();
        this.setResizable(false);//
    }

    private void createFrame() {
        this.setTitle(PropertiesDef.MainFrameTitle);
        this.setName(PropertiesDef.MainFrameName);

        //检查docker服务安装是否安装
        boolean isInstall = ServerUtils.checkServerInstall(PropertiesDef.DockerServiceName);
        //检查docker进程和容器是否运行
        boolean isRun = ServerUtils.checkServerRun(PropertiesDef.DockerProcessName, PropertiesDef.DockerImageName);
        //操作区域
        this.operatorJpanel = new OperatorJPanel(isInstall, isRun, this);


        //日志区域
        this.viewOperatorLogJpanel = new ViewOperatorLogJPanel();
        container.add(this.viewOperatorLogJpanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //关闭
        this.setLocationRelativeTo(getOwner());

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        int x = (int)(toolkit.getScreenSize().getWidth()-PropertiesDef.MainFrameWith)/2;
        int y = (int)(toolkit.getScreenSize().getHeight()-PropertiesDef.MainFrameHigh)/2;
        this.setLocation(x, y);


        this.setResizable(false);   //不允许调整大小
        this.pack();
        this.setVisible(true);
    }


}
