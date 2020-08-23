package com.ps.panel;

import com.ps.constants.PropertiesDef;
import com.ps.queue.LogQueue;

import javax.swing.*;
import java.awt.*;

public class ViewOperatorLogJPanel extends JPanel {
    public TextArea logMessage;
    public static Toolkit toolkit;
    public  Image boardImg;//背景图片
    static {
        toolkit = Toolkit.getDefaultToolkit();
    }

    public ViewOperatorLogJPanel(){
        //boardImg = toolkit.getImage("img/board.jpg");
        setLayout(new BorderLayout());
        this.logMessage = new TextArea("",PropertiesDef.LogViewRows,PropertiesDef.LogViewColumns,TextArea.SCROLLBARS_VERTICAL_ONLY);
        this.add(this.logMessage);

        new Thread(new LogViewRun(this.logMessage)).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //g.drawImage(boardImg, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(PropertiesDef.MainFrameWith,PropertiesDef.MainFrameHigh);
    }

    class LogViewRun implements Runnable {
        public TextArea logMessage;
        public LogViewRun(TextArea logMessage) {
            this.logMessage = logMessage;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    String log = LogQueue.Poll();
                    this.logMessage.append(log+"\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    this.logMessage.append(e.getMessage()+"\n");
                }
            }
        }
    }
}
