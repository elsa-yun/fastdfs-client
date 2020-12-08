package com.elsa.dfsutils.filter.image.convert;

import org.im4java.core.IMOperation;

import com.elsa.dfsutils.constants.ImageWidthNorms;
import com.elsa.dfsutils.file.BaseFile;
import com.elsa.dfsutils.file.ImgFile;
import com.elsa.dfsutils.filter.FileFilter;

/**
 * 图片等比缩放过滤器
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-23 上午1:59:53
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class ImgReSizeFilter extends FileFilter<IMOperation> {

	/**
	 * 调用imagemagick对图片进行等比缩放
	 * 
	 * @throws Exception
	 */
	@Override
	public void doFilter(BaseFile baseFile, IMOperation operation) throws Exception {
		ImgFile imgFile = ImgFile.class.cast(baseFile);
		Integer rawWidth = imgFile.getRawWidth();
		if (rawWidth != null) {
			if (ImageWidthNorms.INSTANCE.contains(rawWidth)) {
				operation.resize(rawWidth, null);// 命令配置
			} else {
				throw new RuntimeException("宽度不符合规范，请参考ImageWidthNorms");
			}
		}
		if (successor != null) {
			successor.doFilter(baseFile, operation);
		}
	}
}
