package com.elsa.dfsutils.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.fastdfs.TrackerServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.elsa.dfsutils.constants.ImageWidthNorms;
import com.elsa.dfsutils.constants.MetaDataKey;
import com.elsa.dfsutils.constants.WaterMarkGravity;
import com.elsa.dfsutils.file.GeneralFile;
import com.elsa.dfsutils.file.ImgFile;
import com.elsa.dfsutils.file.ImgFile.WaterMark;
import com.elsa.dfsutils.file.TxtFile;
import com.elsa.dfsutils.service.impl.DfsServiceImpl;
import com.elsa.dfsutils.util.TrackerServerFactory;
import com.elsa.dfsutils.util.TrackerServerPool;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-22 上午10:14:00
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class ImageServiceTester {
	public DfsService imageService = null;
	public TrackerServerPool pool = null;

	@Before
	public void setUp() throws Exception {// storge 172.16.1.155/172.16.1.156
		PooledObjectFactory factory = new TrackerServerFactory(new String[] { "192.168.24.44:22122", "192.168.24.45:22122" });
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(10);// 池配置还有很多的参数，参考GenericObjectPoolConfig源代码
		pool = new TrackerServerPool(factory, config);
		imageService = new DfsServiceImpl(pool);
	}

	@After
	public void tearDown() throws Exception {

	}

	/**
	 * 测试池化效果
	 */
	@Test
	public void testConnection() {
		try { // 默认为8个连接，具体优化在GenericObjectPoolConfig中操作
			for (int i = 1; i < 100; i++) {
				Thread.sleep(100);
				TrackerServer trackerServer = pool.borrowObject();// 获得跟踪节点（连接）
				System.out.println(i + "\t" + trackerServer);
				pool.returnObject(trackerServer);// 把连接归还给池
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testImgAdd() {
		File file = new File("F:/B.jpg");
		ImgFile imgFile = new ImgFile();
		System.out.println(file.exists());
		imgFile.setFile(file);
		Map<MetaDataKey, String> map = new HashMap();
		map.put(MetaDataKey.FILENAME, "IMG_5109.JPG");
		map.put(MetaDataKey.CREATOR, "叶礼锋");
		imgFile.setMetaData(map);

		imgFile.setInterlace(true);

		 imgFile.setQuality(90d);
		 imgFile.setRawWidth(ImageWidthNorms.WIDTHDEFAULT);
		 ImgFile.WaterMark waterMark= imgFile.new WaterMark();
		 waterMark.setDissolve(60);//透明度
		 waterMark.setGravity(WaterMarkGravity.SouthEast);
		 waterMark.setWmfile(new File("F:/b.png"));
		 waterMark.setX(10);
		 waterMark.setY(10);
		 imgFile.setWatermark(waterMark);

		String fileid = imageService.uploadFile(imgFile);
		System.out.println(fileid);
		// img.clear();
	}

	@Test
	public void testGeneralFileAdd() {
		File file = new File("F:/B.JPG");
		GeneralFile info = new GeneralFile();
		System.out.println(file.exists());
		info.setFile(file);
		Map<MetaDataKey, String> map = new HashMap();
		map.put(MetaDataKey.FILENAME, "F:/B.JPG");
		map.put(MetaDataKey.CREATOR, "叶礼锋");
		info.setMetaData(map);
		String fileid = imageService.uploadFile(info);
		System.out.println(fileid);
	}

	@Test
	public void testTxtAdd() {
		File file = new File("z:/server.xml");
		TxtFile info = new TxtFile();
		System.out.println(file.exists());
		info.setFile(file);
		Map<MetaDataKey, String> map = new HashMap();
		map.put(MetaDataKey.FILENAME, "IMG_5109.JPG");
		map.put(MetaDataKey.CREATOR, "叶礼锋");
		info.setMetaData(map);
		info.setEncryptionFlag(true);
		String fileid = imageService.uploadFile(info);
		System.out.println("密钥：" + info.getSecretKey());
		System.out.println(fileid);
	}

	@Test
	public void testImgBatchAdd() {
		Map<MetaDataKey, String> map = new HashMap();
		map.put(MetaDataKey.FILENAME, "奥迪A.jpg");
		map.put(MetaDataKey.SOURCE, "百度");
		ImgFile info[] = new ImgFile[3];
		File file[] = new File[] { new File("z:/奥迪A.jpg"), new File("z:/奥迪B.jpg"), new File("z:/奥迪C.jpg"), };
		try {
			for (int i = 0; i < 3; i++) {
				info[i] = new ImgFile();
				info[i].setFile(file[i]);
				info[i].setMetaData(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String ids[] = imageService.batchUploadFile(info);
		System.out.println(Arrays.toString(ids));
	}

	@Test
	public void testImgDel() {
		System.out.println("删除状态：" + (imageService.deleteFile("group1/M00/00/00/wKjIM1SX8nGAdgUUAAbrUb-n-lA600.jpg") == 0));
	}

	@Test
	public void testImgBatchDel() {
		String ids[] = new String[] { "group1/M00/00/00/wKjIM1SX8wKABnjxAAbrUb-n-lA414.jpg",
				"group1/M00/00/00/wKjIM1SX8wKALkQFAARNpqrrf4s967.jpg", "group1/M00/00/00/wKjIM1SX8wKAMMqxAA0LT68XzFc942.jpg" };
		int re[] = imageService.batchDeleteFile(ids);
		System.out.println(Arrays.toString(re));
	}

	@Test
	public void testImgGetBytes() {
		String id = "group1/M00/00/00/wKjIM1SX8wKAMMqxAA0LT68XzFc942.jpg";
		byte buf[] = imageService.getFileBytes(id);
		try {
			IOUtils.write(buf, new FileOutputStream("z:/ooo.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testImgGetMetaData() {
		String id = "group1/M00/00/00/wKjIM1SX-jWAQaQtAAI4Sqn8Sc0472.JPG";
		Map map = imageService.getFileMetaData(id);
		System.out.println(map);
	}

	@Test
	public void testImgSetMetaData() {
		String id = "group1/M00/00/00/wKjIM1SX-jWAQaQtAAI4Sqn8Sc0472.JPG";
		Map<MetaDataKey, String> info = new HashMap();
		info.put(MetaDataKey.FILENAME, "测试.jpg");
		info.put(MetaDataKey.SOURCE, "百度");
		System.out.println(imageService.setFileMetaData(id, info));
	}

}
