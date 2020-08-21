package com.ps.panel;

import com.ps.constants.PropertiesDef;

import javax.swing.*;
import java.awt.*;

public class ViewOperatorLogJpanel extends JPanel {
    public static Toolkit toolkit;
    public  Image boardImg;//背景图片
    public ViewOperatorLogJpanel(){
        boardImg = toolkit.getImage("img/board.jpg");
    }

    static {
        toolkit = Toolkit.getDefaultToolkit();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(boardImg, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(PropertiesDef.MainFrameWith,PropertiesDef.MainFrameHigh);
    }
}
