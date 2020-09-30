package com.ps.constants;

public class PropertiesDef {
    public static final String MainFrameName = "系统管理";
    public static final String MainFrameTitle = "系统管理 v1.0.0";

    /**
     * 安装区域
     */
    public static final String InstallLabelName = "  安装状态： ";
    public static final String NotInstallStatus = "  未安装  ";
    public static final String InstallStatus = "  已安装  ";
    public static final String InstallButtonName = "安装";
    public static final String ReloadApplicationButtonName = "重新加载应用";
    public static final String IpLable = "　　　　　　　　　　　　　　　　　　　IP列表:";

    /**
     * 运行区域
     */
    public static final String RunLabelName = "  运行状态： ";
    public static final String NotRunStatus = "  未运行  ";
    public static final String RunStatus = "  正在运行  ";
    public static final String StartButtonName = "启动";
    public static final String StopButtonName = "停止";
    public static final String RestartButtonName = "重启";


    public static final int MainFrameWith = 700;
    public static final int MainFrameHigh = 400;

    //运行配置参数
    public static final String DockerServiceName = "Docker*"; //服务名
    public static final String DockerProcessName = "\"Docker Desktop\""; //进程名
    //public static final String DockerImageName = "owlook*";      //docker镜像名

    public static final String DockerProgramName = "DockerDesktopInstaller.exe";           //安装程序名
    public static final String ProgramHomeKey = "PROGRAM_HOME";           //程序运行根目录

    public static final int LogViewRows = 300;
    public static final int LogViewColumns = 650;

    public static final int TimeDialogYes = 0;    //确认
    public static final int TimeDialogNo = 1;     //取消

    public static final String DockerImageName = "Docker.Image.Name";
    public static final String DockerTaskList = "Docker.Task.List";
    public static final String DockerSrcImageName = "Docker.Src.Image.Name";
    public static final String ApplicationContainerName = "Application.Container.Name";
    public static final String NginxContainerName = "Nginx.Container.Name";

    /**
     * docker命令
     */
    public static final String DockerRunCmd = "run";  //运行
    public static final String DockerStartCmd = "start";
    public static final String DockerLoadImageCmd = "loadImage";
    public static final String DockerRestartCmd = "restart";
    public static final String DockerStopCmd = "stop";
    public static final String DockerInitCmd = "init";
    public static final String DockerReInstallCmd = "reInstall";
    public static final String DockerDeleteInstallCmd = "delete";
}
