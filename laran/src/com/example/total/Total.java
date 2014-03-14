package com.example.total;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
  
/** 
 * 比较两张图片的相似度 
 * @author GuanFengHua
 * 
 */  
public class Total {  
    // 改变成二进制码  
    public static String[][] getPX(String args) {  
        int[] rgb = new int[3];  
        Bitmap bi=BitmapFactory.decodeFile(args);
        int[] pixels = new int[bi.getWidth()*bi.getHeight()];//保存所有的像素的数组，图片宽×高

        bi.getPixels(pixels,0,bi.getWidth(),0,0,bi.getWidth(),bi.getHeight());
        String[][] list = new String[bi.getWidth()][bi.getHeight()];  
        for (int i = 0; i <bi.getWidth()/2; i++) {  
            for (int j = 0; j < bi.getHeight()/2; j++) {  
                int pixel =bi.getPixel(i, j);
                rgb[0] = (pixel & 0xff0000) >> 16;  
                rgb[1] = (pixel & 0xff00) >> 8;  
                rgb[2] = (pixel & 0xff);  
                list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];  
  
            }  
        }  
        return list;  
  
    }  
   
    public static int compareImage(String imgPath1, String imgPath2){  
        String[] images = {imgPath1, imgPath2};  
        if (images.length == 0) {  
            System.out.println("Usage >java BMPLoader ImageFile.bmp");  
            System.exit(0);  
        }  
  
        // 分析图片相似度 begin  
        String[][] list1 = getPX(images[0]);  
        String[][] list2 = getPX(images[1]);  
        int xiangsi = 0;  
        int busi = 0;  
        int i = 0, j = 0;  
        for (String[] strings : list1) {  
            if ((i + 1) == list1.length) {  
                continue;  
            }  
            for (int m=0; m<strings.length/3; m++) {  
                try {  
                    String[] value1 = list1[i][j].toString().split(",");  
                    String[] value2 = list2[i][j].toString().split(",");  
                    int k = 0;  
                    for (int n=0; n<value2.length/3; n++) {  
                        if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {  
                            xiangsi++;  
                        } else {  
                            busi++;  
                        }  
                    }  
                } catch (RuntimeException e) {  
                    continue;  
                }  
                j++;  
            }  
            i++;  
        }  
  
        list1 = getPX(images[1]);  
        list2 = getPX(images[0]);  
        i = 0;  
        j = 0;  
        for (String[] strings : list1) {  
            if ((i + 1) == list1.length) {  
                continue;  
            }  
            for (int m=0; m<strings.length/3; m++) {  
                try {  
                    String[] value1 = list1[i][j].toString().split(",");  
                    String[] value2 = list2[i][j].toString().split(",");  
                    int k = 0;  
                    for (int n=0; n<value2.length; n++) {  
                        if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {  
                            xiangsi++;  
                        } else {  
                            busi++;  
                        }  
                    }  
                } catch (RuntimeException e) {  
                    continue;  
                }  
                j++;  
            }  
            i++;  
        }  
        String baifen = "";  
        try {  
            baifen = ((Double.parseDouble(xiangsi + "") / Double.parseDouble((busi + xiangsi) + "")) + "");  
            baifen = baifen.substring(baifen.indexOf(".") + 1, baifen.indexOf(".") + 3);  
        } catch (Exception e) {  
            baifen = "0";  
        }  
        if (baifen.length() <= 0) {  
            baifen = "0";  
        }  
        if(busi == 0){  
            baifen="100";  
        }  
  
        System.out.println("相似像素数量：" + xiangsi + " 不相似像素数量：" + busi + " 相似率：" + Integer.parseInt(baifen) + "%"); 
        return Integer.parseInt(baifen)+30;
  
    }  
  
  
}  