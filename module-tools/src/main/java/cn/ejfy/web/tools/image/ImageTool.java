package cn.ejfy.web.tools.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class ImageTool {
	/**
	 * 压缩裁剪综合处理： 1、判断图片是否为png，是则转jpg  2、裁剪 3、压缩图片
	 *  @author wyj
	 * @param image  原图文件
	 * @param newPath  新路径   
	 * @param scale  目标图片的高宽比（y/x）,（h/w）  --可为空(按原图宽高比)
	 * @param targetSize  目标图片最大边的像素   --可为空（按原图最大边）
	 */
	public static String handleImage(File image, String newPath, Float scale,Integer targetSize) throws IOException{
		String imagePath=image.getPath();      //传文件再获取路径，避免传的路径出错
		//转jpg
		String ext = getFileExt(imagePath).toLowerCase();
		if("png".equals(ext)){
			imagePath=turnImageFormat(imagePath, newPath);
		}
		if(null==scale||scale<0.000001){
			BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
			int srcH=bufferedImage.getHeight();
			int srcW=bufferedImage.getWidth();
			scale=(float)srcH/(float)srcW;
		}
		//裁剪图片
		newPath=cutImageByScale(imagePath, newPath, scale);
		//压缩图片
		newPath=resizeByScale(newPath, newPath, scale, targetSize);;
		return newPath;
	}
	/**
	 * 将png图片转换为jpg 注意：原png图片未删除
	 * 
	 * @author wyj
	 * @param imagePath png图片路径
	 * @param newPath 转换的jpg图片路径(可传空，本方法会给一个默认路径)
	 * @return 返回jpg图片路径
	 */
	public static String turnImageFormat(String imagePath, String newPath) throws IOException {
		if (newPath == null || "".equals(newPath)) {
			String pattern = "(.*)([\\.])(.*)";
			String thumbPath = "$1$2" + "jpg";
			Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
			Matcher m = p.matcher(imagePath);
			if (m.find()) {
				newPath = m.replaceAll(thumbPath);
			}
		}
		BufferedImage bufferedImage;
		// read image file
		bufferedImage = ImageIO.read(new File(imagePath));
		// create a blank, RGB, same width and height, and a white background
		// 关键点：imageType（有13种）为TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		// 根据设置的bufferedImage画图，并将背景设为Color.WHITE白色
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
		// write to jpeg file
		ImageIO.write(newBufferedImage, "jpg", new File(newPath));
		System.out.println("图片转换jpg成功");
		return newPath;
	}

	/**
	 * 设置比例截取图片
	 * 
	 * @author wyj
	 * @param imagePath 原图片
	 * @param newPath 截取后的保存路径
	 * @param scale  目标图片的高宽比（y/x）,（h/w）
	 */
	public static String cutImageByScale(String imagePath, String newPath, Float scale) throws IOException {
		File originalImage = new File(imagePath);
		String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1, imagePath.length()); // 获取文件后缀
		if (null == originalImage || null == scale || scale < 0.000001) {
			new Exception("截图参数出错！！！");
		}
		// 用ImageIO读取字节流
		BufferedImage bufferedImage = ImageIO.read(originalImage);
		// 返回源图片的宽度。
		int srcW = bufferedImage.getWidth();
		// 返回源图片的高度。
		int srcH = bufferedImage.getHeight();
		int w = 0; // 截取宽度
		int h = 0; // 截取高度
		Float srcScale = ((float)  srcH/ (float)srcW );
		if (srcScale > scale) { // 高长，则以宽为标准计算目标高
			h = (int) (scale * srcW);
			w = srcW;
		} else {
			h = srcH;
			w = (int) ((float) srcH / scale);
		}
		int x1 = 0, y1 = 0; // 切割的起始坐标
		int x2 = 0, y2 = 0; // 切割的终点坐标
		x1 = srcW / 2 - w / 2;
		y1 = srcH / 2 - h / 2;
		x2 = x1 + w;
		y2 = y1 + h;
		// 生成图片
		BufferedImage cutImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = cutImage.getGraphics();
		g.drawImage(bufferedImage, 0, 0, w, h, x1, y1, x2, y2, null); // 2-5参数为x1,y1,x2,y2为目标文件坐标，6-9为原图片坐标。
		File file = new File(newPath);
		ImageIO.write(cutImage, suffix, file);
		System.out.println("图片裁剪成功");
		return newPath;
	}
	
	/**
	 * 根据高宽比，和限定最大高度 压缩图片
	 *  @author wyj
	 * @param imagePath  原图路径
	 * @param newPath  新路径
	 * @param scale  目标图片的高宽比（y/x）,（h/w）
	 * @param targetSize  目标图片最大边的像素   --可为空
	 * @return 
	 */
	public static String resizeByScale(String imagePath, String newPath,Float scale,Integer targetSize) throws IOException{
		int targetW=0;  //目标宽度
		int targetH=0;  //目标高度
		if(targetSize==null||targetSize==0){
			/**
			 * 根据scale比例计算压缩图片的宽高
			 */
			BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
			// 返回源图片的宽度。
			int srcW = bufferedImage.getWidth();
			// 返回源图片的高度。
			int srcH = bufferedImage.getHeight();
			Float srcScale=((float) srcW/(float) srcH);
			if(srcScale>scale){      //高长，则以宽为标准计算目标高
				targetH=(int) (scale*srcW);
				targetW=srcW;
	        }else{
	        	targetH=srcH;
	        	targetW=(int) ((float)srcH/scale);
	        }
		}else{
			/**
			 * 根据scale和targetSize计算压缩图片的宽高
			 */
			if(scale>1.0){   //高比宽 长 ，以高为标准计算宽
				targetH=targetSize;
				targetW=(int) ((float)targetSize/scale);
			}else{
				targetW=targetSize;
				targetH=(int) (targetSize*scale);
			}
		}
        /**
         * 调用压缩图片方法
         */
		resize(imagePath, newPath, targetW, targetH);
		return newPath;
	}
	
	/**
	 * 根据宽高直接压缩图片
	 *  @author wyj
	  * @param imagePath  原图路径
	 * @param newPath  新路径
	 * @param targetW  目标图片宽度
	 * @param targetH  目标图片高度
	 */
	public static String resize(String imagePath, String newPath,int w, int h) throws IOException{
		String ext = getFileExt(imagePath).toLowerCase();
		
		BufferedImage image;
		image = ImageIO.read(new File(imagePath));
		ImageIO.write(Lanczos.resizeImage(image, w, h), ext, new File(newPath));
		System.out.println("图片压缩成功");
		return newPath;
	}
	
	/**
	 * 获取扩展名
	 */
	private static String getFileExt(String fileName) {

		int potPos = fileName.lastIndexOf('.') + 1;
		String type = fileName.substring(potPos, fileName.length());
		return type;
	}
}