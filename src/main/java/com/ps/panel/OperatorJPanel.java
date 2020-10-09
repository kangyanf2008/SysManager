package com.ps.panel;

import com.ps.config.LoadConfig;
import com.ps.constants.PropertiesDef;
import com.ps.env.Env;
import com.ps.frame.MainFrame;
import com.ps.queue.LogQueue;
import com.ps.utils.IpUtils;
import com.ps.utils.ServerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class OperatorJPanel {

    private MainFrame mainFrame;
    //安装工具栏
    private JPanel installJPanel;
    private JLabel installLable;     //安装标签
    private JLabel installStatus;     //安装状态
    private JButton installJbutton;   //安装按钮
    private JButton reloadJbutton;    //重新加载
    private volatile boolean isInstall;             //服务是否安装
    private JLabel ipLable;     //IP列表
    private JComboBox<String> selectIp;   //IP下拉框

    private volatile String nginxIP;
    private volatile String applicationIp;

    //运行工具栏
    private JPanel runJPanel;
    private JLabel runLable;     //运行标签
    private JLabel runStatus;     //运行状态
    private JButton runJbutton;     //安装按钮
    private JButton stopJbutton;     //安装按钮
    private JButton restartJbutton;     //安装按钮
    private volatile boolean dockerIsRun;                 //服务是否正常启动
    private volatile boolean imageIsRun;                 //服务是否正常启动
    private volatile boolean imageIsLoad;                //镜像是否加载
    private JLabel waringLable;     //警告标签


    public OperatorJPanel(boolean isInstall, boolean dockerIsRun, boolean imageIsRun, boolean imageIsLoad, MainFrame mainFrame) {
        this.dockerIsRun = dockerIsRun;
        this.isInstall = isInstall;
        this.imageIsRun = imageIsRun;
        this.imageIsLoad = imageIsLoad;
        this.mainFrame = mainFrame;

        //安钮事件
        ActionMonitor monitor = new ActionMonitor();

        /**
         *安装panel
         */
        this.installLable = new JLabel();    //安装标题
        this.installLable.setText(PropertiesDef.InstallLabelName); //标题

        this.selectIp = new JComboBox();                 //IP选择框
        //读取本机IP
        java.util.List<String> localIPs = IpUtils.getLocalIp();
        Map<String,String> localIPNetWorkMap = new HashMap<String,String>();
        if (localIPs != null && localIPs.size() > 0)  {
            localIPs.forEach(o -> {
                this.selectIp.addItem(o);
                localIPNetWorkMap.put(o,o);
            });
        }
        this.reloadJbutton = new JButton(PropertiesDef.ReloadApplicationButtonName);        //重新加载应用
        this.reloadJbutton.addActionListener(monitor);
        boolean reload = false;
        //查看应用镜像IP地址是否不存在
        String applicationImageHostIp = ServerUtils.hostIp(LoadConfig.getConfig(PropertiesDef.ApplicationContainerName), false);
        //应用IP
        if (applicationImageHostIp != null && applicationImageHostIp != "") {
            applicationIp = applicationImageHostIp;
            //reload应用
            if (!localIPNetWorkMap.containsKey(applicationIp)) {
                reloadJbutton.setEnabled(true);
                reload = true;
            }
        }

        //查看应用镜像IP地址是否不存在
        String nginxImageHostIp = ServerUtils.hostIp(LoadConfig.getConfig(PropertiesDef.NginxContainerName), false);
        //nginxIp
        if (nginxImageHostIp != null && nginxImageHostIp != "") {
            nginxIP = nginxImageHostIp;
            //reload应用
            if (!localIPNetWorkMap.containsKey(nginxIP)) {
                reloadJbutton.setEnabled(true);
                reload = true;
            }
        }
        if (!reload) { //是否允许点击新新加载铵钮
            reloadJbutton.setEnabled(false);
        }

        this.ipLable = new JLabel();
        this.ipLable.setText(PropertiesDef.IpLable);    //ip标签

        //判断是否已经安装，如果为true，则禁止重新安装
        //安装按钮
        this.installJbutton = new JButton(PropertiesDef.InstallButtonName);
        this.installJbutton.addActionListener(monitor);
        this.installStatus = new JLabel();   //安装状态
        if (this.isInstall) {
            this.installJbutton.setEnabled(false);
            this.installStatus.setText(PropertiesDef.InstallStatus); //安装状态
        } else {
            this.installJbutton.setEnabled(true);
            this.installStatus.setText(PropertiesDef.NotInstallStatus); //未安装
        }

        this.installJPanel = new JPanel(); //安装panel
        this.installJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.installJPanel.add(installLable);    //标签
        this.installJPanel.add(installStatus);   //状态
        this.installJPanel.add(installJbutton);  //按钮
        this.installJPanel.add(reloadJbutton);  //重新加载
        this.installJPanel.add(this.ipLable);    //ip标签
        this.installJPanel.add(this.selectIp);   //IP下拉框

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
        this.waringLable = new JLabel();

        if (this.dockerIsRun && this.imageIsRun) { //运行状态
            this.runStatus.setText(PropertiesDef.RunStatus); //运行中
            this.runJbutton.setEnabled(false);
            this.installJbutton.setEnabled(false);
            this.restartJbutton.setEnabled(true);
        } else if (this.isInstall) {//已经安装，未运行状态
            this.installJbutton.setEnabled(false);
            this.runStatus.setText(PropertiesDef.NotRunStatus); //未运行
            this.runJbutton.setEnabled(true);
            this.restartJbutton.setEnabled(false);
            if (!imageIsRun) { //未运行状态
                runStatus.setText(PropertiesDef.NotRunStatus); //运行中
                runJbutton.setEnabled(true);
                restartJbutton.setEnabled(false);
                stopJbutton.setEnabled(false);
            }
        } else {//未安装，未运行
            this.runStatus.setText(PropertiesDef.NotRunStatus); //未运行
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
        this.runJPanel.add(restartJbutton);//重启按钮
        this.runJPanel.add(waringLable);  //警告标签

        //容器显示内容
        Container container = this.mainFrame.getContentPane();
        container.add(this.installJPanel, BorderLayout.NORTH);
        container.add(this.runJPanel, BorderLayout.CENTER);

        //异步检查安装和运行状态
        new Thread(new CheckInstallRunStatus(this, false)).start();

    }

    //tool监听类
    class ActionMonitor implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            if (event.getActionCommand() == PropertiesDef.InstallButtonName) {       //安装服务
                installJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame, "确认安装", 10);
                if (result == PropertiesDef.TimeDialogNo) {
                    installJbutton.setEnabled(true);
                } else { //安装应用操作
                    try {
                        LogQueue.Push("\r\n");
                        LogQueue.Push("####### 正在安装启动服务 #######");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //安装docker服务
                    if (ServerUtils.installProgram(Env.getEnv(PropertiesDef.ProgramHomeKey) + PropertiesDef.DockerProgramName, true)) {

                        //安装成功后，可执行安钮允许操作
                        new Thread(new Runnable() { //异步检查是否处理成功
                            @Override
                            public void run() {
                                //验证docker服务是否启动
                                while (true) {
                                    boolean isInstall = ServerUtils.checkServerInstall(PropertiesDef.DockerServiceName, false);
                                    if (isInstall) {
                                        try {
                                            LogQueue.Push("####### 服务安装成功 #######");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        int result = new TimeDialog().showDialog(mainFrame, "请立即重启机器", 30);
                                        if (result == PropertiesDef.TimeDialogYes) {
                                            OperatorJPanel.this.dockerIsRun = true;
                                            installJbutton.setEnabled(false);   //服务停止后，允许启动
                                            OperatorJPanel.this.installStatus.setText(PropertiesDef.InstallStatus); //运行状态
                                            //重启机器
                                            try {
                                                LogQueue.Push("####### 正在重启机器 #######");
                                                TimeUnit.SECONDS.sleep(1L);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            ServerUtils.execute("shutdown -r -t 0", true);
                                            break;
                                        } else {
                                            new TimeDialog().showDialog(mainFrame, "程序退出", 1);
                                            System.exit(0);
                                        }

                                    }
                                    try {
                                        TimeUnit.SECONDS.sleep(2L);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }//end run
                        }).start();

                    } else {
                        installJbutton.setEnabled(true);
                    }
                }
            } else if (event.getActionCommand() == PropertiesDef.StartButtonName) { //启动服务
                runJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame, "确认启动服务", 10);
                if (result == PropertiesDef.TimeDialogNo) {
                    runJbutton.setEnabled(true);
                    return;
                }
                try {
                    LogQueue.Push("\r\n");
                    LogQueue.Push("####### 正在启动服务...... #######");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //未存在镜像，则进行加载操作
                if (!ServerUtils.checkImageLoad(false)) {
                    try {
                        LogQueue.Push("\r\n");
                        LogQueue.Push("####### 服务正在加载...... #######");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //加载docker镜像成功
                    ServerUtils.dockerServiceOperator(PropertiesDef.DockerLoadImageCmd, true);
                    //验证加载镜像是否成功
                    while (true) {
                        boolean isLoad = ServerUtils.checkImageLoad(false);
                        if (isLoad) {
                            try {
                                LogQueue.Push("\r\n");
                                LogQueue.Push("####### 服务加载成功 #######");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runJbutton.setEnabled(false);
                            imageIsLoad = true;
                            runJbutton.setEnabled(false);   //服务停止后，允许启动
                            restartJbutton.setEnabled(false); //服务停止后，不允许重启
                            runStatus.setText(PropertiesDef.RunLabelName); //运行状态
                            break;
                        }//end if
                        try {
                            TimeUnit.SECONDS.sleep(2L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }//end while
                } // end if

                //验证镜像是否有历史执行。未存在，则进行初始化操作
                if (!ServerUtils.checkImageHaveRunHistory(false)) {
                    try {
                        LogQueue.Push("\r\n");
                        LogQueue.Push("####### 服务正在启动...... #######");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //初始化系统镜像
                    ServerUtils.dockerServiceOperator(PropertiesDef.DockerInitCmd, true);
                    //验证镜像是否启动成功
                    while (true) {
                        boolean isRun = ServerUtils.checkImageRun(false);
                        if (isRun) {
                            try {
                                LogQueue.Push("\r\n");
                                LogQueue.Push("####### 服务启动成功 #######");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runJbutton.setEnabled(false);
                            imageIsRun = true;
                            dockerIsRun = true;
                            runJbutton.setEnabled(false);   //启动成功后，不允许再运行
                            stopJbutton.setEnabled(true); //允许服务停止
                            restartJbutton.setEnabled(true); //允许服务重启
                            runStatus.setText(PropertiesDef.RunLabelName); //运行状态
                            break;
                        }//end if
                        try {
                            TimeUnit.SECONDS.sleep(2L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } // end while
                } else {
                    //启动镜像
                    try {
                        LogQueue.Push("####### 服务正在启动...... #######");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ServerUtils.dockerServiceOperator(PropertiesDef.DockerRestartCmd, true);
                    boolean IsRun = ServerUtils.checkImageRun(false);
                    if (IsRun) {
                        //服务启动成功后，设置允许进行服务停止和重启操作
                        stopJbutton.setEnabled(true);
                        restartJbutton.setEnabled(true);
                        runStatus.setText(PropertiesDef.RunStatus); //运行状态
                        runJbutton.setEnabled(false);
                        imageIsRun = true;
                        try {
                            LogQueue.Push("####### 启动服务成功 #######");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        runStatus.setText(PropertiesDef.NotRunStatus); //未运行
                        runJbutton.setEnabled(true);
                        imageIsRun = false;
                        try {
                            LogQueue.Push("####### 启动服务失败 #######");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    dockerIsRun = true;
                }

            } else if (event.getActionCommand() == PropertiesDef.StopButtonName) { //停止服务
                stopJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame, "确认停止服务", 10);
                if (result == PropertiesDef.TimeDialogNo) {
                    stopJbutton.setEnabled(true);
                } else { //停止服务
                    try {
                        LogQueue.Push("\r\n");
                        LogQueue.Push("####### 正在停止服务 #######");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ServerUtils.dockerServiceOperator(PropertiesDef.DockerStopCmd, true)) {
                        boolean imageIsRun = ServerUtils.checkImageRun(true);
                        if (!imageIsRun) {
                            imageIsRun = false;
                            runJbutton.setEnabled(true);   //服务停止后，允许启动
                            restartJbutton.setEnabled(false); //服务停止后，不允许重启
                            runStatus.setText(PropertiesDef.NotRunStatus); //运行状态

                            try {
                                LogQueue.Push("####### 停止服务成功 #######");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            runStatus.setText(PropertiesDef.RunStatus); //未运行
                            runJbutton.setEnabled(false);   //服务停止后，允许启动
                            restartJbutton.setEnabled(true); //服务停止后，不允许重启
                            imageIsRun = true;
                            try {
                                LogQueue.Push("####### 停止服务失败 #######");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
            } else if (event.getActionCommand() == PropertiesDef.RestartButtonName) { //重启服务
                restartJbutton.setEnabled(false);
                stopJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame, "确认重启服务", 10);
                if (result == PropertiesDef.TimeDialogNo) {
                    restartJbutton.setEnabled(true);
                    stopJbutton.setEnabled(true);
                } else {
                    try {
                        LogQueue.Push("\r\n");
                        LogQueue.Push("####### 正在重启服务 #######");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ServerUtils.dockerServiceOperator(PropertiesDef.DockerRestartCmd, false)) {
                        boolean imageIsRun = ServerUtils.checkImageRun(false);
                        if (imageIsRun) {
                            //设置允许重启操作
                            runStatus.setText(PropertiesDef.RunStatus); //运行状态
                            stopJbutton.setEnabled(true);
                            restartJbutton.setEnabled(true);
                            runJbutton.setEnabled(false);
                            imageIsRun = true;
                            try {
                                LogQueue.Push("####### 重启成功 #######");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else {
                            runStatus.setText(PropertiesDef.NotRunStatus); //未运行
                            runJbutton.setEnabled(true);
                            stopJbutton.setEnabled(false);
                            restartJbutton.setEnabled(false);
                            imageIsRun = false;
                            try {
                                LogQueue.Push("####### 重启失败 #######");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        dockerIsRun = true;
                    } else {
                        try {
                            LogQueue.Push("####### 先重新安装 #######");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        installJbutton.setEnabled(true);
                        installStatus.setText(PropertiesDef.NotInstallStatus); //未安装
                        imageIsRun = false;
                        dockerIsRun = false;
                    }

                }//end if
            } else if ( event.getActionCommand() == PropertiesDef.ReloadApplicationButtonName ){ //重新加载应用
                reloadJbutton.setEnabled(false);
                int result = new TimeDialog().showDialog(mainFrame, "确认新加载应用！", 10);
                if (result == PropertiesDef.TimeDialogNo) {
                    reloadJbutton.setEnabled(true);
                    return;
                }
                try {
                    LogQueue.Push("\r\n");
                    LogQueue.Push("####### 正在重新加载应用 #######");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String ip = OperatorJPanel.this.selectIp.getSelectedItem().toString();

                //查看应用镜像IP地址是否不存在
                String applicationImageHostIp = ServerUtils.hostIp(LoadConfig.getConfig(PropertiesDef.ApplicationContainerName), false);
                //应用IP
                if (applicationImageHostIp != null && !ip.equals(applicationImageHostIp)) {
                    //应用重新加载
                    ServerUtils.dockerServiceOperator(PropertiesDef.DockerReloadCmd + " terminal-web-tenant " + ip, true);
                }

                //查看应用镜像IP地址是否不存在
                String nginxImageHostIp = ServerUtils.hostIp(LoadConfig.getConfig(PropertiesDef.NginxContainerName), false);
                //nginxIp
                if (nginxImageHostIp != null && !ip.equals(nginxImageHostIp)) {
                    //重新加载nginx应用
                    ServerUtils.dockerServiceOperator(PropertiesDef.DockerReloadCmd + " nginx " + ip, true);
                }
                try {
                    LogQueue.Push("\r\n");
                    LogQueue.Push("####### 应用加载完成 #######");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }//end else if (event.getActionCommand() == PropertiesDef.RestartButtonName) { //重启服务

    }

    //检查运行状态线程
    class CheckInstallRunStatus implements Runnable {
        private OperatorJPanel operatorJPanel;
        private boolean isLog;

        public CheckInstallRunStatus(OperatorJPanel operatorJPanel, boolean isLog) {
            this.operatorJPanel = operatorJPanel;
            this.isLog = isLog;
        }

        @Override
        public void run() {
            boolean isReload = false;
            while (true) {
                //检查docker服务安装是否安装
                boolean isInstall = ServerUtils.checkServerInstall(PropertiesDef.DockerServiceName, this.isLog);
                //检查docker进程和容器是否运行
                boolean dockerIsRun = ServerUtils.checkDockerRun(PropertiesDef.DockerProcessName, this.isLog);
                boolean imageIsRun = ServerUtils.checkImageRun(this.isLog);

                //读取本机IP
                java.util.List<String> ips = IpUtils.getLocalIp();

                try {
                    if (ips != null && ips.size() > 0) {
                        Map<String,String> localIPNetWorkMap = new HashMap<String,String>();
                        Map<String,String> selectIpMap = new HashMap<String,String>();
                        //把本机网卡IP放到map
                        ips.forEach(o->{
                            //如果本机select下拉菜单不存在数据，则放
                            localIPNetWorkMap.put(o,o);
                        });

                        //判断本机IP和select下拉框IP是否有变化，假如有变化，则进行重新删除所有select的IP下拉框，进行重新加载
                        AtomicBoolean isReloadSelect = new AtomicBoolean(false);
                        for (int i=0; i < selectIp.getItemCount();i++) {
                            String item =  selectIp.getItemAt(i);
                            //保存需要删除的select列表
                            if (!localIPNetWorkMap.containsKey(item)) {
                                isReloadSelect.set(true);
                                break;
                            }
                            selectIpMap.put(item, item);
                        }
                        //判断本机网卡IP是否跟select下拉菜单有差异
                        if (!isReloadSelect.get()) {
                            ips.forEach(o->{
                                if (!selectIpMap.containsKey(o)) {
                                    isReloadSelect.set(true);
                                    return;
                                }
                            });
                        }

                        if (isReloadSelect.get()) {
                            selectIp.removeAllItems();
                            selectIp.removeAll();
                            ips.forEach(o->{
                                selectIp.addItem(o);
                            });
                        }

                        boolean reload = false;
                        //查看应用镜像IP地址是否不存在
                        String applicationImageHostIp = ServerUtils.hostIp(LoadConfig.getConfig(PropertiesDef.ApplicationContainerName), false);
                        //应用IP
                        if (applicationImageHostIp != null && applicationImageHostIp != "") {
                            applicationIp = applicationImageHostIp;
                            //reload应用
                            if (!localIPNetWorkMap.containsKey(applicationIp)) {
                                reloadJbutton.setEnabled(true);
                                reload = true;
                                waringLable.setText(PropertiesDef.NOT_EXIT_IP + applicationIp);
                            }
                        }

                        //查看应用镜像IP地址是否不存在
                        String nginxImageHostIp = ServerUtils.hostIp(LoadConfig.getConfig(PropertiesDef.NginxContainerName), false);
                        //nginxIp
                        if (nginxImageHostIp != null && nginxImageHostIp != "") {
                            nginxIP = nginxImageHostIp;
                            //reload应用
                            if (!localIPNetWorkMap.containsKey(nginxIP)) {
                                reloadJbutton.setEnabled(true);
                                reload = true;
                                waringLable.setText(PropertiesDef.NOT_EXIT_IP + nginxIP);
                            }
                        }
                        if (!reload) { //允许点击
                            reloadJbutton.setEnabled(false);
                            waringLable.setText("");
                        }
                    } //

                    operatorJPanel.isInstall = isInstall;
                    operatorJPanel.dockerIsRun = imageIsRun;
                    if (dockerIsRun && imageIsRun) { //运行状态
                        operatorJPanel.runStatus.setText(PropertiesDef.RunStatus); //运行中
                        operatorJPanel.runJbutton.setEnabled(false);
                        operatorJPanel.restartJbutton.setEnabled(true);
                    } else if (isInstall) {//已经安装，未运行状态
                        operatorJPanel.runStatus.setText(PropertiesDef.NotRunStatus); //未运行
                        operatorJPanel.runJbutton.setEnabled(true);
                        operatorJPanel.restartJbutton.setEnabled(false);
                        if (!imageIsRun) { //未运行状态
                            operatorJPanel.runStatus.setText(PropertiesDef.NotRunStatus); //运行中
                            operatorJPanel.runJbutton.setEnabled(true);
                            operatorJPanel.restartJbutton.setEnabled(false);
                            operatorJPanel.stopJbutton.setEnabled(false);
                        }
                    } else {//未安装，未运行
                        operatorJPanel.runStatus.setText(PropertiesDef.NotRunStatus); //未运行
                        operatorJPanel.runJbutton.setEnabled(false);
                        operatorJPanel.stopJbutton.setEnabled(false);
                        operatorJPanel.restartJbutton.setEnabled(false);
                    }
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
