package com.elsa.dfsutils.filter.image;

import java.io.File;

import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import com.elsa.dfsutils.file.BaseFile;
import com.elsa.dfsutils.file.ImgFile;
import com.elsa.dfsutils.filter.FileFilter;
import com.elsa.dfsutils.handle.ImgHandler;

/**
 * 图片合成
 * 
 * @author Administrator
 *
 */
public class ImgCompositeFilter extends FileFilter<IMOperation> {

	@Override
	public void doFilter(BaseFile baseFile, IMOperation operation) throws Exception {
		ImgFile imgFile = ImgFile.class.cast(baseFile);
		ImgFile.WaterMark watermark = imgFile.getWatermark();
		if (watermark != null) {
			File file = imgFile.getFile();
			File wmFile = watermark.getWmfile();

			if (wmFile != null && wmFile.exists()) {
//				operation.font("宋体").gravity("southeast").pointsize(18).fill("#BCBFC8")  
//                .draw("text 5,5 juziku.com");
//				operation.addImage();
				operation.addImage(wmFile.getAbsolutePath(), file.getAbsolutePath(), file.getAbsolutePath());
				CompositeCmd composite = new CompositeCmd();
				if (!File.separator.matches("/")) {// 如果是Linux，不需要设置
					String imPath = ImgHandler.searchImageMagickPath();
					if (imPath != null) {
						composite.setSearchPath(imPath);
					} else {
						throw new RuntimeException("Windows下必须设置SearchPath!");
					}
				}
				composite.run(operation);
			} else {
				throw new RuntimeException("找不到水印图片");
			}

		}
	}

	public static void addImgText(String srcPath) throws Exception {  
        IMOperation op = new IMOperation();  
        op.font("宋体").gravity("southeast").pointsize(100).fill("#BCBFC8")  
                .draw("text 800,800 juziku.com");  
        op.addImage();  
        op.addImage();  
        ConvertCmd convert = new ConvertCmd();  
        String imPath = ImgHandler.searchImageMagickPath();
        convert.setSearchPath(imPath);  
        convert.run(op, srcPath,srcPath);
        
    }  
	
	 public static void main(String[] args) throws Exception {  
	        // cutImage("D:\\test.jpg", "D:\\new.jpg", 98, 48, 370,320);  
	        // cutImage(200,300, "/home/1.jpg", "/home/2.jpg");  
	        addImgText("F:/B.jpg");  
	    }  
}
