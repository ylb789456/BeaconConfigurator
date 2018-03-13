package fr.polytech.com.tools;

import java.awt.Cursor;  
import java.awt.Point;  
import java.awt.Rectangle;  
import java.awt.event.MouseAdapter;  
import java.awt.event.MouseEvent;  
  
import javax.swing.JButton;  
import javax.swing.JComponent;  
import javax.swing.JFrame;  
import javax.swing.JList;  
import javax.swing.JPanel;  
import javax.swing.JTable;  
import javax.swing.JTree;  
public class DnDAdapter extends MouseAdapter  
{  
    //每个组件的信息，在进入组件时获得。  
    private JComponent jcom;  
    private Point jcomOriginPoint;//组件在容器内的坐标  
    private int jcomWidth,jcomHeight;  
    //与定位、缩放相关信息  
    private int a=10;//鼠标检测边沿大小  
    private    Point pointMoved;//移动时，容器内坐标  
    private CurType curCuror;//  
    private enum  CurType {LeftTop,LeftBottom,RightTop,RightBottom,Top,Bottom,Left,Right,Center}  
    //光标区域判断  
    private  CurType getCurType(MouseEvent e)  
    {  
        Point p=e.getPoint();//getPoint返回组件内的坐标  
        //左上角 0<x<a,0<y<a  
        if((p.x>=0 && p.x<=a)&&(p.y>=0&&p.y<=a))  
            return CurType.LeftTop;  
        //右下角 w-a<x<w  h-a<y<h  
        else if((p.x>=jcomWidth-a&&p.x<=jcomWidth)&&(p.y>=jcomHeight-a&&p.y<=jcomHeight))  
            return CurType.RightBottom;  
        //右上角w-a<x<w,0<y<a  
        else if((p.x>=jcomWidth-a&&p.x<=jcomWidth)&&(p.y>=0&&p.y<=a))  
            return CurType.RightTop;  
        //左下角0<x<a,h-a<y<h  
        else if((p.x>=0&&p.x<=a)&&(p.y>=jcomHeight-a&&p.y<=jcomHeight))  
            return CurType.LeftBottom;  
        //垂直：w-a<x<w && a<y<h-a 右边  
        else if( p.x>=jcomWidth-a && p.x<=jcomWidth && p.y>=a && p.y<=jcomHeight-a)  
            return CurType.Right;  
        //垂直：0<x<a,a<y<h-a 左边  
        else if(p.x>=0 && p.x<=a && p.y>=a && p.y<=jcomHeight-a)  
            return CurType.Left;  
        //水平 a<x<w-a && 0<y<a  上边  
        else if((p.x>=a&&p.x<=jcomWidth-a) &&p.y>=0&&p.y<=a)  
            return CurType.Top;  
        //水平 a<x<w-a &&h-a<y<h  下边  
        else if((p.x>=a&&p.x<=jcomWidth-a) &&p.y>=jcomHeight-a&&p.y<=jcomHeight)  
            return CurType.Bottom;  
        //中间  
        else  
            //jcom.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));//没有这句鼠标会一直保持形状  
            return CurType.Center;  
          
    }  
    private void setCurType(MouseEvent e)  
    {  
        switch(getCurType(e))  
        {  
        case LeftTop:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));  
            curCuror=CurType.LeftTop;  
            break;  
        case LeftBottom:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));  
            curCuror=CurType.LeftBottom;  
            break;  
        case RightTop:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));  
            curCuror=CurType.RightTop;  
            break;  
        case RightBottom:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));  
            curCuror=CurType.RightBottom;  
            break;  
        case Top:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));  
            curCuror=CurType.Top;  
            break;  
        case Bottom:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));  
            curCuror=CurType.Bottom;  
            break;  
        case Left:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));  
            curCuror=CurType.Left;  
            break;  
        case Right:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));  
            curCuror=CurType.Right;  
            break;  
        default:  
            jcom.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));  
            curCuror=CurType.Center;  
            break;              
        }  
    }  
    private void setPosition(MouseEvent e)  
    {  
        Point pointNow=e.getLocationOnScreen();  
        int incX=pointNow.x-pointMoved.x;//增量是相对于最后一次移动的点。  
        int incY=pointNow.y-pointMoved.y;  
        ///  
        //System.out.print("PointNow:("+pointNow.x+","+pointNow.y+")\t");  
        //System.out.println("PointMoved:("+pointMoved.x+","+pointMoved.y+")\t");  
        Rectangle rect=new Rectangle();  
        switch(curCuror)  
        {  
        case LeftTop:  
            rect.x=jcomOriginPoint.x+incX;  
            rect.y=jcomOriginPoint.y+incY;  
            rect.width=jcomWidth-incX;  
            rect.height=jcomHeight-incY;  
            break;  
        case LeftBottom:  
            rect.x=jcomOriginPoint.x+incX;  
            rect.y=jcomOriginPoint.y;  
            rect.width=jcomWidth-incX;  
            rect.height=jcomHeight+incY;  
            break;  
        case RightTop:  
            rect.x=jcomOriginPoint.x;  
            rect.y=jcomOriginPoint.y+incY;  
            rect.width=jcomWidth+incX;  
            rect.height=jcomHeight-incY;  
            break;  
        case RightBottom:  
            rect.x=jcomOriginPoint.x;  
            rect.y=jcomOriginPoint.y;  
            rect.width=jcomWidth+incX;  
            rect.height=jcomHeight+incY;  
            break;  
            ////////////////////////////////////////////  
        case Top:  
            rect.x=jcomOriginPoint.x;  
            rect.y=jcomOriginPoint.y+incY;  
            rect.width=jcomWidth;  
            rect.height=jcomHeight-incY;  
            break;  
        case Bottom:  
            rect.x=jcomOriginPoint.x;  
            rect.y=jcomOriginPoint.y;  
            rect.width=jcomWidth;  
            rect.height=jcomHeight+incY;  
            break;  
        case Left:  
            rect.x=jcomOriginPoint.x+incX;  
            rect.y=jcomOriginPoint.y;  
            rect.width=jcomWidth-incX;  
            rect.height=jcomHeight;  
            break;  
        case Right://右缩放  
            rect.x=jcomOriginPoint.x;  
            rect.y=jcomOriginPoint.y;  
            rect.width=jcomWidth+incX;  
            rect.height=jcom.getHeight();  
            break;  
            ///////////////////////////////////////////////  
        case Center://移动  
            rect.x=jcomOriginPoint.x+incX;  
            rect.y=jcomOriginPoint.y+incY;  
            rect.width=jcom.getWidth();//防止出现恢复原大小问题。  
            rect.height=jcom.getHeight();  
            break;      
        default:  
            break;              
        }  
        jcom.setBounds(rect);  
    }  
    @Override  
    public void mouseMoved(MouseEvent e)   
    {  
        pointMoved=e.getLocationOnScreen();  
        jcom=(JComponent)e.getSource();//防止抖动关键在这一点  
        jcomOriginPoint=jcom.getLocation();//获取组件在父窗口的位置  
        jcomWidth=jcom.getWidth();  
        jcomHeight=jcom.getHeight();  
//        System.out.print("Old--------------------");  
//        System.out.print("Position:("+jcomOriginPoint.x+","+jcomOriginPoint.y+")\t");  
//        System.out.println(" Width:"+jcomWidth+"  Height:"+jcomHeight);  
        ///////////////////////////////////////////  
        setCurType(e);  
        //super.mouseMoved(e);  
    }  
    public void mouseDragged(MouseEvent e)   
    {  
        //相对于移动最后一点的增量  
        setPosition(e);  
        super.mouseDragged(e);  
    } 
    
    public static void main(String[] args)   
    {  
        JFrame frm=new JFrame();  
        JPanel panel=new JPanel(null);  
        frm.setSize(600    , 600);  
        frm.setLocation(100, 100);  
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        panel.setSize(600, 600);  
        /////////////////////////////////////////  
        JButton btn=new JButton("Button");  
        btn.setSize(100, 100);  
        btn.setLocation(100, 100);  
        DnDAdapter dnd=new DnDAdapter();  
        btn.addMouseMotionListener(dnd);
        JButton btn2=new JButton("Button");  
        btn2.setSize(100, 100);  
        btn2.setLocation(300, 100);  
        btn2.addMouseMotionListener(dnd);
        //btn.addMouseListener(dnd);  
        //btn.removeMouseMotionListener(l);  
        panel.add(btn);
        panel.add(btn2);
        frm.add(panel);      
        frm.setVisible(true);  
          
    }  
}  
