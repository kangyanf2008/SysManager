::生成快捷方式
@echo off
:CreateDesktopShort
set CUR_DIR=%~dp0
set SrcFile=%CUR_DIR%\dist\win-unpacked\imda-client.exe
set LnkFile=%USERPROFILE%\Desktop\imda-client.exe.lnk
call :CreateShort "%SrcFile%" "%LnkFile%"
goto :eof

::Arguments              目标程序参数
::Description            快捷方式备注
::FullName               返回快捷方式完整路径
::Hotkey                 快捷方式快捷键
::IconLocation           快捷方式图标，不设则使用默认图标
::TargetPath             目标
::WindowStyle            窗口启动状态
::WorkingDirectory       起始位置

:CreateShort
mshta VBScript:Execute("Set a=CreateObject(""WScript.Shell""):Set b=a.CreateShortcut(""%~2.lnk""):b.TargetPath=""%~1"":b.WorkingDirectory=""%~dp1"":b.Save:close")
goto :eof